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
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.dto.response.WebUaslSectionsDto;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * 航路予約への通信用ロジック
 */
@Component
public class WebUaslReservationLogic {

  /** 航路予約の航路予約情報取得URL。 */
  private static final String GET_ROUTE_RESERVATION_URL = "get.uasl.reservation.url";

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
  public WebUaslReservationDto getUaslReservation(String uaslReservationId) {
    String urlTemplate = UriComponentsBuilder
        .fromHttpUrl(PropertyUtil.getProperty(GET_ROUTE_RESERVATION_URL, uaslReservationId))
        .toUriString();

    // HTTPヘッダーを設定
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", PropertyUtil.getProperty("route_reservation.token"));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    return restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, WebUaslReservationDto.class)
        .getBody();
  }

  /**
   * 指定された航路予約毎の識別IDに基づいて航路予約情報取得を取得し,戻り値をDB登録用の値に加工します。
   *
   * @param reservationId 航路予約毎の識別ID
   * @return 航路予約情報
   */
  public WebUaslReservationDto getUaslReservationForDb(String uaslReservationId) {
    WebUaslReservationDto reservationDto = getUaslReservation(uaslReservationId);
    reservationDto.setUaslSectionIds(new ArrayList<String>());
    List<WebUaslSectionsDto> sections = reservationDto.getUaslSections();
    for (WebUaslSectionsDto section : sections) {
      if (reservationDto.getStartAt() == null || (section.getStartAt() != null
          && section.getStartAt().compareTo(reservationDto.getStartAt()) < 0)) {
        reservationDto.setStartAt(section.getStartAt());
      }
      if (reservationDto.getEndAt() == null || (section.getEndAt() != null
          && section.getEndAt().compareTo(reservationDto.getEndAt()) > 0)) {
        reservationDto.setEndAt(section.getEndAt());
      }
      if (section.getUaslSectionId() != null) {
        reservationDto.getUaslSectionIds().add(section.getUaslSectionId());
      }
    }
    return reservationDto;
  }
}

