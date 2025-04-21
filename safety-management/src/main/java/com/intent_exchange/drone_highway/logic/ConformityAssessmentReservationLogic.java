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

package com.intent_exchange.drone_highway.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dto.request.AirwayDesignAreaInfoSectionDto;
import com.intent_exchange.drone_highway.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.drone_highway.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.drone_highway.exception.ConformityAssessmentException;
import com.intent_exchange.drone_highway.logic.conformity_assessment.ConformityAssessmentLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 予約時の適合性評価に関するロジック
 */
@Component
public class ConformityAssessmentReservationLogic {

  /** 適合性評価チェックのロジック */
  @Autowired
  private ConformityAssessmentLogic logic;

  /** 適合性評価で使用する航路情報取得のロジック */
  @Autowired
  private AirwayDesignAreaInfoSectionLogic areaInfoLogic;

  /**
   * 適合性評価の結果がNGの場合はExceptionを発生させる
   * 
   * @param dto 適合性評価実施に使用する情報
   * @throws ConformityAssessmentException 適合性評価がNGの場合にスローされる例外
   */
  public ConformityAssessmentResultDto checkReservationConformityAssessment(
      ConformityAssessmentExecutionDto dto) throws ConformityAssessmentException {

    // 航路情報取得
    AirwayDesignAreaInfoSectionDto areaInfoDto = areaInfoLogic.get(dto.getAirwaySectionId());

    // 航路情報をdtoに設定
    dto = ModelMapperUtil.merge(areaInfoDto, dto);

    // 適合性評価チェック
    return logic.executionConformityAssessment(dto);
  }
}

