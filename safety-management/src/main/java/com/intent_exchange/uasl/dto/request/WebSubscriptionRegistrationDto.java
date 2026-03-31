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

package com.intent_exchange.uasl.dto.request;

import java.util.List;
import lombok.Data;

@Data
public class WebSubscriptionRegistrationDto {

  /** サブスクリプション名 */
  private String subscription_name;

  /** 監視対象エリアを表す bounding box（境界ボックス）。配列の形式は[南西緯度, 南西経度, 北東緯度, 北東経度] */
  private List<Double> view;

  /** サブスクリプション開始日時(RFC3339形式) */
  private String time_start;

  /** サブスクリプション終了日時(RFC3339形式) ※制限事項：将来的には24時間以上を指定可能としますが、現状はDSSの制限により開始日時から24時間未満の範囲が有効です。 */
  private String time_end;

  /** サブスクリプションの無期限オプション ※制限事項：将来対応予定 */
  private Boolean is_unlimited;

  /** このサブスクリプションを通知するドローン航路システムのベースURL */
  private String uasl_base_url;
}

