package com.intent_exchange.uasl.logic.mqtt;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.DroneLocationNotificationDto;
import com.intent_exchange.uasl.util.PropertyUtil;
import com.intent_exchange.uasl.webclient.MqttPublishTemplate;

/**
 * 外部システム連携への通信用ロジックのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class MqttDeviationNotificationLogicTest {

  @Mock
  private MqttPublishTemplate mqttTemplate;

  @InjectMocks
  private MqttDeviationNotificationLogic mqttDeviationNotificationLogic; // テスト対象のクラス

  private final int MQTT_MESSAGE_QOS = PropertyUtil.getPropertyInt("mqtt.qos.exactryOnce");

  @Test
  @DisplayName("逸脱検知通知を外部システム連携に送信する")
  public void testNnotifyUaslDeviation1() {
    DroneLocationNotificationDto dto = new DroneLocationNotificationDto();
    dto.setSubscriptionId("subscriptionId01");
    dto.setUasId("usasId01");
    dto.setUaType("uaType01");
    dto.setGetLocationTimestamp("2024-11-13T10:42:00.000Z");
    dto.setLatitude(43.0457375);
    dto.setLongitude(141.3553984);
    dto.setAltitude(75);
    dto.setTrackDirection(90);
    dto.setSpeed(10.0);
    dto.setVerticalSpeed(1.0);
    dto.setRouteDeviationRate(0.0);
    dto.setRouteDeviationRateUpdateTime("2024-11-13T10:42:00.000Z");
    dto.setReservationId("reservationId01");
    dto.setUaslId("uaslId01");
    dto.setUaslSectionId("uaslSectionId01");
    dto.setOperationalStatus("NormalOperation");
    dto.setOperatorId("operatorId01");
    dto.setFlightTime("00:00:00.000");
    doNothing().when(mqttTemplate)
        .publish(anyString(), any(MqttMessage.class), eq(MQTT_MESSAGE_QOS));
    mqttDeviationNotificationLogic.notifyUaslDeviation(dto);
    verify(mqttTemplate, times(1)).publish(anyString(), any(MqttMessage.class),
        eq(MQTT_MESSAGE_QOS));
  }
}
