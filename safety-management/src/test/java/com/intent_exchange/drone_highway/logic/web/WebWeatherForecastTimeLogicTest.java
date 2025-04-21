package com.intent_exchange.drone_highway.logic.web;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.drone_highway.dto.response.WeatherForecastTimeDto;
import com.intent_exchange.drone_highway.dto.response.WeatherForecastTimeListResponseDto;
import com.intent_exchange.drone_highway.exception.WeatherWindSpeedException;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * WebWeatherForecastTimeLogicクラスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class WebWeatherForecastTimeLogicTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private WebWeatherForecastTimeLogic webWeatherForecastTimeLogic;

  @Test
  @DisplayName("SDSPから気象予測時間情報を取得する")
  void testGetWeatherForecastTime() {
    // モックされたレスポンスを作成
    WeatherForecastTimeListResponseDto weatherForecastTimeListResponseDto =
        new WeatherForecastTimeListResponseDto();
    ResponseEntity<WeatherForecastTimeListResponseDto> responseEntity =
        ResponseEntity.ok(weatherForecastTimeListResponseDto);

    // モックの設定
    String forecastUrlTemplate =
        UriComponentsBuilder.fromHttpUrl(PropertyUtil.getProperty("get.weather.forecast.time.url"))
            .queryParam("elements", PropertyUtil.getProperty("weatherWindElements")).toUriString();
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "testToken");
    HttpEntity<String> entity = new HttpEntity<>(headers);

    when(restTemplate.exchange(forecastUrlTemplate, HttpMethod.GET, entity,
        WeatherForecastTimeListResponseDto.class)).thenReturn(responseEntity);

    // メソッドの呼び出し
    ResponseEntity<WeatherForecastTimeListResponseDto> result =
        webWeatherForecastTimeLogic.getWeatherForecastTime("testToken");

    // 結果の検証
    assertNotNull(result);
    assertDoesNotThrow(() -> webWeatherForecastTimeLogic.getWeatherForecastTime("testToken"));
  }
}
