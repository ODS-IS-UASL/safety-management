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

package com.intent_exchange.drone_highway.listener;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * MQTTクライアントのライフサイクルを管理するリスナークラス。
 */
@Component
public class MqttLifecycleListener implements ApplicationListener<ContextRefreshedEvent> {

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    // Do nothing on startup, as the client is already connected in the MqttConfig
  }

  @Component
  public static class MqttShutdownListener implements ApplicationListener<ContextClosedEvent> {

    /** MQTTクライアント ※アプリ起動時に生成 */
    @Autowired
    private MqttClient mqttClient;

    /**
     * アプリ終了イベント
     * 
     * @param event アプリ終了イベント
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
      // MQTTクライアントが接続中の場合、切断
      if (mqttClient != null && mqttClient.isConnected()) {
        try {
          mqttClient.disconnect();
          mqttClient.close();
        } catch (MqttException e) {
          e.printStackTrace();
        }
      }
    }
  }
}

