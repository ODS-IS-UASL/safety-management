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

import java.sql.Timestamp;
import lombok.Data;

/** 航路設計エリア情報の逸脱情報を格納するdto */
@Data
public class UaslDesignAreaInfoDeviationDto {

  /** 予約ID */
  private String reservationId;

  /** 位置情報取得タイムスタンプ */
  private Timestamp getLocationTimestamp;

  /** 緯度 */
  private Double latitude;

  /** 経度 */
  private Double longitude;

  /** 標高 */
  private Double altitude;

  /** 航路逸脱割合 */
  private Double routeDeviationRate;

  /** 水平逸脱距離(m) */
  private Double horizontalDeviation;

  /** 垂直逸脱距離(m) */
  private Double verticalDeviation;

  /** 航路ID */
  private String uaslId;

  /** 航路区画ID */
  private String uaslSectionId;

  /** 運航者ID */
  private String operatorId;

  /** シリアルナンバー */
  private String serialNumber;

  /** 登録ID */
  private String registrationId;

  /** セッションID */
  private String utmId;

  /** フライト識別ID */
  private String specificSessionId;

  /** 機体種別 */
  private Integer aircraftInfoId;

  /** 航路運営者ID **/
  private String uaslAdministratorId;

  /** 運航状況 */
  private String operationalStatus;
}

