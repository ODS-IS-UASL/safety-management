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

package com.intent_exchange.uasl.logic.web;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.uasl.dto.request.WeatherWindSpeedConformityAssessmentDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeDto;
import com.intent_exchange.uasl.dto.response.WeatherWindSpeedListResponseDto;

/** WebWeatherWindSpeedクラスは、SDSPから風速情報を取得するロジック */
@Component
public class WebWeatherWindSpeedLogic {

  /** 風速情報の取得用URL */
  @Value("${get.weather.wind.speeds.url:''}")
  private String getWeatherWindSpeedsUrl;

  /** 気象要素 */
  @Value("${weather.wind.elements:''}")
  private String weatherWindElements;

  /** RestTemplateインスタンス。 このインスタンスを使用してHTTPリクエストを送信します。 */
  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  // Clockインスタンス。現在の時刻を取得するために使用します。
  @Autowired
  private Clock clock;

  /**
   * SDSPから風速情報を取得するメソッド
   *
   * @param dto 風速適合性評価のためのデータ転送オブジェクト
   * @param forecastTimeDto 気象予測情報
   * @return 風速情報
   */
  public ResponseEntity<WeatherWindSpeedListResponseDto> getWeatherWindSpeeds(
      WeatherWindSpeedConformityAssessmentDto dto, WeatherForecastTimeDto forecastTimeDto) {

    // DateTimeFormatterを定義
    DateTimeFormatter formatter =
        DateTimeFormatter.ofPattern("yyyyMMdd'T'HH").withZone(clock.getZone());
    // 予約開始日時のstartAtをyyyyMMdd'T'HHの文字列に変換
    String startAtFormatted = dto.getStartAt().format(formatter);

    // 気象予測時間
    String validtime = null;
    // 気象予測時間情報のdtoの先頭の気象予測時間リストの要素に予約開始日時と一致する値が含まれるいる場合は予約開始日時を気象予測時間に設定
    // 含まれない場合は、気象予測時間リストの末尾（予約開始時間に一番近い）の値を気象予測時間に設定する
    boolean res = forecastTimeDto.getTimeList()
        .get(0)
        .getValidtime()
        .stream()
        .anyMatch(time -> time.equals(startAtFormatted));
    if (res) {
      validtime = startAtFormatted;
    } else {
      validtime = forecastTimeDto.getTimeList().get(0).getValidtime().get(10);
    }

    // 風速情報の取得のURLを構築
    String windSpeedsUrlTemplate = UriComponentsBuilder.fromHttpUrl(getWeatherWindSpeedsUrl)
        .queryParam("elements", weatherWindElements)
        .queryParam("basetime", forecastTimeDto.getTimeList().get(0).getBasetime())
        .queryParam("validtime", validtime)
        // 標高は10m単位指定の為、1の位や小数点を四捨五入し10の倍数に丸める、10mに満たない場合は０mとする
        .queryParam("alt_surface",
            new BigDecimal(dto.getAltitude()).divide(new BigDecimal("10"), 0, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("10")))
        .queryParam("lat_start", dto.getLatStart())
        .queryParam("lon_start", dto.getLonStart())
        .queryParam("lat_end", dto.getLatEnd())
        .queryParam("lon_end", dto.getLonEnd())
        .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", dto.getToken());
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // 気象予測発表時間と気象予測対象時間リストの取得のレスポンスを元に天候・風速情報の取得するGETリクエストを送信
    ResponseEntity<WeatherWindSpeedListResponseDto> responseEntity = restTemplate.exchange(
        windSpeedsUrlTemplate, HttpMethod.GET, entity, WeatherWindSpeedListResponseDto.class);

    return responseEntity;
  }
}

