package com.intent_exchange.drone_highway.task;

import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.service.AirwayDeviationService;

/*  
 * 航路逸脱情報の登録に関するコントローラクラスのテストを提供します。
 */
@ExtendWith(MockitoExtension.class)
class TaskAirwayDeviationLogicControllerTest {

  @InjectMocks
  private TaskAirwayDeviationLogicController controller;

  @Mock
  private AirwayDeviationService service;

  @Test
  @DisplayName("performTaskメソッドのテスト_呼び出されている")
  void testPerformTask() {

    // タスクの実行
    controller.performTask();

    // service.conformityAssessmentが呼び出されていう事を確認する
    verify(service).registerRouteDeviationInfo();
  }

}
