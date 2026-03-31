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

package com.intent_exchange.uasl.controller.mqtt;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import com.intent_exchange.uasl.dto.request.RestrictedAirSpaceDto;
import com.intent_exchange.uasl.dto.response.RestrictedAirSpaceResponseDto;
import com.intent_exchange.uasl.service.RestrictedAirSpaceNotificationService;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import lombok.Getter;

/**
 * 規制/イベントに関連するMQTTメッセージを処理するコントローラークラス。
 */
@Controller
public class MqttRestrictedAirSpaceController extends AbstractMqttAsyncController {

  /** MQTTトピック */
  @Value("${uasl.event.topic}")
  @Getter
  private String topic;

  /** 通信品質(Quality of Service)。 確実に1回到達を保証 */
  @Value("${mqtt.qos.exactryOnce}")
  @Getter
  private int mqttMessageQos;

  /** 規制/イベントサービス */
  @Autowired
  private RestrictedAirSpaceNotificationService service;
  
  /**
   * メッセージを処理します。
   * 
   * @param topic トピック
   * @param message メッセージ
   * @throws Exception 例外
   */
  @Override
  public void handleMessage(String topic, MqttMessage message) throws Exception {
    String payload = new String(message.getPayload(), StandardCharsets.UTF_8);
    Map<String, Object> map = ModelMapperUtil.jsonToMap(payload);
    String areaInfo = map.get("restrictAirSpace").toString();
    areaInfo = areaInfo.replaceAll("(type|coordinates|Polygon)", "\"$1\"");
    areaInfo = areaInfo.replaceAll("=", ":");
    service.notifyRestrictedAirSpace(areaInfo);
  }
}

