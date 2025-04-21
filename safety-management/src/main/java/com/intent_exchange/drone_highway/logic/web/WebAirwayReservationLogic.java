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

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.dto.response.WebAirwaySectionsDto;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * 航路予約(A-1-3)への通信用ロジック
 */
@Component
public class WebAirwayReservationLogic {

  /** 航路予約(A-1-3)の航路予約情報取得URL。 */
  private static final String GET_ROUTE_RESERVATION_URL = "get.airway.reservation.url";

  /** RestTemplateインスタンス。 このインスタンスを使用してHTTPリクエストを送信します。 */
  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  /**
   * 指定された航路予約毎の識別IDに基づいて航路予約情報取得を取得します。
   *
   * @param reservationId 航路予約毎の識別ID
   * @return 航路予約情報
   */
  public WebAirwayReservationDto getAirwayReservation(String airwayReservationId) {
    String urlTemplate = UriComponentsBuilder
        .fromHttpUrl(PropertyUtil.getProperty(GET_ROUTE_RESERVATION_URL, airwayReservationId))
        .toUriString();

    // HTTPヘッダーを設定
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", PropertyUtil.getProperty("route_reservation.token"));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    return restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, WebAirwayReservationDto.class)
        .getBody();
  }

  /**
   * 指定された航路予約毎の識別IDに基づいて航路予約情報取得を取得し,戻り値をDB登録用の値に加工します。
   *
   * @param reservationId 航路予約毎の識別ID
   * @return 航路予約情報
   */
  public WebAirwayReservationDto getAirwayReservationForDb(String airwayReservationId) {
    WebAirwayReservationDto reservationDto = getAirwayReservation(airwayReservationId);
    reservationDto.setAirwaySectionIds(new ArrayList<String>());
    List<WebAirwaySectionsDto> sections = reservationDto.getAirwaySections();
    for (WebAirwaySectionsDto section : sections) {
      if (reservationDto.getStartAt() == null || (section.getStartAt() != null
          && section.getStartAt().compareTo(reservationDto.getStartAt()) < 0)) {
        reservationDto.setStartAt(section.getStartAt());
      }
      if (reservationDto.getEndAt() == null || (section.getEndAt() != null
          && section.getEndAt().compareTo(reservationDto.getEndAt()) > 0)) {
        reservationDto.setEndAt(section.getEndAt());
      }
      if (section.getAirwaySectionId() != null) {
        reservationDto.getAirwaySectionIds().add(section.getAirwaySectionId());
      }
    }
    return reservationDto;
  }
}

