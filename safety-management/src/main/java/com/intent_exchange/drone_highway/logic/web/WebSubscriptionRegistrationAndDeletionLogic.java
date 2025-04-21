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

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.drone_highway.dao.SubscriptionDataMapper;
import com.intent_exchange.drone_highway.dto.request.WebSubscriptionRegistrationDto;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * UTM への通信用ロジック
 */
@Component
public class WebSubscriptionRegistrationAndDeletionLogic {

  /** UTM のサブスクリプション登録・削除URL。 */
  private static final String UTM_SUBSCRIPTION_URL =
      PropertyUtil.getProperty("subscription.registration.and.deletion.url");

  /** RestTemplateインスタンス。 このインスタンスを使用してHTTPリクエストを送信します。 */
  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  @Autowired
  private SubscriptionDataMapper subscriptionDataMapper;

  /**
   * エリア情報をサブスクリプション登録します。
   *
   * @param dto 航路予約毎の識別ID
   * @return サブスクリプション ID を含んだオブジェクト
   */
  public Map<String, String> registerSubscription(WebSubscriptionRegistrationDto dto) {
    return restTemplate.postForObject(UTM_SUBSCRIPTION_URL, dto, Map.class);
  }

  /**
   * サブスクリプションを削除します。
   *
   * @param subscriptionId サブスクリプション ID
   */
  public void deleteSubscription(String subscriptionId) {
    String urlTemplate = UriComponentsBuilder.fromHttpUrl(UTM_SUBSCRIPTION_URL)
        .pathSegment(subscriptionId)
        .toUriString();
    restTemplate.delete(urlTemplate);
  }
}

