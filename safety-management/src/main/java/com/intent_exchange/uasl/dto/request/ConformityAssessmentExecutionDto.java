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

package com.intent_exchange.uasl.dto.request;

import java.time.LocalDateTime;
import com.intent_exchange.uasl.logic.conformity_assessment.IFConformityAssessmentLogic;
import lombok.Data;

/** 適合性評価に使用するdto */
@Data
public class ConformityAssessmentExecutionDto {
  /** 航路ID */
  private String uaslId;
  /** 航路運営者ID */
  private String uaslAdministratorId;
  /** 航路区画ID */
  private String uaslSectionId;
  /** 航路予約毎の識別ID */
  private String uaslReservationId;
  /** 予約開始日時 */
  private LocalDateTime startAt;
  /** 予約終了日時 */
  private LocalDateTime endAt;
  /** 運航者ID */
  private String operatorId;
  /** 適合性評価結果 */
  private Boolean evaluationResults;
  /** 第三者立入監視情報 適合性評価結果 */
  private Boolean thirdPartyEvaluationResults;
  /** 鉄道運航情報 適合性評価結果 */
  private Boolean railwayOperationEvaluationResults;

  /** エリア情報(Geometory) */
  private String areaInfo;
  /** 製造メーカー名 */
  private String maker;
  /** 型式（モデル） */
  private String modelNumber;

  // 風速・天候
  /** 標高 */
  private String altitude;
  /** 北西端緯度 */
  private String latStart;
  /** 北西端経度 */
  private String lonStart;
  /** 南東端緯度 */
  private String latEnd;
  /** 南東端経度 */
  private String lonEnd;

  // 第三者立入監視
  /** 第三者立入判定 */
  private Boolean restricted = false;
  /** APIキー */
  private String apiKey;
  /** トークン */
  private String token;
  /** 風速条件範囲 */
  private Double windSpeedRange;

  // 鉄道運行
  /** 路線と航路の交点情報 */
  private String railwayCrossingInfo;

  /** 適合性評価実行クラス名 */
  private Class<? extends IFConformityAssessmentLogic<?>> logicClazz;
}
