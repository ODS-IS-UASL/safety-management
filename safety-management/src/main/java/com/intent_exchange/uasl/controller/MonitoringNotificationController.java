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

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import com.intent_exchange.uasl.api.MonitoringNotificationApi;
import com.intent_exchange.uasl.entity.MonitoringNotificationDestinationRequestEntity;
import com.intent_exchange.uasl.service.MonitoringNotificationService;

/**
 * 安全管理支援(SafetyManagement) 第三者立入監視情報通知のコントローラクラス。
 */
@RestController
public class MonitoringNotificationController implements MonitoringNotificationApi {

  /**
   * 第三者立入監視情報通知のサービス。
   */
  @Autowired
  private MonitoringNotificationService service;

  /**
   * {@inheritDoc}
   * 
   * @param monitoringNotificationDestinationRequestEntity {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public ResponseEntity<Void> registerMonitoringNotificationDestination(
      @Valid MonitoringNotificationDestinationRequestEntity monitoringNotificationDestinationRequestEntity) {
    // 第三者立入監視情報の通知先をデータプロバイダに登録
    service
        .registerMonitoringNotificationDestination(monitoringNotificationDestinationRequestEntity);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * {@inheritDoc}
   * 
   * @param monitoringNotificationDestinationRequestEntity {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public ResponseEntity<Void> changeMonitoringNotificationDestination(
      @Valid MonitoringNotificationDestinationRequestEntity monitoringNotificationDestinationRequestEntity) {
    // データプロバイダに登録した第三者立入監視情報の通知先を変更
    service.changeMonitoringNotificationDestination(monitoringNotificationDestinationRequestEntity);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  /**
   * {@inheritDoc}
   * 
   * @param thirdPartyEntryRequestEntity {@inheritDoc}
   * @return {@inheritDoc}
   */
  @Override
  public ResponseEntity<Void> deleteMonitoringNotificationDestination() {
    // データプロバイダに登録した情報変更通知先を削除する。
    service.deleteMonitoringNotificationDestination();

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

