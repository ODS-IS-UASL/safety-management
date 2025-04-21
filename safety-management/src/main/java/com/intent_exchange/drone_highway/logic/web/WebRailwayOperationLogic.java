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

package com.intent_exchange.drone_highway.logic.web;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.drone_highway.dto.response.RailwayOperationInformationResponseDto;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * 空域デジタルツイン 鉄道運行情報 (A-1-1-1-5)への通信用ロジック
 */
@Component
public class WebRailwayOperationLogic {

  /** 鉄道運行情報取得URL。 */
  private static final String GET_RAILWAY_SERVICE_REGULAR_DIAGRAM_URL =
      PropertyUtil.getProperty("get.railway.operation.information.url");

  // Clockインスタンス。現在の時刻を取得するために使用します。
  @Autowired
  private Clock clock;

  /** RestTemplateインスタンス。 このインスタンスを使用してHTTPリクエストを送信します。 */
  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  /**
   * 指定されたエリア情報と日時に基づいて定時運行の時刻リストを取得します。
   *
   * @param station1 駅名
   * @param station2 駅名
   * @param startDateTime 開始日時
   * @param endDateTime 終了日時
   * @return 定時運行の時刻リスト
   */
  public RailwayOperationInformationResponseDto getRailwayOperationInformation(String station1,
      String station2, ZonedDateTime startDateTime, ZonedDateTime endDateTime) {
    String urlTemplate = UriComponentsBuilder.fromHttpUrl(GET_RAILWAY_SERVICE_REGULAR_DIAGRAM_URL)
        .queryParam("station1", station1)
        .queryParam("station2", station2)
        // すでに日本時間に設定しているため、そのまま文字列変換すれば良い
        .queryParam("datetimeFrom", startDateTime.format(DateTimeFormatter.ISO_INSTANT))
        .queryParam("datetimeTo", endDateTime.format(DateTimeFormatter.ISO_INSTANT))
        .toUriString();
    return restTemplate.getForObject(urlTemplate, RailwayOperationInformationResponseDto.class);
  }
}

