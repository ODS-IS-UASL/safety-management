package com.intent_exchange.uasl.controller.mqtt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.service.UaslReservationService;
import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MqttUaslReservationControllerTest extends MqttUaslReservationController {

  @Mock private UaslReservationService service;

  @InjectMocks private MqttUaslReservationController controller;

  @Test
  @DisplayName("getTopic / getMqttMessageQos は null / 0 を返す（@Value未注入時）")
  void testGetters() {
    assertEquals(null, controller.getTopic());
    assertEquals(0, controller.getMqttMessageQos());
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（必須フィールドなし→例外）")
  void testHandleMessage1() {
    assertIllegalArgumentAndNoSave("{\"uaslReservationId\":\"001\",\"status\":\"RESERVED\"}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する キャンセル")
  void testHandleMessage2() throws Exception {
    assertDeleteReservationCalled("001", "{\"uaslReservationId\":\"001\",\"status\":\"CANCELED\"}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 廃止")
  void testHandleMessage3() throws Exception {
    assertDeleteReservationCalled(
        "001", "{\"uaslReservationId\":\"001\",\"status\":\"RESCINDED\"}");
  }

  /**
   * saveReservation が1回呼び出されること、deleteUaslReservation が呼び出されないことを検証するヘルパー。
   *
   * @param payloadJson テスト用MQTTペイロードJSON文字列
   */
  private void assertSaveReservationCalled(String payloadJson) throws Exception {
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    doNothing()
        .when(service)
        .saveReservation(
            any(WebUaslReservationDto.class), any(LinkageInformationNotificationDto.class));
    controller.handleMessage("topic", message);
    verify(service, times(1))
        .saveReservation(
            any(WebUaslReservationDto.class), any(LinkageInformationNotificationDto.class));
    verify(service, never()).deleteUaslReservation(any());
  }

  /**
   * handleMessage を実行して {@link WebUaslReservationDto} をキャプチャするヘルパー。
   *
   * <p>computeOverallEvaluation 系テストで共通的に使用するためのセットアップ・実行・キャプチャをまとめたメソッドです。
   *
   * @param payloadJson テスト用MQTTペイロードJSON文字列
   * @return saveReservation に渡された {@link WebUaslReservationDto}
   */
  private WebUaslReservationDto captureReservationDto(String payloadJson) throws Exception {
    ArgumentCaptor<WebUaslReservationDto> captor =
        ArgumentCaptor.forClass(WebUaslReservationDto.class);
    doNothing().when(service).saveReservation(captor.capture(), any());
    controller.handleMessage("topic", new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8)));
    return captor.getValue();
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（originReservation）正常系 → saveReservation呼び出し")
  void testHandleMessage4_reservedOrigin() throws Exception {
    assertSaveReservationCalled(
        "{\"eventId\":\"evt-001\",\"requestId\":\"REQ-001\",\"operatorId\":\"OP-1\","
            + "\"status\":\"RESERVED\",\"reservedAt\":\"2026-02-13T09:00:00.000Z\",\"originReservation\":{"
            + "  \"reservationId\":\"RES-001\",  \"uaslSections\":[{\"uaslSectionId\":\"SEC-A-1\", "
            + "   \"startAt\":\"2026-02-13T09:00:00Z\",\"endAt\":\"2026-02-13T09:30:00Z\"}], "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JU1234567890AB\",\"aircraftInfoId\":1}}]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（originReservation vehicles空）→ 例外")
  void testHandleMessage_originEmptyVehicles() {
    assertIllegalArgumentAndNoSave(
        "{"
            + "\"status\":\"RESERVED\","
            + "\"originReservation\":{"
            + "  \"reservationId\":\"RES-010\","
            + "  \"uaslSections\":[{\"uaslSectionId\":\"SEC-A-1\","
            + "    \"startAt\":\"2026-02-18T09:00:00Z\",\"endAt\":\"2026-02-18T09:30:00Z\"}],"
            + "  \"vehicles\":[]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（destination/フラット）正常系 → saveReservation呼び出し")
  void testHandleMessage5_reservedDestination() throws Exception {
    assertSaveReservationCalled(
        "{\"requestId\":\"REQ-002\",\"reservationId\":\"RES-002\",\"operatorId\":\"OP-2\","
            + "\"status\":\"RESERVED\",\"reservedAt\":\"2026-02-15T10:00:00.000Z\",\"uaslSections\":[{\"uaslSectionId\":\"SEC-B-1\","
            + "  \"startAt\":\"2026-02-15T10:00:00Z\",\"endAt\":\"2026-02-15T10:30:00Z\"}],"
            + "\"operatingAircrafts\":[{\"registrationId\":\"JU0000000004CC\",\"aircraftInfoId\":4}]"
            + "}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（destination operatingAircrafts空）→ 例外")
  void testHandleMessage_destinationEmptyOperatingAircrafts() {
    assertIllegalArgumentAndNoSave(
        "{"
            + "\"status\":\"RESERVED\","
            + "\"reservationId\":\"RES-013\","
            + "\"uaslSections\":[{\"uaslSectionId\":\"SEC-B-1\","
            + "  \"startAt\":\"2026-02-18T11:00:00Z\",\"endAt\":\"2026-02-18T11:30:00Z\"}],"
            + "\"operatingAircrafts\":[]"
            + "}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（uaslSectionsなし、uaslSectionIdsあり）")
  void testHandleMessage_noSections_fallbackUaslSectionIds() throws Exception {
    assertSaveReservationCalled(
        "{\"requestId\":\"REQ-003\",\"operatorId\":\"OP-1\",\"status\":\"RESERVED\","
            + "\"reservedAt\":\"2026-02-16T08:00:00.000Z\",\"uaslSectionIds\":[\"SEC-A-1\"],\"originReservation\":{"
            + "  \"reservationId\":\"RES-003\", "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JU0000000006FF\",\"aircraftInfoId\":6}}]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（uaslSectionsなし・uaslSectionIdsなし）")
  void testHandleMessage_noSectionsAtAll() throws Exception {
    assertSaveReservationCalled(
        "{\"requestId\":\"REQ-004\",\"operatorId\":\"OP-1\",\"status\":\"RESERVED\",\"reservedAt\":\"2026-02-16T09:00:00.000Z\",\"originReservation\":{"
            + "  \"reservationId\":\"RES-004\", "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JU0000000007GG\",\"aircraftInfoId\":7}}]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（複数区画、start最早更新）")
  void testHandleMessage_multipleSections_earliestStartUpdate() throws Exception {
    assertSaveReservationCalled(
        "{\"requestId\":\"REQ-005\",\"operatorId\":\"OP-1\",\"status\":\"RESERVED\",\"reservedAt\":\"2026-02-14T08:00:00.000Z\",\"originReservation\":{"
            + "  \"reservationId\":\"RES-005\",  \"uaslSections\":[   "
            + " {\"uaslSectionId\":\"SEC-A-2\",\"startAt\":\"2026-02-14T08:20:00Z\",\"endAt\":\"2026-02-14T08:40:00Z\"},"
            + "    {\"uaslSectionId\":\"SEC-A-1\",\"startAt\":\"2026-02-14T08:00:00Z\",\"endAt\":\"2026-02-14T09:00:00Z\"}"
            + "  ], "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JU0000000002AA\",\"aircraftInfoId\":2}}]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（startAt/endAtがISO形式でない→fallback使用）")
  void testHandleMessage_fallbackStartEnd() throws Exception {
    assertSaveReservationCalled(
        "{\"requestId\":\"REQ-006\",\"operatorId\":\"OP-1\",\"status\":\"RESERVED\",\"originReservation\":{"
            + "  \"reservationId\":\"RES-006\",  \"uaslSections\":[{\"uaslSectionId\":\"SEC-A-1\", "
            + "   \"startAt\":\"not-a-date\",\"endAt\":\"also-not-a-date\"}], "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JU0000000008HH\",\"aircraftInfoId\":8}}]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（uaslSectionIdのないセクション）→ sectionIds=null")
  void testHandleMessage_sectionWithNoSectionId() throws Exception {
    assertSaveReservationCalled(
        "{\"requestId\":\"REQ-007\",\"operatorId\":\"OP-1\",\"status\":\"RESERVED\",\"originReservation\":{"
            + "  \"reservationId\":\"RES-007\", "
            + " \"uaslSections\":[{\"startAt\":\"2026-02-14T08:00:00Z\",\"endAt\":\"2026-02-14T09:00:00Z\"}],"
            + "  \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JU0000000009II\",\"aircraftInfoId\":9}}]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（uaslSectionsがオブジェクト単体）→ 単一セクションとして処理")
  void testHandleMessage_uaslSectionsAsMapObject() throws Exception {
    assertSaveReservationCalled(
        "{\"requestId\":\"REQ-008\",\"operatorId\":\"OP-1\",\"status\":\"RESERVED\",\"originReservation\":{"
            + "  \"reservationId\":\"RES-008\",  \"uaslSections\":{\"uaslSectionId\":\"SEC-A-1\",  "
            + "  \"startAt\":\"2026-02-14T08:00:00Z\",\"endAt\":\"2026-02-14T09:00:00Z\"}, "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JU0000000010JJ\",\"aircraftInfoId\":10}}]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（requestIdなし）→ saveReservation呼び出し（request_id=null）")
  void testHandleMessage6_reservedNullRequestId() throws Exception {
    assertSaveReservationCalled(
        "{\"operatorId\":\"OP-1\",\"status\":\"RESERVED\",\"reservedAt\":\"2026-02-16T08:00:00.000Z\",\"originReservation\":{"
            + "  \"reservationId\":\"RES-003\",  \"uaslSections\":[{\"uaslSectionId\":\"SEC-A-1\", "
            + "   \"startAt\":\"2026-02-16T08:00:00Z\",\"endAt\":\"2026-02-16T08:30:00Z\"}], "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JU0000000006FF\",\"aircraftInfoId\":6}}]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（originReservationあり・reservationIdなし）→ 例外")
  void testHandleMessage_originReservationNullReservationId() {
    assertIllegalArgumentAndNoSave(
        "{\"status\":\"RESERVED\",\"originReservation\":{ "
            + " \"uaslSections\":[{\"uaslSectionId\":\"SEC-A-1\",   "
            + " \"startAt\":\"2026-02-18T09:00:00Z\",\"endAt\":\"2026-02-18T09:30:00Z\"}], "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JU1234567890AB\",\"aircraftInfoId\":1}}]"
            + "}}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約（originReservationなし・reservationIdなし）→ 例外")
  void testHandleMessage_destinationNullReservationId() {
    assertIllegalArgumentAndNoSave(
        "{\"status\":\"RESERVED\","
            + "\"operatingAircrafts\":[{\"registrationId\":\"JU0000000004CC\",\"aircraftInfoId\":4}]"
            + "}");
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する JSONパース失敗 → 例外")
  void testHandleMessage_invalidJson() {
    String invalidJson = "not-a-json{{{";
    MqttMessage message = new MqttMessage(invalidJson.getBytes(StandardCharsets.UTF_8));
    assertThrows(Exception.class, () -> controller.handleMessage("topic", message));
    verify(service, never()).saveReservation(any(), any());
    verify(service, never()).deleteUaslReservation(any());
  }

  /**
   * handleMessage が {@link IllegalArgumentException} をスローし、 saveReservation が一切呼び出されないことを検証するヘルパー。
   *
   * @param payloadJson テスト用MQTTペイロードJSON文字列
   */
  private void assertIllegalArgumentAndNoSave(String payloadJson) {
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    assertThrows(IllegalArgumentException.class, () -> controller.handleMessage("topic", message));
    verify(service, never()).saveReservation(any(), any());
  }

  /**
   * handleMessage を実行後、deleteUaslReservation が1回呼び出されることを検証するヘルパー。
   *
   * @param reservationId 削除対象の予約ID
   * @param payloadJson テスト用MQTTペイロードJSON文字列
   */
  private void assertDeleteReservationCalled(String reservationId, String payloadJson)
      throws Exception {
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    doNothing().when(service).deleteUaslReservation(eq(reservationId));
    controller.handleMessage("topic", message);
    verify(service, times(1)).deleteUaslReservation(eq(reservationId));
  }

  @Test
  @DisplayName(
      "handleMessage RESERVED: requestIdあり → LinkageDto の"
          + " requestId・uaslReservationId・aircraftInfoId が正しく設定される")
  void testHandleMessage_linkageDto_withRequestId() throws Exception {
    ArgumentCaptor<LinkageInformationNotificationDto> linkageCaptor =
        ArgumentCaptor.forClass(LinkageInformationNotificationDto.class);
    doNothing()
        .when(service)
        .saveReservation(any(WebUaslReservationDto.class), linkageCaptor.capture());

    String json =
        "{\"requestId\":\"REQ-LINK-01\",\"operatorId\":\"OP-1\",\"status\":\"RESERVED\","
            + "\"reservedAt\":\"2026-02-13T09:00:00.000Z\",\"originReservation\":{ "
            + " \"reservationId\":\"RES-LINK-01\", "
            + " \"uaslSections\":[{\"uaslSectionId\":\"SEC-L1\",   "
            + " \"startAt\":\"2026-02-13T09:00:00Z\",\"endAt\":\"2026-02-13T09:30:00Z\"}], "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JULL000001\",\"aircraftInfoId\":11}}]"
            + "}}";
    controller.handleMessage("topic", new MqttMessage(json.getBytes(StandardCharsets.UTF_8)));

    LinkageInformationNotificationDto captured = linkageCaptor.getValue();
    assertEquals("RES-LINK-01", captured.getUaslReservationId());
    assertEquals("REQ-LINK-01", captured.getRequestId());
    assertEquals(11, captured.getAircraftInfoId());
    assertEquals("JULL000001", captured.getUasId().getRegistrationId());
  }

  @Test
  @DisplayName(
      "handleMessage RESERVED: requestIdなし → LinkageDto の requestId が null、他フィールドは正しく設定される")
  void testHandleMessage_linkageDto_nullRequestId() throws Exception {
    ArgumentCaptor<LinkageInformationNotificationDto> linkageCaptor =
        ArgumentCaptor.forClass(LinkageInformationNotificationDto.class);
    doNothing()
        .when(service)
        .saveReservation(any(WebUaslReservationDto.class), linkageCaptor.capture());

    String json =
        "{\"operatorId\":\"OP-1\",\"status\":\"RESERVED\","
            + "\"reservedAt\":\"2026-02-13T09:00:00.000Z\",\"originReservation\":{ "
            + " \"reservationId\":\"RES-LINK-02\", "
            + " \"uaslSections\":[{\"uaslSectionId\":\"SEC-L2\",   "
            + " \"startAt\":\"2026-02-13T10:00:00Z\",\"endAt\":\"2026-02-13T10:30:00Z\"}], "
            + " \"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JULL000002\",\"aircraftInfoId\":22}}]"
            + "}}";
    controller.handleMessage("topic", new MqttMessage(json.getBytes(StandardCharsets.UTF_8)));

    LinkageInformationNotificationDto captured = linkageCaptor.getValue();
    assertEquals("RES-LINK-02", captured.getUaslReservationId());
    assertNull(captured.getRequestId());
    assertEquals(22, captured.getAircraftInfoId());
    assertEquals("JULL000002", captured.getUasId().getRegistrationId());
  }

  @Test
  @DisplayName("computeOverallEvaluation: セクションなし → evaluationResults=true")
  void computeOverall_noSections_returnsTrue() throws Exception {
    String json =
        "{\"status\":\"RESERVED\",\"operatorId\":\"OP-1\",\"originReservation\":{"
            + "\"reservationId\":\"RES-N1\","
            + "\"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JAAAAAAA\",\"aircraftInfoId\":1}}]"
            + "}}";
    assertTrue(captureReservationDto(json).getEvaluationResults());
  }

  @Test
  @DisplayName("computeOverallEvaluation: conformityAssessmentResults にNGあり → evaluationResults=false")
  void computeOverall_oneFalseInSection_returnsFalse() throws Exception {
    String json =
        "{\"status\":\"RESERVED\",\"operatorId\":\"OP-1\",\"originReservation\":{"
            + "\"reservationId\":\"RES-N2\","
            + "\"conformityAssessmentResults\":[{\"evaluationResults\":\"false\"}],"
            + "\"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JABBBBBB\",\"aircraftInfoId\":2}}]"
            + "}}";
    assertFalse(captureReservationDto(json).getEvaluationResults());
  }

  @Test
  @DisplayName(
      "computeOverallEvaluation: conformityAssessmentResults が複数エントリ（一部NGあり）→"
          + " evaluationResults=false")
  void computeOverall_conformityAsMap_returnsFalse() throws Exception {
    String json =
        "{\"status\":\"RESERVED\",\"operatorId\":\"OP-1\",\"originReservation\":{"
            + "\"reservationId\":\"RES-N3\","
            + "\"conformityAssessmentResults\":[{\"evaluationResults\":\"true\"},{\"evaluationResults\":\"false\"}],"
            + "\"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JACCCCCC\",\"aircraftInfoId\":3}}]"
            + "}}";
    assertFalse(captureReservationDto(json).getEvaluationResults());
  }

  @Test
  @DisplayName(
      "computeOverallEvaluation: evaluationResults が boolean false → String.valueOf で false 扱い → evaluationResults=false")
  void computeOverall_evaluationResultsPluralKey_returnsFalse() throws Exception {
    String json =
        "{\"status\":\"RESERVED\",\"operatorId\":\"OP-1\",\"originReservation\":{"
            + "\"reservationId\":\"RES-N4\","
            + "\"conformityAssessmentResults\":[{\"evaluationResults\":false}],"
            + "\"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JADDDDD\",\"aircraftInfoId\":4}}]"
            + "}}";
    assertFalse(captureReservationDto(json).getEvaluationResults());
  }

  @Test
  @DisplayName(
      "computeOverallEvaluation: evaluationResults が文字列 \"false\" → evaluationResults=false")
  void computeOverall_evaluationResultStringFalse_returnsFalse() throws Exception {
    String json =
        "{\"status\":\"RESERVED\",\"operatorId\":\"OP-1\",\"originReservation\":{"
            + "\"reservationId\":\"RES-N5\","
            + "\"conformityAssessmentResults\":[{\"evaluationResults\":\"false\"}],"
            + "\"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JAEEEEE\",\"aircraftInfoId\":5}}]"
            + "}}";
    assertFalse(captureReservationDto(json).getEvaluationResults());
  }

  @Test
  @DisplayName(
      "computeOverallEvaluation: conformityAssessmentResults にevaluationResult キーなし →"
          + " evaluationResults=true")
  void computeOverall_noEvaluationKey_returnsTrue() throws Exception {
    String json =
        "{\"status\":\"RESERVED\",\"operatorId\":\"OP-1\",\"originReservation\":{"
            + "\"reservationId\":\"RES-N6\",\"uaslSections\":[{\"uaslSectionId\":\"SEC-A-6\", "
            + " \"conformityAssessmentResults\":[{\"someOtherKey\":\"value\"}]}],"
            + "\"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JAFFFF\",\"aircraftInfoId\":6}}]"
            + "}}";
    assertTrue(captureReservationDto(json).getEvaluationResults());
  }

  @Test
  @DisplayName(
      "computeOverallEvaluation: conformityAssessmentResults リスト内に非Mapアイテム → スキップされ"
          + " evaluationResults=true")
  void computeOverall_nonMapItemInResultsList_skipped() throws Exception {
    String json =
        "{\"status\":\"RESERVED\",\"operatorId\":\"OP-1\",\"originReservation\":{"
            + "\"reservationId\":\"RES-N7\",\"uaslSections\":[{\"uaslSectionId\":\"SEC-A-7\", "
            + " \"conformityAssessmentResults\":[\"not-a-map\"]}],"
            + "\"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JAGGGGG\",\"aircraftInfoId\":7}}]"
            + "}}";
    assertTrue(captureReservationDto(json).getEvaluationResults());
  }

  @Test
  @DisplayName(
      "computeOverallEvaluation: conformityAssessmentResults がトップレベルに存在しNG検出 → evaluationResults=false")
  void computeOverall_emptyOriginSections_fallbackToTopLevel_returnsFalse() throws Exception {
    String json =
        "{\"status\":\"RESERVED\",\"operatorId\":\"OP-1\","
            + "\"originReservation\":{\"reservationId\":\"RES-N8\","
            + "\"conformityAssessmentResults\":[{\"evaluationResults\":\"false\"}],"
            + "\"vehicles\":[{\"aircraftInfo\":{\"registrationId\":\"JAHHHHH\",\"aircraftInfoId\":8}}]"
            + "}}";
    assertFalse(captureReservationDto(json).getEvaluationResults());
  }
}
