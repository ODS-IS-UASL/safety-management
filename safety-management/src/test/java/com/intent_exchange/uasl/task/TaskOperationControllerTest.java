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
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoOperationLogic;
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoOperationProcessorLogic;
import com.intent_exchange.uasl.service.ConformityAssessmentTaskService;


/**
 * TaskOperationControllerのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class TaskOperationControllerTest {

  @Mock
  private ConformityAssessmentTaskService service;

  @InjectMocks
  private TaskOperationController controller;

  @Test
  @DisplayName("運行中の適合性評価に関するタスクを実行")
  void testPerformTask() {
    // テストデータの準備
    AreaInfoConditionDto dto = new AreaInfoConditionDto();

    // メソッドの実行
    controller.performTask();

    // 結果の検証
    verify(service).conformityAssessment(eq(dto), eq(UaslDesignAreaInfoOperationLogic.class),
        eq(UaslDesignAreaInfoOperationProcessorLogic.class));
  }
}
