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

package com.intent_exchange.uasl.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.intent_exchange.uasl.api.PlannedDeviationApi;
import com.intent_exchange.uasl.entity.PlannedDeviationEntity;
import com.intent_exchange.uasl.service.PlannedDeviationService;

/** 安全管理支援(SafetyManagement) 運行中ドローンの位置情報通知のコントローラクラス。 */
@RestController
public class PlannedDeviationController implements PlannedDeviationApi {

  @Autowired private PlannedDeviationService service;

  /**
   * {@inheritDoc}
   *
   * @param reservationId {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public ResponseEntity<PlannedDeviationEntity> getPlannedDeviation(String reservationId) {
    PlannedDeviationEntity body = service.getPlannedDeviation(reservationId);
    return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(body);
  }

  /**
   * {@inheritDoc}
   *
   * @param reservationId {@inheritDoc}
   * @param enabled {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public ResponseEntity<Void> setPlannedDeviation(String reservationId, Boolean enabled) {
    service.setPlannedDeviation(reservationId, enabled);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
