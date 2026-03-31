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

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.intent_exchange.uasl.dto.request.FlightLogSearchDto;
import com.intent_exchange.uasl.entity.FlightLogListResponse;
import com.intent_exchange.uasl.entity.FlightLogResponse;
import com.intent_exchange.uasl.entity.FlightLogResponse.OperationalStatusEnum;
import com.intent_exchange.uasl.entity.FlightLogResponseUasId;
import com.intent_exchange.uasl.logic.FlightLogsLogic;
import com.intent_exchange.uasl.model.FlightLog;
import com.intent_exchange.uasl.util.ModelMapperUtil;


/** 安全管理支援(SafetyManagement) フライトログ取得APIのサービスクラス。 */
@Service
public class FlightLogsService {

  private static final Logger logger = LoggerFactory.getLogger(FlightLogsService.class);

  @Autowired private FlightLogsLogic flightLogsLogic;

  /**
   * 指定された条件に基づいて運航履歴データを検索する。
   *
   * @param searchDto フライトログ検索DTO
   * @return フライトログリストレスポンス
   */
  public FlightLogListResponse getFlightLogs(FlightLogSearchDto searchDto) {
    Duration duration = Duration.between(searchDto.getStartTime(), searchDto.getEndTime());
    // 31日(24h * 31) よりも長いか判定
    if (duration.compareTo(Duration.ofDays(31)) > 0) {
      logger.error("The search period error: duration={}", duration);
      throw new IllegalArgumentException("The search period exceeds 31 days.");
    }

    // データを検索
    List<FlightLog> entities = flightLogsLogic.getFlightLogs(searchDto);

    FlightLogListResponse response = new FlightLogListResponse();

    // データがない場合
    if (entities == null || entities.isEmpty()) {
      response.setTotalCount(0);
      response.setFlightLogs(Collections.emptyList());
      return response;
    }

    List<FlightLogResponse> logList =
        entities.stream()
            .map(
                entity -> {
                  FlightLogResponse flightLog = ModelMapperUtil.map(entity, FlightLogResponse.class);
                  flightLog.setUasId(ModelMapperUtil.map(entity, FlightLogResponseUasId.class));
                  flightLog.setOperationalStatus(
                      OperationalStatusEnum.fromValue(entity.getOperationalStatus()));
                  return flightLog;
                })
            .toList();
    response.setFlightLogs(logList);
    response.setTotalCount(logList.size());

    return response;
  }
}
