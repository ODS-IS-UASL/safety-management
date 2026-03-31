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
import com.intent_exchange.uasl.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.entity.ConformityAssessmentExecutionEntity;
import com.intent_exchange.uasl.entity.ConformityAssessmentResponseEntity;
import com.intent_exchange.uasl.logic.ConformityAssessmentReservationLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 安全管理支援(SafetyManagement)の航路の適合性評価に関連する処理を記載します。
 */
@Service
public class ConformityAssessmentService {

  /**
   * 予約時の適合性評価に関するロジック
   */
  @Autowired
  private ConformityAssessmentReservationLogic reservationLogic;

  /**
   * 予約時に適合性評価を実行する。
   * 
   * @param conformityAssessmentExecutionEntity 航路の適合性評価実行API用のentity
   */
  public ConformityAssessmentResponseEntity executionReservationConformityAssessment(
      ConformityAssessmentExecutionEntity conformityAssessmentExecutionEntity) {
    ConformityAssessmentExecutionDto conformityAssessmentExecutionDto = ModelMapperUtil
        .map(conformityAssessmentExecutionEntity, ConformityAssessmentExecutionDto.class);
    if (conformityAssessmentExecutionEntity.getAircraftInfo() != null) {
      conformityAssessmentExecutionDto.setMaker(
          conformityAssessmentExecutionEntity.getAircraftInfo().getMaker());
      conformityAssessmentExecutionDto.setModelNumber(
          conformityAssessmentExecutionEntity.getAircraftInfo().getModelNumber());
    }
    // 適合性評価実行
    ConformityAssessmentResultDto dto =
        reservationLogic.checkReservationConformityAssessment(conformityAssessmentExecutionDto);
    ConformityAssessmentResponseEntity entity =
        ModelMapperUtil.map(dto, ConformityAssessmentResponseEntity.class);
    entity.setReasons(dto.getMessage());
    if (dto.getType() != null && !dto.getType().isBlank()) {
      entity.setType(ConformityAssessmentResponseEntity.TypeEnum.fromValue(dto.getType()));
    }
    return entity;
  }
}

