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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.intent_exchange.drone_highway.dao.AirwayDeviationMapper;
import com.intent_exchange.drone_highway.dto.request.AirwayDesignAreaInfoDeviationDto;
import com.intent_exchange.drone_highway.dto.request.AirwayDeviationDto;
import com.intent_exchange.drone_highway.model.AirwayDeviation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 航路逸脱情報の登録に関連するロジックを提供します。
 */
@Component
public class AirwayDeviationLogic {

  /**
   * 航路逸脱情報テーブルに関するマッパー
   */
  @Autowired
  private AirwayDeviationMapper airwayDeviationMapper;

  /**
   * 航路逸脱情報の処理に関連するロジック
   */
  @Autowired
  private AirwayDeviationProcessingLogic airwayDeviationProcessingLogic;

  @Transactional
  public void registerRouteDeviationInfo(List<AirwayDesignAreaInfoDeviationDto> deviations) {


    // 逸脱情報を予約IDごとにグループ化した逸脱情報リストMapを取得
    Map<String, List<AirwayDesignAreaInfoDeviationDto>> groupedByReservationIdMap =
        airwayDeviationProcessingLogic.getGroupedByReservationIdMap(deviations);

    // 予約IDごとの航路逸脱情報を保持する航路逸脱情報dtoリスト
    List<AirwayDeviationDto> airwayDeviationDtoList = new ArrayList<AirwayDeviationDto>();

    // 取得した中身を予約IDのごとにループで取得しながら処理
    for (Map.Entry<String, List<AirwayDesignAreaInfoDeviationDto>> entry : groupedByReservationIdMap
        .entrySet()) {
      String reservationId = entry.getKey();

      // 1つの予約IDで航路区画の数だけ逸脱情報をもつリスト
      List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = entry.getValue();

      // 予約IDごとに航路逸脱情報dtoに設定
      AirwayDeviationDto airwayDeviationDto = new AirwayDeviationDto();
      airwayDeviationDto.setAirwayReservationId(reservationId);
      airwayDeviationDto.setRouteDeviationRate(
          airwayDeviationProcessingLogic.getDeviationRate(reservationDeviations));
      airwayDeviationDto
          .setRouteDeviationAmount(String.format("{\"horizontal\": %s, \"vertical\": %s}",
              airwayDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations),
              airwayDeviationProcessingLogic.getVerticalPercentile(reservationDeviations)));
      airwayDeviationDto.setRouteDeviationTime(
          airwayDeviationProcessingLogic.getJsonTimestamps(reservationDeviations));
      airwayDeviationDto
          .setAirwayAdministratorId(reservationDeviations.get(0).getAirwayAdministratorId());// 予約IDごとの航路運営者IDは同じ;
      airwayDeviationDto.setOperatorId(reservationDeviations.get(0).getOperatorId());// 予約IDごとのオペレータIDは同じ
      airwayDeviationDto.setAirwayId(reservationDeviations.get(0).getAirwayId());// 予約IDごとの航路IDは同じ
      airwayDeviationDto.setAircraftInfoId(reservationDeviations.get(0).getAircraftInfoId());// 予約IDごとの機種IDは同じ
      airwayDeviationDto.setRouteDeviationCoordinates(
          airwayDeviationProcessingLogic.getJsonCoordinates(reservationDeviations));

      // 航路逸脱情報dtoリストに予約IDごとの逸脱情報dtoを追加
      airwayDeviationDtoList.add(airwayDeviationDto);
    }

    // 航路逸脱情報dtoリストの一括登録
    List<AirwayDeviation> airwayDeviationList = airwayDeviationDtoList.stream()
        .map(dto -> ModelMapperUtil.map(dto, AirwayDeviation.class)).collect(Collectors.toList());
    airwayDeviationMapper.insertBatchSelective(airwayDeviationList);
  }
}

