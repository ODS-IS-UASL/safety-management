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

package com.intent_exchange.uasl.interceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StreamUtils;

/**
 * HTTPリクエストおよびレスポンスのログを記録するためのインターセプタークラス。
 * このクラスは、SpringのClientHttpRequestInterceptorを実装し、リクエストとレスポンスの詳細をデバッグログに出力します。
 */
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

  private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

  /**
   * HTTPリクエストをインターセプトし、リクエストとレスポンスのログを記録します。
   *
   * @param request HTTPリクエスト
   * @param body リクエストボディ
   * @param execution リクエスト実行オブジェクト
   * @return HTTPレスポンス
   * @throws IOException 入出力例外が発生した場合
   */
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body,
      ClientHttpRequestExecution execution) throws IOException {
    logRequest(request, body);
    ClientHttpResponse response = execution.execute(request, body);
    logResponse(response);
    return response;
  }

  /**
   * HTTPリクエストの詳細をログに記録します。
   *
   * @param request HTTPリクエスト
   * @param body リクエストボディ
   * @throws IOException 入出力例外が発生した場合
   */
  private void logRequest(HttpRequest request, byte[] body) throws IOException {
    logger.debug("Request URI: {}", request.getURI());
    logger.debug("Request Method: {}", request.getMethod());
    logger.debug("Request Headers: {}", request.getHeaders());
    logger.debug("Request Body: {}", new String(body, StandardCharsets.UTF_8));
  }

  /**
   * HTTPレスポンスの詳細をログに記録します。
   *
   * @param response HTTPレスポンス
   * @throws IOException 入出力例外が発生した場合
   */
  private void logResponse(ClientHttpResponse response) throws IOException {
    logger.debug("Response Status Code: {}", response.getStatusCode());
    logger.debug("Response Headers: {}", response.getHeaders());
    logger.debug("Response Body: {}",
        StreamUtils.copyToString(response.getBody(), StandardCharsets.UTF_8));
  }
}

