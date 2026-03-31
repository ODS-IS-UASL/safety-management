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

import java.util.List;
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
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeListResponseDto;
import com.intent_exchange.uasl.exception.WeatherWindSpeedException;

/** WebWeatherForecastTimeLogicクラスは、SDSPから気象予測時間情報のリストを取得するロジック */
@Component
public class WebWeatherForecastTimeLogic {

  /** 気象予測対象時間リストの取得用URL */
  @Value("${get.weather.forecast.time.url:''}")
  private String getWeatherForecastTimeUrl;

  /** 気象要素 */
  @Value("${weather.wind.elements:''}")
  private String weatherWindElements;

  /** RestTemplateインスタンス。 このインスタンスを使用してHTTPリクエストを送信します。 */
  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  /**
   * SDSPから気象予測時間情報を取得するメソッド
   *
   * @param token SDSPの認証トークン
   * @return 気象予測時間情報
   */
  public ResponseEntity<WeatherForecastTimeListResponseDto> getWeatherForecastTime(String token) {
    String forecastUrlTemplate = UriComponentsBuilder.fromHttpUrl(getWeatherForecastTimeUrl)
        .queryParam("elements", weatherWindElements).toUriString();

    // HTTPヘッダーを設定
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", token);
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // TODO 現状のSDSPの仕様でリクエスト時に予約の開始時間と終了時間を指定できないため、予約時間を指定できるようにしたい。
    // 気象予測対象時間リストを取得するGETリクエストを送信
    ResponseEntity<WeatherForecastTimeListResponseDto> responseEntity = restTemplate.exchange(
        forecastUrlTemplate, HttpMethod.GET, entity, WeatherForecastTimeListResponseDto.class);

    return responseEntity;
  }
}

