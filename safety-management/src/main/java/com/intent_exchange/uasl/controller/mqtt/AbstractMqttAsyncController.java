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

import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.intent_exchange.uasl.webclient.MqttSubscribeManager;

/**
 * MQTTクライアントのセットアップと接続ロジックを処理します。
 */
@Controller
public abstract class AbstractMqttAsyncController {

  /** ロガー */
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  /** MQTT Subscribe マネージャー */
  @Autowired
  private MqttSubscribeManager mqttSubscribeManager;

  @PostConstruct
  public void init() {
    try {
      mqttSubscribeManager.init(this);
    } catch (MqttException e) {
      logger.error("MQTTクライアントの初期化に失敗しました: " + e.getMessage(), e);
    }
  }

  /**
   * MQTTトピックを返却する抽象メソッド。
   * 
   * @return MQTTトピック
   */
  public abstract String getTopic();

  /**
   * 通信品質を返却する抽象メソッド。
   * 
   * @return 通信品質
   */
  public abstract int getMqttMessageQos();

  /**
   * メッセージを処理する抽象メソッド。
   * 
   * @param topic トピック
   * @param message メッセージ
   * @throws Exception 例外
   */
  public abstract void handleMessage(String topic, MqttMessage message) throws Exception;

}

