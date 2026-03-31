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

import java.time.Clock;
import java.time.LocalDateTime;
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
import com.intent_exchange.uasl.dto.request.ThirdPartyEntryRequestDto;
import com.intent_exchange.uasl.dto.response.ThirdPartyMonitoringInformationResponseDto;

/**
 * 空域デジタルツイン 第三者立入監視情報への通信用ロジック
 */
@Component
public class WebThirdPartyMonitoringLogic {

  /** 第三者立入監視情報通知先の登録URL。 */
  @Value("${post.third.party.entry.notification.registration.url:''}")
  private String postThirdPartyEntryNotificationRegistrationUrl;

  /** 第三者立入監視情報通知先の変更URL。 */
  @Value("${put.third.party.entry.notification.registration.url:''}")
  private String putThirdPartyEntryNotificationRegistrationUrl;

  /** 第三者立入監視情報通知先の削除URL。 */
  @Value("${delete.third.party.entry.notification.registration.url:''}")
  private String deleteThirdPartyEntryNotificationRegistrationUrl;

  /** 第三者立入監視情報取得URL。 */
  @Value("${get.third.party.monitoring.information.url:''}")
  private String getThirdPartyMonitoringInformationUrl;

  /** RestTemplateインスタンス。 このインスタンスを使用してHTTPリクエストを送信します。 */
  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  // Clockインスタンス。現在の時刻を取得するために使用します。
  @Autowired
  private Clock clock;

  /**
   * 変更があった場合に通知(Subscribe)を行うため、航路の安全管理支援(SafetyManagement)の通知先をデータプロバイダに登録する。
   * 
   * @param thirdPartyEntryRequestDto 第三者立入監視情報の通知先を格納するdto
   */
  public void registrationThirdPartyEntryNotification(
      ThirdPartyEntryRequestDto thirdPartyEntryRequestDto) {
    // 通知先の登録リクエスト送信
    // 201:正常終了、400:入力チェックエラー、500:サーバーエラー
    // ※201以外の異常終了時の処理は、共通のexceptionHandlerで処理
    restTemplate.postForObject(postThirdPartyEntryNotificationRegistrationUrl,
        thirdPartyEntryRequestDto, ResponseEntity.class);
  }

  /**
   * データプロバイダに登録した情報変更通知先を変更する。
   * 
   * @param thirdPartyEntryRequestDto 第三者立入監視情報の通知先を格納するdto
   */
  public void changeThirdPartyEntryNotification(
      ThirdPartyEntryRequestDto thirdPartyEntryRequestDto) {
    // 通知先の変更リクエスト送信
    // 204:正常終了、400:入力チェックエラー、500:サーバーエラー
    // ※204以外の異常終了時の処理は、共通のexceptionHandlerで処理
    restTemplate.put(putThirdPartyEntryNotificationRegistrationUrl,
        thirdPartyEntryRequestDto);
  }

  /**
   * データプロバイダに登録した情報変更通知先を削除する。
   */
  public void deletionThirdPartyEntryNotification() {
    // 通知先の削除リクエスト送信
    // 204:正常終了、400:入力チェックエラー、500:サーバーエラー
    // ※204以外の異常終了時の処理は、共通のexceptionHandlerで処理
    restTemplate.delete(deleteThirdPartyEntryNotificationRegistrationUrl);
  }

  /**
   * 指定されたエリア情報と予約日時に基づいて第三者立入監視情報を取得します。
   *
   * @param areaInfo エリア情報
   * @param reservationDateTime 予約日時
   * @return 第三者立入監視情報
   */
  public ThirdPartyMonitoringInformationResponseDto getMonitoringInformation(String latStart,
      String lonStart, String latEnd, String lonEnd, LocalDateTime reservationDateTime,
      int maxItemCount, String apiKey) {

    String urlTemplate =
        UriComponentsBuilder.fromHttpUrl(getThirdPartyMonitoringInformationUrl)
            .queryParam("lat1", latStart)
            .queryParam("lon1", lonStart)
            .queryParam("lat2", latEnd)
            .queryParam("lon2", lonEnd)
            .queryParam("timestamp",
                reservationDateTime.atZone(clock.getZone()).format(DateTimeFormatter.ISO_INSTANT))
            .queryParam("maxItemCount", maxItemCount)
            .toUriString();

    // HTTPヘッダーを設定
    HttpHeaders headers = new HttpHeaders();
    headers.set("x-api-key", apiKey);
    headers.set("Content-Type", "application/json");
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // 第三者立入監視情報取得リクエスト送信
    ResponseEntity<ThirdPartyMonitoringInformationResponseDto> responseEntity =
        restTemplate.exchange(urlTemplate, HttpMethod.GET, entity,
            ThirdPartyMonitoringInformationResponseDto.class);

    return responseEntity.getBody();
  }

}

