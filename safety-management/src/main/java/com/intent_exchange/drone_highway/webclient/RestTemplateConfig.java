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

package com.intent_exchange.drone_highway.webclient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpHost;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.drone_highway.interceptor.LoggingInterceptor;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * RestTemplateの設定クラス
 */
@Configuration
public class RestTemplateConfig {

  /**
   * デフォルトのRestTemplateを生成する
   *
   * @return デフォルトのRestTemplate
   */
  @Bean
  public RestTemplate defaultRestTemplate() {
    RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
    restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    return restTemplate;
  }

  /**
   * カスタムRestTemplateを生成する
   *
   * @param objectMapper ObjectMapperインスタンス
   * @return カスタムRestTemplate
   */
  @Bean
  public RestTemplate customRestTemplate(ObjectMapper objectMapper) {
    RestTemplate restTemplate = new RestTemplate();
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper);
    restTemplate.getMessageConverters().add(converter);
    restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    return restTemplate;
  }

  /**
   * ロギング用のRestTemplateを生成する
   *
   * @return ロギング用のRestTemplate
   */
  @Bean
  public RestTemplate loggingRestTemplate() {
    RestTemplate restTemplate = new RestTemplate();
    List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    interceptors.add(new LoggingInterceptor());
    restTemplate.setInterceptors(interceptors);
    restTemplate.setErrorHandler(new RestTemplateResponseErrorHandler());
    return restTemplate;
  }

  /**
   * HTTPリクエストファクトリを生成する
   *
   * @return HTTPリクエストファクトリ
   */
  private HttpComponentsClientHttpRequestFactory httpRequestFactory() {
    HttpHost proxy = null;
    if (!PropertyUtil.getProperty("web.client.proxy.url").isBlank()) {
      proxy = new HttpHost(PropertyUtil.getProperty("web.client.proxy.url"),
          PropertyUtil.getPropertyInt("web.client.proxy.port"));
    }
    RequestConfig config = RequestConfig.custom()
        .setConnectionRequestTimeout(PropertyUtil.getPropertyInt("web.client.connect.timeout"),
            TimeUnit.MILLISECONDS)
        .setResponseTimeout(PropertyUtil.getPropertyInt("web.client.response.timeout"),
            TimeUnit.MILLISECONDS)
        .build();
    CloseableHttpClient httpClient =
        HttpClients.custom().setProxy(proxy).setDefaultRequestConfig(config).build();
    return new HttpComponentsClientHttpRequestFactory(httpClient);
  }
}

