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

package com.intent_exchange.drone_highway.service;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intent_exchange.drone_highway.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.entity.LinkageInformationNotificationEntity;
import com.intent_exchange.drone_highway.logic.IdidLogic;
import com.intent_exchange.drone_highway.logic.web.WebAirwayReservationLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 安全管理支援(A-1-4)に関連する処理をします。
 */
@Service
public class IdidService {

  /**
   * DB操作に関するロジック
   */
  @Autowired
  private IdidLogic ididLogic;
  /**
   * 航路予約(A-1-3)への通信用ロジック
   */
  @Autowired
  private WebAirwayReservationLogic webAirwayReservationLogic;

  /**
   * 機体ID(リモートID)と航路予約毎の識別IDを受信し、航路予約(A-1-3)へ航路予約毎の識別IDを送信し予約情報を受信し、DBに各情報を保存します。
   *
   * @param linkageInformationNotificationEntity 機体ID(リモートID)と航路予約毎の識別
   * 
   */
  public void notifyAirwayReservation(
      @Valid LinkageInformationNotificationEntity linkageInformationNotificationEntity) {
    LinkageInformationNotificationDto notificationDto = ModelMapperUtil
        .map(linkageInformationNotificationEntity, LinkageInformationNotificationDto.class);
    // 航路予約情報取得
    WebAirwayReservationDto airwayReservationDto = webAirwayReservationLogic
        .getAirwayReservationForDb(notificationDto.getAirwayReservationId());
    // 航路予約情報と機体ID(リモートID)の紐づけ情報と航路予約情報とサブスクリプションIDの紐づけ情報を保存
    ididLogic.upsert(airwayReservationDto, notificationDto);
  }

}

