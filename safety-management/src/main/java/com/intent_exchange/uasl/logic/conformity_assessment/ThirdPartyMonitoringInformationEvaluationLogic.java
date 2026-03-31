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

import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.dto.geojson.SubscriptionsGeoJsonFeatures;
import com.intent_exchange.uasl.dto.request.ThirdPartyMonitoringInformationRequestDto;
import com.intent_exchange.uasl.dto.response.ThirdPartyMonitoringInformationResponseDto;
import com.intent_exchange.uasl.logic.MonitoringInformationLogic;
import com.intent_exchange.uasl.logic.web.WebThirdPartyMonitoringLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 第三者立入監視判定情報の適合性評価ロジック
 */
@Component
public class ThirdPartyMonitoringInformationEvaluationLogic
    implements IFConformityAssessmentLogic<ThirdPartyMonitoringInformationRequestDto> {

  // 第三者立入監視情報のintrusionStatusのキー
  private static final String INTRUSION_STATUS = "intrusionStatus";

  /**
   * 第三者立入監視情報への通信用ロジック
   */
  @Autowired
  WebThirdPartyMonitoringLogic logic;

  /**
   * 第三者立入監視情報の登録に関するロジック
   */
  @Autowired
  MonitoringInformationLogic monitoringInformationLogic;

  // Clockインスタンス。現在の時刻を取得するために使用します。
  @Autowired
  private Clock clock;

  /**
   * 第三者立入監視情報の適合性評価を行う
   * 
   * @param dto 第三者立入監視情報のリクエスト情報
   * @return 第三者立入監視情報の適合性評価結果
   */
  @SuppressWarnings({"unchecked"})
  @Override
  public Boolean check(ThirdPartyMonitoringInformationRequestDto dto) {
    // 第三者立入監視判定フラグがfalseの場合、処理をスキップする
    if (dto.getRestricted() != true) {
      return null;
    }

    /* 第三者立入監視判定フラグがtrueの場合、第三者立入監視情報判定を行う */
    int maxItemCount = 1;
    // 運航中の第三者立入監視情報取得
    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        logic.getMonitoringInformation(dto.getLatStart(), dto.getLonStart(), dto.getLatEnd(),
            dto.getLonEnd(), LocalDateTime.now(clock), maxItemCount, dto.getApiKey());

    // 第三者立入監視情報のfeaturesがNULLもしくは空の場合第三者立入なし
    if ((monitoringInfoDto.getFeatures() == null)
        || (monitoringInfoDto.getFeatures().size() == 0)) {
      registrationDetermination(dto.getThirdPartyEvaluationResults(), monitoringInfoDto, dto,
          monitoringInformationLogic);
      return true;
    }

    // 第三者立入監視情報のfeaturesのすべての要素で第三者立入なしの場合はTrueを返却し、一つでも第三者立入ありの場合はFalseを返却
    boolean result = monitoringInfoDto.getFeatures().stream()
        .map(m -> ModelMapperUtil.map(m, SubscriptionsGeoJsonFeatures.class)).allMatch(feature -> {
          // 第三者立入監視情報のfeatures内のpropertiesがNULLもしくは空の場合第三者立入なし
          if ((feature.getProperties() == null) || feature.getProperties().isEmpty()) {
            registrationDetermination(dto.getThirdPartyEvaluationResults(), monitoringInfoDto, dto,
                monitoringInformationLogic);
            return true;
          }
          // 第三者立入監視情報のfeatures内のproperties内のtrafficsがNULLもしくは空の場合第三者立入なし
          else if (feature.getProperties().get(INTRUSION_STATUS).equals(0)) {
            registrationDetermination(dto.getThirdPartyEvaluationResults(), monitoringInfoDto, dto,
                monitoringInformationLogic);
            return true;
          } else {
            // 第三者立入監視情報がある場合、情報を登録する
            monitoringInformationLogic.insert(monitoringInfoDto, dto);
            return false;
          }
        });
    return result;
  }

  /**
   * 第三者立入監視情報を登録するかどうかを判定する
   * 
   * @param thirdPartyEvaluationResults 第三者立入監視情報の適合性評価結果
   * @param monitoringInfoDto 第三者立入監視情報
   * @param dto 第三者立入監視情報のリクエスト情報
   * @param monitoringInformationLogic 第三者立入監視情報の登録に関するロジック
   */
  private static void registrationDetermination(boolean thirdPartyEvaluationResults,
      ThirdPartyMonitoringInformationResponseDto monitoringInfoDto,
      ThirdPartyMonitoringInformationRequestDto dto,
      MonitoringInformationLogic monitoringInformationLogic) {

    // 第三者立入監視情報がなしで、前回有りの場合は登録する
    if (thirdPartyEvaluationResults == false) {
      monitoringInformationLogic.insert(monitoringInfoDto, dto);
    }
  }

}

