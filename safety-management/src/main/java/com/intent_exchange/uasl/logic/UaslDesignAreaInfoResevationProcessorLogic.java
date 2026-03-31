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
import com.intent_exchange.uasl.logic.web.WebTaskReservationLogic;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * Processorロジック。
 */
@Component("UaslDesignAreaInfoResevationProcessorLogic")
public class UaslDesignAreaInfoResevationProcessorLogic
    extends UaslDesignAreaInfoOperationProcessorLogic
    implements Processor<ConformityAssessmentExecutionDto> {

  @Autowired
  WebTaskReservationLogic WebTaskReservationLogic;
  
  @Override
  public boolean setInOperation() {
    // 予約中の航路情報に関するprocesserLogicのため、false(予約中)
    return false;
  }

  @Override
  public void webProcess(ConformityAssessmentResultDto dto) {

    // UTMの有り、無しの判定
    if (PropertyUtil.getProperty("utm.enabled").equals("true")) {

      // UTM有りの場合、外部システム連携に航路状況変更通知する。
      super.webProcess(dto);
    } else {

      // 航路予約システムに航路状況変更通知をする。
      WebTaskReservationLogic.putTaskReservation(dto);
    }
  }
}


