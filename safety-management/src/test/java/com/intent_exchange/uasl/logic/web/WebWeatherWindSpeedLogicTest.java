package com.intent_exchange.uasl.logic.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
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
import com.intent_exchange.uasl.dto.request.WeatherWindSpeedConformityAssessmentDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeListDto;
import com.intent_exchange.uasl.dto.response.WeatherWindSpeedListResponseDto;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * WebWeatherWindSpeedLogicクラスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class WebWeatherWindSpeedLogicTest {

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private Clock clock; // Clockクラスのモック

  @InjectMocks
  private WebWeatherWindSpeedLogic webWeatherWindSpeedLogic;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    String testWeatherWindSpeedsUrl = PropertyUtil.getProperty("get.weather.wind.speeds.url");
    Field weatherWindSpeedsUrlField =
        WebWeatherWindSpeedLogic.class.getDeclaredField("getWeatherWindSpeedsUrl");
    weatherWindSpeedsUrlField.setAccessible(true);
    weatherWindSpeedsUrlField.set(webWeatherWindSpeedLogic, testWeatherWindSpeedsUrl);

    String testWeatherWindElements = PropertyUtil.getProperty("weather.wind.elements");
    Field weatherWindElementsField =
        WebWeatherWindSpeedLogic.class.getDeclaredField("weatherWindElements");
    weatherWindElementsField.setAccessible(true);
    weatherWindElementsField.set(webWeatherWindSpeedLogic, testWeatherWindElements);
  }

  @Test
  @DisplayName("SDSPから風速情報を取得する_気象予測時間リストに予約開始日時と一致する値が含まれる")
  void testGetWeatherWindSpeeds1() {
    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2025, 2, 11, 13, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    // モックされたレスポンスを作成
    WeatherWindSpeedListResponseDto weatherWindSpeedListResponseDto =
        new WeatherWindSpeedListResponseDto();
    ResponseEntity<WeatherWindSpeedListResponseDto> responseEntity =
        ResponseEntity.ok(weatherWindSpeedListResponseDto);

    // モックの設定
    WeatherWindSpeedConformityAssessmentDto dto = new WeatherWindSpeedConformityAssessmentDto();
    dto.setAltitude("30");
    dto.setLatStart("37.8");
    dto.setLonStart("140.7");
    dto.setLatEnd("37.5");
    dto.setLonEnd("141.2");
    dto.setToken("testToken");
    dto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 30));// 予約開始日時
    dto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象情報取得のレスポンス
    WeatherForecastTimeDto forecastTimeDto = new WeatherForecastTimeDto();
    WeatherForecastTimeListDto timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241205T02");
    // 予約開始日時(20241205T11)と一致する値が含まれる気象予測時間リスト
    timeListDto.setValidtime(List.of("20241205T02", "20241205T03", "20241205T04", "20241205T05",
        "20241205T06", "20241205T07", "20241205T08", "20241205T09", "20241205T10", "20241205T11",
        "20241205T12"));
    forecastTimeDto.setTimeList(List.of(timeListDto));

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH");
    String startAtFormatted = dto.getStartAt().format(formatter);
    String validtime = null;
    boolean res = forecastTimeDto.getTimeList()
        .get(0)
        .getValidtime()
        .stream()
        .anyMatch(time -> time.equals(startAtFormatted));
    if (res) {
      validtime = startAtFormatted;
    } else {
      validtime = forecastTimeDto.getTimeList().get(0).getValidtime().get(10);
    }

    String windSpeedsUrlTemplate = UriComponentsBuilder
        .fromHttpUrl(PropertyUtil.getProperty("get.weather.wind.speeds.url"))
        .queryParam("elements", PropertyUtil.getProperty("weather.wind.elements"))
        .queryParam("basetime", forecastTimeDto.getTimeList().get(0).getBasetime())
        .queryParam("validtime", validtime)
        .queryParam("alt_surface",
            new BigDecimal(dto.getAltitude()).divide(new BigDecimal("10"), 0, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("10")))
        .queryParam("lat_start", dto.getLatStart())
        .queryParam("lon_start", dto.getLonStart())
        .queryParam("lat_end", dto.getLatEnd())
        .queryParam("lon_end", dto.getLonEnd())
        .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", dto.getToken());
    HttpEntity<String> entity = new HttpEntity<>(headers);

    when(restTemplate.exchange(windSpeedsUrlTemplate, HttpMethod.GET, entity,
        WeatherWindSpeedListResponseDto.class)).thenReturn(responseEntity);

    // メソッドの呼び出し
    ResponseEntity<WeatherWindSpeedListResponseDto> result =
        webWeatherWindSpeedLogic.getWeatherWindSpeeds(dto, forecastTimeDto);

    // 結果の検証
    assertNotNull(result);
    assertDoesNotThrow(() -> webWeatherWindSpeedLogic.getWeatherWindSpeeds(dto, forecastTimeDto));
  }

  @Test
  @DisplayName("SDSPから風速情報を取得する_気象予測時間リストに予約開始日時と一致する値が含まれない")
  void testGetWeatherWindSpeedsWhenRes2() {
    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2025, 2, 11, 13, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    // モックされたレスポンスを作成
    WeatherWindSpeedListResponseDto weatherWindSpeedListResponseDto =
        new WeatherWindSpeedListResponseDto();
    ResponseEntity<WeatherWindSpeedListResponseDto> responseEntity =
        ResponseEntity.ok(weatherWindSpeedListResponseDto);

    // モックの設定
    WeatherWindSpeedConformityAssessmentDto dto = new WeatherWindSpeedConformityAssessmentDto();
    dto.setAltitude("30");
    dto.setLatStart("37.8");
    dto.setLonStart("140.7");
    dto.setLatEnd("37.5");
    dto.setLonEnd("141.2");
    dto.setToken("testToken");
    dto.setStartAt(LocalDateTime.of(2024, 12, 5, 14, 30));// 予約開始日時
    dto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象情報取得のレスポンス
    WeatherForecastTimeDto forecastTimeDto = new WeatherForecastTimeDto();
    WeatherForecastTimeListDto timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241205T02");
    // 予約開始日時と一致する値が含まれない気象予測時間リスト
    timeListDto.setValidtime(List.of("20241205T02", "20241205T03", "20241205T04", "20241205T05",
        "20241205T06", "20241205T07", "20241205T08", "20241205T09", "20241205T10", "20241205T11",
        "20241205T12"));
    forecastTimeDto.setTimeList(List.of(timeListDto));

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HH");
    String startAtFormatted = dto.getStartAt().format(formatter);
    String validtime = null;
    boolean res = forecastTimeDto.getTimeList()
        .get(0)
        .getValidtime()
        .stream()
        .anyMatch(time -> time.equals(startAtFormatted));
    if (res) {
      validtime = startAtFormatted;
    } else {
      validtime = forecastTimeDto.getTimeList().get(0).getValidtime().get(10);
    }

    String windSpeedsUrlTemplate = UriComponentsBuilder
        .fromHttpUrl(PropertyUtil.getProperty("get.weather.wind.speeds.url"))
        .queryParam("elements", PropertyUtil.getProperty("weather.wind.elements"))
        .queryParam("basetime", forecastTimeDto.getTimeList().get(0).getBasetime())
        .queryParam("validtime", validtime)
        .queryParam("alt_surface",
            new BigDecimal(dto.getAltitude()).divide(new BigDecimal("10"), 0, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("10")))
        .queryParam("lat_start", dto.getLatStart())
        .queryParam("lon_start", dto.getLonStart())
        .queryParam("lat_end", dto.getLatEnd())
        .queryParam("lon_end", dto.getLonEnd())
        .toUriString();

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", dto.getToken());
    HttpEntity<String> entity = new HttpEntity<>(headers);

    when(restTemplate.exchange(windSpeedsUrlTemplate, HttpMethod.GET, entity,
        WeatherWindSpeedListResponseDto.class)).thenReturn(responseEntity);

    // メソッドの呼び出し
    ResponseEntity<WeatherWindSpeedListResponseDto> result =
        webWeatherWindSpeedLogic.getWeatherWindSpeeds(dto, forecastTimeDto);

    // 結果の検証
    assertNotNull(result);
    assertDoesNotThrow(() -> webWeatherWindSpeedLogic.getWeatherWindSpeeds(dto, forecastTimeDto));
  }
}
