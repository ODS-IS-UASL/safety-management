package com.intent_exchange.uasl.logic.conformity_assessment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.RailwayOperationInformationRequestDto;
import com.intent_exchange.uasl.dto.response.RailwayOperationInformationResponseDto;
import com.intent_exchange.uasl.logic.web.WebRailwayOperationLogic;
import com.intent_exchange.uasl.util.PropertyUtil;

@ExtendWith(MockitoExtension.class)
public class RailwayOperationInformationEvaluationLogicTest {

  @Mock
  private WebRailwayOperationLogic logic;

  @InjectMocks
  private RailwayOperationInformationEvaluationLogic evaluationLogic;

  @Mock
  private Clock clock;

  @Test
  @DisplayName("鉄道運行情報の適合性評価 鉄道の立ち入りあり")
  void testCheck1() {
    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 10, 11, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    // テストデータの準備
    RailwayOperationInformationRequestDto requestDto = new RailwayOperationInformationRequestDto();
    requestDto.setRestricted(true);
    requestDto.setStartAt(LocalDateTime.of(2024, 12, 10, 1, 0));
    requestDto.setEndAt(LocalDateTime.of(2024, 12, 10, 2, 0));
    requestDto.setRailwayCrossingInfo(
        "[{\"uasl_id\":\"1-2-2\",\"station1\":\"秩父\",\"station2\":\"御花畑\",\"relative_value\":\"0.8\",\"created_at\":\"2025-12-01T09:00:00\",\"updated_at\":\"2025-12-01T09:00:00\"}]");


    RailwayOperationInformationResponseDto responseDto =
        new RailwayOperationInformationResponseDto();
    responseDto.setRegularDiagram(Arrays.asList(Arrays.asList("09:00", "09:30"),
        Arrays.asList("10:30", "11:00"), Arrays.asList("11:30", "12:00")));

    when(logic.getRailwayOperationInformation(anyString(), anyString(), any(ZonedDateTime.class),
        any(ZonedDateTime.class))).thenReturn(responseDto);

    try (MockedStatic<PropertyUtil> mockedPropertyUtil = mockStatic(PropertyUtil.class)) {
      mockedPropertyUtil
          .when(() -> PropertyUtil.getPropertyDecimal("railway.operation.buffer.time"))
          .thenReturn(new BigDecimal("15.0"));
    }

    // メソッドの呼び出し
    Boolean result = evaluationLogic.check(requestDto);

    // 結果の検証
    assertNotNull(result);
    assertFalse(result);
  }

  @Test
  @DisplayName("鉄道運行情報の適合性評価 鉄道の立ち入りあり②")
  void testCheck1_2() {
    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 10, 11, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    // テストデータの準備
    RailwayOperationInformationRequestDto requestDto = new RailwayOperationInformationRequestDto();
    requestDto.setRestricted(true);
    requestDto.setStartAt(LocalDateTime.of(2024, 12, 10, 0, 0));
    requestDto.setEndAt(LocalDateTime.of(2024, 12, 10, 2, 0));
    requestDto.setRailwayCrossingInfo(
        "[{\"uasl_id\":\"1-2-2\",\"station1\":\"秩父\",\"station2\":\"御花畑\",\"relative_value\":\"0.8\",\"created_at\":\"2025-12-01T09:00:00\",\"updated_at\":\"2025-12-01T09:00:00\"}]");


    RailwayOperationInformationResponseDto responseDto =
        new RailwayOperationInformationResponseDto();
    responseDto.setRegularDiagram(Arrays.asList(Arrays.asList("11:00", "12:00"),
        Arrays.asList("10:30", "11:00"), Arrays.asList("11:30", "12:00")));

    when(logic.getRailwayOperationInformation(anyString(), anyString(), any(ZonedDateTime.class),
        any(ZonedDateTime.class))).thenReturn(responseDto);

    try (MockedStatic<PropertyUtil> mockedPropertyUtil = mockStatic(PropertyUtil.class)) {
      mockedPropertyUtil
          .when(() -> PropertyUtil.getPropertyDecimal("railway.operation.buffer.time"))
          .thenReturn(new BigDecimal("15.0"));
    }

    // メソッドの呼び出し
    Boolean result = evaluationLogic.check(requestDto);

    // 結果の検証
    assertNotNull(result);
    assertFalse(result);
  }


