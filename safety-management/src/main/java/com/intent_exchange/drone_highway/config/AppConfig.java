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

package com.intent_exchange.drone_highway.config;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * AppConfigは、Spring BootアプリケーションでAspectJ自動プロキシを有効にするための設定クラスです。
 *
 * <p>
 * この設定は、AspectJを使用したアスペクト指向プログラミング（AOP）をサポートするために必要です。
 * AspectJ自動プロキシを有効にすることで、SpringはAspectJアノテーションが付与されたBeanのプロキシを自動的に作成し、
 * ロギング、トランザクション管理、セキュリティなどの横断的関心事を宣言的に適用できるようにします。
 *
 * <p>
 * この設定を使用するには、このクラスをSpring Bootアプリケーションコンテキストに含めるだけです。
 *
 * <p>
 * 使用例:
 *
 * <pre>
 * &#64;Configuration
 * &#64;EnableAspectJAutoProxy
 * public class AppConfig {
 * }
 * </pre>
 *
 * @see org.springframework.context.annotation.Configuration
 * @see org.springframework.context.annotation.EnableAspectJAutoProxy
 */
@Configuration
@EnableAspectJAutoProxy
public class AppConfig {
  /**
   * システムのデフォルトタイムゾーンを使用するClockのBeanを定義します。
   *
   * @return システムのデフォルトタイムゾーンを使用するClock
   */
  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }
}

