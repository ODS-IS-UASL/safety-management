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

import java.util.List;
import java.util.Map;
import lombok.Data;

/** MQTTで受信する航路予約ペイロードを保持するDto */
@Data
public class MqttUaslReservationPayloadDto {

  /** 航路予約毎の識別ID */
  private String uaslReservationId;

  /** ステータス（RESERVED / CANCELED / RESCINDED） */
  private String status;

  /** 一括予約リクエストID */
  private String requestId;

  /** 予約ID（destinationペイロード用） */
  private String reservationId;

  /** オペレータID */
  private String operatorId;

  /** 予約日時 */
  private String reservedAt;

  /** 開始日時 */
  private String startAt;

  /** 終了日時 */
  private String endAt;

  /** 航路区画IDリスト */
  private List<String> uaslSectionIds;

  /** originReservation の予約ID（originReservationペイロード用） */
  private String originReservationId;

  /** originReservation の機体リスト */
  private List<Map<String, Object>> originVehicles;

  /** originReservation の航路区画リスト */
  private List<Map<String, Object>> originUaslSections;

  /** 運航機体リスト（destinationペイロード用） */
  private List<Map<String, Object>> operatingAircrafts;

  /** 航路区画リスト（トップレベル） */
  private List<Map<String, Object>> uaslSections;

  /** 適合性評価結果リスト */
  private List<Map<String, Object>> conformityAssessmentResults;

  /** originReservation キーが存在したかどうか */
  private boolean hasOriginReservation;
}
