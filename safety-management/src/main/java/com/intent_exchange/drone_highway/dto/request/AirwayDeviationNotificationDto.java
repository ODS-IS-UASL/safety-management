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

import lombok.Data;

/** 航路逸脱検知情報を格納するdto */
@Data
public class AirwayDeviationNotificationDto {
  /** 航路予約のID */
  private String airwayReservationId;
  /** 機体の状態を指定 */
  private String operationalStatus;
  /** 航路区画のID */
  private String airwaySectionId;
  /** 逸脱発生地点の座標 */
  private Coordinates coordinates;
  /** 機体のID */
  private String aircraftId;
  /** 逸脱発生時刻 */
  private String deviateAt;
  /** 飛行時間 */
  private String flightTime;
  /** 運航者(予約者)のID */
  private String operatorId;

  /** Coordinates クラス */
  @Data
  public class Coordinates {
    /** 緯度 */
    private Double latitude;
    /** 経度 */
    private Double longitude;
    /** 対地高度 */
    private Integer aboveGroundLevel;
  }

}

