
package com.intent_exchange.uasl.task;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.AreaInfoConditionDto;
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoResevasionNowDayLogic;
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoResevationProcessorLogic;
import com.intent_exchange.uasl.service.ConformityAssessmentTaskService;

/** TaskResevasionNowDayController のテストクラスです。 */

// TestConfigとModelMapperConfigのModelMapperインスタンス作成のbeenが重複してエラーのため、Bean 定義の上書きを許可します。
@ExtendWith(MockitoExtension.class)
class TaskResevasionNowDayControllerTest {

  @InjectMocks
  private TaskResevasionNowDayController controller;

  @Mock
  private ConformityAssessmentTaskService service;

  @Test
  @DisplayName("performTaskメソッドのテスト_呼び出されている")
  void testPerformTask() {
    // 第三者立入情報は使用しないため、設定なしのdto
    AreaInfoConditionDto dto = new AreaInfoConditionDto();

    // タスクの実行
    controller.performTask();

    // service.conformityAssessmentが呼び出されていう事を確認する
    verify(service).conformityAssessment(eq(dto),
        eq(UaslDesignAreaInfoResevasionNowDayLogic.class),
        eq(UaslDesignAreaInfoResevationProcessorLogic.class));
  }
}
