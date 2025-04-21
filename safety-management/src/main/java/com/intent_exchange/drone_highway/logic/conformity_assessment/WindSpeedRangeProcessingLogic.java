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

package com.intent_exchange.drone_highway.logic.conformity_assessment;


import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.IntStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dto.request.WeatherWindSpeedConformityAssessmentDto;
import com.intent_exchange.drone_highway.dto.response.WeatherForecastTimeDto;
import com.intent_exchange.drone_highway.dto.response.WeatherForecastTimeListResponseDto;
import com.intent_exchange.drone_highway.dto.response.WeatherWindSpeedsDto;
import com.intent_exchange.drone_highway.exception.WeatherWindSpeedException;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/** WindSpeedRangeProcessingLogicクラスは、予約の開始日時判定と風速条件範囲内判定するロジック */
@Component
public class WindSpeedRangeProcessingLogic {

  /** Clockインスタンス。現在のUTC時刻を取得するために使用します。 */
  @Autowired
  private Clock clock;

  /**
   * 風速条件範囲内判定の処理を行うメソッド。
   *
   * @param weatherWindSpeedsListDto 風速情報のリスト
   * @param weatherWindSpeedConformityAssessmentDto 風速適合性評価のためのデータ転送オブジェクト
   * @return 風速が条件範囲内である場合はtrue、範囲外である場合はfalse
   */
  public Boolean getWindSpeedRangeJudgment(List<WeatherWindSpeedsDto> weatherWindSpeedsListDto,
      WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto) {

    // weatherWindSpeedsListDtoは指定時間のugrd(東西風)とvgrd(南北風)の2つの風速情報を1つづつ持つリスト
    List<Double> ugrdContents = weatherWindSpeedsListDto.get(0).getContents();
    List<Double> vgrdContents = weatherWindSpeedsListDto.get(1).getContents();

    // TODO ヒヤリハット情報で風向きが必要になった場合は、風向きの計算をして、条件範囲外の時の風向きも返すようにする。
    // 風速条件範囲内判定処理、ugrdContentsとvgrdContentsのi番目の要素より風速を計算し、その結果が条件範囲内であるかどうかを判定
    // ugrdの2乗 + vgrdの2乗の平方根が風速
    Boolean res = IntStream.range(0, ugrdContents.size())
        .allMatch(i -> Math.sqrt(Math.pow(ugrdContents.get(i), 2)
            + Math.pow(vgrdContents.get(i), 2)) <= weatherWindSpeedConformityAssessmentDto
                .getWindSpeedRange());
    return res;
  }

  /**
   * 予約の開始日時が現在の時刻プラス10時間以内に含まれない場合、trueを返すメソッド。
   *
   * @param dto 風速適合性評価のためのデータ転送オブジェクト
   * @return 予約の開始日時が現在の時刻プラス10時間以内に含まれない場合はtrue、それ以外はfalse
   */
  public boolean isReservationTimeValid(WeatherWindSpeedConformityAssessmentDto dto) {
    LocalDateTime nowPlus10Hours = LocalDateTime.now(clock).plusHours(10);
    return dto.getStartAt().isAfter(nowPlus10Hours);
  }

  /**
   * 気象予測情報のリストから最初の気象予測情報を取得するメソッド。
   *
   * @param response 気象予測情報のリストdto
   * @return 最初の気象予測情報dot
   */
  public WeatherForecastTimeDto getFirstForecastTime(WeatherForecastTimeListResponseDto response) {
    List<WeatherForecastTimeDto> forecastTimeList = response.getIndex();
    if (forecastTimeList == null || forecastTimeList.isEmpty()) {
      throw new WeatherWindSpeedException(PropertyUtil.getProperty("error.WeatherForecastTime"));
    }
    return forecastTimeList.get(0);
  }
}


