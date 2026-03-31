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

package com.intent_exchange.uasl.converter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * LocalDateTimeをStringに変換するためのコンバータクラス。
 * このクラスはModelMapperで使用され、LocalDateTime形式の日付をStringオブジェクトに変換します。
 */
public class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {


  // Clockインスタンス。現在の時刻を取得するために使用します。
  private Clock clock;

  // コンストラクタ。Clockインスタンスを注入します。
  public LocalDateTimeToStringConverter(Clock clock) {
    this.clock = clock;
  }

  /**
   * LocalDateTimeをStringに変換します。
   *
   * @param context 変換のコンテキスト
   * @return 変換されたStringオブジェクト。ソースがnullの場合はnullを返します。
   */
  @Override
  public String convert(MappingContext<LocalDateTime, String> context) {
    LocalDateTime source = context.getSource();
    if (source == null) {
      return null;
    }
    ZonedDateTime zonedDateTime = source.atZone(clock.getZone());
    return zonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
  }
}

