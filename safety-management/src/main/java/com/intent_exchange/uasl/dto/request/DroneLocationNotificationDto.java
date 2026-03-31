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

/** ドローンの位置情報を格納するdto */
@Data
public class DroneLocationNotificationDto {
  /** エリア情報のサブスクリプション ID */
  private String subscriptionId;
  /** 機体登録 ID */
  private String uasId;
  /** ドローンの種別 */
  private String uaType;
  /** テレメトリ情報取得日時 */
  private String getLocationTimestamp;
  /** ドローンの緯度 */
  private Double latitude;
  /** ドローンの経度 */
  private Double longitude;
  /** ドローンの標高 */
  private Integer altitude;
  /** ドローンの進行方向 */
  private Integer trackDirection;
  /** ドローンの速度 */
  private Double speed;
  /** ドローンの垂直速度 */
  private Double verticalSpeed;
  /** ドローンの航路からの逸脱割合 */
  private Double routeDeviationRate;
  /** ドローンの航路からの逸脱検知時刻 */
  private String routeDeviationRateUpdateTime;
  /** 航路の予約 ID */
  private String reservationId;
  /** 航路 ID */
  private String uaslId;
  /** 航路区画 ID */
  private String uaslSectionId;
  /** ドローンの運行状況 */
  private String operationalStatus;
  /** 運航者 ID */
  private String operatorId;
  /** 飛行時間 */
  private String flightTime;
  /** 計画的な航路逸脱フラグ */
  private Boolean plannedDeviation;
  /** 機体情報ID */
  private Integer aircraftInfoId;
}

