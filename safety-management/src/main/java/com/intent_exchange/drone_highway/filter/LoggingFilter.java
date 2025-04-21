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

package com.intent_exchange.drone_highway.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

/**
 * LoggingFilterは、HTTPリクエストおよびレスポンスの詳細をログに記録するサーブレットフィルタです。
 *
 * <p>このフィルタは、ログ記録のために受信したHttpServletRequestおよびHttpServletResponseをラップして
 * その内容をキャッシュします。HTTPメソッド、URI、リクエストボディ、レスポンスステータス、および レスポンスボディをログに記録します。
 *
 * <p>使用例:
 *
 * <pre>
 * public class LoggingFilter implements Filter {
 *     // フィルタメソッド
 * }
 * </pre>
 *
 * @see jakarta.servlet.Filter
 * @see org.springframework.web.util.ContentCachingRequestWrapper
 * @see org.springframework.web.util.ContentCachingResponseWrapper
 */
public class LoggingFilter implements Filter {
  private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);

  /**
   * HTTPリクエストおよびレスポンスの詳細をログに記録します。
   *
   * @param request ServletRequestオブジェクト
   * @param response ServletResponseオブジェクト
   * @param chain FilterChainオブジェクト
   * @throws IOException リクエストの処理中にI/Oエラーが発生した場合
   * @throws ServletException リクエストの処理中にエラーが発生した場合
   */
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    ContentCachingRequestWrapper wrappedRequest =
        new ContentCachingRequestWrapper((HttpServletRequest) request);
    ContentCachingResponseWrapper wrappedResponse =
        new ContentCachingResponseWrapper((HttpServletResponse) response);

    String requestBody = new String(wrappedRequest.getContentAsByteArray(), StandardCharsets.UTF_8);
    logger.info("Request: {} {}", wrappedRequest.getMethod(), wrappedRequest.getRequestURI());
    logger.debug("Request Body: {}", requestBody);

    chain.doFilter(wrappedRequest, wrappedResponse);

    String responseBody =
        new String(wrappedResponse.getContentAsByteArray(), StandardCharsets.UTF_8);
    logger.info("Response: {}", wrappedResponse.getStatus());
    logger.debug("Response Body: {}", responseBody);

    wrappedResponse.copyBodyToResponse();
  }
}

