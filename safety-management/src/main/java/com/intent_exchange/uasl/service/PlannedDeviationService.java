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

package com.intent_exchange.uasl.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intent_exchange.uasl.dto.request.DroneLocationNotificationDto;
import com.intent_exchange.uasl.entity.PlannedDeviationEntity;
import com.intent_exchange.uasl.exception.PlannedDeviationException;
import com.intent_exchange.uasl.logic.PlannedDeviationLogic;
import com.intent_exchange.uasl.logic.mqtt.MqttDeviationNotificationLogic;
import com.intent_exchange.uasl.model.DroneLocation;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import com.intent_exchange.uasl.util.PropertyUtil;

/** 安全管理支援(SafetyManagement)に関連する処理をします。 */
@Service
public class PlannedDeviationService {

  private static final Logger logger = LoggerFactory.getLogger(PlannedDeviationService.class);

  @Autowired private PlannedDeviationLogic plannedDeviationLogic;

  @Autowired private MqttDeviationNotificationLogic mqttNotificationLogic;

  /**
   * 航路予約毎の識別 ID に該当する計画的な航路逸脱を取得する。
   *
   * @param reservationId 航路予約毎の識別 ID
   * @return 計画的な航路逸脱設定エンティティ
   */
  public PlannedDeviationEntity getPlannedDeviation(String reservationId) {
    // 予約情報取得
    Boolean enabled = getAndValidatePlannedDeviation(reservationId);

    PlannedDeviationEntity entity = new PlannedDeviationEntity();
    entity.setEnabled(enabled);

    return entity;
  }

  /**
   * 航路予約毎の識別 ID に該当する計画的な航路逸脱を設定する。
   *
   * @param reservationId 航路予約毎の識別 ID
   * @param enabled 計画的な航路逸脱
   * @throws PlannedDeviationException 予約情報が存在しない場合
   */
  public void setPlannedDeviation(String reservationId, Boolean enabled) {

    // 予約情報の取得
    Boolean planned = getAndValidatePlannedDeviation(reservationId);

    // 変更なしの場合即時return
    if (planned.equals(enabled)) {
      return;
    }

    // 計画的な航路逸脱フラグ更新
    plannedDeviationLogic.updatePlannedDeviation(reservationId, enabled);

    // 現在の運航状況を取得
    DroneLocation location = plannedDeviationLogic.getDroneLocation(reservationId);
    if (location == null) {
      return;
    }
    DroneLocationNotificationDto notificationDto =
        ModelMapperUtil.map(location, DroneLocationNotificationDto.class);

    // 状態変化に応じて通知送信
    notifyIfStatusChanged(notificationDto, enabled);
  }

  /**
   * 状態変化があれば MQTT 通知を送信
   *
   * @param notificationDto 運行中ドローンの位置情報
   * @param enabled 計画的な航路逸脱フラグ
   * @implNote 現在は文字列リテラルを使用していますが、将来的にはOperationalStatusをenumに変更予定。 関連: L26仕様書、API仕様書の
   *     getCurrentLocation レスポンス定義を参照。
   */
  private void notifyIfStatusChanged(
      DroneLocationNotificationDto notificationDto, Boolean enabled) {
    String currentStatus = notificationDto.getOperationalStatus();
    String newStatus = null;

    if (Boolean.TRUE.equals(enabled) && "RouteDeviation".equals(currentStatus)) {
      // 計画的な航路逸脱あり & 現在逸脱中 → PlannedRouteDeviation
      newStatus = "PlannedRouteDeviation";
    } else if (Boolean.FALSE.equals(enabled) && "PlannedRouteDeviation".equals(currentStatus)) {
      // 計画的な航路逸脱なし & 計画的な航路逸脱中 → RouteDeviation
      newStatus = "RouteDeviation";
    }

    if (newStatus != null) {
      notificationDto.setOperationalStatus(newStatus);
      // 運行状況を更新
      plannedDeviationLogic.updateDroneLocation(notificationDto);
      // MQTT 通知を送信
      mqttNotificationLogic.notifyUaslDeviation(notificationDto);
    }
  }

  /**
   * 予約情報の計画的な航路逸脱フラグを取得し、存在チェックを行う
   *
   * @param reservationId 航路予約毎の識別 ID
   * @return 計画的な航路逸脱フラグ
   * @throws PlannedDeviationException 予約情報が存在しない場合
   */
  private Boolean getAndValidatePlannedDeviation(String reservationId) {
    Boolean enabled = plannedDeviationLogic.getPlannedDeviation(reservationId);
    if (enabled == null) {
      logger.error("Reservation not found: reservationId={}", reservationId);
      throw new PlannedDeviationException(
          PropertyUtil.getProperty("error.planned.deviation.message"));
    }
    return enabled;
  }
}
