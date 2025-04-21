package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.drone_highway.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.logic.conformity_assessment.ConformityAssessmentLogic;
import com.intent_exchange.drone_highway.logic.mqtt.MqttConformityAssessmentLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * AirwayDesignAreaInfoOperationProcessorLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class AirwayDesignAreaInfoOperationProcessorLogicTest {

  @Mock
  private ConformityAssessmentLogic conformityAssessmentLogic;

  @Mock
  private MqttConformityAssessmentLogic webConformityAssessmentLogic;

  @Mock
  private AirwayReservationLogic airwayReservationLogic;

  @InjectMocks
  private AirwayDesignAreaInfoOperationProcessorLogic logic;

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
    WebAirwayReservationDto reservationDto = new WebAirwayReservationDto();
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(resultDto, WebAirwayReservationDto.class))
          .thenReturn(reservationDto);
      logic.updateProcess(resultDto);
      assertDoesNotThrow(() -> (airwayReservationLogic).update(reservationDto));
    }
  }
}
