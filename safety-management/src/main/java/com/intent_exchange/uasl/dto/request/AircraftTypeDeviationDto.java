
 /*
 * Copyright 2026 Intent Exchange, Inc.
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

import java.time.OffsetDateTime;
import lombok.Data;

/** 機体種別別逸脱集計の挿入用DTO。 */
@Data
public class AircraftTypeDeviationDto {
  /** 航空機情報の内部ID */
  private Integer aircraftInfoId;

  /** 機体のシリアル番号（存在する場合） */
  private String serialNumber;

  /** 登録記号（レジストレーション） */
  private String registrationId;

  /** UTM 側の識別子 */
  private String utmId;

  /** 特定セッションを識別する ID（利用可能な場合） */
  private String specificSessionId;

  /** 逸脱率（割合、0.0-1.0 想定） */
  private Double routeDeviationRate;

  /** 逸脱量。JSON 文字列で水平/垂直の指標を保持します。 例: {"horizontal": 12.34, "vertical": 56.78} */
  private String routeDeviationAmount;

  /** 逸脱の開始時刻 */
  private OffsetDateTime routeDeviationStartTime;

  /** 逸脱の終了時刻 */
  private OffsetDateTime routeDeviationEndTime;

  /** 集計対象のフライト数 */
  private Integer flightCount;
}
