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

import java.time.LocalDateTime;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intent_exchange.uasl.dto.request.NotifyUpdatesNearMissInfoDto;
import com.intent_exchange.uasl.logic.NearMissInfoLogic;
import com.intent_exchange.uasl.logic.mqtt.MqttUpdatesNearMissInfoNotificationLogic;

/**
 * 月次のヒヤリハット情報更新確認に関するサービスクラス。<br>
 * TODO: 非同期での処理は実施しなため「～TaskService」の命名は不適切か。
 */
@Service
public class CheckUpdatesMearMissInfoTaskService {

  /**
   * 航路の安全管理（SafetyManagement）管理のヒヤリハット情報に関するロジック
   */
  @Autowired
  private NearMissInfoLogic nearMissInfoLogic;

  /**
  * 外部システム連携へのMQTT通信用ロジック(ヒヤリハット情報更新通知用)
   */
  @Autowired
  private MqttUpdatesNearMissInfoNotificationLogic mqttUpdatesNearMissInfoNotification;

  /**
   * 指定期間内のヒヤリハット情報有無を確認し、<br>
   * 情報が有る（=ヒヤリハットを検知した）運航で利用した航路の航路運営者へ、更新通知(※)を行う。<br>
   * ※航路運営者ID単位でMQTTメッセージをpublish。
   * 
   * @param startAt 抽出対象期間（開始日時）
   * @param endAt 抽出対象期間（終了日時）
   */
  public void checkUpdatesNearMissInfo(LocalDateTime startAt, LocalDateTime endAt) {
    // 航路運営者ID単位でヒヤリハット情報を集計する
    // (key:航路運営者ID value:ヒヤリハット情報更新通知用DTO のMapを作成）
    Map<String, NotifyUpdatesNearMissInfoDto> adminIdNearMissInfoMap =
        nearMissInfoLogic.getAdminIdNearMissInfoMap(startAt, endAt);

    // 航路運営者ID単位でトピックにMQTTメッセージをpublish
    adminIdNearMissInfoMap.forEach((adminId, nearMissInfoDto) -> {
      mqttUpdatesNearMissInfoNotification.notifyUpdatesNearMissInfo(adminId, nearMissInfoDto);
    });
  }
}
