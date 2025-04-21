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
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoResevasionNowDayLogic;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoResevationProcessorLogic;
import com.intent_exchange.drone_highway.service.ConformityAssessmentTaskService;

/**
 * 当日の予約の適合性評価に関するコントローラクラス。
 */
@Component
public class TaskResevasionNowDayController extends AbstractTaskController {

  /** ロガー */
  private static final Logger logger =
      LoggerFactory.getLogger(TaskResevasionNowDayController.class);

  /** 適合性評価に関するサービス */
  @Autowired
  private ConformityAssessmentTaskService service;

  /**
   * 適合性評価に関するタスクを実行します。
   */
  @Override
  @Scheduled(fixedRateString = "${resevasion.task.schedule.fixedRateString.nowday}")
  public void performTask() {

    // TODO DBのデータを＋9時間しないと、想定した値が取れない状態のため、kddi_openAPI.yamlの開始時間を今日に設定し予約してどうなるか検証する必要があります
    logger.info("適合性評価(当日の予約)の定期実行を実施します。");
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    service.conformityAssessment(dto, AirwayDesignAreaInfoResevasionNowDayLogic.class,
        AirwayDesignAreaInfoResevationProcessorLogic.class);
  }
}

