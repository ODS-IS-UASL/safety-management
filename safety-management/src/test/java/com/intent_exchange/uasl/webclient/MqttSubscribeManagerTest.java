package com.intent_exchange.uasl.webclient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.intent_exchange.uasl.controller.mqtt.AbstractMqttAsyncController;
import java.util.concurrent.CountDownLatch;
import org.eclipse.paho.client.mqttv3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class MqttSubscribeManagerTest {
  private MqttSubscribeManager manager;
  private AbstractMqttAsyncController controller;
  private IMqttAsyncClient client;
  private MqttConnectOptions options;

  @BeforeEach
  void setUp() throws Exception {
    manager = Mockito.spy(new MqttSubscribeManager());
    controller = mock(AbstractMqttAsyncController.class);
    client = mock(IMqttAsyncClient.class);
    options = mock(MqttConnectOptions.class);
    manager.client = client;
    // mock controller topic/qos
    when(controller.getTopic()).thenReturn("test/topic");
    when(controller.getMqttMessageQos()).thenReturn(1);
    IMqttToken token = mock(IMqttToken.class);
    when(client.subscribe(anyString(), anyInt())).thenReturn(token);
    doNothing().when(token).waitForCompletion();
  }

  @Test
  void tryConnectOnce_success() throws Exception {
    doAnswer(
            invocation -> {
              IMqttActionListener listener = invocation.getArgument(2);
              listener.onSuccess(mock(IMqttToken.class));
              return null;
            })
        .when(client)
        .connect(any(), any(), any());
    doReturn(true).when(client).isConnected();
    boolean result = manager.tryConnectOnce(options, controller, 1);
    assertTrue(result);
  }

  @Test
  void tryConnectOnce_failure() throws Exception {
    doAnswer(
            invocation -> {
              IMqttActionListener listener = invocation.getArgument(2);
              listener.onFailure(mock(IMqttToken.class), new Exception("fail"));
              return null;
            })
        .when(client)
        .connect(any(), any(), any());
    doReturn(false).when(client).isConnected();
    boolean result = manager.tryConnectOnce(options, controller, 1);
    assertFalse(result);
  }

  @Test
  void tryConnectOnce_mqttException() throws Exception {
    doThrow(new MqttException(0)).when(client).connect(any(), any(), any());
    boolean result = manager.tryConnectOnce(options, controller, 1);
    assertFalse(result);
  }

  @Test
  void tryConnectOnce_interruptedException() throws Exception {
    // スレッドを事前に割り込み、モックなしで await() が例外を投げるようにする
    Thread.currentThread().interrupt();
    doAnswer(
            invocation -> {
              IMqttActionListener listener = invocation.getArgument(2);
              listener.onSuccess(mock(IMqttToken.class));
              return null;
            })
        .when(client)
        .connect(any(), any(), any());

    boolean result = manager.tryConnectOnce(options, controller, 1);
    assertFalse(result);
    assertTrue(Thread.currentThread().isInterrupted());
    Thread.interrupted(); // 他のテストに影響しないよう、割り込み状態をクリアする
  }

  @Test
  void handleConnectSuccess_subscribeSuccess() throws Exception {
    IMqttToken token = mock(IMqttToken.class);
    doReturn(token).when(client).subscribe(anyString(), anyInt());
    doNothing().when(token).waitForCompletion();
    CountDownLatch latch = mock(CountDownLatch.class);
    manager.client = client;
    manager.handleConnectSuccess(controller, latch);
    verify(client).subscribe(anyString(), anyInt());
    verify(latch).countDown();
  }

  @Test
  void handleConnectSuccess_subscribeFailure() throws Exception {
    doThrow(new MqttException(0)).when(client).subscribe(anyString(), anyInt());
    doReturn(true).when(client).isConnected(); // connection is successful, subscribe fails
    CountDownLatch latch = mock(CountDownLatch.class);
    manager.client = client;
    manager.handleConnectSuccess(controller, latch);
    verify(latch).countDown();
  }

  @Test
  void handleConnectFailure_logsAndCountsDown() {
    CountDownLatch latch = mock(CountDownLatch.class);
    Throwable ex = new Exception("fail");
    manager.handleConnectFailure(ex, latch);
    verify(latch).countDown();
  }

  @Test
  void sleepBeforeRetry_normal() {
    long start = System.currentTimeMillis();
    manager.sleepBeforeRetry(100);
    assertTrue(System.currentTimeMillis() - start >= 100);
  }

  @Test
  void sleepBeforeRetry_interrupted() {
    Thread.currentThread().interrupt();
    manager.sleepBeforeRetry(10);
    assertTrue(Thread.interrupted());
  }

  @Test
  void connectWithRetry_allRetriesFail_throwsException() throws Exception {
    MqttSubscribeManager spyManager = Mockito.spy(new MqttSubscribeManager());
    doReturn(false).when(spyManager).tryConnectOnce(any(), any(), anyInt());
    doNothing().when(spyManager).sleepBeforeRetry(anyInt());
    int maxRetries = 3;
    int retryInterval = 10;

    try (MockedStatic<com.intent_exchange.uasl.util.PropertyUtil> mocked =
        Mockito.mockStatic(com.intent_exchange.uasl.util.PropertyUtil.class)) {
      mockPropertyUtil(mocked, maxRetries, retryInterval);

      MqttException ex =
          assertThrows(
              MqttException.class, () -> invokeConnectWithRetry(spyManager, options, controller));

      assertEquals(MqttException.REASON_CODE_CLIENT_EXCEPTION, ex.getReasonCode());
      verify(spyManager, times(maxRetries)).tryConnectOnce(any(), any(), anyInt());
      verify(spyManager, times(maxRetries - 1)).sleepBeforeRetry(anyInt());
    }
  }

  @Test
  void connectWithRetry_successOnSecondAttempt() throws Exception {
    MqttSubscribeManager spyManager = Mockito.spy(new MqttSubscribeManager());
    doReturn(false).doReturn(true).when(spyManager).tryConnectOnce(any(), any(), anyInt());
    doNothing().when(spyManager).sleepBeforeRetry(anyInt());
    int maxRetries = 3;
    int retryInterval = 10;

    try (MockedStatic<com.intent_exchange.uasl.util.PropertyUtil> mocked =
        Mockito.mockStatic(com.intent_exchange.uasl.util.PropertyUtil.class)) {
      mockPropertyUtil(mocked, maxRetries, retryInterval);

      invokeConnectWithRetry(spyManager, options, controller);

      verify(spyManager, times(2)).tryConnectOnce(any(), any(), anyInt());
      verify(spyManager, times(1)).sleepBeforeRetry(anyInt());
    }
  }

  @Test
  void connectWithRetry_retryIntervalIsUsed() throws Exception {
    MqttSubscribeManager spyManager = Mockito.spy(new MqttSubscribeManager());
    doReturn(false).when(spyManager).tryConnectOnce(any(), any(), anyInt());
    doNothing().when(spyManager).sleepBeforeRetry(anyInt());
    int maxRetries = 2;
    int retryInterval = 1234;

    try (MockedStatic<com.intent_exchange.uasl.util.PropertyUtil> mocked =
        Mockito.mockStatic(com.intent_exchange.uasl.util.PropertyUtil.class)) {
      mockPropertyUtil(mocked, maxRetries, retryInterval);

      try {
        invokeConnectWithRetry(spyManager, options, controller);
      } catch (MqttException ignored) {
        // すべての失敗ケースで想定される動作
      }

      verify(spyManager, atLeastOnce()).sleepBeforeRetry(eq(retryInterval));
    }
  }

  // static な PropertyUtil.getPropertyInt をモックするための DRY ヘルパー
  private void mockPropertyUtil(
      MockedStatic<com.intent_exchange.uasl.util.PropertyUtil> mocked,
      int maxRetries,
      int retryInterval) {
    mocked
        .when(
            () ->
                com.intent_exchange.uasl.util.PropertyUtil.getPropertyInt(
                    "mqtt.connect.max.retries"))
        .thenReturn(maxRetries);
    mocked
        .when(
            () ->
                com.intent_exchange.uasl.util.PropertyUtil.getPropertyInt(
                    "mqtt.connect.retry.interval"))
        .thenReturn(retryInterval);
  }

  // リフレクションを使って private な connectWithRetry を呼び出すヘルパー
  private void invokeConnectWithRetry(
      MqttSubscribeManager mgr, MqttConnectOptions opts, AbstractMqttAsyncController ctrl)
      throws Exception {
    var m =
        MqttSubscribeManager.class.getDeclaredMethod(
            "connectWithRetry", MqttConnectOptions.class, int.class, AbstractMqttAsyncController.class);
    m.setAccessible(true);
    try {
      m.invoke(mgr, opts, 0, ctrl);
    } catch (java.lang.reflect.InvocationTargetException ite) {
      if (ite.getTargetException() instanceof Exception e) {
        throw e;
      }
      throw ite;
    }
  }
}
