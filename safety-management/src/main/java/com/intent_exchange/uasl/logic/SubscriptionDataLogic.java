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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.intent_exchange.uasl.dao.SubscriptionDataMapper;
import com.intent_exchange.uasl.dto.request.LinkageSubscriptionIdDto;
import com.intent_exchange.uasl.model.SubscriptionData;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * サブスクリプション ID とリモート ID の紐づけ情報に関するロジック
 */
@Component
public class SubscriptionDataLogic {

  @Autowired
  private SubscriptionDataMapper mapper;

  /**
   * 航路予約ごとの識別 ID とサブスクリプション ID の紐づけ情報を削除します。
   *
   * @param uaslReservationId 航路予約ごとの識別ID
   */
  @Transactional
  public void delete(String uaslReservationId) {
    mapper.deleteByPrimaryKey(uaslReservationId);
  }

  /**
   * 新しい航路予約ごとの識別 ID とサブスクリプション ID の紐づけ情報を作成します。
   *
   * @param dto 航路予約ごとの識別 ID とサブスクリプション ID の紐づけ情報
   */
  @Transactional
  public void upsert(LinkageSubscriptionIdDto subscriptionDataDto) {
    SubscriptionData map = ModelMapperUtil.map(subscriptionDataDto, SubscriptionData.class);
    mapper.upsertSelective(map);
  }

  /**
   * 新しい航路予約ごとの識別 ID で航路 ID とそのエリア情報を取得します。
   *
   * @param uaslReservationId 航路予約ごとの識別ID
   */
  public LinkageSubscriptionIdDto getAreaInfoAndUaslId(String uaslReservationId) {
    // 航路 ID とエリア情報を取得
    Map<String, Object> areaInfoAndUaslId =
        mapper.selectAreaInfoAndUaslIdByReservationId(uaslReservationId);
    String uaslId = (String) areaInfoAndUaslId.get("uasl_id");
    String areaInfo = (String) areaInfoAndUaslId.get("box2d");

    // "BOX(" と ")" を削除
    String coordinatesString = areaInfo.replace("BOX(", "").replace(")", "");
    // 座標を分割
    String[] coordinatePairs = coordinatesString.split(",");
    // 各座標を分割して配列に格納
    List<Double> coordinates = new ArrayList<Double>();
    String[] firstPair = coordinatePairs[0].trim().split(" ");
    String[] secondPair = coordinatePairs[1].trim().split(" ");

    // 緯度経度の順番に格納。配列の形式は[南西緯度, 南西経度, 北東緯度, 北東経度]。
    coordinates.add(Double.parseDouble(firstPair[1]));
    coordinates.add(Double.parseDouble(firstPair[0]));
    coordinates.add(Double.parseDouble(secondPair[1]));
    coordinates.add(Double.parseDouble(secondPair[0]));

    LinkageSubscriptionIdDto dto = new LinkageSubscriptionIdDto();
    dto.setUaslReservationId(uaslReservationId);
    dto.setUaslId(uaslId);
    dto.setAreaInfo(coordinates);
    return dto;
  }

  /**
   * 航路予約ごとの識別 ID で DB の情報を取得します。
   *
   * @param uaslReservationId 航路予約ごとの識別ID
   */
  public LinkageSubscriptionIdDto getSubscriptionData(String uaslReservationId) {
    SubscriptionData subscriptionData = mapper.selectByPrimaryKey(uaslReservationId);
    LinkageSubscriptionIdDto dto = ModelMapperUtil.map(subscriptionData, LinkageSubscriptionIdDto.class);
    return dto;
  }

}

