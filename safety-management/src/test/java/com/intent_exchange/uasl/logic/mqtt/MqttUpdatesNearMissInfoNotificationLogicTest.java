package com.intent_exchange.uasl.logic.mqtt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.NotifyUpdatesNearMissInfoDto;
import com.intent_exchange.uasl.util.PropertyUtil;
import com.intent_exchange.uasl.webclient.MqttPublishTemplate;

/**
 * MqttUpdatesNearMissInfoNotificationLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class MqttUpdatesNearMissInfoNotificationLogicTest {

  /** MQTT publish用クラスのMock  */
  @Mock
  private MqttPublishTemplate mqttTemplate;

  /** テスト対象クラス */
  @InjectMocks
  private MqttUpdatesNearMissInfoNotificationLogic mqttUpdatesNearMissInfoNotificationLogic;

  /** 通信品質(Quality of Service)。 確実に1回到達を保証 */
  private final int MQTT_MESSAGE_QOS = PropertyUtil.getPropertyInt("mqtt.qos.exactryOnce");

  @Test
  @DisplayName("ヒヤリハット情報更通知を外部システム連携に送信する")
  public void testNotifyUpdatesNearMissInfo1() {
    
    // ペイロードに設定するJSON文字列の基となる通知用DTO
    NotifyUpdatesNearMissInfoDto notifyDto = new NotifyUpdatesNearMissInfoDto();
    List<String> deviantUaslIds = new ArrayList<>();
    deviantUaslIds.add("uasl1");
    deviantUaslIds.add("uasl2");
    notifyDto.setDeviantUaslIds(deviantUaslIds);
    List<String> entryUaslIds = new ArrayList<>();
    entryUaslIds.add("uasl1");
    entryUaslIds.add("uasl3");
    notifyDto.setEntryUaslIds(entryUaslIds);
    notifyDto.setReportingStartAt("2024-12-01T00:00:00");
    notifyDto.setReportingEndAt("2024-12-01T00:00:00");
    
    // MQTT通信処理をMock化（何もしない）
    doNothing().when(mqttTemplate)
        .publish(anyString(), any(MqttMessage.class), eq(MQTT_MESSAGE_QOS));
    
    // メソッドの実行
    mqttUpdatesNearMissInfoNotificationLogic.notifyUpdatesNearMissInfo("admin1", notifyDto);

    // 結果の検証(MQTTメッセージのpublish処理が正しく呼び出されることを確認）
    // ペイロードに設定するJSON文字列の期待値
    String expectedPayloadJson = "{\"deviantUaslIds\":[\"uasl1\",\"uasl2\"],\"entryUaslIds\":[\"uasl1\",\"uasl3\"],\"reportingStartAt\":\"2024-12-01T00:00:00\",\"reportingEndAt\":\"2024-12-01T00:00:00\"}";
    // MqttMessageオブジェクトは、eqメソッドでの比較に対応していないため、ArgumentCaptorを使用して個別で検証する
    ArgumentCaptor<MqttMessage> messageCaptor = ArgumentCaptor.forClass(MqttMessage.class);
    verify(mqttTemplate, times(1)).publish(
        eq(PropertyUtil.getProperty("notify.updatesNearMissInfo.topic", "admin1")),
        messageCaptor.capture(),
        eq(MQTT_MESSAGE_QOS));
    assertEquals(expectedPayloadJson, new String(messageCaptor.getValue().getPayload(), StandardCharsets.UTF_8));
  }
}
