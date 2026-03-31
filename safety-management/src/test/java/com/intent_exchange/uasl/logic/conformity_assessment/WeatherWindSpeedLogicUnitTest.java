package com.intent_exchange.uasl.logic.conformity_assessment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.uasl.dto.request.WeatherWindSpeedConformityAssessmentDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeListDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeListResponseDto;
import com.intent_exchange.uasl.dto.response.WeatherWindSpeedListResponseDto;
import com.intent_exchange.uasl.dto.response.WeatherWindSpeedsDto;
import com.intent_exchange.uasl.dto.response.WeatherWindSpeedsRequestParametersDto;
import com.intent_exchange.uasl.exception.WeatherWindSpeedException;
import com.intent_exchange.uasl.logic.web.WebWeatherForecastTimeLogic;
import com.intent_exchange.uasl.logic.web.WebWeatherWindSpeedLogic;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * WeatherWindSpeedLogicクラスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // clockが不要のテストも含まれていても、エラーが発生しないように設定
class WeatherWindSpeedLogicUnitTest {

  @Mock
  private Clock clock;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private WebWeatherForecastTimeLogic webWeatherForecastTimeLogic;

  @Mock
  private WebWeatherWindSpeedLogic webWeatherWindSpeedLogic;

  @Mock
  private WindSpeedRangeProcessingLogic windSpeedRangeProcessingLogic;

  @InjectMocks
  private WeatherWindSpeedLogic weatherWindSpeedLogic;

