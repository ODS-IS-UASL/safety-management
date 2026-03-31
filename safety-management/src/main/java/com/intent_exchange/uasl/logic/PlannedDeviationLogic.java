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

package com.intent_exchange.uasl.logic;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.intent_exchange.uasl.dao.DroneLocationMapper;
import com.intent_exchange.uasl.dao.UaslReservationMapper;
import com.intent_exchange.uasl.dto.request.DroneLocationNotificationDto;
import com.intent_exchange.uasl.model.DroneLocation;
import com.intent_exchange.uasl.model.UaslReservation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/** 計画的な航路逸脱機能に関するロジック */
@Component
public class PlannedDeviationLogic {

  @Autowired private Clock clock;

  @Autowired private UaslReservationMapper mapperReservation;

  @Autowired private DroneLocationMapper mapperLocation;

  /**
   * 航路予約毎の識別 ID に該当する計画的な航路逸脱フラグを取得する。
   *
   * @param reservationId 航路予約毎の識別 ID
   * @return 計画的な航路逸脱
   */
  @Transactional
  public Boolean getPlannedDeviation(String reservationId) {
    return mapperReservation.getPlannedDeviation(reservationId);
  }

  /**
   * 航路予約毎の識別 ID に該当する計画的な航路逸脱フラグを更新する。
   *
   * @param reservationId 航路予約毎の識別 ID
   * @param enabled 計画的な航路逸脱
   */
  @Transactional
  public void updatePlannedDeviation(String reservationId, Boolean enabled) {
    UaslReservation map = new UaslReservation();
    map.setUaslReservationId(reservationId);
    map.setPlannedDeviation(enabled);
    mapperReservation.updateByPrimaryKeySelective(map);
  }

  /**
   * 最新のドローンの位置情報を取得する。
   *
   * @param reservationId 航路予約毎の識別 ID
   * @return ドローンの最新位置情報（存在しない場合はnull）
   */
  @Transactional
  public DroneLocation getDroneLocation(String reservationId) {
    DroneLocation map = new DroneLocation();
    map.setReservationId(reservationId);
    map.setGetLocationTimestamp(LocalDateTime.now(clock));
    return mapperLocation.selectPrevRowByPrimaryKey(map);
  }

  /**
   * 運行中ドローンの位置情報を更新する。
   *
   * @param notificationDto 運行中ドローンの位置情報
   */
  @Transactional
  public void updateDroneLocation(DroneLocationNotificationDto notificationDto) {
    DroneLocation map = ModelMapperUtil.map(notificationDto, DroneLocation.class);
    mapperLocation.updateByPrimaryKeySelective(map);
  }
}
