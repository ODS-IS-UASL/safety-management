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

package com.intent_exchange.drone_highway.util;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import com.intent_exchange.drone_highway.config.PropertyConfig;

/** プロパティユーティリティ。 */
public class PropertyUtil {

  /** アプリケーションのプロパティファイル名。 */
  private static final String APPLICATION_NAME = "application";

  /** アプリケーションのプロパティバンドル。 */
  private static ResourceBundle bundle1 = ResourceBundle.getBundle(APPLICATION_NAME);

  /** システムのプロパティファイル名。 */
  private static final String SYSTEM_NAME = "system";

  /** システムのプロパティバンドル。 */
  private static ResourceBundle bundle2 = ResourceBundle.getBundle(SYSTEM_NAME);

  /** コンストラクタ。 */
  private PropertyUtil() {
    // 外部からのインスタンス生成を抑止。
  }

  /**
   * プロパティ値取得
   *
   * <p>
   * キーが存在しない場合は空文字を返します。
   *
   * @param key キー
   * @return キーに対応する値、キーが存在しない場合は空文字
   */
  public static String getProperty(final String key, final Object... args) {
    if (args.length == 0) {
      return getProperty(key);
    } else {
      return MessageFormat.format(getProperty(key), args);
    }
  }

  /**
   * プロパティ値取得(整数)
   *
   * <p>
   * キーが存在しない場合はnullを返します。
   *
   * @param key キー
   * @return キーに対応する整数、キーが存在しない場合はnull
   */
  public static Integer getPropertyInt(final String key) {
    if (getProperty(key).isBlank()) {
      return null;
    } else {
      return Integer.valueOf(getProperty(key));
    }
  }

  /**
   * プロパティ値取得(数値)
   *
   * <p>
   * キーが存在しない場合はnullを返します。
   *
   * @param key キー
   * @return キーに対応する数値、キーが存在しない場合はnull
   */
  public static BigDecimal getPropertyDecimal(final String key) {
    if (getProperty(key).isBlank()) {
      return null;
    } else {
      return new BigDecimal(getProperty(key));
    }
  }

  /**
   * プロパティバンドルからプロパティ値取得
   *
   * <p>
   * キーが存在しない場合は空文字を返します。
   *
   * @param key キー
   * @return キーに対応する値、キーが存在しない場合は空文字
   */
  private static String getProperty(final String key) {
    if (PropertyConfig.getBundle1() != null) {
      bundle1 = PropertyConfig.getBundle1();
    }
    if (PropertyConfig.getBundle2() != null) {
      bundle2 = PropertyConfig.getBundle2();
    }
    String rtn = "";
    if (bundle1 != null && bundle1.containsKey(key)) {
      rtn = bundle1.getString(key);
    } else if (bundle2 != null && bundle2.containsKey(key)) {
      rtn = bundle2.getString(key);
    }
    return rtn;
  }
}

