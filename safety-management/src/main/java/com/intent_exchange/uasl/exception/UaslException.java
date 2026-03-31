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

package com.intent_exchange.uasl.exception;

import com.intent_exchange.uasl.entity.ErrorResponseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/** UaslExceptionは、Uaslアプリケーションに特有のエラーを示す例外である。 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UaslException extends RuntimeException {

  /**
   * エラー時のResponse用Entity
   */
  private ErrorResponseEntity entity;

  /**
   * 指定された詳細メッセージで新しいUaslExceptionを構築する。
   *
   * @param message 詳細メッセージ
   */
  public UaslException(String message) {
    super(message);
  }

  /**
   * 指定された詳細メッセージで新しいUaslExceptionを構築する。
   *
   * @param entity エラー時のResponse用Entity
   */
  public UaslException(ErrorResponseEntity entity) {
    super();
    this.entity = entity;
  }

  /**
   * 指定された詳細メッセージで新しいUaslExceptionを構築する。
   *
   * @param message 詳細メッセージ
   * @param entity エラー時のResponse用Entity
   */
  public UaslException(String message, ErrorResponseEntity entity) {
    super(message);
    this.entity = entity;
  }

  /**
   * 指定された詳細メッセージおよび原因で新しいUaslExceptionを構築する。
   *
   * @param message 詳細メッセージ
   * @param cause 例外の原因
   */
  public UaslException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * 指定された詳細メッセージおよび原因で新しいUaslExceptionを構築する。
   *
   * @param message 詳細メッセージ
   * @param cause 例外の原因
   * @param entity エラー時のResponse用Entity
   */
  public UaslException(String message, Throwable cause, ErrorResponseEntity entity) {
    super(message, cause);
    this.entity = entity;
  }
}

