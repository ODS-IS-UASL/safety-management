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

package com.intent_exchange.uasl.logic.conformity_assessment;

import java.time.Clock;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.dto.request.UaslDesignRailwayCrossingInfoDto;
import com.intent_exchange.uasl.dto.request.RailwayOperationInformationRequestDto;
import com.intent_exchange.uasl.dto.response.RailwayOperationInformationResponseDto;
import com.intent_exchange.uasl.logic.web.WebRailwayOperationLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * 鉄道運行情報の適合性評価ロジック
 */
@Component
public class RailwayOperationInformationEvaluationLogic
    implements IFConformityAssessmentLogic<RailwayOperationInformationRequestDto> {

  /**
   * 鉄道運行情報への通信用ロジック
   */
  @Autowired
  WebRailwayOperationLogic logic;

  // 現在の時刻を取得するために使用
  @Autowired
  private Clock clock;

  /**
   * 鉄道運行情報の適合性評価を行う
   * 
   * @param dto 鉄道運行情報のリクエスト情報
   * @return 鉄道運行情報を用いた適合性評価の結果
   */
  @Override
  public Boolean check(RailwayOperationInformationRequestDto dto) {

    // 予約開始日時
    LocalDateTime startAt = dto.getStartAt();

    // 初回実行フラグがfalseかつ、当日＞予約日の場合処理をスキップする
    if (!dto.getRestricted() && LocalDateTime.now(clock).isAfter(startAt)) {
      return null;
    }

    // 駅名と相対値の取得
    // 駅がない場合は、確認ができず鉄道の影響は受けないのでtrueを返却
    if (dto.getRailwayCrossingInfo() == null || dto.getRailwayCrossingInfo().isBlank()) {
      return true;
    }
    List<Map<String, Object>> railwayCrossingInfoMapList =
        ModelMapperUtil.jsonToListMap(dto.getRailwayCrossingInfo());
    List<UaslDesignRailwayCrossingInfoDto> railwayCrossingInfoDtoList =
        ModelMapperUtil.convertListMapToListDto(railwayCrossingInfoMapList,
            UaslDesignRailwayCrossingInfoDto.class);

    // 交点情報がない場合、true(適合性評価OK)を返す
    if (railwayCrossingInfoDtoList.contains(null))
      return true;

    // DateTimeFormatterを使用して文字列をLocalDateTimeに変換
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm").withZone(clock.getZone());

    // 予約終了日時
    LocalDateTime endAt = dto.getEndAt();

    // MVP3では交点情報は一つのみ取得を想定している
    // 相対値
    Double relativeValue =
        Double.parseDouble(railwayCrossingInfoDtoList.getFirst().getRelativeValue());
    // 駅名
    String stationName = railwayCrossingInfoDtoList.getFirst().getStation1();
    String otherStationName = railwayCrossingInfoDtoList.getFirst().getStation2();
    // 予約開始、終了日を日本時間で持つ
    ZonedDateTime ｊStartAt =
        startAt.atZone(clock.getZone()).withZoneSameInstant(ZoneId.of("Asia/Tokyo"));
    ZonedDateTime ｊEndAt =
        endAt.atZone(clock.getZone()).withZoneSameInstant(ZoneId.of("Asia/Tokyo"));

    // 運行ダイヤ(1日全てのダイヤ)の取得(上りと下りの2種類)
    RailwayOperationInformationResponseDto railwayOperationInfoDto =
        logic.getRailwayOperationInformation(stationName, otherStationName, ｊStartAt, ｊEndAt);
    RailwayOperationInformationResponseDto otherRailwayOperationInfoDto =
        logic.getRailwayOperationInformation(otherStationName, stationName, ｊStartAt, ｊEndAt);

    // バッファ時間
    long bufferTime = PropertyUtil.getPropertyDecimal("railway.operation.buffer.time").longValue();
    // 適合性評価で使用する運行ダイヤの取得時間を用意
    LocalTime fetchStartTime = ｊStartAt.minusMinutes(bufferTime).toLocalTime();
    LocalTime fetchEndTime = ｊEndAt.plusMinutes(bufferTime).toLocalTime();

    // 1日の運行ダイヤを取得するため、予約時間付近のデータを入れるように制御
    List<List<LocalTime>> trainScheduleList = createTrainScheduleList(
        railwayOperationInfoDto.getRegularDiagram(), fetchStartTime, fetchEndTime, formatter);
    trainScheduleList.addAll(createTrainScheduleList(
        otherRailwayOperationInfoDto.getRegularDiagram(), fetchStartTime, fetchEndTime, formatter));

    // 適合性評価を実行
    return checkConformityAssessment(trainScheduleList, ｊStartAt.toLocalTime(),
        ｊEndAt.toLocalTime(), relativeValue, bufferTime);
  }

  /**
   * 予約時間付近の運行時刻のリストを作成
   * 
   * @param railwayTimetableList 運行ダイヤ
   * @param fetchStartTime リストに追加する開始時刻
   * @param fetchEndTime リストに追加する終了時刻
   * @param formatter 時間用フォーマッター
   * @return 予約時間付近の運行時刻のリスト
   */
  private List<List<LocalTime>> createTrainScheduleList(List<List<String>> railwayTimetableList,
      LocalTime fetchStartTime, LocalTime fetchEndTime, DateTimeFormatter formatter) {

    // 予約時間付近の運行時刻のリスト
    List<List<LocalTime>> trainScheduleList = new ArrayList<>();

    for (List<String> schedule : railwayTimetableList) {
      // 出発駅時刻と到着駅時刻を取得
      LocalTime departureTime = LocalTime.parse(schedule.get(0), formatter);
      LocalTime arrivalTime = LocalTime.parse(schedule.get(1), formatter);

      // 出発駅時刻<=リスト追加終了時刻かつリスト追加開始時刻<=到着駅時刻の場合のみリストに追加
      if ((departureTime.isBefore(fetchEndTime) || departureTime.equals(fetchEndTime))
          && (arrivalTime.isAfter(fetchStartTime) || arrivalTime.equals(fetchStartTime))) {

        List<LocalTime> trainSchedule = new ArrayList<>();
        trainSchedule.add(departureTime);
        trainSchedule.add(arrivalTime);
        trainScheduleList.add(trainSchedule);
      }
    }

    return trainScheduleList;
  }

  /**
   * 鉄道運行情報を用いた適合性評価を実行する
   * 
   * @param trainScheduleList 運行ダイヤ
   * @param startTime 予約開始時間
   * @param endTime 予約終了時間
   * @param relativeValue 相対値
   * @param bufferTime バッファ時間
   * @return 適合性評価結果
   */
  private Boolean checkConformityAssessment(List<List<LocalTime>> trainScheduleList,
      LocalTime startTime, LocalTime endTime, Double relativeValue, long bufferTime) {

    // 運行ダイヤ情報のリストをループ処理
    for (List<LocalTime> schedule : trainScheduleList) {
      // 出発駅時間と到着駅時間を取得
      LocalTime departureTime = schedule.get(0);
      LocalTime arrivalTime = schedule.get(1);

      // 鉄道の通過時間を計算
      long travelTimeMinutes = Duration.between(departureTime, arrivalTime).toMinutes();
      // 相対値を使用して航路との交点までの時間を計算
      long adjustedTime = (long) (travelTimeMinutes * relativeValue);

      // 通過開始時刻
      LocalTime trainEntryTime = departureTime.minusMinutes(bufferTime).plusMinutes(adjustedTime);
      // 通過終了時刻
      LocalTime trainExitTime =
          departureTime.plusMinutes((long) bufferTime).plusMinutes(adjustedTime);

      // 通過開始時刻<=予約終了日時か確認
      boolean isTrainEntryBeforeOrAtEnd =
          trainEntryTime.isBefore(endTime) || trainEntryTime.equals(endTime);
      // 予約開始日時<=通過終了時刻か確認
      boolean isTrainExitAfterOrAtStart =
          trainExitTime.isAfter(startTime) || trainExitTime.equals(startTime);

      // 通過時刻が予約時間内にある場合false(適合性評価NG)
      if (isTrainEntryBeforeOrAtEnd && isTrainExitAfterOrAtStart)
        return false;
    }

    return true;
  }

}

