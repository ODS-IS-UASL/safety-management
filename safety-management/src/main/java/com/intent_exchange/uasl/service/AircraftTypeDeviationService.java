/*
 * Copyright 2026 Intent Exchange, Inc.
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

import com.intent_exchange.uasl.dao.AircraftTypeDeviationMapper;
import com.intent_exchange.uasl.logic.AircraftTypeDeviationLogic;
import java.sql.Timestamp;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** スケジュールタスク向けに航空機種別逸脱の集計機能を公開するサービス。 */
@Service
public class AircraftTypeDeviationService {

  @Autowired private AircraftTypeDeviationLogic logic;

  @Autowired private AircraftTypeDeviationMapper selectMapper;

  private static final int PAGE_SIZE = 5000;

  /**
   * 指定期間 [start, end) の集計を実行します。
   *
   * @param start 期間開始（含む）
   * @param end 期間終了（排除）
   */
  public void registerAircraftTypeDeviationForPeriod(Instant start, Instant end) {
    // 期間を Timestamp に変換して期間指定のセレクトを実行
    Timestamp periodStart = Timestamp.from(start);
    Timestamp periodEnd = Timestamp.from(end);

    int offset = 0;
    while (true) {
      var infoList =
          selectMapper.selectRouteDeviationInfoForPeriodPaged(
              periodStart, periodEnd, PAGE_SIZE, offset);
      if (infoList == null || infoList.isEmpty()) {
        break;
      }
      logic.registerAircraftTypeDeviation(infoList);
      if (infoList.size() < PAGE_SIZE) {
        break;
      }
      offset += PAGE_SIZE;
    }
  }
}
