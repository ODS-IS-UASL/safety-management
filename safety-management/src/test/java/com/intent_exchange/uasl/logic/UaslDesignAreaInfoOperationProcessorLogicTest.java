package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.logic.conformity_assessment.ConformityAssessmentLogic;
import com.intent_exchange.uasl.logic.mqtt.MqttConformityAssessmentLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * UaslDesignAreaInfoOperationProcessorLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class UaslDesignAreaInfoOperationProcessorLogicTest {

  @Mock
  private ConformityAssessmentLogic conformityAssessmentLogic;

  @Mock
  private MqttConformityAssessmentLogic webConformityAssessmentLogic;

  @Mock
  private UaslReservationLogic uaslReservationLogic;

  @InjectMocks
  private UaslDesignAreaInfoOperationProcessorLogic logic;

  /**
   * checkProcessメソッドのテストを行います。
   */
  @Test
  @DisplayName("チェック処理をします。")
  void testCheckProcess() {
    // Arrange
    ConformityAssessmentExecutionDto executionDto = new ConformityAssessmentExecutionDto();
    ConformityAssessmentResultDto resultDto = new ConformityAssessmentResultDto();
    when(conformityAssessmentLogic.executionConformityAssessment(executionDto))
        .thenReturn(resultDto);
    ConformityAssessmentResultDto result = logic.checkProcess(executionDto);
    assertEquals(resultDto, result);
    assertDoesNotThrow(
        () -> (conformityAssessmentLogic).executionConformityAssessment(executionDto));
  }
  
  /**
   * setInOperationメソッドのテストを行います。
   */
  @Test
  @DisplayName("運航中の状態を設定します。")
  void testSetInOperation() {
    // Arrange
    boolean result = logic.setInOperation();
    assertTrue(result);
  }

  /**
   * webProcessメソッドのテストを行います。
   */
  @Test
  @DisplayName("外部通信を行います。")
  void testWebProcess() {
    // Arrange
    ConformityAssessmentResultDto resultDto = new ConformityAssessmentResultDto();
    logic.webProcess(resultDto);
    assertDoesNotThrow(() -> (webConformityAssessmentLogic).notifyConformityAssessment(resultDto));
  }

  /**
   * updateProcessメソッドのテストを行います。
   */
  @Test
  @DisplayName("データ更新を行います。")
  void testUpdateProcess() {
    // Arrange
    ConformityAssessmentResultDto resultDto = new ConformityAssessmentResultDto();
    WebUaslReservationDto reservationDto = new WebUaslReservationDto();
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(resultDto, WebUaslReservationDto.class))
          .thenReturn(reservationDto);
      logic.updateProcess(resultDto);
      assertDoesNotThrow(() -> (uaslReservationLogic).update(reservationDto));
    }
  }
}
