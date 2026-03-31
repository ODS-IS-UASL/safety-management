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

package com.intent_exchange.uasl.dto.response;

import lombok.Data;

/** 適合性評価の評価結果を返すdto */
@Data
public class ConformityAssessmentResultDto {
  /** 航路ID */
  private String uaslId;
  /** 航路予約毎の識別ID */
  private String uaslReservationId;
  /** 運航者ID */
  private String operatorId;
  /** 運航中フラグ(true：運航中 false：予約中) ※運航中を初期値とする。予約中の場合のみ、適合性評価実施時にfalseに更新します。 */
  private boolean inOperation = true;

  /** 適合性評価結果 */
  private Boolean evaluationResults;
  /** 第三者立入監視情報 適合性評価結果 */
  private Boolean thirdPartyEvaluationResults;
  /** 鉄道運航情報 適合性評価結果 */
  private Boolean railwayOperationEvaluationResults;


  /** 種別 */
  private String type;
  /** メッセージ */
  private String message;
  /** 適合性評価結果の変更有無 */
  private Boolean changenResults;

}

