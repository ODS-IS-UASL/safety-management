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

package com.intent_exchange.uasl.logic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 航路逸脱情報の計算に関するロジックを提供します。
 */
@Component
public class UaslDeviationCalculator {

  /**
   * 引数に指定された逸脱量（小数値）リストの95パーセンタイルを計算します。<br>
   * DBへのINSERTするデータ形式の関係上、計算結果を文字列に変換して返却します。<br>
   *
   * @param deviationList 逸脱量（小数値）リスト
   * @return 95パーセンタイル
   */
  public String calcPercentileStr(List<Double> deviationList) {
    
    // 小数値リストのnull・空チェック
    if (deviationList == null || deviationList.isEmpty()) {
      // データが無い場合は0.0を返却
      return "0.0";
    }
    
    // リストを昇順ソート
    Collections.sort(deviationList);

    // 分割位置を計算する
    // 【1】 0.95*(データ数+1)
    double splitIndex = 0.95 * (deviationList.size() + 1);
    // 【2】 下限インデックス(【1】の小数点以下を切り捨て）
    int lowerIndex = (int) Math.floor(splitIndex);
    // 【3】 上限インデックス(【1】の小数点以下を切り上げ）
    int upperIndex = (int) Math.ceil(splitIndex);

    // パーセンタイルを算出する
    BigDecimal percentile = BigDecimal.ZERO;
    if (lowerIndex == upperIndex) {
      // 分割位置が整数値の場合（下限インデックスと上限インデックスが等しい場合）、その位置の値を戻り値とする
      // TODO: 分割位置が小数の場合と同様に必要であれば、桁数補正
      percentile = BigDecimal.valueOf(deviationList.get(lowerIndex - 1));
    } else if (deviationList.size() >= upperIndex) {
      // 分割位置が小数値の場合（下限インデックスと上限インデックスが異なる場合）且つ、逸脱量リストに上限インデックスの値が存在する場合
      // 下限インデックスと上限インデックスの値の平均値を戻り値とする
      BigDecimal lowerValue = BigDecimal.valueOf(deviationList.get(lowerIndex - 1));
      BigDecimal upperValue = BigDecimal.valueOf(deviationList.get(upperIndex - 1));
      BigDecimal sum = lowerValue.add(upperValue);
      percentile = sum.divide(new BigDecimal("2"), 2, RoundingMode.HALF_UP); // 割り切れない場合は小数第3位で四捨五入
    } else {
      // 分割位置が小数値の場合（下限インデックスと上限インデックスが異なる場合）且つ、逸脱量リストに上限インデックスの値が存在しない場合
      // 下限インデックスの値を戻り値とする
      percentile = BigDecimal.valueOf(deviationList.get(lowerIndex - 1));
    }

    // パーセンタイルを文字列に変換して返す
    return percentile.toString();
  }
}

