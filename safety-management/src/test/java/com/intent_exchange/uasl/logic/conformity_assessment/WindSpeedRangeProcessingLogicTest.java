package com.intent_exchange.uasl.logic.conformity_assessment;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import com.intent_exchange.uasl.dto.request.WeatherWindSpeedConformityAssessmentDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeListDto;
import com.intent_exchange.uasl.dto.response.WeatherForecastTimeListResponseDto;
import com.intent_exchange.uasl.dto.response.WeatherWindSpeedsDto;
import com.intent_exchange.uasl.exception.WeatherWindSpeedException;

/**
 * WindSpeedRangeProcessingLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)//clockが不要のテストも含まれていても、エラーが発生しないように設定
class WindSpeedRangeProcessingLogicTest {

  @Mock
  private Clock clock;

  @InjectMocks
  private WindSpeedRangeProcessingLogic windSpeedRangeProcessingLogic;

  @BeforeEach
  void setUp() {
    // モックされた 現在時刻のClock を設定
    Clock fixedClock = Clock.fixed(Instant.parse("2024-12-05T01:30:00Z"), ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(fixedClock.instant());
    when(clock.getZone()).thenReturn(fixedClock.getZone());
  }

  @Test
  @DisplayName("風速が条件範囲内_風速が条件範囲内である場合")
  void testGetWindSpeedRangeJudgment1() {
    // モックされたデータを作成
    WeatherWindSpeedsDto ugrdDto = new WeatherWindSpeedsDto();
    ugrdDto.setContents(List.of(2.558, 2.569, 2.581, 2.730, 2.735, 2.741, 2.742, 2.743, 2.744));

    WeatherWindSpeedsDto vgrdDto = new WeatherWindSpeedsDto();
    vgrdDto.setContents(List.of(4.0, 3.569, 3.581, 3.730, 3.735, 3.741, 3.742, 3.743, 3.744));

    WeatherWindSpeedConformityAssessmentDto dto = new WeatherWindSpeedConformityAssessmentDto();
    dto.setWindSpeedRange(4.8);// 風速のmax値は: sqrt(2.744^2 + 3.744^2) = 4.754

    // メソッドの呼び出し
    Boolean result =
        windSpeedRangeProcessingLogic.getWindSpeedRangeJudgment(List.of(ugrdDto, vgrdDto), dto);

    // 結果の検証
    assertTrue(result);
  }

  @Test
  @DisplayName("風速条件範囲内判定_風速が条件範囲外である場合")
  void testGetWindSpeedRangeJudgment2() {
    /// モックされたデータを作成
    WeatherWindSpeedsDto ugrdDto = new WeatherWindSpeedsDto();
    ugrdDto.setContents(List.of(2.558, 2.569, 2.581, 2.730, 2.735, 2.741, 2.742, 2.743, 2.744));

    WeatherWindSpeedsDto vgrdDto = new WeatherWindSpeedsDto();
    vgrdDto.setContents(List.of(4.0, 3.569, 3.581, 3.730, 3.735, 3.741, 3.742, 3.743, 3.744));

    WeatherWindSpeedConformityAssessmentDto dto = new WeatherWindSpeedConformityAssessmentDto();
    dto.setWindSpeedRange(4.6);// 風速のmax値は: sqrt(2.744^2 + 3.744^2) = 4.754

    // メソッドの呼び出し
    Boolean result =
        windSpeedRangeProcessingLogic.getWindSpeedRangeJudgment(List.of(ugrdDto, vgrdDto), dto);

    // 結果の検証
    assertFalse(result);
  }

  @Test
  @DisplayName("予約の開始日時が現在の時刻プラス10時間以内に含まれない場合")
  void testIsReservationTimeValid1() {

    // モックされたデータを作成
    WeatherWindSpeedConformityAssessmentDto dto = new WeatherWindSpeedConformityAssessmentDto();
    dto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 31));// 現在の時刻プラス10時間を超える値

    // メソッドの呼び出し
    boolean result = windSpeedRangeProcessingLogic.isReservationTimeValid(dto);

    // 結果の検証
    assertTrue(result);
  }

  @Test
  @DisplayName("予約の開始日時が現在の時刻プラス10時間以内に含まれる場合")
  void testIsReservationTimeValid2() {

    // モックされたデータを作成
    WeatherWindSpeedConformityAssessmentDto dto = new WeatherWindSpeedConformityAssessmentDto();
    dto.setStartAt(LocalDateTime.of(2024, 12, 5, 11, 29));// 現在の時刻プラス10時間以内の値

    // メソッドの呼び出し
    boolean result = windSpeedRangeProcessingLogic.isReservationTimeValid(dto);

    // 結果の検証
    assertFalse(result);
  }

  @Test
  @DisplayName("気象予測情報のリストから最初の気象予測情報を取得する")
  void testGetFirstForecastTime1() {
    // index.get(0)のデータを作成
    List<WeatherForecastTimeListDto> forecastTimeList = new ArrayList<WeatherForecastTimeListDto>();
    WeatherForecastTimeListDto timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("2024-12-05T01:00:00Z");
    timeListDto.setValidtime(List.of("2024-12-05T01:00:00Z", "2024-12-05T02:00:00Z"));
    forecastTimeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("2024-12-05T02:00:00Z");
    timeListDto.setValidtime(List.of("2024-12-05T00:00:00Z", "2024-12-05T01:00:00Z"));
    forecastTimeList.add(timeListDto);

    WeatherForecastTimeDto forecastTimeDto1 = new WeatherForecastTimeDto();
    forecastTimeDto1.setElement("ugrd,vgrd");
    forecastTimeDto1.setMessage("Success");
    forecastTimeDto1.setTimeList(forecastTimeList);

    // index.get(1)のデータを作成
    forecastTimeList = new ArrayList<WeatherForecastTimeListDto>();
    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("2024-12-05T01:00:00Z");
    timeListDto.setValidtime(List.of("2024-12-05T01:00:00Z", "2024-12-05T02:00:00Z"));
    forecastTimeList.add(timeListDto);

    timeListDto = new WeatherForecastTimeListDto();
    timeListDto.setBasetime("2024-12-05T02:00:00Z");
    timeListDto.setValidtime(List.of("2024-12-05T00:00:00Z", "2024-12-05T01:00:00Z"));
    forecastTimeList.add(timeListDto);

    WeatherForecastTimeDto forecastTimeDto2 = new WeatherForecastTimeDto();
    forecastTimeDto1.setElement("ugrd,vgrd");
    forecastTimeDto1.setMessage("Success");
    forecastTimeDto1.setTimeList(forecastTimeList);

    WeatherForecastTimeListResponseDto response = new WeatherForecastTimeListResponseDto();
    response.setIndex(List.of(forecastTimeDto1, forecastTimeDto2));

    // メソッドの呼び出し
    WeatherForecastTimeDto result = windSpeedRangeProcessingLogic.getFirstForecastTime(response);

    // 結果の検証
    assertNotNull(result);
    assertTrue(result.equals(forecastTimeDto1));
  }

  @Test
  @DisplayName("気象予測情報のリストが空の場合に例外をスローする")
  void testGetFirstForecastTime2() {
    // モックされたデータを作成
    WeatherForecastTimeListResponseDto response = new WeatherForecastTimeListResponseDto();
    response.setIndex(List.of());

    // メソッドの呼び出しと結果の検証
    assertThrows(WeatherWindSpeedException.class, () -> {
      windSpeedRangeProcessingLogic.getFirstForecastTime(response);
    });
  }
}
