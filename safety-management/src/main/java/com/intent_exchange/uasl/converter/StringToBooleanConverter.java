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

import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

/**
 * StringをBooleanに変換するためのコンバータクラス。 このクラスはModelMapperで使用され、String形式のBooleanオブジェクトに変換します。
 */
public class StringToBooleanConverter implements Converter<String, Boolean> {
  /**
   * StringをBooleanに変換します。
   *
   * @param context 変換のコンテキスト
   * @return 変換されたBooleanオブジェクト。ソースがnullの場合はnullを返します。
   */
  @Override
  public Boolean convert(MappingContext<String, Boolean> context) {
    return context.getSource() == null ? null : Boolean.valueOf(context.getSource());
  }
}

