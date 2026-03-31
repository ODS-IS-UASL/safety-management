package com.intent_exchange.uasl.controller.mqtt;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.webclient.MqttSubscribeManager;

@ExtendWith(MockitoExtension.class)
class AbstractMqttAsyncControllerTest {

  @Mock
  private MqttSubscribeManager mqttSubscribeManager;

  @InjectMocks
  private TestMqttAsyncController controller = new TestMqttAsyncController();

  @Test
  @DisplayName("MQTTクライアントのセットアップ 正常系")
  public void testInit1() throws MqttException {
    doNothing().when(mqttSubscribeManager).init(any(AbstractMqttAsyncController.class));
    controller.init();
    verify(mqttSubscribeManager, times(1)).init(any(AbstractMqttAsyncController.class));
  }

  @Test
  @DisplayName("MQTTクライアントのセットアップ 異常系")
  public void testInit2() throws MqttException {
    doThrow(new MqttException(MqttException.REASON_CODE_CLIENT_EXCEPTION))
        .when(mqttSubscribeManager)
        .init(any(AbstractMqttAsyncController.class));
    controller.init();
    verify(mqttSubscribeManager, times(1)).init(any(AbstractMqttAsyncController.class));
  }

  public class TestMqttAsyncController extends AbstractMqttAsyncController {
    @Override
    public String getTopic() {
      return "test/topic";
    }

    @Override
    public int getMqttMessageQos() {
      return 1;
    }

    @Override
    public void handleMessage(String topic, MqttMessage message) {}
  }
}
