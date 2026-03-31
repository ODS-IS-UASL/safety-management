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

import com.intent_exchange.uasl.dao.UaslReservationMapper;
import com.intent_exchange.uasl.model.UaslReservation;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** L25 有効な予約情報の取得に関するロジック。 */
@Component
public class ActiveUaslReservationsLogic {

  @Autowired private UaslReservationMapper reservationMapper;

  /**
   * 現在時刻以降の有効な予約情報を取得します。
   *
   * @param uaslSectionIds 航路区画IDリスト
   * @param now 現在時刻
   * @return 有効な予約情報リスト
   */
  @Transactional(readOnly = true)
  public List<UaslReservation> selectActiveReservations(
      List<String> uaslSectionIds, LocalDateTime now) {
    return reservationMapper.selectActiveBySectionIds(uaslSectionIds, now);
  }
}
