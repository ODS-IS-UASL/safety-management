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

package com.intent_exchange.drone_highway.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.intent_exchange.drone_highway.dao.AirwayReservationMapper;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.model.AirwayReservation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 航路予約情報に関するロジック
 */
@Component
public class AirwayReservationLogic {

  @Autowired
  private AirwayReservationMapper mapper;

  /**
   * 航路予約情報を削除します。
   *
   * @param airwayReservationId 航路予約ごとの識別ID
   */
  @Transactional
  public void delete(String airwayReservationId) {
    mapper.deleteByPrimaryKey(airwayReservationId);
  }

  /**
   * 新しい航路予約情報を作成します。
   *
   * @param dto 新しい航路予約情報
   */
  @Transactional
  public void upsert(WebAirwayReservationDto airwayReservationDto) {
    AirwayReservation map = ModelMapperUtil.map(airwayReservationDto, AirwayReservation.class);
    mapper.upsertSelective(map);
  }
  
  /**
   * 航路予約情報を更新します。
   *
   * @param dto 航路予約情報
   */
  @Transactional
  public void update(WebAirwayReservationDto airwayReservationDto) {
    AirwayReservation map = ModelMapperUtil.map(airwayReservationDto, AirwayReservation.class);
    mapper.updateByPrimaryKeySelective(map);
  }
}

