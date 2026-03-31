/*
 * Copyright 2025 Intent Exchange, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.intent_exchange.uasl.controller.mqtt;

import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationUasIdDto;
import com.intent_exchange.uasl.dto.request.MqttUaslReservationPayloadDto;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.dto.response.WebUaslSectionsDto;
import com.intent_exchange.uasl.service.UaslReservationService;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

/**
 * 航路予約に関連するMQTTメッセージを処理するコントローラークラス。
 */
@Controller
public class MqttUaslReservationController extends AbstractMqttAsyncController {

  /** MQTTトピック */
  @Value("${uasl.reservation.topic}")
  @Getter
  private String topic;

  /** 通信品質(Quality of Service)。 確実に1回到達を保証 */
  @Value("${mqtt.qos.exactryOnce}")
  @Getter
  private int mqttMessageQos;

  /** 航路予約サービス */
  @Autowired
  private UaslReservationService service;

  /**
   * MQTT メッセージを処理します。
   *
   * <p>RESERVED の場合、payload（originReservation または top-level）から必要項目を抽出し、 reservationId /
   * registrationId / aircraftInfoId の存在を検証します。必須項目が欠けると {@link IllegalArgumentException} を投げ、正常なら
   * DTO を作成して {@link UaslReservationService#saveReservation} に委譲します。CANCELED/RESCINDED は削除を委譲します。
   *
   * @param topic MQTT トピック
   * @param message MQTT メッセージ
   * @throws IllegalArgumentException 必須項目が欠けている場合
   * @throws Exception その他の処理エラー
   */
  @Override
  public void handleMessage(String topic, MqttMessage message) throws Exception {
    String rawPayload = new String(message.getPayload(), StandardCharsets.UTF_8);
    MqttUaslReservationPayloadDto parsed = parsePayload(rawPayload);
    Status status = new Status(parsed.getStatus());

    if (Status.RESERVED.equals(status)) {
      String reservationId;
      String registrationId;
      Integer aircraftInfoId;

      if (parsed.isHasOriginReservation()) {
        // UaslReservation ペイロード（originReservation キーあり）
        reservationId = parsed.getOriginReservationId();
        AircraftInfo aircraftData = extractAircraftInfoFromOrigin(parsed.getOriginVehicles());
        registrationId = aircraftData.registrationId();
        aircraftInfoId = aircraftData.aircraftInfoId();
      } else {
        // DestinationReservationNotification ペイロード（originReservation キーなし）
        reservationId = parsed.getReservationId();
        AircraftInfo aircraftData =
            extractAircraftInfoFromOperating(parsed.getOperatingAircrafts());
        registrationId = aircraftData.registrationId();
        aircraftInfoId = aircraftData.aircraftInfoId();
      }

      if (reservationId == null || registrationId == null || aircraftInfoId == null) {
        throw new IllegalArgumentException(
            "必須フィールドが null です: reservationId="
                + reservationId
                + ", registrationId="
                + registrationId
                + ", aircraftInfoId="
                + aircraftInfoId);
      }

      // LinkageInformationNotificationDto を構築
      LinkageInformationNotificationDto linkageDto = new LinkageInformationNotificationDto();
      linkageDto.setUaslReservationId(reservationId);
      linkageDto.setRequestId(parsed.getRequestId());
      linkageDto.setAircraftInfoId(aircraftInfoId);
      LinkageInformationNotificationUasIdDto uasId = new LinkageInformationNotificationUasIdDto();
      uasId.setRegistrationId(registrationId);
      linkageDto.setUasId(uasId);

      // WebUaslReservationDto を構築
      WebUaslReservationDto reservationDto = new WebUaslReservationDto();
      reservationDto.setUaslReservationId(reservationId);
      reservationDto.setOperatorId(parsed.getOperatorId());
      reservationDto.setReservedAt(parsed.getReservedAt());
      SectionInfo sectionInfo = extractSectionInfo(parsed);
      reservationDto.setUaslSections(sectionInfo.sections());
      reservationDto.setUaslSectionIds(
          sectionInfo.sectionIds() != null ? sectionInfo.sectionIds() : parsed.getUaslSectionIds());
      reservationDto.setStartAt(
          sectionInfo.startAt() != null ? sectionInfo.startAt() : parsed.getStartAt());
      reservationDto.setEndAt(
          sectionInfo.endAt() != null ? sectionInfo.endAt() : parsed.getEndAt());

      reservationDto.setEvaluationResults(computeOverallEvaluation(parsed));

      service.saveReservation(reservationDto, linkageDto);

    } else if (Status.CANCELED.equals(status) || Status.RESCINDED.equals(status)) {
      service.deleteUaslReservation(parsed.getUaslReservationId());
    }
  }

  private record AircraftInfo(String registrationId, Integer aircraftInfoId) {}

  private AircraftInfo extractAircraftInfoFromOrigin(List<Map<String, Object>> vehicles) {
    if (vehicles != null && !vehicles.isEmpty()) {
      Map<String, Object> aircraftInfo = toMap(vehicles.get(0).get("aircraftInfo"));
      String reg = aircraftInfo != null ? (String) aircraftInfo.get("registrationId") : null;
      Integer id = aircraftInfo != null ? (Integer) aircraftInfo.get("aircraftInfoId") : null;
      return new AircraftInfo(reg, id);
    }
    return new AircraftInfo(null, null);
  }

  private AircraftInfo extractAircraftInfoFromOperating(
      List<Map<String, Object>> operatingAircrafts) {
    if (operatingAircrafts != null && !operatingAircrafts.isEmpty()) {
      String reg = (String) operatingAircrafts.get(0).get("registrationId");
      Integer id = (Integer) operatingAircrafts.get(0).get("aircraftInfoId");
      return new AircraftInfo(reg, id);
    }
    return new AircraftInfo(null, null);
  }

