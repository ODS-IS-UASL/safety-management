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

package com.intent_exchange.drone_highway.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * FilterConfigは、Spring Bootアプリケーションのサーブレットフィルタを登録するための設定クラスです。
 *
 * <p>この設定クラスは、受信したHTTPリクエストに適用されるサーブレットフィルタのBeanを定義します。 フィルタは、ログ記録、認証、リクエストの修正など、さまざまな目的で使用できます。
 *
 * <p>この例では、LoggingFilterが登録され、すべてのURLパターンに対してHTTPリクエストおよびレスポンスの詳細をログに記録します。
 *
 * <p>使用例:
 *
 * <pre>
 * &#64;Configuration
 * public class FilterConfig {
 *     &#64;Bean
 *     public FilterRegistrationBean&lt;LoggingFilter&gt; loggingFilter() {
 *         FilterRegistrationBean&lt;LoggingFilter&gt; registrationBean = new FilterRegistrationBean&lt;&gt;();
 *         registrationBean.setFilter(new LoggingFilter());
 *         registrationBean.addUrlPatterns("/*");
 *         return registrationBean;
 *     }
 * }
 * </pre>
 *
 * @see org.springframework.boot.web.servlet.FilterRegistrationBean
 * @see org.springframework.context.annotation.Bean
 * @see org.springframework.context.annotation.Configuration
 */
@Configuration
public class FilterConfig {

  /**
   * LoggingFilterを登録し、HTTPリクエストおよびレスポンスの詳細をログに記録します。
   *
   * @return LoggingFilterのFilterRegistrationBean
   */
  @Bean
  public FilterRegistrationBean<LoggingFilter> loggingFilter() {
    FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new LoggingFilter());
    registrationBean.addUrlPatterns("/*");
    return registrationBean;
  }
}

