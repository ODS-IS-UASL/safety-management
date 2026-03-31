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

package com.intent_exchange.uasl.logic.mqtt;

import java.nio.charset.StandardCharsets;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.dto.request.NotifyUpdatesNearMissInfoDto;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import com.intent_exchange.uasl.util.PropertyUtil;
import com.intent_exchange.uasl.webclient.MqttPublishTemplate;

/**
 * 外部システム連携へのMQTT通信用ロジック<br>
 * ヒヤリハット情報更新通知用
 */
@Component
public class MqttUpdatesNearMissInfoNotificationLogic {

  /** MQTT publish用  */
  @Autowired
  private MqttPublishTemplate mqttTemplate;

  /** 通信品質(Quality of Service)。 確実に1回到達を保証 */
  private final int MQTT_MESSAGE_QOS = PropertyUtil.getPropertyInt("mqtt.qos.exactryOnce");

  /**
   * ヒヤリハット情報更新通知を外部システム連携に送信。
   *
   * @param administratorId 航路運営者ID （topicに埋め込む）
   * @param notifyDto ヒヤリハット情報更新通知用DTO （ペイロードに設定する）
   */
  public void notifyUpdatesNearMissInfo(String administratorId, NotifyUpdatesNearMissInfoDto notifyDto) {
    // ペイロード（適合性評価結果DTOをJSON文字列に変換したもの）
    String payloadJson = ModelMapperUtil.mapToJson(ModelMapperUtil.convertDtoToMap(notifyDto));
    // トピック(航路運営者IDを埋め込み）
    String topic = PropertyUtil.getProperty("notify.updatesNearMissInfo.topic", administratorId);
    // メッセージ
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    // メッセージをpublish
    mqttTemplate.publish(topic, message, MQTT_MESSAGE_QOS);
  }
}

