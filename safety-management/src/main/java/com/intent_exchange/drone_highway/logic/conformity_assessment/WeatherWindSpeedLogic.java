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

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dto.request.WeatherWindSpeedConformityAssessmentDto;
import com.intent_exchange.drone_highway.dto.response.WeatherForecastTimeDto;
import com.intent_exchange.drone_highway.dto.response.WeatherForecastTimeListResponseDto;
import com.intent_exchange.drone_highway.dto.response.WeatherWindSpeedListResponseDto;
import com.intent_exchange.drone_highway.exception.WeatherWindSpeedException;
import com.intent_exchange.drone_highway.logic.web.WebWeatherForecastTimeLogic;
import com.intent_exchange.drone_highway.logic.web.WebWeatherWindSpeedLogic;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * WeatherWindSpeedLogicクラスは、気象予測データを使用して風速の適合性を評価するロジックを提供します。
 * このクラスはIFConformityAssessmentLogicインターフェースを実装しています。
 */
@Component
public class WeatherWindSpeedLogic
    implements IFConformityAssessmentLogic<WeatherWindSpeedConformityAssessmentDto> {

  /** WebWeatherForecastTimeLogicインスタンス。このインスタンスを使用して気象予測情報を取得します。 */
  @Autowired
  private WebWeatherForecastTimeLogic webWeatherForecastTimeLogic;

  /** WebWeatherWindSpeedLogicインスタンス。このインスタンスを使用して風速情報を取得します。 */
  @Autowired
  private WebWeatherWindSpeedLogic webWeatherWindSpeedLogic;

  /** WindSpeedRangeJudgmentLogicインスタンス。このインスタンスを使用して風速条件範囲内判定結果を取得します。 */
  @Autowired
  private WindSpeedRangeProcessingLogic windSpeedRangeProcessingLogic;

  /**
   * 風速適合性評価を行うメソッド。
   *
   * @param dto 風速適合性評価のためのデータ転送オブジェクト
   * @return 風速が条件範囲内である場合はtrue、範囲外である場合はfalse
   */
  @Override
  public Boolean check(WeatherWindSpeedConformityAssessmentDto dto) {

    // 予約の開始日時が現在の時刻プラス10時間以内に含まれない場合、風速判定処理は行わなない。
    if (windSpeedRangeProcessingLogic.isReservationTimeValid(dto)) {
      return true;
    }

    // 気象予測情報の取得
    ResponseEntity<WeatherForecastTimeListResponseDto> forecastTimeListResponseEntity =
        webWeatherForecastTimeLogic.getWeatherForecastTime(dto.getToken());

    // レスポンスがnullまたはボディがnullの場合、WeatherWindSpeedExceptionを返す
    if (forecastTimeListResponseEntity == null
        || forecastTimeListResponseEntity.getBody() == null) {
      throw new WeatherWindSpeedException(PropertyUtil.getProperty("error.WeatherForecastTime"));
    }

    WeatherForecastTimeListResponseDto forecastResponse = forecastTimeListResponseEntity.getBody();

    // 気象予測対象時間リストは気象要素に関係なく同じのため、0番目の気象要素の気象予測対象時間リストを取得
    WeatherForecastTimeDto forecastTimeDto =
        windSpeedRangeProcessingLogic.getFirstForecastTime(forecastResponse);

    // 東西風、南北風成分の風速情報の取得
    ResponseEntity<WeatherWindSpeedListResponseDto> weatherWindSpeedListResponseEntity =
        webWeatherWindSpeedLogic.getWeatherWindSpeeds(dto, forecastTimeDto);

    // レスポンスがnullまたはボディがnullの場合、WeatherWindSpeedExceptionを返す
    if (weatherWindSpeedListResponseEntity == null
        || weatherWindSpeedListResponseEntity.getBody() == null) {
      throw new WeatherWindSpeedException(PropertyUtil.getProperty("error.WeatherWindSpeed"));
    }

    WeatherWindSpeedListResponseDto weatherWindSpeedListResponseDto =
        weatherWindSpeedListResponseEntity.getBody();
    // 風速適合性評価の実行結果を返す
    return windSpeedRangeProcessingLogic
        .getWindSpeedRangeJudgment(weatherWindSpeedListResponseDto.getData(), dto);
  }
}

