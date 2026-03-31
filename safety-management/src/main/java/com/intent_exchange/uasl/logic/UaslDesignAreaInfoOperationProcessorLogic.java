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

package com.intent_exchange.uasl.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.async.Processor;
import com.intent_exchange.uasl.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.logic.conformity_assessment.ConformityAssessmentLogic;
import com.intent_exchange.uasl.logic.mqtt.MqttConformityAssessmentLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * Processorロジック。
 */
@Component("UaslDesignAreaInfoOperationProcessorLogic")
public class UaslDesignAreaInfoOperationProcessorLogic
    implements Processor<ConformityAssessmentExecutionDto> {


  /**
   * WebConformityAssessmentLogicのインスタンス。
   * 外部システムとの連携に使用されます。
   */
  @Autowired
  private MqttConformityAssessmentLogic webConformityAssessmentLogic;

  /**
   * UaslReservationLogicのインスタンス。
   * 航路予約情報の操作に使用されます。
   */
  @Autowired
  private UaslReservationLogic uaslReservationLogic;

  /**
   * ConformityAssessmentLogicのインスタンス。
   * 適合性評価の実行に使用されます。
   */
  @Autowired
  private ConformityAssessmentLogic conformityAssessmentLogic;



  @Override
  public ConformityAssessmentResultDto checkProcess(ConformityAssessmentExecutionDto dto) {
    return conformityAssessmentLogic.executionConformityAssessment(dto);
  }
  
  @Override
  public boolean setInOperation() {
    // 運航中の航路情報に関するprocesserLogicのため、true(運航中)
    return true;
  }

  @Override
  public void webProcess(ConformityAssessmentResultDto dto) {
    // 運航中航路状況変更通知を外部システム連携に送信
    webConformityAssessmentLogic.notifyConformityAssessment(dto);
  }

  @Override
  public void updateProcess(ConformityAssessmentResultDto dto) {
    // 航路予約情報更新
    uaslReservationLogic.update(ModelMapperUtil.map(dto, WebUaslReservationDto.class));
  }
}

