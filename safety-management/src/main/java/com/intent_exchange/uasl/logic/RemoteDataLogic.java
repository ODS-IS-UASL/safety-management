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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.intent_exchange.uasl.dao.RemoteDataMapper;
import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.uasl.model.RemoteData;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 予約IDとリモートIDの紐づけ情報に関するロジック
 */
@Component
public class RemoteDataLogic {

  @Autowired
  private RemoteDataMapper mapper;

  /**
   * 航路予約ごとの識別IDとリモートIDの紐づけ情報を削除します。
   *
   * @param uaslReservationId 航路予約ごとの識別ID
   */
  @Transactional
  public void delete(String uaslReservationId) {
    mapper.deleteByPrimaryKey(uaslReservationId);
  }

  /**
   * 新しい航路予約ごとの識別IDとリモートIDの紐づけ情報を作成します。
   *
   * @param dto 航路予約ごとの識別IDとリモートIDの紐づけ情報
   */
  @Transactional
  public void upsert(LinkageInformationNotificationDto remoteDataDto) {
    RemoteData map = ModelMapperUtil.map(remoteDataDto, RemoteData.class);
    if (remoteDataDto.getUasId() != null) {
      map.setSerialNumber(remoteDataDto.getUasId().getSerialNumber());
      map.setRegistrationId(remoteDataDto.getUasId().getRegistrationId());
      map.setUtmId(remoteDataDto.getUasId().getUtmId());
      map.setSpecificSessionId(remoteDataDto.getUasId().getSpecificSessionId());
    }
    mapper.upsertSelective(map);
  }
}
