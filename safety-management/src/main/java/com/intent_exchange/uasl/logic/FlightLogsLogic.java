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

package com.intent_exchange.uasl.logic;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.intent_exchange.uasl.dao.DroneLocationMapper;
import com.intent_exchange.uasl.dto.request.FlightLogSearchDto;
import com.intent_exchange.uasl.model.FlightLog;
import com.intent_exchange.uasl.model.FlightLogSearch;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/** フライトログ取得APIに関するロジック */
@Component
public class FlightLogsLogic {

  @Autowired private DroneLocationMapper mapper;

  /**
   * DBから指定された条件に基づいた運行情報を取得する。
   *
   * @param searchDto フライトログ検索DTO
   * @return 運航情報リスト
   */
  @Transactional
  public List<FlightLog> getFlightLogs(FlightLogSearchDto searchDto) {
    FlightLogSearch map = ModelMapperUtil.map(searchDto, FlightLogSearch.class);
    return mapper.getFlightLogs(map);
  }
}
