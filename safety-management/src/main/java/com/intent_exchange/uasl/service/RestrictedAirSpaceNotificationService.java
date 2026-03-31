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

package com.intent_exchange.uasl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intent_exchange.uasl.dto.request.AreaInfoConditionDto;
import com.intent_exchange.uasl.dto.request.RestrictedAirSpaceDto;
import com.intent_exchange.uasl.dto.response.RestrictedAirSpaceResponseDto;
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoOperationLogic;
import com.intent_exchange.uasl.logic.UaslDesignAreaInfoOperationProcessorLogic;
import com.intent_exchange.uasl.logic.conformity_assessment.RestrictedAirSpaceEvaluationLogic;

/**
 * 安全管理支援(SafetyManagement) 規制/イベント情報に関連する処理をします。
    */
@Service
public class RestrictedAirSpaceNotificationService {
  
  /** 適合性評価に関するサービス */
  @Autowired
  private ConformityAssessmentTaskService service;

  /**
   * 規制/イベント情報変更通知に伴い適合性評価を実施する
   * 
   * @param areaInfo 規制/イベントのエリア情報
   */
  public void notifyRestrictedAirSpace(String areaInfo) {
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    dto.setRestrictedArea(areaInfo);
    dto.setLogicClazz(RestrictedAirSpaceEvaluationLogic.class);

    service.conformityAssessment(dto, UaslDesignAreaInfoOperationLogic.class,
        UaslDesignAreaInfoOperationProcessorLogic.class);
  }
}

