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

/**
 * 
 */
package com.intent_exchange.uasl.controller;

import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.intent_exchange.uasl.api.DroneLocationApi;
import com.intent_exchange.uasl.entity.CurrentLocationEntity;
import com.intent_exchange.uasl.entity.DroneLocationNotificationEntity;
import com.intent_exchange.uasl.exception.CurrentLocationException;
import com.intent_exchange.uasl.service.DroneLocationService;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * 安全管理支援(SafetyManagement) 運行中ドローンの位置情報通知のコントローラクラス。
 */
@RestController
public class DroneLocationController implements DroneLocationApi {

  /** ロガー */
  private static final Logger logger = LoggerFactory.getLogger(DroneLocationController.class);

  /** 運行中ドローンの位置情報通知のサービス。 */
  @Autowired
  private DroneLocationService service;

  /**
   * {@inheritDoc}
   * 
   * @param droneLocationNotificationEntity {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public ResponseEntity<Void> notifyDroneLocation(
      @Valid DroneLocationNotificationEntity droneLocationNotificationEntity) {
    if (droneLocationNotificationEntity.getReservationId() == null
        && droneLocationNotificationEntity.getUasId() == null) {
      logger.error(
          "Both reservationId and uasId are null. Please specify either reservationId or uasId. An ID to identify the reservation is required.");
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    service.notifyDroneLocation(droneLocationNotificationEntity);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * {@inheritDoc}
   * 
   * @param uaslReservationId {@inheritDoc}
   * @return {@inheritDoc}
   * @throws CurrentLocationException
   */
  @Override
  public ResponseEntity<CurrentLocationEntity> getCurrentLocation(String uaslReservationId)
      throws CurrentLocationException {
    CurrentLocationEntity body = service.getCurrentLocation(uaslReservationId);
    if (body == null) {
      logger.error("{} : {}", PropertyUtil.getProperty("error.get.current.location.message"),
          PropertyUtil.getProperty("error.get.current.location.description"));
      throw new CurrentLocationException(
          PropertyUtil.getProperty("error.get.current.location.message"));
    }
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
  }

}

