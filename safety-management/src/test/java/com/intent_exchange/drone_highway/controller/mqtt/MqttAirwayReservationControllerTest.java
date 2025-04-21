package com.intent_exchange.drone_highway.controller.mqtt;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.service.AirwayReservationService;

@ExtendWith(MockitoExtension.class)
class MqttAirwayReservationControllerTest extends MqttAirwayReservationController {

  @Mock
  private AirwayReservationService service;

  @InjectMocks
  private MqttAirwayReservationController controller;

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 予約")
  void testHandleMessage1() throws Exception {
    String topic = "topic";
    String payloadJson = "{\"airwayReservationId\":\"001\",\"status\":\"RESERVED\"}";
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    // doNothing().when(service).deleteAirwayReservation(eq("001"));
    controller.handleMessage(topic, message);
    verify(service, times(0)).deleteAirwayReservation(eq("001"));
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する キャンセル")
  void testHandleMessage2() throws Exception {
    String topic = "topic";
    String payloadJson = "{\"airwayReservationId\":\"001\",\"status\":\"CANCELED\"}";
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    doNothing().when(service).deleteAirwayReservation(eq("001"));
    controller.handleMessage(topic, message);
    verify(service, times(1)).deleteAirwayReservation(eq("001"));
  }

  @Test
  @DisplayName("航路予約に関連するMQTTメッセージを処理する 廃止")
  void testHandleMessage3() throws Exception {
    String topic = "topic";
    String payloadJson = "{\"airwayReservationId\":\"001\",\"status\":\"RESCINDED\"}";
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    doNothing().when(service).deleteAirwayReservation(eq("001"));
    controller.handleMessage(topic, message);
    verify(service, times(1)).deleteAirwayReservation(eq("001"));
  }
}
