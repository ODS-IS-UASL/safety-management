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
import com.intent_exchange.drone_highway.entity.ThirdPartyEntryNotificationEntity;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoOperationLogic;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoOperationProcessorLogic;
import com.intent_exchange.drone_highway.logic.conformity_assessment.ThirdPartyMonitoringInformationEvaluationLogic;

/**
 * 第三者立入監視情報変更通知のサービスクラス。
 */
@Service
public class ThirdPartyEntryNotificationService {

  /** 適合性評価に関するサービス */
  @Autowired
  private ConformityAssessmentTaskService service;

  /**
   * 第三者立入監視情報変更通知に伴い適合性評価を実施する
   * 
   * @param entity 第三者立入監視情報のエンティティ
   */
  public void notifyThirdPartyEntry(ThirdPartyEntryNotificationEntity entity) {

    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    entity.getFeatures().forEach(feature -> {
      dto.setRestrictedArea(null);
      dto.setLogicClazz(ThirdPartyMonitoringInformationEvaluationLogic.class);
      service.conformityAssessment(dto, AirwayDesignAreaInfoOperationLogic.class,
          AirwayDesignAreaInfoOperationProcessorLogic.class);
    });
  }

}

