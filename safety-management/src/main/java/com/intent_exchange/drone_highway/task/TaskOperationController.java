/*
 * Copyright 2025 Intent Exchange, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.intent_exchange.drone_highway.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dto.request.AreaInfoConditionDto;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoOperationLogic;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoOperationProcessorLogic;
import com.intent_exchange.drone_highway.service.ConformityAssessmentTaskService;

/**
 * 運行中の適合性評価に関するコントローラクラス。
 */
@Component
public class TaskOperationController extends AbstractTaskController {

  /** ロガー */
  private static final Logger logger = LoggerFactory.getLogger(TaskOperationController.class);

  /** 適合性評価に関するサービス */
  @Autowired
  private ConformityAssessmentTaskService service;

  /**
   * 運行中の適合性評価に関するタスクを実行します。
   */
  @Override
  @Scheduled(fixedRateString = "${operation.task.schedule.rate}")
  public void performTask() {
    logger.info("適合性評価(運航中)の定期実行を実施します。");
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    service.conformityAssessment(dto, AirwayDesignAreaInfoOperationLogic.class,
        AirwayDesignAreaInfoOperationProcessorLogic.class);
  }
}

