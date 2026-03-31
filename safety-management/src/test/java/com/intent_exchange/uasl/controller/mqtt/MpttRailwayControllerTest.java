package com.intent_exchange.uasl.controller.mqtt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.response.RailwayOperationNotificationResponseDto;
import com.intent_exchange.uasl.service.RailwayOperationNotificationService;

@ExtendWith(MockitoExtension.class)
class MpttRailwayControllerTest {

  @Mock
  private RailwayOperationNotificationService service;

  @InjectMocks
  private MpttRailwayController controller;

  @Test
  @DisplayName("鉄道運航情報変更通知を処理")
  void testHandleMessage1() throws Exception {
    String topic = "topic";
    String payloadJson =
        "{\"station1\":\"西部秩父駅\",\"station2\":\"横瀬\",\"timeBefore\":[[\"11:08\",\"11:11\"],[\"11:24\",\"11:27\"]],\"timeAfter\":[[\"11:29\",\"11:32\"]]}";
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));

    controller.handleMessage(topic, message);

    ArgumentCaptor<RailwayOperationNotificationResponseDto> captor =
        ArgumentCaptor.forClass(RailwayOperationNotificationResponseDto.class);
    verify(service, times(1)).notifyRailwayOperation(captor.capture());

    RailwayOperationNotificationResponseDto capturedDto = captor.getValue();
    assertEquals("西部秩父駅", capturedDto.getStation1());
    assertEquals("横瀬", capturedDto.getStation2());
    assertEquals(Arrays.asList(Arrays.asList("11:08", "11:11"), Arrays.asList("11:24", "11:27")),
        capturedDto.getTimeBefore());
    assertEquals(Arrays.asList(Arrays.asList("11:29", "11:32")), capturedDto.getTimeAfter());
  }
}
