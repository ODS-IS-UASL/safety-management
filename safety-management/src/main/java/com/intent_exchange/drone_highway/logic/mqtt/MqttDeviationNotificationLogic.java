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

package com.intent_exchange.drone_highway.logic.mqtt;

import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dto.request.AirwayDeviationNotificationDto;
import com.intent_exchange.drone_highway.dto.request.AirwayDeviationNotificationDto.Coordinates;
import com.intent_exchange.drone_highway.dto.request.DroneLocationNotificationDto;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;
import com.intent_exchange.drone_highway.util.PropertyUtil;
import com.intent_exchange.drone_highway.webclient.MqttPublishTemplate;

/**
 * 外部システム連携(A-1-6)への通信用ロジック
 */
@Component
public class MqttDeviationNotificationLogic {

  @Autowired
  private MqttPublishTemplate mqttTemplate;

  /** 通信品質(Quality of Service)。 確実に1回到達を保証 */
  private final int MQTT_MESSAGE_QOS = PropertyUtil.getPropertyInt("mqtt.qos.exactryOnce");

  /**
   * 逸脱検知通知を外部システム連携(A-1-6)に送信。
   *
   * @param droneLocationNotification のdto
   */
  public void notifyAirwayDeviation(DroneLocationNotificationDto dto) {
    // Coordinates 作成
    Coordinates coordinates = new AirwayDeviationNotificationDto().new Coordinates();
    coordinates.setLatitude(dto.getLatitude());
    coordinates.setLongitude(dto.getLongitude());
    coordinates.setAboveGroundLevel(dto.getAboveGroundLevel());
    // 逸脱通知作成
    AirwayDeviationNotificationDto notifyDto = new AirwayDeviationNotificationDto();
    notifyDto.setAirwayReservationId(dto.getReservationId());
    notifyDto.setOperationalStatus(dto.getOperationalStatus());
    notifyDto.setAirwaySectionId(dto.getAirwaySectionId());
    notifyDto.setCoordinates(coordinates);
    notifyDto.setAircraftId(dto.getUasId());
    notifyDto.setDeviateAt(dto.getGetLocationTimestamp());
    notifyDto.setFlightTime(dto.getFlightTime());
    notifyDto.setOperatorId(dto.getOperatorId());
    // MQTT通信
    // ペイロード（DTOをJSON文字列に変換したもの）
    String payloadJson = ModelMapperUtil.mapToJson(ModelMapperUtil.convertDtoToMap(notifyDto));
    // トピック(運航者IDを埋め込み）
    String topic = PropertyUtil.getProperty("drone.location.topic", dto.getOperatorId());
    // メッセージ
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    // メッセージをpublish
    mqttTemplate.publish(topic, message, MQTT_MESSAGE_QOS);
  }
}

