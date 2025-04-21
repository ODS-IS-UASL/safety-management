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

package com.intent_exchange.drone_highway.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * MQTT通信のための設定クラス。
 */
@Configuration
public class MqttConfig {

  /** MQTTブローカのホスト */
  @Value("${mqtt.host}")
  private String mqttHost;
  /** MQTTブローカのポート */
  @Value("${mqtt.port}")
  private String mqttPort;
  /** MQTTクライアントID */
  @Value("${mqtt.clientID}")
  private String mqttClientId;
  /** MQTTクライアン */
  private MqttClient client;

  /**
   * MQTTクライアントを作成し、MQTTブローカーに接続します。※切断はアプリ終了時に実施。
   * 
   * @return MQTTクライアント
   * @throws MqttException MQTT例外
   */
  @Bean
  public MqttClient mqttClient() throws MqttException {
    // MQTTブローカーのURL
    // TODO：MVP2ではプロトコルはTCP固定とする。MVP3以降でMQTTS(TCP+TLS)接続のケースを考慮。
    String brokerUrl = "tcp://" + mqttHost + ":" + mqttPort;
    // MQTTクライアントを作成し、接続
    client = new MqttClient(brokerUrl, mqttClientId, new MemoryPersistence());
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setConnectionTimeout(PropertyUtil.getPropertyInt("mqtt.connection.timeout"));
    options.setKeepAliveInterval(PropertyUtil.getPropertyInt("mqtt.keep.alive.interval"));
    options.setCleanSession(true);
    client.connect(options);
    return client;
  }
}