  private SectionInfo extractSectionInfo(MqttUaslReservationPayloadDto parsed) {
    List<Map<String, Object>> sectionMaps =
        parsed.isHasOriginReservation() ? parsed.getOriginUaslSections() : null;
    if (sectionMaps == null || sectionMaps.isEmpty()) {
      sectionMaps = parsed.getUaslSections();
    }
    if (sectionMaps == null || sectionMaps.isEmpty()) {
      return new SectionInfo(null, null, null, null);
    }

    List<String> sectionIds = new ArrayList<>();
    List<WebUaslSectionsDto> sections = new ArrayList<>();
    Instant earliestStart = null;
    Instant latestEnd = null;
    String fallbackStart = null;
    String fallbackEnd = null;
    for (Map<String, Object> section : sectionMaps) {
      String sectionId = (String) section.get("uaslSectionId");
      if (sectionId != null) {
        sectionIds.add(sectionId);
      }
      WebUaslSectionsDto dto = new WebUaslSectionsDto();
      dto.setUaslSectionId(sectionId);
      String startAtValue = (String) section.get("startAt");
      String endAtValue = (String) section.get("endAt");
      dto.setStartAt(startAtValue);
      dto.setEndAt(endAtValue);
      sections.add(dto);

      Instant start = parseInstant(startAtValue);
      if (start != null) {
        if (earliestStart == null || start.isBefore(earliestStart)) {
          earliestStart = start;
        }
      } else if (fallbackStart == null && startAtValue != null) {
        fallbackStart = startAtValue;
      }
      Instant end = parseInstant(endAtValue);
      if (end != null) {
        if (latestEnd == null || end.isAfter(latestEnd)) {
          latestEnd = end;
        }
      } else if (endAtValue != null) {
        fallbackEnd = endAtValue;
      }
    }

    String startAt = earliestStart != null ? earliestStart.toString() : fallbackStart;
    String endAt = latestEnd != null ? latestEnd.toString() : fallbackEnd;
    return new SectionInfo(
        sectionIds.isEmpty() ? null : sectionIds,
        sections.isEmpty() ? null : sections,
        startAt,
        endAt);
  }

  /**
   * JSON 文字列を解析し {@link MqttUaslReservationPayloadDto} を返します。 すべての unchecked キャストはこのメソッドに集約されます。
   *
   * @param json MQTTメッセージのペイロード文字列
   * @return パース済みのペイロードDto
   */
  private MqttUaslReservationPayloadDto parsePayload(String json) {
    Map<String, Object> map = ModelMapperUtil.jsonToMap(json);

    MqttUaslReservationPayloadDto dto = new MqttUaslReservationPayloadDto();
    dto.setUaslReservationId((String) map.get("uaslReservationId"));
    dto.setStatus((String) map.get("status"));
    dto.setRequestId((String) map.get("requestId"));
    dto.setReservationId((String) map.get("reservationId"));
    dto.setOperatorId((String) map.get("operatorId"));
    dto.setReservedAt((String) map.get("reservedAt"));
    dto.setStartAt((String) map.get("startAt"));
    dto.setEndAt((String) map.get("endAt"));
    dto.setUaslSectionIds(castList(map.get("uaslSectionIds")));
    dto.setOperatingAircrafts(toMapList(map.get("operatingAircrafts")));
    dto.setUaslSections(toMapList(map.get("uaslSections")));

    Map<String, Object> originRaw = toMap(map.get("originReservation"));
    if (originRaw != null) {
      dto.setHasOriginReservation(true);
      dto.setOriginReservationId((String) originRaw.get("reservationId"));
      dto.setOriginVehicles(toMapList(originRaw.get("vehicles")));
      dto.setOriginUaslSections(toMapList(originRaw.get("uaslSections")));
      dto.setConformityAssessmentResults(toMapList(originRaw.get("conformityAssessmentResults")));
    } else {
      dto.setConformityAssessmentResults(toMapList(map.get("conformityAssessmentResults")));
    }

    return dto;
  }

  @SuppressWarnings("unchecked")
  private List<String> castList(Object value) {
    if (!(value instanceof List<?>)) {
      return null;
    }
    return (List<String>) value;
  }

  private Instant parseInstant(String value) {
    if (value == null) {
      return null;
    }
    try {
      return Instant.parse(value);
    } catch (DateTimeParseException e) {
      return null;
    }
  }

  private boolean computeOverallEvaluation(MqttUaslReservationPayloadDto parsed) {
    List<Map<String, Object>> results = parsed.getConformityAssessmentResults();
    if (results == null) {
      return true;
    }
    for (Map<String, Object> result : results) {
      Object evaluationResults = result.get("evaluationResults");
      if ("false".equalsIgnoreCase(String.valueOf(evaluationResults))) {
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> toMap(Object value) {
    if (!(value instanceof Map<?, ?>)) {
      return null;
    }
    return (Map<String, Object>) value;
  }

  private List<Map<String, Object>> toMapList(Object value) {
    if (!(value instanceof List<?> rawList)) {
      return null;
    }
    List<Map<String, Object>> result = new ArrayList<>();
    for (Object item : rawList) {
      Map<String, Object> mapItem = toMap(item);
      if (mapItem != null) {
        result.add(mapItem);
      }
    }
    return result;
  }

  private record SectionInfo(
      List<String> sectionIds, List<WebUaslSectionsDto> sections, String startAt, String endAt) {}

  /**
   * 航路予約のステータスを表すレコード。
   */
  private record Status(String value) {
    public static final Status RESERVED = new Status("RESERVED");
    public static final Status CANCELED = new Status("CANCELED");
    public static final Status RESCINDED = new Status("RESCINDED");
  }
}

