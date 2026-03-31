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

package com.intent_exchange.uasl.logic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.uasl.dto.request.WebTaskReservationDto;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;

/** WebTaskReservationLogicクラスは、適合性評価結果変化通知先の航路予約APIに対してHTTPリクエストを送信するロジックを提供します。 */
@Component
public class WebTaskReservationLogic {

  /** RestTemplateインスタンス。 このインスタンスを使用してHTTPリクエストを送信します。 */
  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  /** 適合性評価結果変化通知APIのURL */
  @Value("${put.notify.conformity.assessment.result.url:''}")
  private String putNotifyConformityAssessmentResultUrl;

  /** 適合性評価結果変化通知を行うメソッド */
  public void putTaskReservation(ConformityAssessmentResultDto dto) {

    // URLテンプレートを作成
    String notifyConformityAssessmentResultUrlTemplate =
        UriComponentsBuilder.fromHttpUrl(putNotifyConformityAssessmentResultUrl).toUriString();

    // WebTaskReservationDtoオブジェクトを作成し、値を設定
    WebTaskReservationDto WebTaskReservationDto = new WebTaskReservationDto();
    WebTaskReservationDto.setUaslReservationId(dto.getUaslReservationId());
    WebTaskReservationDto.setMessage(dto.getMessage());
    WebTaskReservationDto.setEvaluationResults(dto.getEvaluationResults() ? "OK" : "NG");

    // PUTリクエストを送信
    restTemplate.put(notifyConformityAssessmentResultUrlTemplate, WebTaskReservationDto);
  }
}

