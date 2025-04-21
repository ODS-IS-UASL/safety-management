package com.intent_exchange.drone_highway.controller.mqtt;

import static org.mockito.Mockito.*;
import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dto.response.RestrictedAirSpaceResponseDto;
import com.intent_exchange.drone_highway.service.RestrictedAirSpaceNotificationService;

@ExtendWith(MockitoExtension.class)
class MqttRestrictedAirSpaceControllerTest {

  @Mock
  private RestrictedAirSpaceNotificationService service;

  @InjectMocks
  private MqttRestrictedAirSpaceController controller;

  @Test
  @DisplayName("規制/イベントに関連するMQTTメッセージを処理する")
  void testHandleMessage1() throws Exception {
    String topic = "topic";
    String payloadJson =
        "{\"restrictAirSpace\": {\"type\": \"Polygon\", \"coordinates\": [[[135.760497, 35.012033], [135.760497, 30.011655], [130.76097, 35.011655], [125.76097, 25.012033], [135.760497, 35.012033]]]}, \"restrictTimeScope\": [\"2025-01-10T01:00:00Z\", \"2025-01-10T03:00:00Z\"], \"description\": \"\\u822a\\u7a7a\\u30a4\\u30d9\\u30f3\\u30c8\\u306b\\u3088\\u308b\\u898f\\u5236\", \"notification\": \"Add\"}";
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    String areaInfo = "{\"type\":\"Polygon\", \"coordinates\":[[[135.760497, 35.012033], [135.760497, 30.011655], [130.76097, 35.011655], [125.76097, 25.012033], [135.760497, 35.012033]]]}";

    controller.handleMessage(topic, message);
    verify(service, times(1)).notifyRestrictedAirSpace(areaInfo);
  }

}
