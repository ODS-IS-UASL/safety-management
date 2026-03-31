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
import com.intent_exchange.uasl.entity.ThirdPartyEntryNotificationEntity;
import com.intent_exchange.uasl.entity.ThirdPartyEntryNotificationEntityFeaturesInner;
import com.intent_exchange.uasl.entity.ThirdPartyEntryNotificationEntityFeaturesInnerGeometry;
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoOperationLogic;
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoOperationProcessorLogic;
import com.intent_exchange.uasl.logic.conformity_assessment.ThirdPartyMonitoringInformationEvaluationLogic;

/**
 * 第三者立入監視情報変更通知のサービスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class ThirdPartyEntryNotificationServiceTest {

  @Mock
  private ConformityAssessmentTaskService thirdPartyEntryNotificationService;

  @InjectMocks
  private ThirdPartyEntryNotificationService service;

  @Test
  @DisplayName("三者立入監視情報変更通知に伴い適合性評価を実施")
  void testNotifyThirdPartyEntry1() {

    // リクエストEntity
    ThirdPartyEntryNotificationEntity entity = new ThirdPartyEntryNotificationEntity();
    entity.setType("FeatureCollection");
    List<ThirdPartyEntryNotificationEntityFeaturesInner> features = new ArrayList<>();
    ThirdPartyEntryNotificationEntityFeaturesInner feature =
        new ThirdPartyEntryNotificationEntityFeaturesInner();
    ThirdPartyEntryNotificationEntityFeaturesInnerGeometry geometry =
        new ThirdPartyEntryNotificationEntityFeaturesInnerGeometry();
    geometry.setType("POLYGON");
    feature.setGeometry(geometry);
    features.add(feature);
    entity.setFeatures(features);
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    dto.setRestrictedArea(null);
    dto.setLogicClazz(ThirdPartyMonitoringInformationEvaluationLogic.class);

    doNothing().when(thirdPartyEntryNotificationService)
        .conformityAssessment(dto, UaslDesignAreaInfoOperationLogic.class,
            UaslDesignAreaInfoOperationProcessorLogic.class);

    assertDoesNotThrow(() -> service.notifyThirdPartyEntry(entity));

  }
}
