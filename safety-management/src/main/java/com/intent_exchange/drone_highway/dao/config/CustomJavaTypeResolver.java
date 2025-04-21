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

package com.intent_exchange.drone_highway.dao.config;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

/**
 * CustomJavaTypeResolverは、JavaTypeResolverDefaultImplのカスタム実装で、 データベースのカラム名をスネークケースからキャメルケースに変換します。
 */
public class CustomJavaTypeResolver extends JavaTypeResolverDefaultImpl {

  /**
   * 指定されたイントロスペクテッドカラムのJava型を計算し、 カラム名をキャメルケースに変換します。
   *
   * @param introspectedColumn イントロスペクテッドカラム
   * @return 完全修飾されたJava型
   */
  @Override
  public FullyQualifiedJavaType calculateJavaType(IntrospectedColumn introspectedColumn) {
    FullyQualifiedJavaType answer = super.calculateJavaType(introspectedColumn);
    String camelCaseName = convertToCamelCase(introspectedColumn.getActualColumnName());
    introspectedColumn.setJavaProperty(camelCaseName);
    return answer;
  }

  /**
   * スネークケースの文字列をキャメルケースに変換します。
   *
   * @param snakeCase スネークケースの文字列
   * @return キャメルケースの文字列
   */
  private String convertToCamelCase(String snakeCase) {
    StringBuilder result = new StringBuilder();
    boolean nextIsUpper = false;
    for (char c : snakeCase.toCharArray()) {
      if (c == '_') {
        nextIsUpper = true;
      } else {
        if (nextIsUpper) {
          result.append(Character.toUpperCase(c));
          nextIsUpper = false;
        } else {
          result.append(c);
        }
      }
    }
    return result.toString();
  }
}

