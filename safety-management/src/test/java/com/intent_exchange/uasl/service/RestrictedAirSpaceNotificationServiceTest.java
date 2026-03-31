package com.intent_exchange.uasl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.AreaInfoConditionDto;
import com.intent_exchange.uasl.dto.response.RestrictedAirSpaceResponseDto;
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoOperationLogic;
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoOperationProcessorLogic;
import com.intent_exchange.uasl.logic.conformity_assessment.RestrictedAirSpaceEvaluationLogic;

/**
 * 規制/イベント情報変更通知のサービスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class RestrictedAirSpaceNotificationServiceTest {
  @Mock
  private ConformityAssessmentTaskService RestrictedAirSpaceNotificationService;

  @InjectMocks
  private RestrictedAirSpaceNotificationService service;

  @Test
  @DisplayName("規制/イベント情報変更通知に伴い適合性評価を実施")
  void testRestrictedAirSpaceNotificationServiceEntry1() {

    // リクエストEntity
    String areaInfo = ""; 
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    dto.setRestrictedArea(areaInfo);
    dto.setLogicClazz(RestrictedAirSpaceEvaluationLogic.class);

    doNothing().when(RestrictedAirSpaceNotificationService)
        .conformityAssessment(dto, UaslDesignAreaInfoOperationLogic.class,
            UaslDesignAreaInfoOperationProcessorLogic.class);

    assertDoesNotThrow(() -> service.notifyRestrictedAirSpace(areaInfo));

  }
}
