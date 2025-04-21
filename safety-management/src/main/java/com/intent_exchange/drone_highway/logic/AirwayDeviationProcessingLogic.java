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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.intent_exchange.drone_highway.dto.request.AirwayDesignAreaInfoDeviationDto;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 航路逸脱情報の処理に関連するロジックを提供します。
 */
@Component
public class AirwayDeviationProcessingLogic {

  /**
   * 逸脱計算ロジック
   */
  @Autowired
  private AirwayDeviationCalculator airwayDeviationCalculator;

  /**
   * 逸脱情報リストを予約IDごとにグループ化したMapを返す。
   *
   * @param deviations 逸脱情報リストdto
   * @return 予約IDごとにグループ化した逸脱情報リストMap
   */
  public Map<String, List<AirwayDesignAreaInfoDeviationDto>> getGroupedByReservationIdMap(
      List<AirwayDesignAreaInfoDeviationDto> deviations) {
    // 逸脱情報リストがnullの場合は空のMapを返す
    if (deviations == null) {
      return new HashMap<>();
    }

    // 予約IDごとに逸脱情報をグループ化したMapを返す
    return deviations.stream()
        .collect(Collectors.groupingBy(AirwayDesignAreaInfoDeviationDto::getReservationId));
  }

  /**
   * 逸脱割合を算出する。
   *
   * @param reservationDeviations 予約IDごとの逸脱情報リスト
   * @return 逸脱割合
   */
  public Double getDeviationRate(List<AirwayDesignAreaInfoDeviationDto> reservationDeviations) {
    // 逸脱情報リストがnullもしくは空の場合は0.0を返す
    if (reservationDeviations == null || reservationDeviations.isEmpty()) {
      return 0.0;
    }

    // 逸脱情報リストの全データ数と、航路逸脱中と判断されたデータ数
    // （運航情報は1秒間隔で取得されるため、データ数が多いのでlong型で計算）
    long totalRecords = reservationDeviations.size();
    long deviationRecords = reservationDeviations.stream()
        .filter(deviation -> deviation.getOperationalStatus().equals("RouteDeviation")).count();

    // 逸脱割合を算出して返す( 航路逸脱中と判断されたデータ数 ÷ 逸脱情報リストの全データ数 ) × 100 ※小数第2位で四捨五入
    BigDecimal deviationRate = BigDecimal.valueOf(deviationRecords)
        .divide(BigDecimal.valueOf(totalRecords), 4, RoundingMode.HALF_UP)
        .multiply(BigDecimal.valueOf(100));

    return deviationRate.setScale(2, RoundingMode.HALF_UP).doubleValue();
  }

  /**
   * 水平逸脱量の95パーセンタイルを算出する。
   *
   * @param reservationDeviations 予約IDごとの逸脱情報リスト
   * @return 95パーセンタイル
   */
  public String getHorizontalPercentile(
      List<AirwayDesignAreaInfoDeviationDto> reservationDeviations) {
    // 逸脱情報リストがnullの場合は0.0を返す
    if (reservationDeviations == null) {
      return "0.0";
    }

    // 水平逸脱量のリストを作成
    List<Double> horizontalDeviations = // [0.0, 0.0, 8.92639633, 8.92639633,18.0353067,27.1437593]
        reservationDeviations.stream().map(AirwayDesignAreaInfoDeviationDto::getHorizontalDeviation)
            .collect(Collectors.toList());
    // 95パーセンタイルを算出して返す
    return airwayDeviationCalculator.calcPercentileStr(horizontalDeviations);
  }

  /**
   * 垂直逸脱量の95パーセンタイルを算出する。
   *
   * @param reservationDeviations 予約IDごとの逸脱情報リスト
   * @return 95パーセンタイル
   */
  public String getVerticalPercentile(
      List<AirwayDesignAreaInfoDeviationDto> reservationDeviations) {
    // 逸脱情報リストがnullの場合は0.0を返す
    if (reservationDeviations == null) {
      return "0.0";
    }

    // 垂直逸脱量のリストを作成
    List<Double> verticalDeviations = // [0.0, 0.0, 20.0, 20.0, 30.0, 30.0]
        reservationDeviations.stream().map(AirwayDesignAreaInfoDeviationDto::getVerticalDeviation)
            .collect(Collectors.toList());

    // 95パーセンタイル算出して返す
    return airwayDeviationCalculator.calcPercentileStr(verticalDeviations);
  }

  /**
   * 逸脱検知時刻を取得する。
   *
   * @param reservationDeviations 予約IDごとの逸脱情報dtoリスト
   * @return 逸脱検知時刻
   */
  public String getJsonTimestamps(List<AirwayDesignAreaInfoDeviationDto> reservationDeviations) {
    // 逸脱情報リストがnullの場合は空の逸脱検知時刻を返す
    if (reservationDeviations == null) {
      return ModelMapperUtil.mapToJson(Map.of("time", new ArrayList<String>()));
    }

    // routeDeviationRate が 0.0 より大きい場合の getLocationTimestamp をフィルタリングしてJSON形式に変換して文字列を蓄積して逸脱検知時刻を返す
    List<String> timestamps = reservationDeviations.stream()
        .filter(deviation -> deviation.getOperationalStatus().equals("RouteDeviation"))
        .map(deviation -> deviation.getGetLocationTimestamp().toString())
        .collect(Collectors.toList());
    return ModelMapperUtil.mapToJson(Map.of("time", timestamps));
  }

  /**
   * 逸脱検知情報を取得する。
   *
   * @param reservationDeviations 予約IDごとの逸脱情報dtoリスト
   * @return 逸脱検知情報
   * @throws JsonProcessingException JSON処理中にエラーが発生した場合
   */
  public String getJsonCoordinates(List<AirwayDesignAreaInfoDeviationDto> reservationDeviations) {
    // 逸脱情報リストがnullの場合は空の逸脱検知情報を返す
    if (reservationDeviations == null) {
      return ModelMapperUtil.mapToJson(Map.of("coordinates", new ArrayList<Map<String, Object>>()));
    }

    // routeDeviationRate が 0.0 より大きい場合の航路ID、緯度、経度、高度をフィルタリングしてJSON形式に変換して文字列を蓄積して逸脱検知情報を返す
    List<Map<String, Object>> routeDeviationDetectionInfoList = new ArrayList<>();
    for (AirwayDesignAreaInfoDeviationDto deviation : reservationDeviations) {
      if (deviation.getOperationalStatus().equals("RouteDeviation")) {
        Map<String, Object> routeDeviationDetectionInfo = new LinkedHashMap<>();
        routeDeviationDetectionInfo.put("airwaySectionId", deviation.getAirwaySectionId());

        Map<String, Object> coordinates = new LinkedHashMap<>();
        coordinates.put("latitude", deviation.getLatitude());
        coordinates.put("longitude", deviation.getLongitude());
        coordinates.put("aboveGroundLevel", deviation.getAboveGroundLevel());

        routeDeviationDetectionInfo.put("coordinates", coordinates);
        routeDeviationDetectionInfoList.add(routeDeviationDetectionInfo);
      }
    }
    return ModelMapperUtil
        .mapToJson(Map.of("RouteDeviationDetectionInfo", routeDeviationDetectionInfoList));
  }
}

