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
import com.intent_exchange.uasl.dto.response.CurrentLocationDto;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.entity.CurrentLocationEntity;
import com.intent_exchange.uasl.entity.DroneLocationNotificationEntity;
import com.intent_exchange.uasl.logic.DroneLocationLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 安全管理支援(SafetyManagement)に関連する処理をします。
 */
@Service
public class DroneLocationService {

  /**
   * ロガー
   */
  private static final Logger logger = LoggerFactory.getLogger(DroneLocationService.class);

  /**
   * 運行中ドローンの位置情報に関するロジック
   */
  @Autowired
  private DroneLocationLogic droneLocationLogic;


  /**
   * 運行中ドローンの位置情報を受信し、保存します。
   *
   * @param droneLocationNotificationEntity 運行中ドローンの位置情報を受信し、保存API用のentity
   * 
   */
  public void notifyDroneLocation(DroneLocationNotificationEntity droneLocationNotificationEntity) {
    DroneLocationNotificationDto notificationDto = new DroneLocationNotificationDto();
    // エンティティを DTO にマッピング
    notificationDto.setSubscriptionId(droneLocationNotificationEntity.getSubscriptionId());
    notificationDto.setReservationId(droneLocationNotificationEntity.getReservationId());
    if (droneLocationNotificationEntity.getUasId() != null) {
      notificationDto.setUasId(droneLocationNotificationEntity.getUasId().getUasId());
    }
    notificationDto.setUaType(droneLocationNotificationEntity.getUaType());
    notificationDto.setGetLocationTimestamp(droneLocationNotificationEntity.getTimestamp());
    notificationDto.setLatitude((droneLocationNotificationEntity.getLatitude()).doubleValue());
    notificationDto.setLongitude((droneLocationNotificationEntity.getLongitude()).doubleValue());
    notificationDto.setAltitude(droneLocationNotificationEntity.getAltitude());
    notificationDto.setTrackDirection(droneLocationNotificationEntity.getTrackDirection());
    if (droneLocationNotificationEntity.getSpeed() != null) {
      notificationDto.setSpeed((droneLocationNotificationEntity.getSpeed()).doubleValue());
    }
    if (droneLocationNotificationEntity.getVerticalSpeed() != null) {
      notificationDto
          .setVerticalSpeed((droneLocationNotificationEntity.getVerticalSpeed()).doubleValue());
    }

    // 予約情報取得
    WebUaslReservationDto reservationInfo =
        droneLocationLogic.getReservationInfo(notificationDto);
    // 受信した情報が予約情報 DB に存在するか確認する
    if (reservationInfo == null) {
      logger.info(
          "Reservation information not found.(Subscription ID: {}, Reservation ID: {}, UAS ID: {}, Timestamp: {})",
          notificationDto.getSubscriptionId(), notificationDto.getReservationId(),
          notificationDto.getUasId(), notificationDto.getGetLocationTimestamp());
      notificationDto = null;
    } else {
      notificationDto.setReservationId(reservationInfo.getUaslReservationId());
      notificationDto.setOperatorId(reservationInfo.getOperatorId());
      notificationDto.setPlannedDeviation(reservationInfo.getPlannedDeviation());
      notificationDto.setAircraftInfoId(reservationInfo.getAircraftInfoId());
      droneLocationLogic.insert(notificationDto);

      // 一番近い航路区画 ID を取得
      String nearestUaslSectionId = droneLocationLogic.getNearestUaslSectionId(notificationDto);
      notificationDto.setUaslSectionId(nearestUaslSectionId);

      // 逸脱判定 兼 逸脱判定結果通知
      droneLocationLogic.deviationDetect(notificationDto);
      logger.info("[Async] Deviation Detect (UAS ID: {}, Timestamp: {})",
          notificationDto.getUasId(), notificationDto.getGetLocationTimestamp());
      notificationDto = null;
    }
  }

  /**
   * 航路予約毎の識別 ID に該当するドローンの現在の位置情報を取得します。
   *
   * @param reservationId 航路予約毎の識別 ID
   * @return 現在のドローンの位置情報
   */
  public CurrentLocationEntity getCurrentLocation(String reservationId) {
    CurrentLocationDto dto = droneLocationLogic.getCurrentLocation(reservationId);
    if (dto == null) {
      return null;
    }
    return ModelMapperUtil.map(dto, CurrentLocationEntity.class);
  }
}

