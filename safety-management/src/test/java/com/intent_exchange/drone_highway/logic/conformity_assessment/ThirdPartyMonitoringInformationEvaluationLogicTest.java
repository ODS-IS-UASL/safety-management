package com.intent_exchange.drone_highway.logic.conformity_assessment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dto.geojson.SubscriptionsGeoJsonFeatures;
import com.intent_exchange.drone_highway.dto.request.ThirdPartyMonitoringInformationRequestDto;
import com.intent_exchange.drone_highway.dto.response.ThirdPartyMonitoringInformationResponseDto;
import com.intent_exchange.drone_highway.logic.MonitoringInformationLogic;
import com.intent_exchange.drone_highway.logic.web.WebThirdPartyMonitoringLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
public class ThirdPartyMonitoringInformationEvaluationLogicTest {

  @Mock
  private Clock clock; // Clockクラスのモック

  @Mock
  private WebThirdPartyMonitoringLogic WebThirdPartyMonitoringLogic;

  @Mock
  private MonitoringInformationLogic MonitoringInformationLogic;

  @InjectMocks
  private ThirdPartyMonitoringInformationEvaluationLogic logic;

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視あり")
  void testCheck1() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(true);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");
    dto.setApiKey("QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsb");

    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = new ArrayList<>();
    SubscriptionsGeoJsonFeatures feature = new SubscriptionsGeoJsonFeatures();
    Map<String, Object> properties = new HashMap<>();
    int intrusionStatus = 1;
    properties.put("intrusionStatus", intrusionStatus);
    feature.setProperties(properties);
    features.add(feature);
    monitoringInfoDto.setFeatures(features);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getLatStart(),
          dto.getLonStart(), dto.getLatEnd(), dto.getLonEnd(), LocalDateTime.of(2024, 12, 5, 1, 30),
          1, dto.getApiKey())).thenReturn(monitoringInfoDto);
      mockedStatic.when(() -> ModelMapperUtil.map(feature, SubscriptionsGeoJsonFeatures.class))
          .thenReturn(feature);

      assertFalse(logic.check(dto));
      verify(MonitoringInformationLogic).insert(monitoringInfoDto, dto);

    }
  }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし intrusionStatusが0 前回の結果がfalse")
  void testCheck2() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(true);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");
    dto.setApiKey("QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsb");
    dto.setThirdPartyEvaluationResults(false);

    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = new ArrayList<>();
    SubscriptionsGeoJsonFeatures feature = new SubscriptionsGeoJsonFeatures();
    Map<String, Object> properties = new HashMap<>();
    int intrusionStatus = 0;
    properties.put("intrusionStatus", intrusionStatus);
    feature.setProperties(properties);
    features.add(feature);
    monitoringInfoDto.setFeatures(features);
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(feature, SubscriptionsGeoJsonFeatures.class))
          .thenReturn(feature);
      when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getLatStart(),
          dto.getLonStart(), dto.getLatEnd(), dto.getLonEnd(), LocalDateTime.of(2024, 12, 5, 1, 30),
          1, dto.getApiKey())).thenReturn(monitoringInfoDto);

      assertTrue(logic.check(dto));
      verify(MonitoringInformationLogic).insert(monitoringInfoDto, dto);
    }
  }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし intrusionStatusが0 前回の結果がtrue")
  void testCheck3() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(true);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");
    dto.setApiKey("QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsb");
    dto.setThirdPartyEvaluationResults(true);

    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = new ArrayList<>();
    SubscriptionsGeoJsonFeatures feature = new SubscriptionsGeoJsonFeatures();
    Map<String, Object> properties = new HashMap<>();
    int intrusionStatus = 0;
    properties.put("intrusionStatus", intrusionStatus);
    feature.setProperties(properties);
    features.add(feature);
    monitoringInfoDto.setFeatures(features);
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(feature, SubscriptionsGeoJsonFeatures.class))
          .thenReturn(feature);
      when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getLatStart(),
          dto.getLonStart(), dto.getLatEnd(), dto.getLonEnd(), LocalDateTime.of(2024, 12, 5, 1, 30),
          1, dto.getApiKey())).thenReturn(monitoringInfoDto);

      assertTrue(logic.check(dto));
      verify(MonitoringInformationLogic, never()).insert(monitoringInfoDto, dto);
    }
  }

  // @Test
  // @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし trafficsが空")
  // void testCheck3() {
  //
  // // モックされた現在の時刻を設定
  // ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
  // when(clock.instant()).thenReturn(mockZonedNow.toInstant());
  // when(clock.getZone()).thenReturn(mockZonedNow.getZone());
  //
  // ThirdPartyMonitoringInformationRequestDto dto = new
  // ThirdPartyMonitoringInformationRequestDto();
  // dto.setRestricted(true);
  // dto.setAreaInfo("areaInfo01");
  //
  // ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
  // new ThirdPartyMonitoringInformationResponseDto();
  // List<Object> features = new ArrayList<>();
  // SubscriptionsGeoJsonFeatures feature = new SubscriptionsGeoJsonFeatures();
  // Map<String, Object> properties = new HashMap<>();
  // List<Object> traffics = new ArrayList<>();
  // properties.put("traffics", traffics);
  // feature.setProperties(properties);
  // features.add(feature);
  // monitoringInfoDto.setFeatures(features);
  //
  // try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
  // when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getAreaInfo(),
  // LocalDateTime.of(2024, 12, 5, 1, 30))).thenReturn(monitoringInfoDto);
  // mockedStatic.when(() -> ModelMapperUtil.map(feature, SubscriptionsGeoJsonFeatures.class))
  // .thenReturn(feature);
  //
  // assertTrue(logic.check(dto));
  //
  // }
  // }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし propertiesがnull 前回の結果がfalse")
  void testCheck4() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(true);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");
    dto.setApiKey("QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsb");
    dto.setThirdPartyEvaluationResults(false);

    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = new ArrayList<>();
    SubscriptionsGeoJsonFeatures feature = new SubscriptionsGeoJsonFeatures();
    Map<String, Object> properties = null;
    feature.setProperties(properties);
    features.add(feature);
    monitoringInfoDto.setFeatures(features);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getLatStart(),
          dto.getLonStart(), dto.getLatEnd(), dto.getLonEnd(), LocalDateTime.of(2024, 12, 5, 1, 30),
          1, dto.getApiKey())).thenReturn(monitoringInfoDto);
      mockedStatic.when(() -> ModelMapperUtil.map(feature, SubscriptionsGeoJsonFeatures.class))
          .thenReturn(feature);

      assertTrue(logic.check(dto));
      verify(MonitoringInformationLogic).insert(monitoringInfoDto, dto);
    }
  }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし propertiesが空 前回の結果がtrue")
  void testCheck5() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(true);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");
    dto.setApiKey("QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsb");
    dto.setThirdPartyEvaluationResults(true);

    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = new ArrayList<>();
    SubscriptionsGeoJsonFeatures feature = new SubscriptionsGeoJsonFeatures();
    Map<String, Object> properties = new HashMap<>();
    feature.setProperties(properties);
    features.add(feature);
    monitoringInfoDto.setFeatures(features);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getLatStart(),
          dto.getLonStart(), dto.getLatEnd(), dto.getLonEnd(), LocalDateTime.of(2024, 12, 5, 1, 30),
          1, dto.getApiKey())).thenReturn(monitoringInfoDto);
      mockedStatic.when(() -> ModelMapperUtil.map(feature, SubscriptionsGeoJsonFeatures.class))
          .thenReturn(feature);

      assertTrue(logic.check(dto));
      verify(MonitoringInformationLogic, never()).insert(monitoringInfoDto, dto);
    }

  }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし featuresがnull 前回の結果がfalse")
  void testCheck6() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(true);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");
    dto.setApiKey("QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsb");
    dto.setThirdPartyEvaluationResults(false);

    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = null;
    monitoringInfoDto.setFeatures(features);

    when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getLatStart(), dto.getLonStart(),
        dto.getLatEnd(), dto.getLonEnd(), LocalDateTime.of(2024, 12, 5, 1, 30), 1, dto.getApiKey()))
            .thenReturn(monitoringInfoDto);

    assertTrue(logic.check(dto));
    verify(MonitoringInformationLogic).insert(monitoringInfoDto, dto);
  }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし featuresがnull 前回の結果がtrue")
  void testCheck7() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(true);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");
    dto.setApiKey("QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsb");
    dto.setThirdPartyEvaluationResults(true);

    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = null;
    monitoringInfoDto.setFeatures(features);

    when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getLatStart(), dto.getLonStart(),
        dto.getLatEnd(), dto.getLonEnd(), LocalDateTime.of(2024, 12, 5, 1, 30), 1, dto.getApiKey()))
            .thenReturn(monitoringInfoDto);

    assertTrue(logic.check(dto));
    verify(MonitoringInformationLogic, never()).insert(monitoringInfoDto, dto);
  }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし featuresが空 前回の結果がfalse")
  void testCheck8() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(true);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");
    dto.setApiKey("QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsb");
    dto.setThirdPartyEvaluationResults(false);

    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = new ArrayList<>();
    monitoringInfoDto.setFeatures(features);

    when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getLatStart(), dto.getLonStart(),
        dto.getLatEnd(), dto.getLonEnd(), LocalDateTime.of(2024, 12, 5, 1, 30), 1, dto.getApiKey()))
            .thenReturn(monitoringInfoDto);

    assertTrue(logic.check(dto));
    verify(MonitoringInformationLogic).insert(monitoringInfoDto, dto);
  }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし featuresが空 前回の結果がtrue")
  void testCheck9() {

    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2024, 12, 5, 1, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedNow.toInstant());
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(true);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");
    dto.setApiKey("QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsb");
    dto.setThirdPartyEvaluationResults(true);

    ThirdPartyMonitoringInformationResponseDto monitoringInfoDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = new ArrayList<>();
    monitoringInfoDto.setFeatures(features);

    when(WebThirdPartyMonitoringLogic.getMonitoringInformation(dto.getLatStart(), dto.getLonStart(),
        dto.getLatEnd(), dto.getLonEnd(), LocalDateTime.of(2024, 12, 5, 1, 30), 1, dto.getApiKey()))
            .thenReturn(monitoringInfoDto);

    assertTrue(logic.check(dto));
    verify(MonitoringInformationLogic, never()).insert(monitoringInfoDto, dto);
  }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視フラグがfalse")
  void testCheck10() {

    ThirdPartyMonitoringInformationRequestDto dto = new ThirdPartyMonitoringInformationRequestDto();
    dto.setRestricted(false);
    dto.setLonStart("123.4567");
    dto.setLonEnd("123.8901");
    dto.setLatStart("23.4567");
    dto.setLatEnd("89.0123");

    assertNull(logic.check(dto));

  }
}
