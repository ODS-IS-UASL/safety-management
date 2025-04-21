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

package com.intent_exchange.drone_highway.dto.request;

import java.util.List;
import lombok.Data;

/**
 * 第三者立入監視判定フラグとエリア情報を格納するdto
 */
@Data
public class ThirdPartyMonitoringInformationRequestDto {
  /** 初回実行 */
  private Boolean restricted;
  /** エリア情報 */
  private String areaInfo;

  /** 北西端緯度 */
  private String latStart;
  /** 北西端経度 */
  private String lonStart;
  /** 南東端緯度 */
  private String latEnd;
  /** 南東端経度 */
  private String lonEnd;

  /** APIキー */
  private String apiKey; 
  /** 航路予約毎の識別ID */
  private String airwayReservationId;
  /** 第三者立入監視情報 */
  private List<Object> monitoringInformation;
  /** 航路運営者ID */
  private String airwayAdministratorId;
  /** 運航者ID */
  private String operatorId;
  /** 航路ID */
  private String airwayId;
  /** 第三者立入監視情報 適合性評価結果 */
  private Boolean thirdPartyEvaluationResults;

}
