package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dto.request.AirwayDesignAreaInfoSectionDto;
import com.intent_exchange.drone_highway.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.drone_highway.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.drone_highway.exception.ConformityAssessmentException;
import com.intent_exchange.drone_highway.logic.conformity_assessment.ConformityAssessmentLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class ConformityAssessmentReservationLogicTest {

  @Mock
  private ConformityAssessmentLogic logic;

  @Mock
  private AirwayDesignAreaInfoSectionLogic areaInfoLogic;

  @InjectMocks
  private ConformityAssessmentReservationLogic reservationLogic;

  private ConformityAssessmentExecutionDto executionDto;
  private AirwayDesignAreaInfoSectionDto areaInfoDto;
  private ConformityAssessmentResultDto resultDto;

  @BeforeEach
  void setUp() {
    executionDto = new ConformityAssessmentExecutionDto();
    executionDto.setAirwaySectionId("1");

    areaInfoDto = new AirwayDesignAreaInfoSectionDto();
    areaInfoDto.setAirwaySectionId("1");

    resultDto = new ConformityAssessmentResultDto();
    resultDto.setEvaluationResults(true);
  }

  @Test
  @DisplayName("適合性評価のチェック")
  void testCheckReservationConformityAssessment1() throws ConformityAssessmentException {
    when(areaInfoLogic.get("1")).thenReturn(areaInfoDto);
    when(logic.executionConformityAssessment(any(ConformityAssessmentExecutionDto.class)))
        .thenReturn(resultDto);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.merge(areaInfoDto, executionDto))
          .thenReturn(executionDto);

      assertDoesNotThrow(() -> reservationLogic.checkReservationConformityAssessment(executionDto));
    }
  }
}
