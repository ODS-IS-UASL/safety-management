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

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intent_exchange.uasl.dto.request.MonitoringNotificationDestinationRequestDto;
import com.intent_exchange.uasl.entity.MonitoringNotificationDestinationRequestEntity;
import com.intent_exchange.uasl.logic.web.WebMonitoringNotificationDestinationLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 安全管理支援(SafetyManagement) 第三者立入監視情報に関連する処理をします。
 */
@Service
public class MonitoringNotificationService {

  /**
   * 空域デジタルツイン 第三者立入監視情報への通信用ロジック
   */
  @Autowired
  private WebMonitoringNotificationDestinationLogic webMonitoringNotificationDestinationLogic;

  /**
   * 変更があった場合に通知(Subscribe)を行うため、航路の安全管理支援(SafetyManagement)の通知先をデータプロバイダに登録する。
   * 
   * @param thirdPartyEntryRequestEntity 通知先情報Entity
   */
  public void registerMonitoringNotificationDestination(
      @Valid MonitoringNotificationDestinationRequestEntity monitoringNotificationDestinationRequestEntity) {
    // 空域デジタルツイン 第三者立入監視情報通知先の登録をリクエスト
    webMonitoringNotificationDestinationLogic.registerMonitoringNotificationDestination(
        ModelMapperUtil.map(monitoringNotificationDestinationRequestEntity,
            MonitoringNotificationDestinationRequestDto.class));
  }

  /**
   * データプロバイダに登録した情報変更通知先を変更する。
   * 
   * @param thirdPartyEntryRequestEntity 通知先情報Entity
   */
  public void changeMonitoringNotificationDestination(
      @Valid MonitoringNotificationDestinationRequestEntity monitoringNotificationDestinationRequestEntity) {
    // 空域デジタルツイン 第三者立入監視情報へ通知先の登録をリクエスト
    webMonitoringNotificationDestinationLogic.changeMonitoringNotificationDestination(
        ModelMapperUtil.map(monitoringNotificationDestinationRequestEntity,
            MonitoringNotificationDestinationRequestDto.class));
  }

  /**
   * データプロバイダに登録した情報変更通知先を削除する。
   */
  public void deleteMonitoringNotificationDestination() {
    // 空域デジタルツイン 第三者立入監視情報へ通知先の削除をリクエスト
    webMonitoringNotificationDestinationLogic.deleteMonitoringNotificationDestination();
  }



}

