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

package com.intent_exchange.drone_highway.logic;

import java.time.Clock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.intent_exchange.drone_highway.dao.DroneLocationMapper;
import com.intent_exchange.drone_highway.dto.request.DroneLocationNotificationDto;
import com.intent_exchange.drone_highway.dto.response.CurrentLocationDto;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.logic.mqtt.MqttDeviationNotificationLogic;
import com.intent_exchange.drone_highway.model.AirwayReservation;
import com.intent_exchange.drone_highway.model.CurrentLocation;
import com.intent_exchange.drone_highway.model.DroneLocation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 運行中ドローンの位置情報に関するロジック
 */
@Component
public class DroneLocationLogic {

  /** ロガー */
  private static final Logger logger = LoggerFactory.getLogger(DroneLocationLogic.class);

  @Autowired
  private DroneLocationMapper mapper;

  @Autowired
  private Clock clock;

  /**
   * MqttDeviationNotificationLogic のインスタンス。 外部システムとの連携に使用されます。
   */
  @Autowired
  private MqttDeviationNotificationLogic mqttDeviationNotificationLogic;

  /**
   * 運行中ドローンの位置情報を格納します。
   *
   * @param dto 運行中ドローンの位置情報
   */
  @Transactional
  public void insert(DroneLocationNotificationDto notificationDto) {
    DroneLocation map = ModelMapperUtil.map(notificationDto, DroneLocation.class);
    mapper.insertSelective(map);
  }

  /**
   * 運行中ドローンの位置情報を更新します。
   *
   * @param dto 運行中ドローンの位置情報
   */
  @Transactional
  public void updateDroneLocation(DroneLocationNotificationDto notificationDto) {
    DroneLocation map = ModelMapperUtil.map(notificationDto, DroneLocation.class);
    mapper.updateByPrimaryKeySelective(map);
  }

  /**
   * 運行中ドローンの位置情報から逸脱検知します。
   *
   * @param dto 運行中ドローンの位置情報
   */
  @Async("taskExecutor")
  public void deviationDetect(DroneLocationNotificationDto notificationDto) {
    DroneLocation map = ModelMapperUtil.map(notificationDto, DroneLocation.class);
    // 逸脱検知
    DroneLocation res = mapper.deviationDetect(map);
    notificationDto = ModelMapperUtil.map(res, DroneLocationNotificationDto.class);
    notificationDto.setRouteDeviationRateUpdateTime(String.valueOf(clock.instant()));
    // 一つ前の運行情報を取得
    DroneLocation prev = mapper.selectPrevRowByPrimaryKey(map);
    DroneLocationNotificationDto prevDto = null;
    if (prev != null) {
      prevDto = ModelMapperUtil.map(prev, DroneLocationNotificationDto.class);
    }

    // 逸脱判定結果通知
    if (notificationDto.getOperationalStatus() != null
        && (notificationDto.getOperationalStatus()).equals("RouteApproach") || prev == null) {
      // ログ出力
      logger
          .info(Double.valueOf(notificationDto.getRouteDeviationRate()).equals(Double.valueOf(0.0))
              ? "Entered the route"
              : "Entering the route");
      // 逸脱判定結果で DB を更新
      notificationDto.setOperationalStatus(
          Double.valueOf(notificationDto.getRouteDeviationRate()).equals(Double.valueOf(0.0))
              ? "NormalOperation"
              : notificationDto.getOperationalStatus());
      this.updateDroneLocation(notificationDto);
    } else if (Double.valueOf(notificationDto.getRouteDeviationRate()).equals(Double.valueOf(0.0))
        && Double.valueOf(prevDto.getRouteDeviationRate())
            .equals(Double.valueOf(notificationDto.getRouteDeviationRate()))) {
      // ログ出力
      logger.info("Normal Operation");
      // 逸脱判定結果で DB を更新
      notificationDto.setOperationalStatus("NormalOperation");
      this.updateDroneLocation(notificationDto);
    } else if (!Double.valueOf(notificationDto.getRouteDeviationRate())
        .equals(prevDto.getRouteDeviationRate())) {
      // ログ出力
      logger.info("Deviation detected or Returning to the route");
      // 逸脱判定結果で DB を更新
      notificationDto.setOperationalStatus(
          Double.valueOf(notificationDto.getRouteDeviationRate()).equals(Double.valueOf(0.0))
              ? "NormalOperation"
              : "RouteDeviation");
      this.updateDroneLocation(notificationDto);
      // 逸脱通知
      mqttDeviationNotificationLogic.notifyAirwayDeviation(notificationDto);
    } else {
      // ログ出力
      logger.info("Deviation detected");
      // 逸脱判定結果で DB を更新
      notificationDto.setOperationalStatus("RouteDeviation");
      this.updateDroneLocation(notificationDto);
    }
  }

  /**
   * 予約情報を取得します。
   *
   * @param dto 運行中ドローンの位置情報
   */
  public WebAirwayReservationDto getReservationInfo(DroneLocationNotificationDto notificationDto) {
    DroneLocation map = ModelMapperUtil.map(notificationDto, DroneLocation.class);
    AirwayReservation res = mapper.getReservationInfo(map);
    if (res == null) {
      return null;
    }
    return ModelMapperUtil.map(res, WebAirwayReservationDto.class);
  }

  /**
   * 一番近い航路区画 ID を取得します。
   * 
   * @param dto 運行中ドローンの位置情報
   */
  public String getNearestAirwaySectionId(DroneLocationNotificationDto notificationDto) {
    DroneLocation map = ModelMapperUtil.map(notificationDto, DroneLocation.class);
    String res = mapper.getNearestAirwaySectionId(map);
    return res;
  }

  /**
   * 航路予約毎の識別 ID に該当するドローンの現在の位置情報を取得します。
   * 
   * @param reservationId 航路予約毎の識別 ID
   * @return 現在のドローンの位置情報
   */
  public CurrentLocationDto getCurrentLocation(String reservationId) {
    CurrentLocation res = mapper.getCurrentLocation(reservationId);
    if (res == null) {
      return null;
    }
    return ModelMapperUtil.map(res, CurrentLocationDto.class);
  }

}

