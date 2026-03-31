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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import jakarta.annotation.PreDestroy;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.controller.mqtt.AbstractMqttAsyncController;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * MQTTクライアントのセットアップと接続の管理クラスです。
 */
@Component
public class MqttSubscribeManager {
  /** ロガー */
  protected final Logger logger = LoggerFactory.getLogger(this.getClass());

  /** MQTTブローカのホスト */
  @Value("${mqtt.host}")
  private String mqttHost;

  /** MQTTブローカのポート */
  @Value("${mqtt.port}")
  private String mqttPort;

  /** MQTTクライアントID */
  @Value("${mqtt.subscribe.clientID}")
  private String clientID;

  /** MQTTコールバックハンドラーを集約したオブジェクト */
  private CompositeMqttCallback compositeCallback = new CompositeMqttCallback();

  /** MQTT非同期クライアント */
  protected IMqttAsyncClient client;

  /**
   * MQTTクライアントを初期化し、ブローカーに接続します。
   * 
   * @param controller mattのsubscribeコントローラー
   * @throws MqttException MQTT例外
   */
  public void init(AbstractMqttAsyncController controller) throws MqttException {
    if (client == null || !client.isConnected()) {
      String brokerUrl = "tcp://" + mqttHost + ":" + mqttPort;
      client = new MqttAsyncClient(brokerUrl, clientID, new MemoryPersistence());
      compositeCallback.addCallback(controller.getTopic(), new MqttCallbackHandler(controller));
      client.setCallback(compositeCallback);
      MqttConnectOptions options = setOptions();
      connectWithRetry(options, 0, controller);
    } else {
      compositeCallback.addCallback(controller.getTopic(), new MqttCallbackHandler(controller));
      IMqttToken subToken = client.subscribe(controller.getTopic(), controller.getMqttMessageQos());
      subToken.waitForCompletion();
    }
  }

  /**
   * MqttConnectOptionsを設定する。
   * 
   * @return MqttConnectOptions
   */
  private MqttConnectOptions setOptions() {
    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setConnectionTimeout(PropertyUtil.getPropertyInt("mqtt.connection.timeout")); // タイムアウトを増やす
    options.setKeepAliveInterval(PropertyUtil.getPropertyInt("mqtt.keep.alive.interval")); // キープアライブ間隔を増やす
    options.setCleanSession(false);
    options.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
    return options;
  }

