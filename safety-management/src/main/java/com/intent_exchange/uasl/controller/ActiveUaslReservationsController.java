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

import com.intent_exchange.uasl.api.ActiveUaslReservationsApi;
import com.intent_exchange.uasl.entity.ActiveReservationResponse;
import com.intent_exchange.uasl.entity.SearchActiveUaslReservationsRequest;
import com.intent_exchange.uasl.service.ActiveUaslReservationsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 安全管理支援(SafetyManagement) L25有効な予約情報の照会APIコントローラクラス。
 *
 * <p>航路区画IDリストで有効な予約情報を検索し、サマリーを返却します。
 */
@RestController
public class ActiveUaslReservationsController implements ActiveUaslReservationsApi {

  @Autowired private ActiveUaslReservationsService activeReservationsService;

  /**
   * {@inheritDoc}
   *
   * @param request {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public ResponseEntity<ActiveReservationResponse> searchActiveUaslReservations(
      @Valid @RequestBody SearchActiveUaslReservationsRequest request) {
    ActiveReservationResponse response =
        activeReservationsService.searchActiveUaslReservations(request);
    return ResponseEntity.ok(response);
  }
}
