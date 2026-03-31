/*
 * Copyright 2026 Intent Exchange, Inc.
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

package com.intent_exchange.uasl.logic;

import com.intent_exchange.uasl.dao.AircraftTypeDeviationMapper;
import com.intent_exchange.uasl.dto.request.AircraftTypeDeviationDto;
import com.intent_exchange.uasl.dto.request.UaslDesignAreaInfoDeviationDto;
import com.intent_exchange.uasl.model.AircraftTypeDeviation;
import com.intent_exchange.uasl.model.AircraftTypeDeviationInfo;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** 航空機種別の逸脱集計ロジック。 */
@Component
public class AircraftTypeDeviationLogic {

  @Autowired private UaslDeviationProcessingLogic processingLogic;

  @Autowired private AircraftTypeDeviationMapper aircraftTypeDeviationMapper;

  /**
   * 逸脱メタデータのリストを受け取り、集計して DB に insert します。 入力が null または空の場合は処理を行いません。
   *
   * @param infoList メタデータ付き逸脱情報のリスト
   */
  @Transactional
  public void registerAircraftTypeDeviation(List<AircraftTypeDeviationInfo> infoList) {
    if (infoList == null || infoList.isEmpty()) {
      return;
    }
    Map<String, AircraftTypeDeviationInfo> remoteData = new HashMap<>();
    for (AircraftTypeDeviationInfo info : infoList) {
      if (info == null) {
        continue;
      }
      remoteData.putIfAbsent(info.getReservationId(), info);
    }
    List<UaslDesignAreaInfoDeviationDto> deviationList =
        ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class);
    aggregateAndInsert(deviationList, remoteData);
  }

  /**
   * 事前に選択された逸脱 DTO の一覧を航空機／reservation ごとに集約して DB に insert します。 定期実行やバックフィルで使用されます。{@code
   * deviationList} が null または空の場合は何もしません。
   *
   * @param deviationList 逸脱情報の DTO リスト
   */
  private void aggregateAndInsert(
      List<UaslDesignAreaInfoDeviationDto> deviationList,
      Map<String, AircraftTypeDeviationInfo> metadataByReservation) {
    if (deviationList == null || deviationList.isEmpty()) {
      return;
    }

    // まず aircraftInfoId でグループ化し、その後 reservation ごとに 1 件の集計を作成します。
    Map<Integer, List<UaslDesignAreaInfoDeviationDto>> grouped =
        deviationList.stream()
            .filter(Objects::nonNull)
            .collect(Collectors.groupingBy(UaslDesignAreaInfoDeviationDto::getAircraftInfoId));

    for (Map.Entry<Integer, List<UaslDesignAreaInfoDeviationDto>> entry : grouped.entrySet()) {
      List<UaslDesignAreaInfoDeviationDto> group = entry.getValue();
      if (group == null || group.isEmpty()) {
        continue;
      }
      // この航空機グループ内で registrationId ごとのユニーク予約数（累計フライト数）をカウントします。
      Map<String, Integer> flightCountByRegistrationId =
          computeFlightCountByRegistrationId(group, metadataByReservation);

      // Compute registration key once per DTO to avoid redundant metadata lookups
      Map<String, List<UaslDesignAreaInfoDeviationDto>> byRegistrationId =
          group.stream()
              .filter(Objects::nonNull)
              .map(
                  dto ->
                      new java.util.AbstractMap.SimpleEntry<>(
                          resolveRegistrationKey(dto, metadataByReservation), dto))
              .filter(e -> e.getKey() != null)
              .collect(
                  Collectors.groupingBy(
                      java.util.Map.Entry::getKey,
                      Collectors.mapping(java.util.Map.Entry::getValue, Collectors.toList())));

      // registrationId ごとに 1 行の集計レコードを出力する
      for (Map.Entry<String, List<UaslDesignAreaInfoDeviationDto>> rEntry :
          byRegistrationId.entrySet()) {
        List<UaslDesignAreaInfoDeviationDto> registrationGroup = rEntry.getValue();
        if (registrationGroup == null || registrationGroup.isEmpty()) {
          continue;
        }

        AircraftTypeDeviationDto aircraftDeviationDto = new AircraftTypeDeviationDto();
        aircraftDeviationDto.setAircraftInfoId(registrationGroup.get(0).getAircraftInfoId());

        AircraftTypeDeviationInfo meta = resolveMeta(registrationGroup, metadataByReservation);
        String actualRegistrationId = meta == null ? null : meta.getRegistrationId();
        if (meta != null) {
          aircraftDeviationDto.setSpecificSessionId(meta.getSpecificSessionId());
          aircraftDeviationDto.setSerialNumber(meta.getSerialNumber());
          aircraftDeviationDto.setRegistrationId(actualRegistrationId);
          aircraftDeviationDto.setUtmId(meta.getUtmId());
        }

        Double routeDeviationRate = processingLogic.getDeviationRate(registrationGroup);
        if (routeDeviationRate == null || Double.compare(routeDeviationRate, 0.0d) == 0) {
          continue;
        }

        aircraftDeviationDto.setRouteDeviationRate(routeDeviationRate);
        aircraftDeviationDto.setRouteDeviationAmount(
            String.format(
                "{\"horizontal\": %s, \"vertical\": %s}",
                processingLogic.getHorizontalPercentile(registrationGroup),
                processingLogic.getVerticalPercentile(registrationGroup)));

        aircraftDeviationDto.setRouteDeviationStartTime(computeStartTime(registrationGroup));
        aircraftDeviationDto.setRouteDeviationEndTime(computeEndTime(registrationGroup));

        int flightCount =
            actualRegistrationId == null
                ? 1
                : Math.max(1, flightCountByRegistrationId.getOrDefault(actualRegistrationId, 1));
        aircraftDeviationDto.setFlightCount(flightCount);

        AircraftTypeDeviation model =
            ModelMapperUtil.map(aircraftDeviationDto, AircraftTypeDeviation.class);
        model.setAircraftTypeDeviationId(null);
        aircraftTypeDeviationMapper.insertSelective(model);
      }
    }
  }

  /**
   * 集計キーを解決します。
   *
   * <p>metadata から {@code registrationId} を取得し、metadata が存在しない場合のみ {@code reservationId}
   * をフォールバックとして返します。metadata が存在しても registrationId が null の場合は null を返し、集計対象から除外されます。
   *
   * <p>registrationId が null になる主な例: メタデータ欠落、旧データ、取得エラーなど。
   *
   * @param dto メタデータ参照のための reservationId を持つ DTO
   * @param metadataByReservation reservationId -> metadata のマップ
   * @return registrationId（metadata がない場合は reservationId）、または dto/reservationId が null の場合は null
   */
  private String resolveRegistrationKey(
      UaslDesignAreaInfoDeviationDto dto,
      Map<String, AircraftTypeDeviationInfo> metadataByReservation) {
    if (dto == null || dto.getReservationId() == null) {
      return null;
    }
    AircraftTypeDeviationInfo meta = metadataByReservation.get(dto.getReservationId());
    return meta == null ? dto.getReservationId() : meta.getRegistrationId();
  }

  private AircraftTypeDeviationInfo resolveMeta(
      List<UaslDesignAreaInfoDeviationDto> group,
      Map<String, AircraftTypeDeviationInfo> metadataByReservation) {
    if (group == null || group.isEmpty()) {
      return null;
    }
    for (UaslDesignAreaInfoDeviationDto dto : group) {
      if (dto == null || dto.getReservationId() == null) {
        continue;
      }
      AircraftTypeDeviationInfo meta = metadataByReservation.get(dto.getReservationId());
      if (meta != null) {
        return meta;
      }
    }
    return null;
  }

  /** グループ内の最も早い RouteDeviation のタイムスタンプを返します。 */
  private OffsetDateTime computeStartTime(List<UaslDesignAreaInfoDeviationDto> group) {
    return routeDeviationTimestamps(group).min(OffsetDateTime::compareTo).orElse(null);
  }

  /** グループ内の最も遅い RouteDeviation のタイムスタンプを返します。 */
  private OffsetDateTime computeEndTime(List<UaslDesignAreaInfoDeviationDto> group) {
    return routeDeviationTimestamps(group).max(OffsetDateTime::compareTo).orElse(null);
  }

  /** RouteDeviation のタイムスタンプのストリームを返します。 */
  private java.util.stream.Stream<OffsetDateTime> routeDeviationTimestamps(
      List<UaslDesignAreaInfoDeviationDto> group) {
    return group.stream()
        .filter(Objects::nonNull)
        .filter(dto -> "RouteDeviation".equals(dto.getOperationalStatus()))
        .map(UaslDesignAreaInfoDeviationDto::getGetLocationTimestamp)
        .filter(Objects::nonNull)
        .map(ts -> ts.toInstant().atOffset(ZoneOffset.UTC));
  }

  /**
   * 指定した DTO グループから、registrationId ごとにユニークな予約数を計算して返します。
   *
   * <p>メタデータは reservationId をキーにして {@code metadataByReservation} から参照します。 入力が null/空の場合は空のマップを返します。
   *
   * @param group 対象の逸脱 DTO のリスト
   * @param metadataByReservation reservationId をキーとするメタデータマップ
   * @return registrationId -> ユニーク予約数 のマップ（存在しない場合は空マップ）
   */
  private Map<String, Integer> computeFlightCountByRegistrationId(
      List<UaslDesignAreaInfoDeviationDto> group,
      Map<String, AircraftTypeDeviationInfo> metadataByReservation) {
    if (group == null || group.isEmpty()) {
      return Map.of();
    }

    // metadata lookup を一度だけ行い、(registrationId, reservationId) のペアにマップして
    // null を取り除いてから groupingBy で集計します。
    return group.stream()
        .filter(Objects::nonNull)
        .map(
            dto -> {
              String reservationId = dto.getReservationId();
              AircraftTypeDeviationInfo meta =
                  reservationId == null ? null : metadataByReservation.get(reservationId);
              String registration = meta == null ? null : meta.getRegistrationId();
              return new java.util.AbstractMap.SimpleEntry<>(registration, reservationId);
            })
        .filter(e -> e.getKey() != null && e.getValue() != null)
        .collect(
            Collectors.groupingBy(
                java.util.Map.Entry::getKey,
                Collectors.mapping(
                    java.util.Map.Entry::getValue,
                    Collectors.collectingAndThen(Collectors.toSet(), Set::size))));
  }
}
