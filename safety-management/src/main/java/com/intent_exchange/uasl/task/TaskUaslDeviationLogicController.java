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

package com.intent_exchange.uasl.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.service.UaslDeviationService;
import com.intent_exchange.uasl.service.AircraftTypeDeviationService;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * 航路逸脱情報の登録に関するコントローラクラス。
 */
@Component
public class TaskUaslDeviationLogicController extends AbstractTaskController {
  /** ロガー */
  private static final Logger logger =
      LoggerFactory.getLogger(TaskUaslDeviationLogicController.class);

  /** 航路逸脱情報に関するサービス */
  @Autowired
  private UaslDeviationService uaslDeviationService;

  /** 月次の機体種別毎逸脱集計処理を実施します */
  @Autowired
  private AircraftTypeDeviationService aircraftTypeDeviationService;

  @Override
  @Scheduled(cron = "${deviation.task.cron.selectRouteDeviationInfo}")
  public void performTask() {
    logger.info("航路逸脱情報の登録を実施します。");
    uaslDeviationService.registerRouteDeviationInfo();
  }

  /**
   * 月次の機体種別逸脱集計を実行するスケジュールメソッド。
   * Cronはプロパティ {@code deviation.task.cron.aircraftTypeDeviation} で上書きできます（デフォルト: 毎月1日 00:00）。
   * 処理は {@link com.intent_exchange.uasl.service.AircraftTypeDeviationService#registerAircraftTypeDeviationForPeriod(Instant, Instant)} に委譲します。
   */
  @Scheduled(cron = "${deviation.task.cron.aircraftTypeDeviation:0 0 0 1 * ?}")
  public void performAircraftTypeDeviationTask() {
    logger.info("月次の機体種別毎逸脱集計処理を実施します。");
    // 前月について、UTC におけるクローズド・オープン期間 [startOfPrevMonth, startOfMonth) を計算する
    ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
    ZonedDateTime startOfMonth = nowUtc.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
    ZonedDateTime startOfPrevMonth = startOfMonth.minus(1, ChronoUnit.MONTHS);

    Instant start = startOfPrevMonth.toInstant();
    Instant end = startOfMonth.toInstant();

    logger.info("Processing previous month period: {} - {}", start, end);
    aircraftTypeDeviationService.registerAircraftTypeDeviationForPeriod(start, end);
  }
}

