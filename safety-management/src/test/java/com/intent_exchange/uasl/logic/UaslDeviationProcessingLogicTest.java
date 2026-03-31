package com.intent_exchange.uasl.logic;

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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.intent_exchange.uasl.config.TestConfig;
import com.intent_exchange.uasl.dto.request.UaslDesignAreaInfoDeviationDto;
/**
 * UaslDeviationProcessingLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class UaslDeviationProcessingLogicTest {

  // テスト用定義 リソース定義化されていないため、DroneLocationLogicから拝借
  /** 運航状況 DB登録値（航路侵入前） */
  private static final String OPERATIONAL_STATUS_APPROACH = "RouteApproach";
  /** 運航状況 DB登録値（運航中） */
  private static final String OPERATIONAL_STATUS_OPERATION = "NormalOperation";
  /** 運航状況 DB登録値（航路逸脱中） */
  private static final String OPERATIONAL_STATUS_DEVIATION = "RouteDeviation";

  /** 逸脱量計算用ロジッククラスのMock */
  @Mock
  private UaslDeviationCalculator uaslDeviationCalculator;

  /** テスト対象クラス */
  @InjectMocks
  private UaslDeviationProcessingLogic uaslDeviationProcessingLogic;

  @Test
  @DisplayName("逸脱情報リストがnull")
  void testGetGroupedByReservationIdMap1() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> deviations = null;

    // テスト対象メソッドを実行
    Map<String, List<UaslDesignAreaInfoDeviationDto>> resultMap =
        uaslDeviationProcessingLogic.getGroupedByReservationIdMap(deviations);

    // 結果の検証
    // 空のMapが返却される
    assertEquals(0, resultMap.size());
  }

  @Test
  @DisplayName("逸脱情報リストが空")
  void testGetGroupedByReservationIdMap2() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> deviations = new ArrayList<>();

    // テスト対象メソッドを実行
    Map<String, List<UaslDesignAreaInfoDeviationDto>> resultMap =
        uaslDeviationProcessingLogic.getGroupedByReservationIdMap(deviations);

    // 結果の検証
    // 空のMapが返却される
    assertEquals(0, resultMap.size());
  }

  @Test
  @DisplayName("1つの予約IDに対して複数のデータが存在する逸脱情報リストのグループ化")
  void testGetGroupedByReservationIdMap3() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> deviations = new ArrayList<>();
    UaslDesignAreaInfoDeviationDto deviation1_1 = createTestUaslDesignAreaInfoDeviationDto(
        "001", Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_APPROACH);
    UaslDesignAreaInfoDeviationDto deviation1_2 = createTestUaslDesignAreaInfoDeviationDto(
        "001", Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_OPERATION);
    UaslDesignAreaInfoDeviationDto deviation2_1 = createTestUaslDesignAreaInfoDeviationDto(
        "002", Timestamp.valueOf("2025-02-15 16:30:00"), OPERATIONAL_STATUS_APPROACH);
    UaslDesignAreaInfoDeviationDto deviation2_2 = createTestUaslDesignAreaInfoDeviationDto(
        "002", Timestamp.valueOf("2025-02-15 16:30:01"), OPERATIONAL_STATUS_OPERATION);
    UaslDesignAreaInfoDeviationDto deviation2_3 = createTestUaslDesignAreaInfoDeviationDto(
        "002", Timestamp.valueOf("2025-02-15 16:30:02"), OPERATIONAL_STATUS_DEVIATION);
    deviations.add(deviation1_1);
    deviations.add(deviation1_2);
    deviations.add(deviation2_1);
    deviations.add(deviation2_2);
    deviations.add(deviation2_3);

    // テスト対象メソッドを実行
    Map<String, List<UaslDesignAreaInfoDeviationDto>> resultMap =
        uaslDeviationProcessingLogic.getGroupedByReservationIdMap(deviations);

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
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    Double result = uaslDeviationProcessingLogic.getDeviationRate(reservationDeviations);

    // 結果の検証
    // 計算不可のため0.0が返却される
    assertEquals(0.0, result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空")
  void testGetDeviationRate2() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // テスト対象メソッドを実行
    Double result = uaslDeviationProcessingLogic.getDeviationRate(reservationDeviations);

    // 結果の検証
    // 計算不可のため0.0が返却される
    assertEquals(0.0, result);
  }

  @Test
  @DisplayName("逸脱量が割り切れない")
  void testGetDeviationRate3() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_APPROACH));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:02"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:03"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:04"), OPERATIONAL_STATUS_DEVIATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:05"), OPERATIONAL_STATUS_DEVIATION));

    // テスト対象メソッドを実行
    Double result = uaslDeviationProcessingLogic.getDeviationRate(reservationDeviations);

    // 結果の検証
    // (2 ÷ 6) × 100 = 33.3333... ≒ 33.33
    assertEquals(33.33, result);
  }

  @Test
  @DisplayName("逸脱量が割り切れる（50%逸脱）")
  void testGetDeviationRate4() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_APPROACH));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_DEVIATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:02"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:03"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:04"), OPERATIONAL_STATUS_DEVIATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:05"), OPERATIONAL_STATUS_DEVIATION));

    // テスト対象メソッドを実行
    Double result = uaslDeviationProcessingLogic.getDeviationRate(reservationDeviations);

    // 結果の検証
    // (3 ÷ 6) × 100 = 50.00
    assertEquals(50.00, result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnull")
  void testGetHorizontalPercentile1() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行されず、0.0が返却される
    verify(uaslDeviationCalculator, times(0)).calcPercentileStr(anyList());
    assertEquals("0.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空の場合")
  void testGetHorizontalPercentile2() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // 水平逸脱量のリストを作成
    List<Double> horizontalDeviations = reservationDeviations.stream()
        .map(UaslDesignAreaInfoDeviationDto::getHorizontalDeviation).collect(Collectors.toList());

    // モックの設定
    when(uaslDeviationCalculator.calcPercentileStr(horizontalDeviations)).thenReturn("0.0");

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行されず、0.0が返却される
    verify(uaslDeviationCalculator, times(1)).calcPercentileStr(anyList());
    assertEquals("0.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullまた空ではない場合")
  void testGetHorizontalPercentile3() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // 水平逸脱量のリストを作成
    List<Double> horizontalDeviations = reservationDeviations.stream()
        .map(UaslDesignAreaInfoDeviationDto::getHorizontalDeviation).collect(Collectors.toList());

    // モックの設定
    when(uaslDeviationCalculator.calcPercentileStr(horizontalDeviations)).thenReturn("15.0");

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行され、期待される値が返却される
    verify(uaslDeviationCalculator, times(1)).calcPercentileStr(horizontalDeviations);
    assertEquals("15.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストにPlannedRouteDeviationが含まれる場合")
  void testGetHorizontalPercentile_WithPlannedRouteDeviation() {
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    UaslDesignAreaInfoDeviationDto deviation1 = new UaslDesignAreaInfoDeviationDto();
    deviation1.setHorizontalDeviation(10.0);
    deviation1.setOperationalStatus(OPERATIONAL_STATUS_DEVIATION);
    reservationDeviations.add(deviation1);
    UaslDesignAreaInfoDeviationDto deviation2 = new UaslDesignAreaInfoDeviationDto();
    deviation2.setHorizontalDeviation(20.0);
    deviation2.setOperationalStatus("PlannedRouteDeviation");
    reservationDeviations.add(deviation2);

    when(uaslDeviationCalculator.calcPercentileStr(anyList())).thenReturn("5.0");

    String result = uaslDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<Double>> listCaptor = ArgumentCaptor.forClass(List.class);
    verify(uaslDeviationCalculator, times(1)).calcPercentileStr(listCaptor.capture());

    List<Double> capturedList = listCaptor.getValue();
    assertEquals(2, capturedList.size());
    assertEquals(10.0, capturedList.get(0));
    assertEquals(0.0, capturedList.get(1));
    assertEquals("5.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnull")
  void testGetVerticalPercentile1() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getVerticalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行されず、0.0が返却される
    verify(uaslDeviationCalculator, times(0)).calcPercentileStr(anyList());
    assertEquals("0.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空の場合")
  void testGetVerticalPercentile2() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // 水平逸脱量のリストを作成
    List<Double> verticalDeviations = reservationDeviations.stream()
        .map(UaslDesignAreaInfoDeviationDto::getVerticalDeviation).collect(Collectors.toList());

    // モックの設定
    when(uaslDeviationCalculator.calcPercentileStr(verticalDeviations)).thenReturn("0.0");

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getHorizontalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行されず、0.0が返却される
    verify(uaslDeviationCalculator, times(1)).calcPercentileStr(anyList());
    assertEquals("0.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullまた空ではない場合")
  void testGetVerticalPercentile3() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // 水平逸脱量のリストを作成
    List<Double> verticalDeviations = reservationDeviations.stream()
        .map(UaslDesignAreaInfoDeviationDto::getVerticalDeviation).collect(Collectors.toList());

    // モックの設定
    when(uaslDeviationCalculator.calcPercentileStr(verticalDeviations)).thenReturn("15.0");

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getVerticalPercentile(reservationDeviations);

    // 結果の検証
    // パーセンタイル算出処理が実行され、期待される値が返却される
    verify(uaslDeviationCalculator, times(1)).calcPercentileStr(verticalDeviations);
    assertEquals("15.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストにPlannedRouteDeviationが含まれる場合")
  void testGetVerticalPercentile_WithPlannedRouteDeviation() {
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    UaslDesignAreaInfoDeviationDto deviation1 = new UaslDesignAreaInfoDeviationDto();
    deviation1.setVerticalDeviation(30.0);
    deviation1.setOperationalStatus(OPERATIONAL_STATUS_DEVIATION);
    reservationDeviations.add(deviation1);
    UaslDesignAreaInfoDeviationDto deviation2 = new UaslDesignAreaInfoDeviationDto();
    deviation2.setVerticalDeviation(40.0);
    deviation2.setOperationalStatus("PlannedRouteDeviation");
    reservationDeviations.add(deviation2);

    when(uaslDeviationCalculator.calcPercentileStr(anyList())).thenReturn("15.0");

    String result = uaslDeviationProcessingLogic.getVerticalPercentile(reservationDeviations);

    @SuppressWarnings("unchecked")
    ArgumentCaptor<List<Double>> listCaptor = ArgumentCaptor.forClass(List.class);
    verify(uaslDeviationCalculator, times(1)).calcPercentileStr(listCaptor.capture());

    List<Double> capturedList = listCaptor.getValue();
    assertEquals(2, capturedList.size());
    assertEquals(30.0, capturedList.get(0));
    assertEquals(0.0, capturedList.get(1));
    assertEquals("15.0", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullの場合の逸脱検知時刻")
  void testGetJsonTimestamps1() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getJsonTimestamps(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知時刻が返却される
    assertEquals("{\"time\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空の場合の逸脱検知時刻")
  void testGetJsonTimestamps2() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getJsonTimestamps(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知時刻が返却される
    assertEquals("{\"time\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullまたは空ではない_operationalStatusにRouteDeviationが含まれない場合の逸脱検知時刻")
  void testGetJsonTimestamps3() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_APPROACH));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:02"), OPERATIONAL_STATUS_OPERATION));

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getJsonTimestamps(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知時刻が返却される
    assertEquals("{\"time\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullではない_operationalStatusにRouteDeviationが含まれる場合の逸脱検知時刻")
  void testGetJsonTimestamps4() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:00"), OPERATIONAL_STATUS_DEVIATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:01"), OPERATIONAL_STATUS_OPERATION));
    reservationDeviations.add(createTestUaslDesignAreaInfoDeviationDto("001",
        Timestamp.valueOf("2025-02-15 15:30:02"), OPERATIONAL_STATUS_DEVIATION));

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getJsonTimestamps(reservationDeviations);

    // 結果の検証
    // 逸脱検知時刻が返却される
    assertEquals("{\"time\":[\"2025-02-15 15:30:00.0\",\"2025-02-15 15:30:02.0\"]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullの場合の逸脱検知情報")
  void testGetJsonCoordinates1() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = null;

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getJsonCoordinates(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知情報が返却される
    assertEquals("{\"coordinates\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストが空の場合の逸脱検知情報")
  void testGetJsonCoordinates2() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getJsonCoordinates(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知情報が返却される
    assertEquals("{\"RouteDeviationDetectionInfo\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullまたは空ではない_operationalStatusにRouteDeviationが含まれない場合の逸脱検知情報")
  void testGetJsonCoordinates3() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    UaslDesignAreaInfoDeviationDto uaslDesignAreaInfoDeviationDto =
        new UaslDesignAreaInfoDeviationDto();
    uaslDesignAreaInfoDeviationDto.setLatitude(30.0);
    uaslDesignAreaInfoDeviationDto.setLongitude(134.0);
    uaslDesignAreaInfoDeviationDto.setAltitude(50.0);
    uaslDesignAreaInfoDeviationDto.setUaslSectionId("1");
    uaslDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_OPERATION);
    reservationDeviations.add(uaslDesignAreaInfoDeviationDto);

    uaslDesignAreaInfoDeviationDto = new UaslDesignAreaInfoDeviationDto();
    uaslDesignAreaInfoDeviationDto.setLatitude(35.0);
    uaslDesignAreaInfoDeviationDto.setLongitude(139.0);
    uaslDesignAreaInfoDeviationDto.setAltitude(100.0);
    uaslDesignAreaInfoDeviationDto.setUaslSectionId("2");
    uaslDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_APPROACH);

    uaslDesignAreaInfoDeviationDto = new UaslDesignAreaInfoDeviationDto();
    uaslDesignAreaInfoDeviationDto.setLatitude(40.0);
    uaslDesignAreaInfoDeviationDto.setLongitude(144.0);
    uaslDesignAreaInfoDeviationDto.setAltitude(150.0);
    uaslDesignAreaInfoDeviationDto.setUaslSectionId("2");
    uaslDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_APPROACH);
    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getJsonCoordinates(reservationDeviations);

    // 結果の検証
    // 空の逸脱検知情報が返却される
    assertEquals("{\"RouteDeviationDetectionInfo\":[]}", result);
  }

  @Test
  @DisplayName("予約IDごとの逸脱情報リストがnullではない_operationalStatusにRouteDeviationが含まれる場合の逸脱検知情報")
  void testGetJsonCoordinates4() {
    // 逸脱情報リスト
    List<UaslDesignAreaInfoDeviationDto> reservationDeviations = new ArrayList<>();
    UaslDesignAreaInfoDeviationDto uaslDesignAreaInfoDeviationDto =
        new UaslDesignAreaInfoDeviationDto();
    uaslDesignAreaInfoDeviationDto.setLatitude(30.0);
    uaslDesignAreaInfoDeviationDto.setLongitude(134.0);
    uaslDesignAreaInfoDeviationDto.setAltitude(50.0);
    uaslDesignAreaInfoDeviationDto.setUaslSectionId("1");
    uaslDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_DEVIATION);
    reservationDeviations.add(uaslDesignAreaInfoDeviationDto);

    uaslDesignAreaInfoDeviationDto = new UaslDesignAreaInfoDeviationDto();
    uaslDesignAreaInfoDeviationDto.setLatitude(35.0);
    uaslDesignAreaInfoDeviationDto.setLongitude(139.0);
    uaslDesignAreaInfoDeviationDto.setAltitude(100.0);
    uaslDesignAreaInfoDeviationDto.setUaslSectionId("2");
    uaslDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_OPERATION);

    uaslDesignAreaInfoDeviationDto = new UaslDesignAreaInfoDeviationDto();
    uaslDesignAreaInfoDeviationDto.setLatitude(40.0);
    uaslDesignAreaInfoDeviationDto.setLongitude(144.0);
    uaslDesignAreaInfoDeviationDto.setAltitude(150.0);
    uaslDesignAreaInfoDeviationDto.setUaslSectionId("2");
    uaslDesignAreaInfoDeviationDto.setOperationalStatus(OPERATIONAL_STATUS_APPROACH);

    // テスト対象メソッドを実行
    String result = uaslDeviationProcessingLogic.getJsonCoordinates(reservationDeviations);

    // 結果の検証
    // 逸脱検知情報が返却される
    assertEquals("{\"RouteDeviationDetectionInfo\":[{\"uaslSectionId\":\"1\",\"coordinates\":{\"latitude\":30.0,\"longitude\":134.0,\"altitude\":50.0}}]}",result);
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
  private UaslDesignAreaInfoDeviationDto createTestUaslDesignAreaInfoDeviationDto(
      String reservationId, Timestamp getLocationTimestamp, String operationalStatus) {
    UaslDesignAreaInfoDeviationDto dto = new UaslDesignAreaInfoDeviationDto();
    dto.setReservationId(reservationId);
    dto.setOperationalStatus(operationalStatus);
    dto.setGetLocationTimestamp(getLocationTimestamp);
    return dto;
  }

}