  /**
   * 指定されたオプションとコントローラーでMQTTブローカーに接続します。接続に失敗した場合は最大リトライ回数まで再試行し、全て失敗した場合は例外をスローします。
   *
   * @param options MQTT接続オプション
   * @param controller MQTTのsubscribeコントローラー
   * @throws MqttSecurityException
   * @throws MqttException MQTT例外が発生した場合
   */
  private void connectWithRetry(MqttConnectOptions options, int retryCount,
      AbstractMqttAsyncController controller) throws MqttSecurityException, MqttException {
    final int maxRetries = PropertyUtil.getPropertyInt("mqtt.connect.max.retries");
    final int retryInterval = PropertyUtil.getPropertyInt("mqtt.connect.retry.interval");

    for (int attempt = 1; attempt <= maxRetries; attempt++) {
      if (tryConnectOnce(options, controller, attempt)) {
        return;
      }
      logger.warn("接続試行失敗: attempt {} / {}", attempt, maxRetries);
      if (attempt == maxRetries) {
        logger.error("最大接続試行回数に達しました。試行回数: {}", attempt);
        throw new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION);
      }
      sleepBeforeRetry(retryInterval);
    }
  }

  /**
   * 1回だけMQTTブローカーへの接続を試み、コールバックで結果を待機します。
   *
   * @param options MQTT接続オプション
   * @param controller MQTTのsubscribeコントローラー
   * @return true: 接続成功 / false: 接続失敗
   */
  boolean tryConnectOnce(
      MqttConnectOptions options, AbstractMqttAsyncController controller, int attempt) {
    CountDownLatch latch = new CountDownLatch(1);
    try {
      client.connect(
          options,
          null,
          new IMqttActionListener() {
            @Override
            public void onSuccess(IMqttToken asyncActionToken) {
              handleConnectSuccess(controller, latch);
            }

            @Override
            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
              logger.warn("接続試行に失敗しました (試行回数: {}): {}", attempt, exception.getMessage(), exception);
              handleConnectFailure(exception, latch);
            }
          });
      latch.await();
      return client.isConnected();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      logger.error("接続試行に失敗しました (試行回数: {}): {}", attempt, e.getMessage(), e);
      return false;
    } catch (MqttException e) {
      logger.error("接続試行に失敗しました (試行回数: {}): {}", attempt, e.getMessage(), e);
      return false;
    }
  }

  /**
   * 指定されたミリ秒だけスリープし、割り込み時はログ出力します。
   *
   * @param retryInterval リトライ間隔（ミリ秒）
   */
  void sleepBeforeRetry(int retryInterval) {
    try {
      Thread.sleep(retryInterval);
    } catch (InterruptedException ie) {
      Thread.currentThread().interrupt();
      logger.error("再試行待機中に割り込みが発生しました: {}", ie.getMessage(), ie);
    }
  }

  /**
   * MQTT接続成功時にトピックへサブスクライブを試みます。 サブスクライブに失敗した場合は、エラーログを出力し、接続は成功として扱われます。
   *
   * @param controller MQTTのsubscribeコントローラー
   * @param latch 非同期完了通知用ラッチ
   */
  void handleConnectSuccess(AbstractMqttAsyncController controller, CountDownLatch latch) {
    try {
      logger.debug("接続に成功しました");
      IMqttToken subToken = client.subscribe(controller.getTopic(), controller.getMqttMessageQos());
      subToken.waitForCompletion();
    } catch (MqttException e) {
      logger.error("サブスクライブに失敗しました : {}", e.getMessage(), e);
    } finally {
      latch.countDown();
    }
  }

  /**
   * MQTT接続失敗時にエラーログを出力し、ラッチをカウントダウンします。
   *
   * @param exception 発生した例外
   * @param latch 非同期完了通知用ラッチ
   */
  void handleConnectFailure(Throwable exception, CountDownLatch latch) {
    logger.warn("接続に失敗しました : {}", exception.getMessage(), exception);
    latch.countDown();
  }

  /**
   * アプリケーション終了時にMQTTクライアントをクローズします。
   */
  @PreDestroy
  public void close() {
    try {
      if (client != null) {
        client.disconnect();
        client.close();
      }
    } catch (MqttException e) {
      logger.error("{}", e.getMessage(), e);
    }
  }

  /**
   * MQTTコールバックハンドラークラス。
   */
  private class MqttCallbackHandler implements MqttCallback {
    private final AbstractMqttAsyncController controller;

    public MqttCallbackHandler(AbstractMqttAsyncController controller) {
      logger.debug("コントローラーを取得しました: {}", controller.getClass().getSimpleName());
      this.controller = controller;
    }

    @Override
    public void connectionLost(Throwable cause) {
      // 接続喪失時の処理
      // 再接続を試みる
      logger.error("接続を喪失しました");
      try {
        if (client != null && !client.isConnected()) {
          connectWithRetry(setOptions(), 0, controller);
        }
      } catch (MqttException e) {
        logger.error("再接続に失敗しました: " + e.getMessage(), e);
      }
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
      try {
        // ログ出力
        logger.debug("[MQTT] -> MsgBroker: " + topic);
        controller.handleMessage(topic, message);
      } catch (Exception e) {
        logger.error("メッセージ処理に失敗しました: " + e.getMessage(), e);
      }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
      // 配信完了時の処理
      logger.debug("配信完了しました");
    }
  }

  /**
   * MQTTコールバックハンドラーを集約したクラス。
   */
  public class CompositeMqttCallback implements MqttCallback {
    /** topicとコールバックを格納 */
    private final Map<String, MqttCallback> topicCallbacks = new HashMap<>();

    /** コールバックをtopicをキーにして追加 */
    public void addCallback(String topic, MqttCallback callback) {
      topicCallbacks.put(topic, callback);
    }

    @Override
    public void connectionLost(Throwable cause) {
      // 処理は各ハンドラーに任せるため何もしない
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
      for (Map.Entry<String, MqttCallback> entry : topicCallbacks.entrySet()) {
        if (topicMatches(entry.getKey(), topic)) {
          entry.getValue().messageArrived(topic, message);
          break;
        }
      }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
      // 処理は各ハンドラーに任せるため何もしない
    }

    /**
     * topicのワイルドカードの変換処理
     * 
     * @param pattern 設定されているtopic
     * @param topic 受信したtopic
     * @return true:一致 / false:不一致
     */
    private boolean topicMatches(String pattern, String topic) {
      String regex = pattern.replace("+", "[^/]+").replace("#", ".+");
      return Pattern.matches(regex, topic);
    }
  }
}