  @Test
  @DisplayName("鉄道運行情報の適合性評価 鉄道の立ち入りなし(予約に近い列車時間がない)")
  void testCheck2() {
    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 10, 11, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    // テストデータの準備
    RailwayOperationInformationRequestDto requestDto = new RailwayOperationInformationRequestDto();
    requestDto.setRestricted(true);
    requestDto.setStartAt(LocalDateTime.of(2024, 12, 10, 13, 0));
    requestDto.setEndAt(LocalDateTime.of(2024, 12, 10, 14, 0));
    requestDto.setRailwayCrossingInfo(
        "[{\"uasl_id\":\"1-2-2\",\"station1\":\"秩父\",\"station2\":\"御花畑\",\"relative_value\":\"0.8\",\"created_at\":\"2025-12-01T09:00:00\",\"updated_at\":\"2025-12-01T09:00:00\"}]");

    RailwayOperationInformationResponseDto responseDto =
        new RailwayOperationInformationResponseDto();
    responseDto.setRegularDiagram(Arrays.asList(Arrays.asList("09:00", "09:30"),
        Arrays.asList("10:30", "11:00"), Arrays.asList("11:30", "12:00")));

    when(logic.getRailwayOperationInformation(anyString(), anyString(), any(ZonedDateTime.class),
        any(ZonedDateTime.class))).thenReturn(responseDto);

    try (MockedStatic<PropertyUtil> mockedPropertyUtil = mockStatic(PropertyUtil.class)) {
      mockedPropertyUtil
          .when(() -> PropertyUtil.getPropertyDecimal("railway.operation.buffer.time"))
          .thenReturn(new BigDecimal("15.0"));
    }

    // メソッドの呼び出し
    Boolean result = evaluationLogic.check(requestDto);

    // 結果の検証
    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  @DisplayName("鉄道運行情報の適合性評価 鉄道の立ち入りなし(予約に近い列車時間があり)")
  void testCheck3() {
    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 10, 11, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    // テストデータの準備
    RailwayOperationInformationRequestDto requestDto = new RailwayOperationInformationRequestDto();
    requestDto.setRestricted(true);
    requestDto.setStartAt(LocalDateTime.of(2024, 12, 10, 13, 0));
    requestDto.setEndAt(LocalDateTime.of(2024, 12, 10, 14, 0));
    requestDto.setRailwayCrossingInfo(
        "[{\"uasl_id\":\"1-2-2\",\"station1\":\"秩父\",\"station2\":\"御花畑\",\"relative_value\":\"0.9\",\"created_at\":\"2025-12-01T09:00:00\",\"updated_at\":\"2025-12-01T09:00:00\"}]");

    RailwayOperationInformationResponseDto responseDto =
        new RailwayOperationInformationResponseDto();
    responseDto.setRegularDiagram(Arrays.asList(Arrays.asList("09:00", "09:30"),
        Arrays.asList("10:30", "11:00"), Arrays.asList("11:30", "12:46")));

    when(logic.getRailwayOperationInformation(anyString(), anyString(), any(ZonedDateTime.class),
        any(ZonedDateTime.class))).thenReturn(responseDto);

    try (MockedStatic<PropertyUtil> mockedPropertyUtil = mockStatic(PropertyUtil.class)) {
      mockedPropertyUtil
          .when(() -> PropertyUtil.getPropertyDecimal("railway.operation.buffer.time"))
          .thenReturn(new BigDecimal("15.0"));
    }

    // メソッドの呼び出し
    Boolean result = evaluationLogic.check(requestDto);

    // 結果の検証
    assertNotNull(result);
    assertTrue(result);
  }

  @Test
  @DisplayName("鉄道運行情報の適合性評価 鉄道との交点情報なし(空文字)")
  void testCheck4() {

    // テストデータの準備
    RailwayOperationInformationRequestDto requestDto = new RailwayOperationInformationRequestDto();
    requestDto.setRestricted(true);
    requestDto.setStartAt(LocalDateTime.of(2024, 12, 10, 10, 0));
    requestDto.setEndAt(LocalDateTime.of(2024, 12, 10, 12, 0));
    requestDto.setRailwayCrossingInfo("[null]");

    // メソッドの呼び出し
    Boolean result = evaluationLogic.check(requestDto);

    // 結果の検証
    assertNotNull(result);
    assertTrue(result);

  }

  @Test
  @DisplayName("鉄道運行情報の適合性評価 鉄道との交点情報なし(空文字)②")
  void testCheck4_2() {

    // テストデータの準備
    RailwayOperationInformationRequestDto requestDto = new RailwayOperationInformationRequestDto();
    requestDto.setRestricted(true);
    requestDto.setStartAt(LocalDateTime.of(2024, 12, 10, 10, 0));
    requestDto.setEndAt(LocalDateTime.of(2024, 12, 10, 12, 0));
    requestDto.setRailwayCrossingInfo(" ");

    // メソッドの呼び出し
    Boolean result = evaluationLogic.check(requestDto);

    // 結果の検証
    assertNotNull(result);
    assertTrue(result);

  }

  @Test
  @DisplayName("鉄道運行情報の適合性評価 鉄道との交点情報なし(null)")
  void testCheck5() {

    // テストデータの準備
    RailwayOperationInformationRequestDto requestDto = new RailwayOperationInformationRequestDto();
    requestDto.setRestricted(true);
    requestDto.setStartAt(LocalDateTime.of(2024, 12, 10, 10, 0));
    requestDto.setEndAt(LocalDateTime.of(2024, 12, 10, 12, 0));
    requestDto.setRailwayCrossingInfo(null);

    // メソッドの呼び出し
    Boolean result = evaluationLogic.check(requestDto);

    // 結果の検証
    assertNotNull(result);
    assertTrue(result);

  }

  @Test
  @DisplayName("鉄道運行情報の適合性評価 初回実行フラグがfalse")
  void testCheck6() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 9, 11, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    // テストデータの準備
    RailwayOperationInformationRequestDto requestDto = new RailwayOperationInformationRequestDto();
    requestDto.setRestricted(false);
    requestDto.setStartAt(LocalDateTime.of(2024, 12, 10, 1, 0));
    requestDto.setEndAt(LocalDateTime.of(2024, 12, 10, 3, 0));
    requestDto.setRailwayCrossingInfo(
        "[{\"uasl_id\":\"1-2-2\",\"station1\":\"秩父\",\"station2\":\"御花畑\",\"relative_value\":\"0.8\",\"created_at\":\"2025-12-01T09:00:00\",\"updated_at\":\"2025-12-01T09:00:00\"}]");


    RailwayOperationInformationResponseDto responseDto =
        new RailwayOperationInformationResponseDto();
    responseDto.setRegularDiagram(Arrays.asList(Arrays.asList("09:00", "09:30"),
        Arrays.asList("10:30", "11:00"), Arrays.asList("11:30", "12:00")));

    when(logic.getRailwayOperationInformation(anyString(), anyString(), any(ZonedDateTime.class),
        any(ZonedDateTime.class))).thenReturn(responseDto);

    try (MockedStatic<PropertyUtil> mockedPropertyUtil = mockStatic(PropertyUtil.class)) {
      mockedPropertyUtil
          .when(() -> PropertyUtil.getPropertyDecimal("railway.operation.buffer.time"))
          .thenReturn(new BigDecimal("15.0"));
    }

    // メソッドの呼び出し
    Boolean result = evaluationLogic.check(requestDto);

    // 結果の検証
    assertNotNull(result);
    assertFalse(result);
  }

  @Test
  @DisplayName("鉄道運行情報の適合性評価 初回実行フラグがfalseで現在時刻が予約日時を過ぎているとき")
  void testCheck7() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 10, 11, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    // テストデータの準備
    RailwayOperationInformationRequestDto requestDto = new RailwayOperationInformationRequestDto();
    requestDto.setRestricted(false);
    requestDto.setStartAt(LocalDateTime.of(2024, 12, 10, 10, 0));

    // 結果の検証
    assertNull(evaluationLogic.check(requestDto));

  }
}