  @BeforeEach
  void setUp() {
    // モックの初期化
    MockitoAnnotations.openMocks(this);
    // モックされた Clock を設定
    Clock fixedClock = Clock.fixed(Instant.parse("2024-12-05T01:30:00Z"), ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(fixedClock.instant());
    when(clock.getZone()).thenReturn(fixedClock.getZone());
  }

  @Test
  @DisplayName("予約の開始日時が現在の時刻プラス10時間と同じ_風速判定処理を実行しfalseを返す")
  void testIsReservationTimeValid1() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(4.6);// 風速条件範囲外になる値
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 30));// 開始時刻が現在時刻+10時間同じになる値
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象予測対象時間リスト取得用のモックされたレスポンスを作成
    WeatherForecastTimeListResponseDto weatherForecastTimeListResponseDto =
        createWeatherForecastTimeListResponseDto();
    ResponseEntity<WeatherForecastTimeListResponseDto> forecastTimeListResponseEntity =
        ResponseEntity.ok(weatherForecastTimeListResponseDto);
    // 天候・風速リスト取得のモックされたレスポンスを作成
    WeatherWindSpeedListResponseDto weatherWindSpeedListResponseDto =
        createWeatherWindSpeedListResponseDto();
    ResponseEntity<WeatherWindSpeedListResponseDto> weatherWindSpeedListResponseEntity =
        ResponseEntity.ok(weatherWindSpeedListResponseDto);


    // 気象予測対象時間リスト取得用のモックされたレスポンスを設定
    when(webWeatherForecastTimeLogic.getWeatherForecastTime(anyString()))
        .thenReturn(forecastTimeListResponseEntity);
    // 天候・風速リスト取得用のモックされたレスポンスを設定
    when(webWeatherWindSpeedLogic.getWeatherWindSpeeds(any(), any()))
        .thenReturn(weatherWindSpeedListResponseEntity);

    // 風速条件範囲内判定処理を実行
    Boolean result = weatherWindSpeedLogic.check(weatherWindSpeedConformityAssessmentDto);

    // 結果を検証
    assertNotNull(result);
    assertFalse(result);
  }

  @Test
  @DisplayName("予約の開始日時が現在の時刻プラス10時間を超える_風速判定処理は実行せずtrueを返す")
  void testIsReservationTimeValid2() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(4.0);
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 31));// 開始時刻が現在時刻+10時間を超える値
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 予約の開始日時が現在の時刻プラス10時間以内に含まれない場合、風速判定処理は行わなないずtrueを返す値を設定
    when(windSpeedRangeProcessingLogic.isReservationTimeValid(any())).thenReturn(true);

    // 風速条件範囲内判定処理を実行
    Boolean result = weatherWindSpeedLogic.check(weatherWindSpeedConformityAssessmentDto);

    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  @DisplayName("予約の開始日時が現在の時刻プラス10時間超えない_風速判定処理を実行しfalseを返す")
  void testIsReservationTimeValid3() {



    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(4.6);// 風速条件範囲外になる値
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 29));
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象予測対象時間リスト取得用のモックされたレスポンスを作成
    WeatherForecastTimeListResponseDto weatherForecastTimeListResponseDto =
        createWeatherForecastTimeListResponseDto();
    ResponseEntity<WeatherForecastTimeListResponseDto> forecastTimeListResponseEntity =
        ResponseEntity.ok(weatherForecastTimeListResponseDto);
    // 天候・風速リスト取得のモックされたレスポンスを作成
    WeatherWindSpeedListResponseDto weatherWindSpeedListResponseDto =
        createWeatherWindSpeedListResponseDto();
    ResponseEntity<WeatherWindSpeedListResponseDto> weatherWindSpeedListResponseEntity =
        ResponseEntity.ok(weatherWindSpeedListResponseDto);


    // 気象予測対象時間リスト取得用のモックされたレスポンスを設定
    when(webWeatherForecastTimeLogic.getWeatherForecastTime(anyString()))
        .thenReturn(forecastTimeListResponseEntity);
    // 天候・風速リスト取得用のモックされたレスポンスを設定
    when(webWeatherWindSpeedLogic.getWeatherWindSpeeds(any(), any()))
        .thenReturn(weatherWindSpeedListResponseEntity);

    // 風速条件範囲内判定処理を実行
    Boolean result = weatherWindSpeedLogic.check(weatherWindSpeedConformityAssessmentDto);

    // 結果を検証
    assertNotNull(result);
    assertFalse(result);
  }

  @Test
  @DisplayName("風速条件範囲内判定_取得した風速範囲のmax値が風速条件範囲を超えない")
  void testCheck1() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    // 風速のMax値≒4.886
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(4.9);// 風速条件範囲内になる値
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 29));
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象予測対象時間リスト取得用のモックされたレスポンスを作成
    WeatherForecastTimeListResponseDto weatherForecastTimeListResponseDto =
        createWeatherForecastTimeListResponseDto();
    ResponseEntity<WeatherForecastTimeListResponseDto> forecastTimeListResponseEntity =
        ResponseEntity.ok(weatherForecastTimeListResponseDto);
    // 天候・風速リスト取得のモックされたレスポンスを作成
    WeatherWindSpeedListResponseDto weatherWindSpeedListResponseDto =
        createWeatherWindSpeedListResponseDto();
    ResponseEntity<WeatherWindSpeedListResponseDto> weatherWindSpeedListResponseEntity =
        ResponseEntity.ok(weatherWindSpeedListResponseDto);


    // 気象予測対象時間リスト取得用のモックされたレスポンスを設定
    when(webWeatherForecastTimeLogic.getWeatherForecastTime(anyString()))
        .thenReturn(forecastTimeListResponseEntity);
    // 天候・風速リスト取得用のモックされたレスポンスを設定
    when(webWeatherWindSpeedLogic.getWeatherWindSpeeds(any(), any()))
        .thenReturn(weatherWindSpeedListResponseEntity);
    // 風速条件範囲内判定処理のモックされたレスポンスを設定
    when(windSpeedRangeProcessingLogic.getWindSpeedRangeJudgment(any(), any())).thenReturn(true);

    // 風速条件範囲内判定処理を実行
    Boolean result = weatherWindSpeedLogic.check(weatherWindSpeedConformityAssessmentDto);

    // 結果を検証
    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  @DisplayName("風速条件範囲内判定_SDSPから取得した風速範囲のmax値が風速条件範囲を超える")
  void testCheck2() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(4.6);// 風速条件範囲外になる値
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 29));
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象予測対象時間リスト取得用のモックされたレスポンスを作成
    WeatherForecastTimeListResponseDto weatherForecastTimeListResponseDto =
        createWeatherForecastTimeListResponseDto();
    ResponseEntity<WeatherForecastTimeListResponseDto> forecastTimeListResponseEntity =
        ResponseEntity.ok(weatherForecastTimeListResponseDto);
    // 天候・風速リスト取得のモックされたレスポンスを作成
    WeatherWindSpeedListResponseDto weatherWindSpeedListResponseDto =
        createWeatherWindSpeedListResponseDto();
    ResponseEntity<WeatherWindSpeedListResponseDto> weatherWindSpeedListResponseEntity =
        ResponseEntity.ok(weatherWindSpeedListResponseDto);


    // 気象予測対象時間リスト取得用のモックされたレスポンスを設定
    when(webWeatherForecastTimeLogic.getWeatherForecastTime(anyString()))
        .thenReturn(forecastTimeListResponseEntity);
    // 天候・風速リスト取得用のモックされたレスポンスを設定
    when(webWeatherWindSpeedLogic.getWeatherWindSpeeds(any(), any()))
        .thenReturn(weatherWindSpeedListResponseEntity);

    // 風速条件範囲内判定処理を実行
    Boolean result = weatherWindSpeedLogic.check(weatherWindSpeedConformityAssessmentDto);

    // 結果を検証
    assertNotNull(result);
    assertFalse(result);
  }

  @Test
  @DisplayName("SDSPから気象予測発表時間と気象予測対象時間リストの取得のレスポンスがnullの場合_WeatherWindSpeedExceptionを返す")
  void testGetWeatherForecastTime() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(3.9);// 条件範囲外になる値
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 29));
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象予測対象時間リスト取得用のURLを設定
    String forecastUrlTemplate =
        UriComponentsBuilder.fromHttpUrl(PropertyUtil.getProperty("get.weather.forecast.time.url"))
            .queryParam("elements", PropertyUtil.getProperty("weather.wind.elements")).toUriString();

    // HTTPヘッダーを設定
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", weatherWindSpeedConformityAssessmentDto.getToken());
    HttpEntity<String> entity = new HttpEntity<>(headers);

    // 気象予測対象時間リスト取得用のリクエストをモックし、モックレスポンスにnullを返すように設定
    when(restTemplate.exchange(eq(forecastUrlTemplate), eq(HttpMethod.GET), eq(entity),
        eq(WeatherForecastTimeListResponseDto.class))).thenReturn(null);

    // 風速条件範囲内判定処理を実行し、例外がスローされることを確認
    WeatherWindSpeedException exception = assertThrows(WeatherWindSpeedException.class, () -> {
      weatherWindSpeedLogic.check(weatherWindSpeedConformityAssessmentDto);
    });

    // 例外メッセージを検証
    assertEquals(PropertyUtil.getProperty("error.WeatherForecastTime"), exception.getMessage());
  }


  @Test
  @DisplayName("SDSPから天候・風速情報の取得のレスポンスがnullの場合_WeatherWindSpeedExceptionを返す")
  void testGetWeatherWindSpeeds() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(3.9);// 条件範囲外になる値
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 29));
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象予測対象時間リスト取得用のモックされたレスポンスを作成
    WeatherForecastTimeListResponseDto weatherForecastTimeListResponseDto =
        createWeatherForecastTimeListResponseDto();
    ResponseEntity<WeatherForecastTimeListResponseDto> forecastTimeListResponseEntity =
        ResponseEntity.ok(weatherForecastTimeListResponseDto);

    // 気象予測対象時間リスト取得用のモックされたレスポンスを設定
    when(webWeatherForecastTimeLogic.getWeatherForecastTime(anyString()))
        .thenReturn(forecastTimeListResponseEntity);

    // 天候・風速リスト取得用のモックされたレスポンスにnullを設定
    when(webWeatherWindSpeedLogic.getWeatherWindSpeeds(any(), any()))
        .thenReturn(ResponseEntity.ok(null));

    // 風速条件範囲内判定処理を実行し、例外がスローされることを確認
    WeatherWindSpeedException exception = assertThrows(WeatherWindSpeedException.class, () -> {
      weatherWindSpeedLogic.check(weatherWindSpeedConformityAssessmentDto);
    });
    // 例外メッセージを検証
    assertEquals(PropertyUtil.getProperty("error.WeatherWindSpeed"), exception.getMessage());
  }

  @Test
  @DisplayName("SDSPから気象予測発表時間と気象予測対象時間リストの取得のレスポンスがnullの場合_WeatherWindSpeedExceptionを返す②")
  void testGetWeatherForecastTime2() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(3.9);// 条件範囲外になる値
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 29));
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象予測対象時間リスト取得用のURLを設定
    ResponseEntity<WeatherForecastTimeListResponseDto> forecastTimeListResponseEntity =
        ResponseEntity.ok(null);

    // 天候・風速リスト取得のモックされたレスポンスを作成
    WeatherWindSpeedListResponseDto weatherWindSpeedListResponseDto =
        createWeatherWindSpeedListResponseDto();
    ResponseEntity<WeatherWindSpeedListResponseDto> weatherWindSpeedListResponseEntity =
        ResponseEntity.ok(weatherWindSpeedListResponseDto);


    // 気象予測対象時間リスト取得用のモックされたレスポンスを設定
    when(webWeatherForecastTimeLogic.getWeatherForecastTime(anyString()))
        .thenReturn(forecastTimeListResponseEntity);
    // 天候・風速リスト取得用のモックされたレスポンスを設定
    when(webWeatherWindSpeedLogic.getWeatherWindSpeeds(any(), any()))
        .thenReturn(weatherWindSpeedListResponseEntity);

    // 風速条件範囲内判定処理を実行し、例外がスローされることを確認
    WeatherWindSpeedException exception = assertThrows(WeatherWindSpeedException.class, () -> {
      weatherWindSpeedLogic.check(weatherWindSpeedConformityAssessmentDto);
    });

    // 例外メッセージを検証
    assertEquals(PropertyUtil.getProperty("error.WeatherForecastTime"), exception.getMessage());
  }


  @Test
  @DisplayName("SDSPから天候・風速情報の取得のレスポンスがnullの場合_WeatherWindSpeedExceptionを返す②")
  void testGetWeatherWindSpeeds2() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(3.9);// 条件範囲外になる値
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 29));
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.of(2024, 12, 5, 17, 00));

    // 気象予測対象時間リスト取得用のモックされたレスポンスを作成
    WeatherForecastTimeListResponseDto weatherForecastTimeListResponseDto =
        createWeatherForecastTimeListResponseDto();
    ResponseEntity<WeatherForecastTimeListResponseDto> forecastTimeListResponseEntity =
        ResponseEntity.ok(weatherForecastTimeListResponseDto);

    // 気象予測対象時間リスト取得用のモックされたレスポンスを設定
    when(webWeatherForecastTimeLogic.getWeatherForecastTime(anyString()))
        .thenReturn(forecastTimeListResponseEntity);

    // 天候・風速リスト取得用のモックされたレスポンスにnullを設定
    when(webWeatherWindSpeedLogic.getWeatherWindSpeeds(any(), any())).thenReturn(null);

    // 風速条件範囲内判定処理を実行し、例外がスローされることを確認
    WeatherWindSpeedException exception = assertThrows(WeatherWindSpeedException.class, () -> {
      weatherWindSpeedLogic.check(weatherWindSpeedConformityAssessmentDto);
    });
    // 例外メッセージを検証
    assertEquals(PropertyUtil.getProperty("error.WeatherWindSpeed"), exception.getMessage());
  }

  /**
   * モックされた気象予測時間情報リストのレスポンスを作成
   *
   * @return 気象予測時間情報リストのDTO
   */
  private WeatherForecastTimeListResponseDto createWeatherForecastTimeListResponseDto() {

    List<WeatherForecastTimeDto> index = new ArrayList<>();

    WeatherForecastTimeDto weatherForecastTimeDto = new WeatherForecastTimeDto();
    List<WeatherForecastTimeListDto> timeList = new ArrayList<>();

    // 実サーバのレスポンスはugrdとvgrdの風速情報は過去24時間保存された値が返るので、実際はbasetimeが1時間づつ遡ったugrdのtimeListが24個、vgrdのtimeListが24個返る
    // ugrdのWeatherForecastTimeDtoを作成
    weatherForecastTimeDto.setElement("UGRD");
    weatherForecastTimeDto.setMessage("Index for element [ugrd] retrieved successfully.");

    WeatherForecastTimeListDto timeListDto = new WeatherForecastTimeListDto();

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T21");
    timeListDto.setValidtime(List.of("20241204T21", "20241204T22", "20241204T23", "20241205T00",
        "20241205T01", "20241205T02", "20241205T03", "20241205T04", "20241205T05", "20241205T06",
        "20241205T07"));
    timeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T20");
    timeListDto.setValidtime(List.of("20241204T20", "20241204T21", "20241204T22", "20241204T23",
        "20241205T00", "20241205T01", "20241205T02", "20241205T03", "20241205T04", "20241205T05",
        "20241205T06"));
    timeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T19");
    timeListDto.setValidtime(List.of("20241204T19", "20241204T20", "20241204T21", "20241204T22",
        "20241204T23", "20241205T00", "20241205T01", "20241205T02", "20241205T03", "20241205T04",
        "20241205T05"));
    timeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T18");
    timeListDto.setValidtime(List.of("20241204T18", "20241204T19", "20241204T20", "20241204T21",
        "20241204T22", "20241204T23", "20241205T00", "20241205T01", "20241205T02", "20241205T03",
        "20241205T04"));
    timeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T17");
    timeListDto.setValidtime(List.of("20241204T17", "20241204T18", "20241204T19", "20241204T20",
        "20241204T21", "20241204T22", "20241204T23", "20241205T00", "20241205T01", "20241205T02",
        "20241205T03"));
    timeList.add(timeListDto);
    weatherForecastTimeDto.setTimeList(timeList);

    // vgrdのWeatherForecastTimeDtoを作成
    weatherForecastTimeDto.setElement("VGRD");
    weatherForecastTimeDto.setMessage("Index for element [vgrd] retrieved successfully.");

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T21");
    timeListDto.setValidtime(List.of("20241204T21", "20241204T22", "20241204T23", "20241205T00",
        "20241205T01", "20241205T02", "20241205T03", "20241205T04", "20241205T05", "20241205T06",
        "20241205T07"));
    timeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T20");
    timeListDto.setValidtime(List.of("20241204T20", "20241204T21", "20241204T22", "20241204T23",
        "20241205T00", "20241205T01", "20241205T02", "20241205T03", "20241205T04", "20241205T05",
        "20241205T06"));
    timeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T19");
    timeListDto.setValidtime(List.of("20241204T19", "20241204T20", "20241204T21", "20241204T22",
        "20241204T23", "20241205T00", "20241205T01", "20241205T02", "20241205T03", "20241205T04",
        "20241205T05"));
    timeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T18");
    timeListDto.setValidtime(List.of("20241204T18", "20241204T19", "20241204T20", "20241204T21",
        "20241204T22", "20241204T23", "20241205T00", "20241205T01", "20241205T02", "20241205T03",
        "20241205T04"));
    timeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("20241204T17");
    timeListDto.setValidtime(List.of("20241204T17", "20241204T18", "20241204T19", "20241204T20",
        "20241204T21", "20241204T22", "20241204T23", "20241205T00", "20241205T01", "20241205T02",
        "20241205T03"));
    timeList.add(timeListDto);
    weatherForecastTimeDto.setTimeList(timeList);

    // ugrdとvgrdのタイムリストを設定したWeatherForecastTimeDtoをindexに設定
    index.add(weatherForecastTimeDto);

    // WeatherForecastTimeListResponseDtoにindexを設定
    WeatherForecastTimeListResponseDto weatherForecastTimeListResponseDto =
        new WeatherForecastTimeListResponseDto();
    weatherForecastTimeListResponseDto.setIndex(index);

    return weatherForecastTimeListResponseDto;
  }

  /**
   * モックされた風速情報のレスポンスを作成
   *
   * @return 風速情報のDTO
   */
  private WeatherWindSpeedListResponseDto createWeatherWindSpeedListResponseDto() {

    // 風速情報のレスポンスは指定されたvalidtime（気象予測時間）のugrdとvgrdの風速情報が返る
    List<WeatherWindSpeedsDto> weatherWindSpeedsListDto = new ArrayList<>();
    WeatherWindSpeedsDto weatherWindSpeedsDto = new WeatherWindSpeedsDto();
    WeatherWindSpeedsRequestParametersDto WeatherWindSpeedsRequestParametersDto =
        new WeatherWindSpeedsRequestParametersDto();

    WeatherWindSpeedsRequestParametersDto.setBasetime("20241204T17");
    WeatherWindSpeedsRequestParametersDto.setValidtime("20241205T07");
    WeatherWindSpeedsRequestParametersDto.setMinute("0");
    WeatherWindSpeedsRequestParametersDto.setElement("ugrd");
    WeatherWindSpeedsRequestParametersDto.setAltSurface("270");
    WeatherWindSpeedsRequestParametersDto.setAltSurface("20");
    WeatherWindSpeedsRequestParametersDto.setLatStart("35.993876");
    WeatherWindSpeedsRequestParametersDto.setLonStart("139.101484");
    WeatherWindSpeedsRequestParametersDto.setLatEnd("35.991419");
    WeatherWindSpeedsRequestParametersDto.setLonEnd("139.101044");
    WeatherWindSpeedsRequestParametersDto.setGrid("250m");
    WeatherWindSpeedsRequestParametersDto.setLatInterval("-0.0025");
    WeatherWindSpeedsRequestParametersDto.setLonInterval("0.003125");
    WeatherWindSpeedsRequestParametersDto.setLatStartBase("35.993876");
    WeatherWindSpeedsRequestParametersDto.setLonStartBase("139.101484");
    WeatherWindSpeedsRequestParametersDto.setLatEndBase("35.991419");
    WeatherWindSpeedsRequestParametersDto.setLonEndBase("139.101044");
    WeatherWindSpeedsRequestParametersDto.setHeight(3);
    WeatherWindSpeedsRequestParametersDto.setWidth(3);
    weatherWindSpeedsDto
        .setContents(List.of(2.558, 2.569, 2.581, 2.73, 2.735, 2.741, 2.742, 2.743, 2.744));
    weatherWindSpeedsDto.setRequestParameters(WeatherWindSpeedsRequestParametersDto);
    weatherWindSpeedsListDto.add(weatherWindSpeedsDto);

    // 2つ目のWeatherWindSpeedsDtoオブジェクト
    weatherWindSpeedsDto = new WeatherWindSpeedsDto();
    WeatherWindSpeedsRequestParametersDto = new WeatherWindSpeedsRequestParametersDto();
    WeatherWindSpeedsRequestParametersDto.setBasetime("20241204T17");
    WeatherWindSpeedsRequestParametersDto.setValidtime("20241205T07");
    WeatherWindSpeedsRequestParametersDto.setMinute("0");
    WeatherWindSpeedsRequestParametersDto.setElement("vgrd");
    WeatherWindSpeedsRequestParametersDto.setAltSurface("270");
    WeatherWindSpeedsRequestParametersDto.setAltSurface("20");
    WeatherWindSpeedsRequestParametersDto.setLatStart("35.993876");
    WeatherWindSpeedsRequestParametersDto.setLonStart("139.101484");
    WeatherWindSpeedsRequestParametersDto.setLatEnd("35.991419");
    WeatherWindSpeedsRequestParametersDto.setLonEnd("139.101044");
    WeatherWindSpeedsRequestParametersDto.setGrid("250m");
    WeatherWindSpeedsRequestParametersDto.setLatInterval("-0.0025");
    WeatherWindSpeedsRequestParametersDto.setLonInterval("0.003125");
    WeatherWindSpeedsRequestParametersDto.setLatStartBase("35.993876");
    WeatherWindSpeedsRequestParametersDto.setLonStartBase("139.101484");
    WeatherWindSpeedsRequestParametersDto.setLatEndBase("35.991419");
    WeatherWindSpeedsRequestParametersDto.setLonEndBase("139.101044");
    WeatherWindSpeedsRequestParametersDto.setHeight(3);
    WeatherWindSpeedsRequestParametersDto.setWidth(3);
    weatherWindSpeedsDto
        .setContents(List.of(4.0, 3.569, 3.581, 3.73, 3.735, 3.741, 3.742, 3.743, 3.744));
    weatherWindSpeedsDto.setRequestParameters(WeatherWindSpeedsRequestParametersDto);
    weatherWindSpeedsListDto.add(weatherWindSpeedsDto);

    WeatherWindSpeedListResponseDto weatherWindSpeedListResponseDto =
        new WeatherWindSpeedListResponseDto();
    weatherWindSpeedListResponseDto.setData(weatherWindSpeedsListDto);

    return weatherWindSpeedListResponseDto;
  }
}
