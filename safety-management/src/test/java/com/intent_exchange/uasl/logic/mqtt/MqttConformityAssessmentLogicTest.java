package com.intent_exchange.uasl.logic.mqtt;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.HashMap;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.NotifyConformityAssessmentResultDto;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import com.intent_exchange.uasl.util.PropertyUtil;
import com.intent_exchange.uasl.webclient.MqttPublishTemplate;

/**
 * 外部システム連携への通信用ロジックのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class MqttConformityAssessmentLogicTest {

  @Mock
  private MqttPublishTemplate mqttTemplate;

  @InjectMocks
  private MqttConformityAssessmentLogic mqttConformityAssessmentLogic; // テスト対象のクラス

  private final int MQTT_MESSAGE_QOS = PropertyUtil.getPropertyInt("mqtt.qos.exactryOnce");

  @Test
  @DisplayName("運航中航路状況変更通知を外部システム連携に送信する")
  public void testNotifyConformityAssessment1() {
    ConformityAssessmentResultDto dto = new ConformityAssessmentResultDto();
    dto.setUaslReservationId("uaslReservationId");
    dto.setEvaluationResults(true);
    dto.setMessage("message");
    dto.setType("type");
    NotifyConformityAssessmentResultDto reqDto = new NotifyConformityAssessmentResultDto();
    reqDto.setUaslReservationId("uaslReservationId");
    reqDto.setEvaluationResults("true");
    reqDto.setReasons("message");
    reqDto.setType("type");
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, NotifyConformityAssessmentResultDto.class))
          .thenReturn(reqDto);
      mockedStatic.when(() -> ModelMapperUtil.convertDtoToMap(reqDto))
          .thenReturn(new HashMap<String, Object>());
      mockedStatic.when(() -> ModelMapperUtil.mapToJson(any())).thenReturn("{}");
      doNothing().when(mqttTemplate)
          .publish(anyString(), any(MqttMessage.class), eq(MQTT_MESSAGE_QOS));
      mqttConformityAssessmentLogic.notifyConformityAssessment(dto);
      verify(mqttTemplate, times(1)).publish(anyString(), any(MqttMessage.class),
          eq(MQTT_MESSAGE_QOS));
    }
  }
}
