package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import com.intent_exchange.drone_highway.config.TestConfig;
import com.intent_exchange.drone_highway.dto.request.AirwayDesignAreaInfoDeviationDto;
/**
 * AirwayDeviationProcessingLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class AirwayDeviationProcessingLogicTest {

  // テスト用定義 リソース定義化されていないため、DroneLocationLogicから拝借
  /** 運航状況 DB登録値（航路侵入前） */
  private static final String OPERATIONAL_STATUS_APPROACH = "RouteApproach";
  /** 運航状況 DB登録値（運航中） */
  private static final String OPERATIONAL_STATUS_OPERATION = "NormalOperation";
  /** 運航状況 DB登録値（航路逸脱中） */
  private static final String OPERATIONAL_STATUS_DEVIATION = "RouteDeviation";

  /** 逸脱量計算用ロジッククラスのMock */
  @Mock
  private AirwayDeviationCalculator airwayDeviationCalculator;

  /** テスト対象クラス */
  @InjectMocks
  private AirwayDeviationProcessingLogic airwayDeviationProcessingLogic;

  @Test
  @DisplayName("逸脱情報リストがnull")
  void testGetGroupedByReservationIdMap1() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> deviations = null;

    // テスト対象メソッドを実行
    Map<String, List<AirwayDesignAreaInfoDeviationDto>> resultMap =
        airwayDeviationProcessingLogic.getGroupedByReservationIdMap(deviations);

    // 結果の検証
    // 空のMapが返却される
    assertEquals(0, resultMap.size());
  }

  @Test
  @DisplayName("逸脱情報リストが空")
  void testGetGroupedByReservationIdMap2() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> deviations = new ArrayList<>();

    // テスト対象メソッドを実行
    Map<String, List<AirwayDesignAreaInfoDeviationDto>> resultMap =
        airwayDeviationProcessingLogic.getGroupedByReservationIdMap(deviations);

    // 結果の検証
    // 空のMapが返却される
    assertEquals(0, resultMap.size());
  }

  @Test
  @DisplayName("1つの予約IDに対して複数のデータが存在する逸脱情報リストのグループ化")
  void testGetGroupedByReservationIdMap3() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> deviations = new ArrayList<>();
    AirwayDesignAreaInfoDeviationDto deviation1_1 = createTestAirwayDesignAreaInfoDeviationDto(
        "001", Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_APPROACH);
    AirwayDesignAreaInfoDeviationDto deviation1_2 = createTestAirwayDesignAreaInfoDeviationDto(
        "001", Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_OPERATION);
    AirwayDesignAreaInfoDeviationDto deviation2_1 = createTestAirwayDesignAreaInfoDeviationDto(
        "002", Timestamp.valueOf("2025-02-15 16:30:00"), OPERATIONAL_STATUS_APPROACH);
    AirwayDesignAreaInfoDeviationDto deviation2_2 = createTestAirwayDesignAreaInfoDeviationDto(
        "002", Timestamp.valueOf("2025-02-15 16:30:01"), OPERATIONAL_STATUS_OPERATION);
    AirwayDesignAreaInfoDeviationDto deviation2_3 = createTestAirwayDesignAreaInfoDeviationDto(
        "002", Timestamp.valueOf("2025-02-15 16:30:02"), OPERATIONAL_STATUS_DEVIATION);
    deviations.add(deviation1_1);
    deviations.add(deviation1_2);
    deviations.add(deviation2_1);
    deviations.add(deviation2_2);
    deviations.add(deviation2_3);

    // テスト対象メソッドを実行
    Map<String, List<AirwayDesignAreaInfoDeviationDto>> resultMap =
        airwayDeviationProcessingLogic.getGroupedByReservationIdMap(deviations);

    // 結果の検証
    // 予約IDごとにグループ化されたMapが返却される
    assertEquals(2, resultMap.size());
    // 予約ID"001"のデータ
    assertEquals(2, resultMap.get("001").size());
    assertEquals(deviation1_1, resultMap.get("001").get(0));
    assertEquals(deviation1_2, resultMap.get("001").get(1));
    // 予約ID"002"のデータ
    assertEquals(3, resultMap.get("002").size());
    assertEquals(deviation2_1, resultMap.get("002").get(0));
    assertEquals(deviation2_2, resultMap.get("002").get(1));
    assertEquals(deviation2_3, resultMap.get("002").get(2));
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnull")
  void testGetDeviationRate1() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    Double result = airwayDeviationProcessingLogic.getDeviationRate(reservationDeviations);

    // 結果の検証
    // 計算不可のため0.0が返却される
    assertEquals(0.0, result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空")
  void testGetDeviationRate2() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // テスト対象メソッドを実行
    Double result = airwayDeviationProcessingLogic.getDeviationRate(reservationDeviations);

    // 結果の検証
    // 計算不可のため0.0が返却される
    assertEquals(0.0, result);
  }

  @Test
  @DisplayName("逸脱量が割り切れない")
  void testGetDeviationRate3() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_APPROACH));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:02"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:03"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:04"), OPERATIONAL_STATUS_DEVIATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:05"), OPERATIONAL_STATUS_DEVIATION));

    // テスト対象メソッドを実行
    Double result = airwayDeviationProcessingLogic.getDeviationRate(reservationDeviations);

    // 結果の検証
    // (2 ÷ 6) × 100 = 33.3333... ≒ 33.33
    assertEquals(33.33, result);
  }

  @Test
  @DisplayName("逸脱量が割り切れる（50%逸脱）")
  void testGetDeviationRate4() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_APPROACH));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_DEVIATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:02"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:03"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:04"), OPERATIONAL_STATUS_DEVIATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:05"), OPERATIONAL_STATUS_DEVIATION));

    // テスト対象メソッドを実行
    Double result = airwayDeviationProcessingLogic.getDeviationRate(reservationDeviations);

    // 結果の検証
    // (3 ÷ 6) × 100 = 50.00
    assertEquals(50.00, result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnull")
  void testGetHorizontalPercentile1() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行されず、0.0が返却される
    verify(airwayDeviationCalculator, times(0)).calcPercentileStr(anyList());
    assertEquals("0.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空の場合")
  void testGetHorizontalPercentile2() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // 水平逸脱量のリストを作成
    List<Double> horizontalDeviations = reservationDeviations.stream()
        .map(AirwayDesignAreaInfoDeviationDto::getHorizontalDeviation).collect(Collectors.toList());

    // モックの設定
    when(airwayDeviationCalculator.calcPercentileStr(horizontalDeviations)).thenReturn("0.0");

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行されず、0.0が返却される
    verify(airwayDeviationCalculator, times(1)).calcPercentileStr(anyList());
    assertEquals("0.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullまた空ではない場合")
  void testGetHorizontalPercentile3() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // 水平逸脱量のリストを作成
    List<Double> horizontalDeviations = reservationDeviations.stream()
        .map(AirwayDesignAreaInfoDeviationDto::getHorizontalDeviation).collect(Collectors.toList());

    // モックの設定
    when(airwayDeviationCalculator.calcPercentileStr(horizontalDeviations)).thenReturn("15.0");

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行され、期待される値が返却される
    verify(airwayDeviationCalculator, times(1)).calcPercentileStr(horizontalDeviations);
    assertEquals("15.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnull")
  void testGetVerticalPercentile1() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getVerticalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行されず、0.0が返却される
    verify(airwayDeviationCalculator, times(0)).calcPercentileStr(anyList());
    assertEquals("0.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空の場合")
  void testGetVerticalPercentile2() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // 水平逸脱量のリストを作成
    List<Double> verticalDeviations = reservationDeviations.stream()
        .map(AirwayDesignAreaInfoDeviationDto::getVerticalDeviation).collect(Collectors.toList());

    // モックの設定
    when(airwayDeviationCalculator.calcPercentileStr(verticalDeviations)).thenReturn("0.0");

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行されず、0.0が返却される
    verify(airwayDeviationCalculator, times(1)).calcPercentileStr(anyList());
    assertEquals("0.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullまた空ではない場合")
  void testGetVerticalPercentile3() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // 水平逸脱量のリストを作成
    List<Double> verticalDeviations = reservationDeviations.stream()
        .map(AirwayDesignAreaInfoDeviationDto::getVerticalDeviation).collect(Collectors.toList());

    // モックの設定
    when(airwayDeviationCalculator.calcPercentileStr(verticalDeviations)).thenReturn("15.0");

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getVerticalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行され、期待される値が返却される
    verify(airwayDeviationCalculator, times(1)).calcPercentileStr(verticalDeviations);
    assertEquals("15.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullの場合の逸脱検知時刻")
  void testGetJsonTimestamps1() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getJsonTimestamps(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知時刻が返却される
    assertEquals("{\"time\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空の場合の逸脱検知時刻")
  void testGetJsonTimestamps2() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getJsonTimestamps(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知時刻が返却される
    assertEquals("{\"time\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullまたは空ではない_operationalStatusにRouteDeviationが含まれない場合の逸脱検知時刻")
  void testGetJsonTimestamps3() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_APPROACH));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:02"), OPERATIONAL_STATUS_OPERATION));

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getJsonTimestamps(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知時刻が返却される
    assertEquals("{\"time\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullではない_operationalStatusにRouteDeviationが含まれる場合の逸脱検知時刻")
  void testGetJsonTimestamps4() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_DEVIATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestAirwayDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:02"), OPERATIONAL_STATUS_DEVIATION));

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getJsonTimestamps(reservationDeviations);

    // 結果の検証
    // 逸脱検知時刻が返却される
    assertEquals("{\"time\":[\"2025-02-15 15:30:00.0\",\"2025-02-15 15:30:02.0\"]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullの場合の逸脱検知情報")
  void testGetJsonCoordinates1() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getJsonCoordinates(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知情報が返却される
    assertEquals("{\"coordinates\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空の場合の逸脱検知情報")
  void testGetJsonCoordinates2() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getJsonCoordinates(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知情報が返却される
    assertEquals("{\"RouteDeviationDetectionInfo\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullまたは空ではない_operationalStatusにRouteDeviationが含まれない場合の逸脱検知情報")
  void testGetJsonCoordinates3() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    AirwayDesignAreaInfoDeviationDto airwayDesignAreaInfoDeviationDto =
        new AirwayDesignAreaInfoDeviationDto();
    airwayDesignAreaInfoDeviationDto.setLatitude(30.0);
    airwayDesignAreaInfoDeviationDto.setLongitude(134.0);
    airwayDesignAreaInfoDeviationDto.setAboveGroundLevel(50.0);
    airwayDesignAreaInfoDeviationDto.setAirwaySectionId("1");
    airwayDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_OPERATION);
    reservationDeviations.add(airwayDesignAreaInfoDeviationDto);

    airwayDesignAreaInfoDeviationDto = new AirwayDesignAreaInfoDeviationDto();
    airwayDesignAreaInfoDeviationDto.setLatitude(35.0);
    airwayDesignAreaInfoDeviationDto.setLongitude(139.0);
    airwayDesignAreaInfoDeviationDto.setAboveGroundLevel(100.0);
    airwayDesignAreaInfoDeviationDto.setAirwaySectionId("2");
    airwayDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_APPROACH);

    airwayDesignAreaInfoDeviationDto = new AirwayDesignAreaInfoDeviationDto();
    airwayDesignAreaInfoDeviationDto.setLatitude(40.0);
    airwayDesignAreaInfoDeviationDto.setLongitude(144.0);
    airwayDesignAreaInfoDeviationDto.setAboveGroundLevel(150.0);
    airwayDesignAreaInfoDeviationDto.setAirwaySectionId("2");
    airwayDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_APPROACH);
    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getJsonCoordinates(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知情報が返却される
    assertEquals("{\"RouteDeviationDetectionInfo\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullではない_operationalStatusにRouteDeviationが含まれる場合の逸脱検知情報")
  void testGetJsonCoordinates4() {
    // 逸脱情報リスト
    List<AirwayDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    AirwayDesignAreaInfoDeviationDto airwayDesignAreaInfoDeviationDto =
        new AirwayDesignAreaInfoDeviationDto();
    airwayDesignAreaInfoDeviationDto.setLatitude(30.0);
    airwayDesignAreaInfoDeviationDto.setLongitude(134.0);
    airwayDesignAreaInfoDeviationDto.setAboveGroundLevel(50.0);
    airwayDesignAreaInfoDeviationDto.setAirwaySectionId("1");
    airwayDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_DEVIATION);
    reservationDeviations.add(airwayDesignAreaInfoDeviationDto);

    airwayDesignAreaInfoDeviationDto = new AirwayDesignAreaInfoDeviationDto();
    airwayDesignAreaInfoDeviationDto.setLatitude(35.0);
    airwayDesignAreaInfoDeviationDto.setLongitude(139.0);
    airwayDesignAreaInfoDeviationDto.setAboveGroundLevel(100.0);
    airwayDesignAreaInfoDeviationDto.setAirwaySectionId("2");
    airwayDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_OPERATION);

    airwayDesignAreaInfoDeviationDto = new AirwayDesignAreaInfoDeviationDto();
    airwayDesignAreaInfoDeviationDto.setLatitude(40.0);
    airwayDesignAreaInfoDeviationDto.setLongitude(144.0);
    airwayDesignAreaInfoDeviationDto.setAboveGroundLevel(150.0);
    airwayDesignAreaInfoDeviationDto.setAirwaySectionId("2");
    airwayDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_APPROACH);

    // テスト対象メソッドを実行
    String result = airwayDeviationProcessingLogic.getJsonCoordinates(reservationDeviations);

    // 結果の検証
    // 逸脱検知情報が返却される
    assertEquals("{\"RouteDeviationDetectionInfo\":[{\"airwaySectionId\":\"1\",\"coordinates\":{\"latitude\":30.0,\"longitude\":134.0,\"aboveGroundLevel\":50.0}}]}",result);
  }

  // 以下はテスト用データ生成用メソッド

  /**
   * 引数に指定した内容で、航路逸脱情報のテストデータを生成する。
   * 
   * @param reservationId 予約ID
   * @param getLocationTimestamp テレメトリ情報取得日時
   * @param operationalStatus 運航状況
   * @return 航路逸脱情報のテストデータ
   */
  private AirwayDesignAreaInfoDeviationDto createTestAirwayDesignAreaInfoDeviationDto(
      String reservationId, Timestamp getLocationTimestamp, String operationalStatus) {
    AirwayDesignAreaInfoDeviationDto dto = new AirwayDesignAreaInfoDeviationDto();
    dto.setReservationId(reservationId);
    dto.setOperationalStatus(operationalStatus);
    dto.setGetLocationTimestamp(getLocationTimestamp);
    return dto;
  }

}
