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

package com.intent_exchange.drone_highway.logic.conformity_assessment;

/**
 * このインターフェースは、適合性評価ロジックを定義します。
 *
 * @param <T> 評価対象のデータ型
 */
public interface IFConformityAssessmentLogic<T> {
  /**
   * 指定されたデータ転送オブジェクト (DTO) をチェックします。
   *
   * @param dto チェック対象のデータ転送オブジェクト
   * @return チェックが成功した場合は true、失敗した場合は false、 チェック処理をスキップする場合は null
   */
  Boolean check(T dto);

  /**
   * 自身のクラス名を返却します。
   *
   * @return 自身のクラス名
   */
  default String getClassInfo() {
    return this.getClass().getSimpleName();
  }

  /**
   * 評価対象のデータ型のクラスを取得します。
   *
   * @return 評価対象のデータ型のクラス
   */
  @SuppressWarnings("unchecked")
  default Class<T> getGenericTypeClass() {
    return (Class<T>) ((java.lang.reflect.ParameterizedType) getClass().getGenericInterfaces()[0])
        .getActualTypeArguments()[0];
  }
}

