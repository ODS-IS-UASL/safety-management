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

package com.intent_exchange.uasl.controller;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.intent_exchange.uasl.api.FlightLogsApi;
import com.intent_exchange.uasl.dto.request.FlightLogSearchDto;
import com.intent_exchange.uasl.entity.FlightLogListResponse;
import com.intent_exchange.uasl.service.FlightLogsService;

/** 安全管理支援(SafetyManagement) フライトログ取得APIのコントローラクラス。 */
@RestController
public class FlightLogsController implements FlightLogsApi {

  @Autowired private FlightLogsService service;

  /**
   * {@inheritDoc}
   *
   * @param reservationId {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public ResponseEntity<FlightLogListResponse> getFlightLogs(
      String startTime,
      String endTime,
      String reservationId,
      String uaslId,
      String operatorId,
      Integer aircraftInfoId,
      String uaType) {

    FlightLogSearchDto flightLog =
        FlightLogSearchDto.builder()
            .startTime(OffsetDateTime.parse(startTime).toLocalDateTime())
            .endTime(OffsetDateTime.parse(endTime).toLocalDateTime())
            .reservationId(reservationId)
            .uaslId(uaslId)
            .operatorId(operatorId)
            .aircraftInfoId(aircraftInfoId)
            .uaType(uaType)
            .build();

    FlightLogListResponse response = service.getFlightLogs(flightLog);
    return ResponseEntity.ok(response);
  }
}
