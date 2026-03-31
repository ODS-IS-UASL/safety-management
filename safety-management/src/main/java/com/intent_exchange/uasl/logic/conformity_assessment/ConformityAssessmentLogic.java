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

package com.intent_exchange.uasl.logic.conformity_assessment;

import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.dao.UaslReservationMapper;
import com.intent_exchange.uasl.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * 適合性評価に関するロジック
 */
@Component
public class ConformityAssessmentLogic {

  /** ロガー */
  private static final Logger logger = LoggerFactory.getLogger(ConformityAssessmentLogic.class);

  /** 航路予約情報のMapper */
  @Autowired
  private UaslReservationMapper uaslReservationMapper;

  /** 風速条件範囲内判定ロジック */
  @Autowired
  private WeatherWindSpeedLogic weatherWindLogic;

  /** 規制／イベント有無判定ロジック */
  @Autowired
  private RestrictedAirSpaceEvaluationLogic restrictedAirSpaceLogic;

  /** 鉄道運行判定ロジック */
  @Autowired
  private RailwayOperationInformationEvaluationLogic railwayLogic;

  /** 第三者立入判定ロジック */
  @Autowired
  private ThirdPartyMonitoringInformationEvaluationLogic thirdPartyLogic;

  /** 判定ロジックをリスト */
  private List<IFConformityAssessmentLogic> logicList;

  /**
   * 判定ロジックをリスト初期化
   */
  @Autowired
  public void init() {
    logicList = List.of(weatherWindLogic, restrictedAirSpaceLogic, railwayLogic, thirdPartyLogic);
  }

  /**
   * 適合性評価の実行
   * 
   * @param dto 適合性評価実施に使用する情報
   * @return 適合性評価の結果
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public ConformityAssessmentResultDto executionConformityAssessment(
      ConformityAssessmentExecutionDto dto) {
    dto.setToken(PropertyUtil.getProperty("token"));
    dto.setApiKey(PropertyUtil.getProperty("apiKey"));

    // 機体情報毎の最大風速を取得し、Nullの場合は固定値を使用する
    Double maxWindSpeed =
        uaslReservationMapper.selectMaxWindSpeed(
            dto.getUaslReservationId(), dto.getMaker(), dto.getModelNumber());
    if (maxWindSpeed != null) {
      dto.setWindSpeedRange(maxWindSpeed);
    } else {
      dto.setWindSpeedRange(PropertyUtil.getPropertyDecimal("dummy.wind.speed.range").doubleValue());
    }

    ConformityAssessmentResultDto result =
        ModelMapperUtil.map(dto, ConformityAssessmentResultDto.class);


    String message = null;
    Boolean isThirdParty = false;
    Boolean isRailway = false;
    for (IFConformityAssessmentLogic logic : logicList) {
      try {
        if (dto.getLogicClazz() == null) {
          Object mappedDto = ModelMapperUtil.map(dto, logic.getGenericTypeClass());
          Boolean rs = logic.check(mappedDto);
          if (logic instanceof ThirdPartyMonitoringInformationEvaluationLogic && rs != null) {
            result.setThirdPartyEvaluationResults(rs);
            isThirdParty = true;
          }
          if (logic instanceof RailwayOperationInformationEvaluationLogic && rs != null) {
            result.setRailwayOperationEvaluationResults(rs);
            isRailway = true;
          }
          if (Boolean.FALSE.equals(rs)) {
            message = logic.getClassInfo();
            break;
          }
        } else if (dto.getLogicClazz().isInstance(logic)) {
          Object mappedDto = ModelMapperUtil.map(dto, logic.getGenericTypeClass());
          if (Boolean.FALSE.equals(logic.check(mappedDto))) {
            message = logic.getClassInfo();
          }
          break;
        }
      } catch (Exception e) {
        // 他APIとの接続等でエラーが発生した場合は、warnを出力し次の適合性評価へ進む
        logger.warn(e.getMessage(), e);
      }
    }

    // 適合性評価の結果設定
    result.setChangenResults(Boolean
        .valueOf(Objects.toString(dto.getEvaluationResults(), "true")) != Objects.isNull(message)); // 適合性評価がOKでも過去の鉄道運航、第三者立ち入りがNGならOKとしない
    if ((!isRailway
        && !Boolean.valueOf(Objects.toString(dto.getRailwayOperationEvaluationResults(), "true")))
        || (!isThirdParty
            && !Boolean.valueOf(Objects.toString(dto.getThirdPartyEvaluationResults(), "true")))) {
      result.setChangenResults(false);
    }
    result.setEvaluationResults(Objects.isNull(message));
    result.setMessage(PropertyUtil.getProperty(message + ".conformity.assessment.message"));
    result.setType(PropertyUtil.getProperty(message + ".conformity.assessment.type"));

    return result;
  }
}

