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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.intent_exchange.uasl.dao.UaslDeviationMapper;
import com.intent_exchange.uasl.dto.request.UaslDesignAreaInfoDeviationDto;
import com.intent_exchange.uasl.dto.request.UaslDeviationDto;
import com.intent_exchange.uasl.model.UaslDeviation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 航路逸脱情報の登録に関連するロジックを提供します。
 */
@Component
public class UaslDeviationLogic {

  /**
   * 航路逸脱情報テーブルに関するマッパー
   */
  @Autowired
  private UaslDeviationMapper uaslDeviationMapper;

  /**
   * 航路逸脱情報の処理に関連するロジック
   */
  @Autowired
  private UaslDeviationProcessingLogic uaslDeviationProcessingLogic;

  @Transactional
  public void registerRouteDeviationInfo(List<UaslDesignAreaInfoDeviationDto> deviations) {


    // 逸脱情報を予約IDごとにグループ化した逸脱情報リストMapを取得
    Map<String, List<UaslDesignAreaInfoDeviationDto>> groupedByReservationIdMap =
        uaslDeviationProcessingLogic.getGroupedByReservationIdMap(deviations);

    // 予約IDごとの航路逸脱情報を保持する航路逸脱情報dtoリスト
    List<UaslDeviationDto> uaslDeviationDtoList = new ArrayList<UaslDeviationDto>();

    // 取得した中身を予約IDのごとにループで取得しながら処理
    for (Map.Entry<String, List<UaslDesignAreaInfoDeviationDto>> entry : groupedByReservationIdMap
        .entrySet()) {
      String reservationId = entry.getKey();

      // 1つの予約IDで航路区画の数だけ逸脱情報をもつリスト
      List<UaslDesignAreaInfoDeviationDto> reservationDeviations = entry.getValue();

      // 逸脱割合が0.0の場合は登録対象外
      Double routeDeviationRate =
          uaslDeviationProcessingLogic.getDeviationRate(reservationDeviations);
      if (routeDeviationRate == 0.0) {
        continue;
      }

      // 予約IDごとに航路逸脱情報dtoに設定
      UaslDeviationDto uaslDeviationDto = new UaslDeviationDto();
      uaslDeviationDto.setUaslReservationId(reservationId);
      uaslDeviationDto.setRouteDeviationRate(routeDeviationRate);
      uaslDeviationDto
          .setRouteDeviationAmount(String.format("{\"horizontal\": %s, \"vertical\": %s}",
              uaslDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations),
              uaslDeviationProcessingLogic.getVerticalPercentile(reservationDeviations)));
      uaslDeviationDto.setRouteDeviationTime(
          uaslDeviationProcessingLogic.getJsonTimestamps(reservationDeviations));
      uaslDeviationDto
          .setUaslAdministratorId(reservationDeviations.get(0).getUaslAdministratorId());// 予約IDごとの航路運営者IDは同じ;
      uaslDeviationDto.setOperatorId(reservationDeviations.get(0).getOperatorId());// 予約IDごとのオペレータIDは同じ
      uaslDeviationDto.setUaslId(reservationDeviations.get(0).getUaslId());// 予約IDごとの航路IDは同じ
      uaslDeviationDto.setSerialNumber(reservationDeviations.get(0).getSerialNumber());// 予約IDごとのシリアルナンバーは同じ
      uaslDeviationDto.setRegistrationId(reservationDeviations.get(0).getRegistrationId());// 予約IDごとの登録IDは同じ
      uaslDeviationDto.setUtmId(reservationDeviations.get(0).getUtmId());// 予約IDごとのセッションIDは同じ
      uaslDeviationDto.setSpecificSessionId(reservationDeviations.get(0).getSpecificSessionId());// 予約IDごとのフライト識別IDは同じ
      uaslDeviationDto.setAircraftInfoId(reservationDeviations.get(0).getAircraftInfoId());// 予約IDごとの機種IDは同じ
      uaslDeviationDto.setRouteDeviationCoordinates(
          uaslDeviationProcessingLogic.getJsonCoordinates(reservationDeviations));

      // 航路逸脱情報dtoリストに予約IDごとの逸脱情報dtoを追加
      uaslDeviationDtoList.add(uaslDeviationDto);
    }

    if (uaslDeviationDtoList.isEmpty()) {
      return;
    }
    // 航路逸脱情報dtoリストの一括登録
    List<UaslDeviation> uaslDeviationList = uaslDeviationDtoList.stream()
        .map(dto -> ModelMapperUtil.map(dto, UaslDeviation.class)).collect(Collectors.toList());
    uaslDeviationMapper.insertBatchSelective(uaslDeviationList);
  }
}

