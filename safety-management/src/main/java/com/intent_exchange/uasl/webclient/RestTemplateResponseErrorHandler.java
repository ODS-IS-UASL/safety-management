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

package com.intent_exchange.uasl.webclient;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import com.intent_exchange.uasl.exception.UaslException;

/**
 * カスタムレスポンスエラーハンドラー HTTPエラーを処理するためのクラス
 */
public class RestTemplateResponseErrorHandler implements ResponseErrorHandler {

  /**
   * レスポンスにエラーが含まれているかどうかを判定する
   *
   * @param response クライアントHTTPレスポンス
   * @return エラーが含まれている場合はtrue、そうでない場合はfalse
   * @throws IOException 入出力例外
   */
  @Override
  public boolean hasError(ClientHttpResponse response) throws IOException {
    return new DefaultResponseErrorHandler().hasError(response);
  }

  /**
   * エラーを処理する
   *
   * @param response クライアントHTTPレスポンス
   * @throws IOException 入出力例外
   */
  @Override
  public void handleError(ClientHttpResponse response) throws IOException {
    URI url = getRequestUri(response);
    throw new UaslException("HTTPステータスコード: " + response.getStatusCode() + ", URL: " + url);
  }

  /**
   * ClientHttpResponseからリクエストURIを取得する
   *
   * @param response クライアントHTTPレスポンス
   * @return リクエストURI
   * @throws IOException 入出力例外
   */
  private URI getRequestUri(ClientHttpResponse response) throws IOException {
    try {
      Field requestField = response.getClass().getDeclaredField("request");
      requestField.setAccessible(true);
      Object request = requestField.get(response);
      Field uriField = request.getClass().getDeclaredField("uri");
      uriField.setAccessible(true);
      return (URI) uriField.get(request);
    } catch (Exception e) {
      throw new UaslException("HTTPステータスコード: " + response.getStatusCode());
    }
  }
}

