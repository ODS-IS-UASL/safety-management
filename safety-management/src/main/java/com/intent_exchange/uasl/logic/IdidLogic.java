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

package com.intent_exchange.uasl.logic;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.uasl.dto.request.LinkageSubscriptionIdDto;
import com.intent_exchange.uasl.dto.request.WebSubscriptionRegistrationDto;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.logic.web.WebSubscriptionRegistrationAndDeletionLogic;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * 安全管理支援(SafetyManagement)のDBに関連する処理をします。
 */
@Component
public class IdidLogic {

  /**
   * サブスクリプションの無期限オプション
   */
  private static final Boolean IS_UNLIMITED =
      Boolean.valueOf(PropertyUtil.getProperty("unlimited.subscription.enabled"));

  /**
   * 安全管理支援(SafetyManagement)に接続するためのベース URL
   */
  private static final String Uasl_BASE_URL =
      PropertyUtil.getProperty("uasl.host");

  /**
   * 航路予約情報に関するロジック
   */
  @Autowired
  private UaslReservationLogic uaslReservationLogic;
  /**
   * 予約IDとリモートIDの紐づけ情報に関するロジック
   */
  @Autowired
  private RemoteDataLogic remoteDataLogic;
  /**
   * サブスクリプションIDとリモートIDの紐づけ情報に関するロジック
   */
  @Autowired
  private SubscriptionDataLogic subscriptionDataLogic;
  /**
   * UTM への通信用ロジック
   */
  @Autowired
  private WebSubscriptionRegistrationAndDeletionLogic webSubscriptionRegistrationAndDeletionLogic;


  /**
   * DBから情報を削除します。
   *
   * @param uaslReservationId 航路予約ごとの識別ID
   */
  @Transactional
  public void delete(String uaslReservationId) {
    /** 予約IDとリモートIDの紐づけ情報を削除します。 */
    remoteDataLogic.delete(uaslReservationId);
    /** サブスクリプション ID を取得します。 */
    LinkageSubscriptionIdDto linkageSubscriptionIdDto =
        subscriptionDataLogic.getSubscriptionData(uaslReservationId);
    /** サブスクリプション削除します。 */
    webSubscriptionRegistrationAndDeletionLogic
        .deleteSubscription(linkageSubscriptionIdDto.getSubscriptionId());
    /** サブスクリプションIDとリモートIDの紐づけ情報を削除します。 */
    subscriptionDataLogic.delete(uaslReservationId);
    /** 航路予約情報を削除します。 */
    uaslReservationLogic.delete(uaslReservationId);
  }

  /**
   * 新しい情報をDBに登録します。
   *
   * @param uaslReservationDto 新しい航路予約情報
   * @param linkageInformationNotificationDto 新しい予約IDとリモートIDの紐づけ情報
   */
  @Transactional
  public void upsert(WebUaslReservationDto uaslReservationDto,
      LinkageInformationNotificationDto linkageInformationNotificationDto) {
    /** 航路予約情報を作成します。 */
    uaslReservationLogic.upsert(uaslReservationDto);
    /** 予約IDとリモートIDの紐づけ情報を登録します。 */
    remoteDataLogic.upsert(linkageInformationNotificationDto);
    // 航路 ID とエリア情報を取得
    LinkageSubscriptionIdDto linkageSubscriptionIdDto = subscriptionDataLogic
        .getAreaInfoAndUaslId(linkageInformationNotificationDto.getUaslReservationId());
    // サブスクリプション ID を登録するための DTO を作成
    WebSubscriptionRegistrationDto webSubscriptionRegistrationDto =
        new WebSubscriptionRegistrationDto();
    webSubscriptionRegistrationDto.setSubscription_name(linkageSubscriptionIdDto.getUaslId());
    webSubscriptionRegistrationDto.setView(linkageSubscriptionIdDto.getAreaInfo());
    webSubscriptionRegistrationDto.setTime_start(uaslReservationDto.getStartAt());
    webSubscriptionRegistrationDto.setTime_end(uaslReservationDto.getEndAt());
    webSubscriptionRegistrationDto.setIs_unlimited(IS_UNLIMITED);
    webSubscriptionRegistrationDto.setUasl_base_url(Uasl_BASE_URL);
    // サブスクリプション ID を発行
    Map<String, String> subscriptionId = webSubscriptionRegistrationAndDeletionLogic
        .registerSubscription(webSubscriptionRegistrationDto);
    linkageSubscriptionIdDto.setSubscriptionId(subscriptionId.get("subscription_id"));
    // サブスクリプションIDとリモートIDの紐づけ情報を保存
    subscriptionDataLogic.upsert(linkageSubscriptionIdDto);
  }


}

