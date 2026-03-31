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
import com.intent_exchange.uasl.dto.request.NotifyConformityAssessmentResultDto;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import com.intent_exchange.uasl.util.PropertyUtil;
import com.intent_exchange.uasl.webclient.MqttPublishTemplate;

/**
 * 外部システム連携への通信用ロジック
 */
@Component
public class MqttConformityAssessmentLogic {

  @Autowired
  private MqttPublishTemplate mqttTemplate;

  /** 通信品質(Quality of Service)。 確実に1回到達を保証 */
  private final int MQTT_MESSAGE_QOS = PropertyUtil.getPropertyInt("mqtt.qos.exactryOnce");

  /**
   * 運航中航路状況変更通知を外部システム連携に送信。
   *
   * @param conformityAssessmentResult 適合性評価結果のdto
   */
  public void notifyConformityAssessment(ConformityAssessmentResultDto dto) {
    NotifyConformityAssessmentResultDto notifyDto =
        ModelMapperUtil.map(dto, NotifyConformityAssessmentResultDto.class);
    notifyDto.setReasons(dto.getMessage());
    // MQTT通信
    // TODO：航路情報が運航中/予約中で異なるtopic/payloadとする場合は、dto.isInOperationで判断し処理する。
    // ペイロード（適合性評価結果DTOをJSON文字列に変換したもの）
    String payloadJson = ModelMapperUtil.mapToJson(ModelMapperUtil.convertDtoToMap(notifyDto));
    // トピック(運航者IDを埋め込み）
    String topic = PropertyUtil.getProperty("notify.uaslEvaluation.topic", dto.getOperatorId());
    // メッセージ
    MqttMessage message = new MqttMessage(payloadJson.getBytes(StandardCharsets.UTF_8));
    // メッセージをpublish
    mqttTemplate.publish(topic, message, MQTT_MESSAGE_QOS);
  }
}

