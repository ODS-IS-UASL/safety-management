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

package com.intent_exchange.drone_highway.annotation.converter;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * DateとStringの間でフォーマットしシステムのタイムゾーンに沿って変換するためのコンバータクラス。
 */
@Configurable
@Converter(autoApply = true)
public class DateAttributeConverter implements AttributeConverter<Date, String> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

  @Autowired
  private Clock clock;

  /**
   * DateオブジェクトをStringに変換します。
   *
   * @param date 変換するDateオブジェクト
   * @return 変換されたString。dateがnullの場合はnullを返します。
   */
  @Override
  public String convertToDatabaseColumn(Date date) {
    if (date == null) {
      return null;
    }
    ZonedDateTime zonedDateTime = date.toInstant().atZone(clock.getZone());
    return zonedDateTime.format(FORMATTER);
  }

  /**
   * StringをDateオブジェクトに変換します。
   *
   * @param dbData 変換するString
   * @return 変換されたDateオブジェクト。dbDataがnullの場合はnullを返します。
   */
  @Override
  public Date convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    ZonedDateTime zonedDateTime = ZonedDateTime.parse(dbData, FORMATTER.withZone(clock.getZone()));
    return Date.from(zonedDateTime.toInstant());
  }
}

