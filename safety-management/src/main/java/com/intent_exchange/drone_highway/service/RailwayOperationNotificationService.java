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

package com.intent_exchange.drone_highway.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intent_exchange.drone_highway.dto.request.AreaInfoConditionDto;
import com.intent_exchange.drone_highway.dto.response.RailwayOperationNotificationResponseDto;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoOperationLogic;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoOperationProcessorLogic;
import com.intent_exchange.drone_highway.logic.conformity_assessment.RailwayOperationInformationEvaluationLogic;

/**
 * 鉄道運航情報変更通知のサービスクラス。
 */
@Service
public class RailwayOperationNotificationService {

  /** 適合性評価に関するサービス */
  @Autowired
  private ConformityAssessmentTaskService service;

  /**
   * 鉄道運航情報変更通知に伴い適合性評価を実施する
   * 
   * @param railwayOperationNotificationResponseDto 鉄道運航変更通知メッセージ
   */
  public void notifyRailwayOperation(
      RailwayOperationNotificationResponseDto railwayOperationNotificationResponseDto) {
    AreaInfoConditionDto dto = new AreaInfoConditionDto();

    dto.setStation1(railwayOperationNotificationResponseDto.getStation1());
    dto.setStation2(railwayOperationNotificationResponseDto.getStation2());
    dto.setLogicClazz(RailwayOperationInformationEvaluationLogic.class);

    service.conformityAssessment(dto, AirwayDesignAreaInfoOperationLogic.class,
        AirwayDesignAreaInfoOperationProcessorLogic.class);
  }

}

