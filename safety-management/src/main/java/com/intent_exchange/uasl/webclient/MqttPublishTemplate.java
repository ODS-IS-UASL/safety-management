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

package com.intent_exchange.uasl.webclient;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.exception.UaslException;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * MQTT publish用クラス
 */
@Component
public class MqttPublishTemplate {

  /** MQTTクライアントのインスタンス。 このインスタンスを使用してMQTTブローカーと通信します。 */
  @Autowired
  private MqttClient mqttClient;

  /** ロガー */
  Logger logger = LoggerFactory.getLogger(MqttPublishTemplate.class);

  /**
   * MQTT通信でメッセージをpublishする。
   * 
   * @param topic メッセージをpublishするtopic
   * @param message メッセージ
   * @param qos 通信品質
   */
  public void publish(String topic, MqttMessage message, int qos) {
    try {
      if (!mqttClient.isConnected()) {
        reconnect();
      }
      message.setQos(qos);
      // topicへのpublish
      mqttClient.publish(topic, message);
    } catch (MqttException e) {
      throw new UaslException(e.getMessage());
    }
    // ログ出力
    logger.debug("[MQTT] -> MsgBroker: " + topic);
  }

  /**
   * 再接続
   */
  private void reconnect() {
    try {
      MqttConnectOptions options = new MqttConnectOptions();
      options.setAutomaticReconnect(true);
      options.setConnectionTimeout(PropertyUtil.getPropertyInt("mqtt.connection.timeout"));
      options.setKeepAliveInterval(PropertyUtil.getPropertyInt("mqtt.keep.alive.interval"));
      options.setCleanSession(true);
      mqttClient.connect(options);
    } catch (MqttException e) {
      throw new UaslException("MQTT reconnect failed", e);
    }
  }
}

