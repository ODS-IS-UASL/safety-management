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

import lombok.Data;

/** 航路逸脱情報テーブル用の値を格納するdto */
@Data
public class UaslDeviationDto {

  /** 予約ID */
  private String uaslReservationId;

  /** 逸脱割合 */
  private Double routeDeviationRate;

  /** 逸脱量 */
  private String routeDeviationAmount;

  /** 逸脱検知時刻 */
  private String routeDeviationTime;

  /** 航路運営者ID */
  private String uaslAdministratorId;

  /** 運航者ID */
  private String operatorId;

  /** 航路ID */
  private String uaslId;

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

  /** 逸脱検知情報 */
  private String routeDeviationCoordinates;
}
