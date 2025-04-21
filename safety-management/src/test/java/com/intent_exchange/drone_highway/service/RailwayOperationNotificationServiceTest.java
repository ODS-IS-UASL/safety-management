package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dto.request.AreaInfoConditionDto;
import com.intent_exchange.drone_highway.dto.response.RailwayOperationNotificationResponseDto;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoOperationLogic;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoOperationProcessorLogic;
import com.intent_exchange.drone_highway.logic.conformity_assessment.RailwayOperationInformationEvaluationLogic;

/**
 * 鉄道運航情報変更通知のサービスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class RailwayOperationNotificationServiceTest {
  @Mock
  private ConformityAssessmentTaskService RailwayOperationNotificationService;

  @InjectMocks
  private RailwayOperationNotificationService service;

  @Test
  @DisplayName("鉄道運航情報変更通知に伴い適合性評価を実施")
  void testNotifyRailwayOperation1() {
    RailwayOperationNotificationResponseDto railwayOperationNotificationResponseDto =
        new RailwayOperationNotificationResponseDto();
    railwayOperationNotificationResponseDto.setStation1("station1");
    railwayOperationNotificationResponseDto.setStation2("station2");
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    dto.setStation1(railwayOperationNotificationResponseDto.getStation1());
    dto.setStation2(railwayOperationNotificationResponseDto.getStation2());
    dto.setLogicClazz(RailwayOperationInformationEvaluationLogic.class);

    doNothing().when(RailwayOperationNotificationService)
        .conformityAssessment(dto, AirwayDesignAreaInfoOperationLogic.class,
            AirwayDesignAreaInfoOperationProcessorLogic.class);

    assertDoesNotThrow(
        () -> service.notifyRailwayOperation(railwayOperationNotificationResponseDto));

  }
}
