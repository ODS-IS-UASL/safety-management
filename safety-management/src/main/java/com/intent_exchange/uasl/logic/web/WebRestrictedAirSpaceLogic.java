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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.intent_exchange.uasl.dto.request.RestrictedAirSpaceRequestDto;
import com.intent_exchange.uasl.dto.response.RestrictedAirSpaceResponseDto;

/**
 * 空域デジタルツイン 規制/イベント情報への通信用ロジック
 */
@Component
public class WebRestrictedAirSpaceLogic {
  
  /** TODO 規制/イベント情報通知処理URL。 */
  
  /** 規制/イベント情報取得URL。 */
  @Value("${post.restricted.air.space.url:''}")
  private String postRestrictedAirSpaceUrl;

  /** RestTemplateインスタンス。 このインスタンスを使用してHTTPリクエストを送信します。 */
  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

 // TODO 規制/イベント情報通知処理

  /**
   * 指定されたエリア情報と予約日時に基づいて規制/イベント情報を取得します。
   * @param RestrictedAirSpaceRequestDto 規制/イベント情報リクエストdto
   * @return 規制/イベント情報
   */
  public  RestrictedAirSpaceResponseDto getRestrictedAirSpace(RestrictedAirSpaceRequestDto requestDto) {
    return restTemplate.postForObject(postRestrictedAirSpaceUrl, requestDto, RestrictedAirSpaceResponseDto.class);
  }

}

