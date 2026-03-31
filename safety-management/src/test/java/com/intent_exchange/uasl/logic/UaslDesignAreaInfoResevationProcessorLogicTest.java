package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.logic.mqtt.MqttConformityAssessmentLogic;
import com.intent_exchange.uasl.logic.web.WebTaskReservationLogic;
import com.intent_exchange.uasl.util.PropertyUtil;

/** UaslDesignAreaInfoResevationProcessorLogicのテストクラスです。 */

@ExtendWith(MockitoExtension.class)
class UaslDesignAreaInfoResevationProcessorLogicTest {

  @Mock
  private WebTaskReservationLogic WebTaskReservationLogic;

  @Mock
  private MqttConformityAssessmentLogic webConformityAssessmentLogic;

  @InjectMocks
  private UaslDesignAreaInfoResevationProcessorLogic processorLogic;

  @InjectMocks
  private UaslDesignAreaInfoOperationProcessorLogic operationProcessorLogic;

  @Test
  @DisplayName("UTMが有りの場合_航路状況変更通知先が外部システム連携")
  void testWebProcess1() {
    ConformityAssessmentResultDto dto = new ConformityAssessmentResultDto();
    try (var mockedPropertyUtil = mockStatic(PropertyUtil.class)) {
      // UTMが有りの場合
      mockedPropertyUtil.when(() -> PropertyUtil.getProperty("utm.enabled")).thenReturn("true");

      processorLogic.webProcess(dto);

      // 外部システム連携へ航路状況変更通知が実行されることの確認
      verify(webConformityAssessmentLogic).notifyConformityAssessment(dto);
    }
  }

  @Test
  @DisplayName("UTMが無しの場合_航路状況変更通知先が航路予約システム")
  void testWebProcess2() {
    ConformityAssessmentResultDto dto = new ConformityAssessmentResultDto();
    try (var mockedPropertyUtil = mockStatic(PropertyUtil.class)) {
      // UTMが無しの場合
      mockedPropertyUtil.when(() -> PropertyUtil.getProperty("utm.enabled")).thenReturn("false");

      processorLogic.webProcess(dto);

      // 航路予約システムへ航路状況変更通知が実行されることの確認
      verify(WebTaskReservationLogic).putTaskReservation(dto);
    }
  }

  @Test
  @DisplayName("setInOperationの確認")
  void testSetInOperation1() {
    boolean result = processorLogic.setInOperation();
    assertFalse(result);
  }
}
