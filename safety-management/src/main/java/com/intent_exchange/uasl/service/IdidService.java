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
import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.entity.LinkageInformationNotificationEntity;
import com.intent_exchange.uasl.logic.IdidLogic;
import com.intent_exchange.uasl.logic.web.WebUaslReservationLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 安全管理支援(SafetyManagement)に関連する処理をします。
 */
@Service
public class IdidService {

  /**
   * DB操作に関するロジック
   */
  @Autowired
  private IdidLogic ididLogic;
  /**
   * 航路予約への通信用ロジック
   */
  @Autowired
  private WebUaslReservationLogic webUaslReservationLogic;

  /**
   * 機体ID(リモートID)と航路予約毎の識別IDを受信し、航路予約へ航路予約毎の識別IDを送信し予約情報を受信し、DBに各情報を保存します。
   *
   * @param linkageInformationNotificationEntity 機体ID(リモートID)と航路予約毎の識別
   * 
   */
  public void notifyUaslReservation(
      @Valid LinkageInformationNotificationEntity linkageInformationNotificationEntity) {
    LinkageInformationNotificationDto notificationDto = ModelMapperUtil
        .map(linkageInformationNotificationEntity, LinkageInformationNotificationDto.class);
    // 航路予約情報取得
    WebUaslReservationDto uaslReservationDto = webUaslReservationLogic
        .getUaslReservationForDb(notificationDto.getUaslReservationId());
    // 航路予約情報と機体ID(リモートID)の紐づけ情報と航路予約情報とサブスクリプションIDの紐づけ情報を保存
    ididLogic.upsert(uaslReservationDto, notificationDto);
  }

}

