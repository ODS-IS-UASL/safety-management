package com.intent_exchange.drone_highway.logic.conformity_assessment;

import java.time.Clock;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import com.intent_exchange.drone_highway.config.TestConfig;
import com.intent_exchange.drone_highway.dto.request.WeatherWindSpeedConformityAssessmentDto;
import com.intent_exchange.drone_highway.logic.web.WebWeatherForecastTimeLogic;
import com.intent_exchange.drone_highway.logic.web.WebWeatherWindSpeedLogic;

/**
 * WeatherWindSpeedLogicで風速適合性評価のSDSPへの疎通確認を行うテストクラス。
 */
@SpringBootTest
class WeatherWindSpeedLogicTest {

  @Autowired
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  @Autowired
  private WeatherWindSpeedLogic logic;

  /** Clockインスタンス。現在の時刻を取得するために使用します。 */
  @Autowired
  private Clock clock;

  @Test
  @DisplayName("風速条件範囲内判定_取得した風速範囲のmax値が風速条件範囲内")
  void testCheck1() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.now(clock));
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.now(clock).plusHours(1));
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(4.8);// 条件範囲内になる値、4.754がmax値(yamlで仮の設定値）
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");

    // TODO:疎通試験用のためコメントアウト
    // // テスト対象メソッドを呼び出し
    // Boolean result = logic.check(weatherWindSpeedConformityAssessmentDto);
    //
    // // 結果を検証
    // assertNotNull(result);
    // assertTrue(result);
  }

  @Test
  @DisplayName("風速条件範囲内判定_SDSPから取得した風速範囲のmax値が風速条件範囲を超える")
  void testCheck2() {

    // 適合性評価の引数を設定
    WeatherWindSpeedConformityAssessmentDto weatherWindSpeedConformityAssessmentDto =
        new WeatherWindSpeedConformityAssessmentDto();
    weatherWindSpeedConformityAssessmentDto.setStartAt(LocalDateTime.now(clock));
    weatherWindSpeedConformityAssessmentDto.setEndAt(LocalDateTime.now(clock).plusHours(1));
    weatherWindSpeedConformityAssessmentDto.setAltitude("30");
    weatherWindSpeedConformityAssessmentDto.setLatStart("37.8");
    weatherWindSpeedConformityAssessmentDto.setLonStart("140.7");
    weatherWindSpeedConformityAssessmentDto.setLatEnd("37.5");
    weatherWindSpeedConformityAssessmentDto.setLonEnd("141.2");
    weatherWindSpeedConformityAssessmentDto.setWindSpeedRange(4.6);// 条件範囲内になる値、4.754がmax値(yamlで仮の設定値）
    weatherWindSpeedConformityAssessmentDto.setToken("neccrossindbizdev");

    // TODO:疎通試験用のためコメントアウト
    // // テスト対象メソッドを呼び出し
    // Boolean result = logic.check(weatherWindSpeedConformityAssessmentDto);
    //
    // // 結果を検証
    // assertNotNull(result);
    // assertFalse(result);
  }

  /** テスト用の設定クラス */
  @Configuration
  static class TestRestConfig extends TestConfig {

    @Bean
    @Qualifier("customRestTemplate")
    public RestTemplate restTemplate() {
      return new RestTemplate();
    }

    @Bean
    public WeatherWindSpeedLogic logic() {
      return new WeatherWindSpeedLogic();
    }

    @Bean
    public WebWeatherForecastTimeLogic WebWeatherForecastTime() {
      return new WebWeatherForecastTimeLogic();
    }

    @Bean
    public WebWeatherWindSpeedLogic WebWeatherWindSpeed() {
      return new WebWeatherWindSpeedLogic();
    }

    @Bean
    public WindSpeedRangeProcessingLogic WebWindSpeedRangeJudgment() {
      return new WindSpeedRangeProcessingLogic();
    }

    @Bean
    public Clock clock() {
      return Clock.systemUTC();
    }
  }
}
