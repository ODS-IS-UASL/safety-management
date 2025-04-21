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

package com.intent_exchange.drone_highway.task;

import java.time.Clock;
import java.time.YearMonth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.service.CheckUpdatesMearMissInfoTaskService;

/**
 * ヒヤリハット情報の更新確認に関するタスクコントローラクラス。
 */
@Component
public class TaskCheckUpdatesNearMissInfoController extends AbstractTaskController {

  /** ロガー */
  private static final Logger logger =
      LoggerFactory.getLogger(TaskCheckUpdatesNearMissInfoController.class);

  /** Clockインスタンス。現在の時刻を取得するために使用します。 */
  @Autowired
  private Clock clock;

  /** ヒヤリハット情報の更新確認に関するサービス */
  @Autowired
  private CheckUpdatesMearMissInfoTaskService service;

  /**
   * ヒヤリハット情報の更新確認に関するタスクを実行します。<br>
   * 月次で実施。実施スケジュールは、@Scheduledアノテーションのcron属性で設定。
   */
  @Override
  @Scheduled(cron = "${task.schedule.cron.checkUpdatesNearMissInfo}")
  public void performTask() {
    logger.info("月次のヒヤリハット情報更新確認を実施します。");
    // 前月1日の00:00:00～前月末の23:59:59間に運航が完了した予約のヒヤリハット情報が対象
    YearMonth prevYearMonth = YearMonth.now(clock).minusMonths(1);
    service.checkUpdatesNearMissInfo(
        prevYearMonth.atDay(1).atStartOfDay(),
        prevYearMonth.atEndOfMonth().atTime(23, 59, 59));
  }
}

