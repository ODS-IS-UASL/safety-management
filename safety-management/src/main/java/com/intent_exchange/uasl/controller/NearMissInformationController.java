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

import java.text.ParseException;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.intent_exchange.uasl.api.NearMissInformationApi;
import com.intent_exchange.uasl.entity.NearMissInformationRequest;
import com.intent_exchange.uasl.entity.NearMissInformationResponse;
import com.intent_exchange.uasl.service.NearMissInformationService;

/**
 * ヒヤリハット情報の取得のコントローラクラス。
 */
@RestController
public class NearMissInformationController implements NearMissInformationApi {

  /** ヒヤリハット情報の取得のサービス。 */
  @Autowired
  private NearMissInformationService service;

  /**
   * {@inheritDoc}
   * 
   * @param NearMissInformationRequest {@inheritDoc}
   * @return {@inheritDoc}
   * @throws ParseException
   * @throws JsonProcessingException
   * @throws JsonMappingException
   */
  @Override
  public ResponseEntity<NearMissInformationResponse> getNearMissInformation(
      @Valid NearMissInformationRequest nearMissInformationRequest) {

    // 正常系の時のレスポンス
    NearMissInformationResponse responce =
        service.nearMissInformationEntry(nearMissInformationRequest);
    return ResponseEntity.status(HttpStatus.OK).body(responce);
  }
}

