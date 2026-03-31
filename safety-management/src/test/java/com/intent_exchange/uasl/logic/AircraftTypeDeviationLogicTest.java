package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.intent_exchange.uasl.dao.AircraftTypeDeviationMapper;
import com.intent_exchange.uasl.dto.request.AircraftTypeDeviationDto;
import com.intent_exchange.uasl.dto.request.UaslDesignAreaInfoDeviationDto;
import com.intent_exchange.uasl.model.AircraftTypeDeviation;
import com.intent_exchange.uasl.model.AircraftTypeDeviationInfo;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

/** 機体種別別逸脱集計ロジックのテストを提供します。 */
@ExtendWith(MockitoExtension.class)
class AircraftTypeDeviationLogicTest {

  @Mock private UaslDeviationProcessingLogic processingLogic;

  @Mock private AircraftTypeDeviationMapper aircraftTypeDeviationMapper;

  @InjectMocks private AircraftTypeDeviationLogic logic;

  @Test
  @DisplayName("予約ごとに集計してDBへ登録する")
  void testRegisterAircraftTypeDeviationAggregates() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();

    AircraftTypeDeviationInfo info1 = new AircraftTypeDeviationInfo();
    info1.setReservationId("res-001");
    info1.setSerialNumber("SN-001");
    info1.setRegistrationId("REG-001");
    info1.setUtmId("utm-001");
    info1.setSpecificSessionId("sess-001");
    infoList.add(info1);

    AircraftTypeDeviationInfo info2 = new AircraftTypeDeviationInfo();
    info2.setReservationId("res-002");
    info2.setSerialNumber("SN-002");
    info2.setRegistrationId("REG-002");
    info2.setUtmId("utm-002");
    info2.setSpecificSessionId("sess-002");
    infoList.add(info2);

    Instant res1Start = Instant.parse("2026-01-01T00:00:00Z");
    Instant res1End = Instant.parse("2026-01-01T00:05:00Z");
    Instant res2Start = Instant.parse("2026-01-02T00:00:00Z");
    Instant res2End = Instant.parse("2026-01-02T00:05:00Z");

    List<UaslDesignAreaInfoDeviationDto> deviationList = new ArrayList<>();
    deviationList.add(buildDeviation("res-001", 1001, res1Start, "RouteDeviation"));
    deviationList.add(buildDeviation("res-001", 1001, res1End, "RouteDeviation"));
    deviationList.add(buildDeviation("res-002", 1001, res2Start, "RouteDeviation"));
    deviationList.add(buildDeviation("res-002", 1001, res2End, "RouteDeviation"));

    when(processingLogic.getDeviationRate(any())).thenReturn(100.0);
    when(processingLogic.getHorizontalPercentile(any())).thenReturn("1.0");
    when(processingLogic.getVerticalPercentile(any())).thenReturn("2.0");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(deviationList);
      mockedStatic
          .when(
              () ->
                  ModelMapperUtil.map(
                      any(AircraftTypeDeviationDto.class), eq(AircraftTypeDeviation.class)))
          .thenAnswer(invocation -> mapToModel(invocation.getArgument(0)));

      logic.registerAircraftTypeDeviation(infoList);

      verify(processingLogic, times(2)).getDeviationRate(any());

      ArgumentCaptor<AircraftTypeDeviation> captor =
          ArgumentCaptor.forClass(AircraftTypeDeviation.class);
      verify(aircraftTypeDeviationMapper, times(2)).insertSelective(captor.capture());

      Map<String, AircraftTypeDeviation> bySerial =
          captor.getAllValues().stream()
              .collect(Collectors.toMap(AircraftTypeDeviation::getSerialNumber, model -> model));

      AircraftTypeDeviation res1 = bySerial.get("SN-001");
      assertNotNull(res1);
      assertEquals(1001, res1.getAircraftInfoId());
      assertEquals("REG-001", res1.getRegistrationId());
      assertEquals("utm-001", res1.getUtmId());
      assertEquals("sess-001", res1.getSpecificSessionId());
      assertEquals(100.0, res1.getRouteDeviationRate());
      assertEquals("{\"horizontal\": 1.0, \"vertical\": 2.0}", res1.getRouteDeviationAmount());
      assertEquals(
          OffsetDateTime.ofInstant(res1Start, ZoneOffset.UTC), res1.getRouteDeviationStartTime());
      assertEquals(
          OffsetDateTime.ofInstant(res1End, ZoneOffset.UTC), res1.getRouteDeviationEndTime());
      assertEquals(1, res1.getFlightCount());

      AircraftTypeDeviation res2 = bySerial.get("SN-002");
      assertNotNull(res2);
      assertEquals(1001, res2.getAircraftInfoId());
      assertEquals("REG-002", res2.getRegistrationId());
      assertEquals("utm-002", res2.getUtmId());
      assertEquals("sess-002", res2.getSpecificSessionId());
      assertEquals(100.0, res2.getRouteDeviationRate());
      assertEquals("{\"horizontal\": 1.0, \"vertical\": 2.0}", res2.getRouteDeviationAmount());
      assertEquals(
          OffsetDateTime.ofInstant(res2Start, ZoneOffset.UTC), res2.getRouteDeviationStartTime());
      assertEquals(
          OffsetDateTime.ofInstant(res2End, ZoneOffset.UTC), res2.getRouteDeviationEndTime());
      assertEquals(1, res2.getFlightCount());
    }
  }

  private static UaslDesignAreaInfoDeviationDto buildDeviation(
      String reservationId, int aircraftInfoId, Instant timestamp, String status) {
    UaslDesignAreaInfoDeviationDto deviation = new UaslDesignAreaInfoDeviationDto();
    deviation.setReservationId(reservationId);
    deviation.setAircraftInfoId(aircraftInfoId);
    deviation.setGetLocationTimestamp(Timestamp.from(timestamp));
    deviation.setOperationalStatus(status);
    deviation.setHorizontalDeviation(1.0);
    deviation.setVerticalDeviation(2.0);
    return deviation;
  }

  private static AircraftTypeDeviation mapToModel(AircraftTypeDeviationDto dto) {
    AircraftTypeDeviation model = new AircraftTypeDeviation();
    model.setAircraftInfoId(dto.getAircraftInfoId());
    model.setSerialNumber(dto.getSerialNumber());
    model.setRegistrationId(dto.getRegistrationId());
    model.setUtmId(dto.getUtmId());
    model.setSpecificSessionId(dto.getSpecificSessionId());
    model.setRouteDeviationRate(dto.getRouteDeviationRate());
    model.setRouteDeviationAmount(dto.getRouteDeviationAmount());
    model.setRouteDeviationStartTime(dto.getRouteDeviationStartTime());
    model.setRouteDeviationEndTime(dto.getRouteDeviationEndTime());
    model.setFlightCount(dto.getFlightCount());
    return model;
  }

  @Test
  @DisplayName("同一registrationIdが複数予約に紐づく場合、各レコードのflightCountが予約数になる")
  void sameSerialAcrossMultipleReservations() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();

    AircraftTypeDeviationInfo a1 = new AircraftTypeDeviationInfo();
    a1.setReservationId("res-001");
    a1.setSerialNumber("SN-AAA");
    a1.setRegistrationId("REG-AAA");
    a1.setAircraftInfoId(1001);
    infoList.add(a1);

    AircraftTypeDeviationInfo a2 = new AircraftTypeDeviationInfo();
    a2.setReservationId("res-003");
    a2.setSerialNumber("SN-AAA");
    a2.setRegistrationId("REG-AAA");
    a2.setAircraftInfoId(1001);
    infoList.add(a2);

    Instant t = Instant.parse("2026-01-10T00:00:00Z");
    List<UaslDesignAreaInfoDeviationDto> deviationList = new ArrayList<>();
    deviationList.add(buildDeviation("res-001", 1001, t, "RouteDeviation"));
    deviationList.add(buildDeviation("res-003", 1001, t, "RouteDeviation"));

    when(processingLogic.getDeviationRate(any())).thenReturn(100.0);
    when(processingLogic.getHorizontalPercentile(any())).thenReturn("1.0");
    when(processingLogic.getVerticalPercentile(any())).thenReturn("2.0");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(deviationList);
      mockedStatic
          .when(
              () ->
                  ModelMapperUtil.map(
                      any(AircraftTypeDeviationDto.class), eq(AircraftTypeDeviation.class)))
          .thenAnswer(invocation -> mapToModel(invocation.getArgument(0)));

      logic.registerAircraftTypeDeviation(infoList);

      ArgumentCaptor<AircraftTypeDeviation> captor =
          ArgumentCaptor.forClass(AircraftTypeDeviation.class);
      verify(aircraftTypeDeviationMapper, times(1)).insertSelective(captor.capture());

      AircraftTypeDeviation model = captor.getValue();
      assertNotNull(model);
      assertEquals("SN-AAA", model.getSerialNumber());
      assertEquals(2, model.getFlightCount(), "SN-AAA record should report flightCount 2");
    }
  }

  @Test
  @DisplayName("同一予約IDに複数のmetadataがある場合は先頭のmetadataが採用される")
  void duplicateMetadataForSameReservationUsesFirst() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();

    AircraftTypeDeviationInfo first = new AircraftTypeDeviationInfo();
    first.setReservationId("res-dup");
    first.setSerialNumber("SN-FIRST");
    first.setRegistrationId("REG-FIRST");
    first.setAircraftInfoId(1001);
    infoList.add(first);

    AircraftTypeDeviationInfo second = new AircraftTypeDeviationInfo();
    second.setReservationId("res-dup");
    second.setSerialNumber("SN-SECOND");
    second.setRegistrationId("REG-SECOND");
    second.setAircraftInfoId(1001);
    infoList.add(second);

    Instant t = Instant.parse("2026-01-11T00:00:00Z");
    List<UaslDesignAreaInfoDeviationDto> deviationList = new ArrayList<>();
    deviationList.add(buildDeviation("res-dup", 1001, t, "RouteDeviation"));

    when(processingLogic.getDeviationRate(any())).thenReturn(100.0);
    when(processingLogic.getHorizontalPercentile(any())).thenReturn("1.0");
    when(processingLogic.getVerticalPercentile(any())).thenReturn("2.0");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(deviationList);
      mockedStatic
          .when(
              () ->
                  ModelMapperUtil.map(
                      any(AircraftTypeDeviationDto.class), eq(AircraftTypeDeviation.class)))
          .thenAnswer(invocation -> mapToModel(invocation.getArgument(0)));

      logic.registerAircraftTypeDeviation(infoList);

      ArgumentCaptor<AircraftTypeDeviation> captor =
          ArgumentCaptor.forClass(AircraftTypeDeviation.class);
      verify(aircraftTypeDeviationMapper, times(1)).insertSelective(captor.capture());

      AircraftTypeDeviation model = captor.getValue();
      assertNotNull(model);
      assertEquals(
          "SN-FIRST", model.getSerialNumber(), "First metadata entry's serial should be used");
      assertEquals("REG-FIRST", model.getRegistrationId());
    }
  }

  @Test
  @DisplayName("metadata の registrationId が null の場合、挿入は行われない")
  void nullSerialInMetadataDefaultsToOne() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();
    AircraftTypeDeviationInfo info = new AircraftTypeDeviationInfo();
    info.setReservationId("r-null");
    info.setSerialNumber(null);
    info.setAircraftInfoId(1001);
    infoList.add(info);

    Instant t = Instant.parse("2026-01-12T00:00:00Z");
    List<UaslDesignAreaInfoDeviationDto> deviationList = new ArrayList<>();
    deviationList.add(buildDeviation("r-null", 1001, t, "RouteDeviation"));

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(deviationList);
      mockedStatic
          .when(
              () ->
                  ModelMapperUtil.map(
                      any(AircraftTypeDeviationDto.class), eq(AircraftTypeDeviation.class)))
          .thenAnswer(invocation -> mapToModel(invocation.getArgument(0)));

      logic.registerAircraftTypeDeviation(infoList);

      verify(aircraftTypeDeviationMapper, times(0)).insertSelective(any());
    }
  }

  @Test
  @DisplayName("processingLogic.getDeviationRate が null の場合、挿入は行われない")
  void skipWhenDeviationRateIsNull() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();
    AircraftTypeDeviationInfo info = new AircraftTypeDeviationInfo();
    info.setReservationId("r-skip-null");
    info.setSerialNumber("SN-SKIP");
    info.setRegistrationId("REG-SKIP");
    info.setAircraftInfoId(1001);
    infoList.add(info);

    Instant t = Instant.parse("2026-01-13T00:00:00Z");
    List<UaslDesignAreaInfoDeviationDto> deviationList = new ArrayList<>();
    deviationList.add(buildDeviation("r-skip-null", 1001, t, "RouteDeviation"));

    when(processingLogic.getDeviationRate(any())).thenReturn(null);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(deviationList);
      // mapping to model should not be invoked, but stub it just in case
      mockedStatic
          .when(
              () ->
                  ModelMapperUtil.map(
                      any(AircraftTypeDeviationDto.class), eq(AircraftTypeDeviation.class)))
          .thenAnswer(invocation -> mapToModel(invocation.getArgument(0)));

      logic.registerAircraftTypeDeviation(infoList);

      verify(aircraftTypeDeviationMapper, times(0)).insertSelective(any());
    }
  }

  @Test
  @DisplayName("processingLogic.getDeviationRate が 0.0 の場合、挿入は行われない")
  void skipWhenDeviationRateIsZero() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();
    AircraftTypeDeviationInfo info = new AircraftTypeDeviationInfo();
    info.setReservationId("r-skip-zero");
    info.setSerialNumber("SN-SKIP-ZERO");
    info.setRegistrationId("REG-SKIP-ZERO");
    info.setAircraftInfoId(1001);
    infoList.add(info);

    Instant t = Instant.parse("2026-01-14T00:00:00Z");
    List<UaslDesignAreaInfoDeviationDto> deviationList = new ArrayList<>();
    deviationList.add(buildDeviation("r-skip-zero", 1001, t, "RouteDeviation"));

    when(processingLogic.getDeviationRate(any())).thenReturn(0.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(deviationList);
      mockedStatic
          .when(
              () ->
                  ModelMapperUtil.map(
                      any(AircraftTypeDeviationDto.class), eq(AircraftTypeDeviation.class)))
          .thenAnswer(invocation -> mapToModel(invocation.getArgument(0)));

      logic.registerAircraftTypeDeviation(infoList);

      verify(aircraftTypeDeviationMapper, times(0)).insertSelective(any());
    }
  }

  @Test
  @DisplayName("metadata が存在しない予約の場合でも reservationId で集計される")
  void aggregatesWhenMetadataMissing() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();
    AircraftTypeDeviationInfo info = new AircraftTypeDeviationInfo();
    info.setReservationId("res-meta");
    info.setSerialNumber("SN-META");
    info.setRegistrationId("REG-META");
    info.setAircraftInfoId(1001);
    infoList.add(info);

    Instant t = Instant.parse("2026-01-20T00:00:00Z");
    List<UaslDesignAreaInfoDeviationDto> deviationList = new ArrayList<>();
    deviationList.add(buildDeviation("res-no-meta", 1001, t, "RouteDeviation"));

    when(processingLogic.getDeviationRate(any())).thenReturn(100.0);
    when(processingLogic.getHorizontalPercentile(any())).thenReturn("1.0");
    when(processingLogic.getVerticalPercentile(any())).thenReturn("2.0");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(deviationList);
      mockedStatic
          .when(
              () ->
                  ModelMapperUtil.map(
                      any(AircraftTypeDeviationDto.class), eq(AircraftTypeDeviation.class)))
          .thenAnswer(invocation -> mapToModel(invocation.getArgument(0)));

      logic.registerAircraftTypeDeviation(infoList);

      ArgumentCaptor<AircraftTypeDeviation> captor =
          ArgumentCaptor.forClass(AircraftTypeDeviation.class);
      verify(aircraftTypeDeviationMapper, times(1)).insertSelective(captor.capture());

      AircraftTypeDeviation model = captor.getValue();
      assertNotNull(model);
      assertNull(model.getRegistrationId(), "metadata がない場合は registrationId が設定されない");
      assertEquals(1, model.getFlightCount());
    }
  }

  @Test
  @DisplayName("reservationId が null の DTO は集計対象から除外される")
  void skipsWhenReservationIdIsNull() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();
    AircraftTypeDeviationInfo info = new AircraftTypeDeviationInfo();
    info.setReservationId("res-valid");
    info.setSerialNumber("SN-VALID");
    info.setRegistrationId("REG-VALID");
    info.setAircraftInfoId(1001);
    infoList.add(info);

    UaslDesignAreaInfoDeviationDto deviation = new UaslDesignAreaInfoDeviationDto();
    deviation.setReservationId(null);
    deviation.setAircraftInfoId(1001);
    deviation.setOperationalStatus("RouteDeviation");
    deviation.setGetLocationTimestamp(Timestamp.from(Instant.parse("2026-01-21T00:00:00Z")));

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(List.of(deviation));

      logic.registerAircraftTypeDeviation(infoList);

      verify(aircraftTypeDeviationMapper, times(0)).insertSelective(any());
    }
  }

  @Test
  @DisplayName("RouteDeviation がない場合は開始/終了時刻が null になる")
  void startEndTimesAreNullWhenNoRouteDeviation() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();
    AircraftTypeDeviationInfo info = new AircraftTypeDeviationInfo();
    info.setReservationId("res-active");
    info.setSerialNumber("SN-ACTIVE");
    info.setRegistrationId("REG-ACTIVE");
    info.setAircraftInfoId(1001);
    infoList.add(info);

    Instant t = Instant.parse("2026-01-22T00:00:00Z");
    List<UaslDesignAreaInfoDeviationDto> deviationList = new ArrayList<>();
    deviationList.add(buildDeviation("res-active", 1001, t, "active"));

    when(processingLogic.getDeviationRate(any())).thenReturn(100.0);
    when(processingLogic.getHorizontalPercentile(any())).thenReturn("1.0");
    when(processingLogic.getVerticalPercentile(any())).thenReturn("2.0");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(deviationList);
      mockedStatic
          .when(
              () ->
                  ModelMapperUtil.map(
                      any(AircraftTypeDeviationDto.class), eq(AircraftTypeDeviation.class)))
          .thenAnswer(invocation -> mapToModel(invocation.getArgument(0)));

      logic.registerAircraftTypeDeviation(infoList);

      ArgumentCaptor<AircraftTypeDeviation> captor =
          ArgumentCaptor.forClass(AircraftTypeDeviation.class);
      verify(aircraftTypeDeviationMapper, times(1)).insertSelective(captor.capture());

      AircraftTypeDeviation model = captor.getValue();
      assertNull(model.getRouteDeviationStartTime());
      assertNull(model.getRouteDeviationEndTime());
    }
  }

  @Test
  @DisplayName("infoList に null 要素が含まれていても安全に処理される")
  void handlesNullEntriesInInfoList() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();
    // include a null entry to exercise the continue branch in registerAircraftTypeDeviation
    infoList.add(null);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      // mapList should be invoked but return an empty list (no DTOs)
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(infoList, UaslDesignAreaInfoDeviationDto.class))
          .thenReturn(List.of());

      logic.registerAircraftTypeDeviation(infoList);

      // no inserts should be attempted
      verify(aircraftTypeDeviationMapper, times(0)).insertSelective(any());
    }
  }

  @Test
  @DisplayName("resolveRegistrationKey は null DTO を安全に扱う")
  void resolveRegistrationKeyReturnsNullForNullDto() throws Exception {
    // use reflection to call the private method and assert null is returned for null dto
    java.lang.reflect.Method m =
        AircraftTypeDeviationLogic.class.getDeclaredMethod(
            "resolveRegistrationKey", UaslDesignAreaInfoDeviationDto.class, Map.class);
    m.setAccessible(true);

    String result = (String) m.invoke(logic, null, Map.of());
    assertNull(result);
  }

  @Test
  @DisplayName("resolveMeta は null / reservationId==null をスキップして後続の metadata を見つける")
  void resolveMetaSkipsNullsAndFindsMeta() throws Exception {
    // prepare a group with: null, dto with null reservationId, dto with a reservation that has metadata
    UaslDesignAreaInfoDeviationDto d1 = null;
    UaslDesignAreaInfoDeviationDto d2 = new UaslDesignAreaInfoDeviationDto();
    d2.setReservationId(null);
    d2.setAircraftInfoId(1001);

    UaslDesignAreaInfoDeviationDto d3 = new UaslDesignAreaInfoDeviationDto();
    d3.setReservationId("res-found");
    d3.setAircraftInfoId(1001);

    AircraftTypeDeviationInfo meta = new AircraftTypeDeviationInfo();
    meta.setReservationId("res-found");
    meta.setRegistrationId("REG-FOUND");

    Map<String, AircraftTypeDeviationInfo> metadata = Map.of("res-found", meta);

    java.lang.reflect.Method m =
        AircraftTypeDeviationLogic.class.getDeclaredMethod(
            "resolveMeta", List.class, Map.class);
    m.setAccessible(true);

  AircraftTypeDeviationInfo result =
    (AircraftTypeDeviationInfo) m.invoke(logic, java.util.Arrays.asList(d1, d2, d3), metadata);
    assertNotNull(result);
    assertEquals("REG-FOUND", result.getRegistrationId());
  }

  @Test
  @DisplayName("infoList が空の場合は早期 return する")
  void registerReturnsWhenInfoListEmpty() {
    List<AircraftTypeDeviationInfo> infoList = new ArrayList<>();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      logic.registerAircraftTypeDeviation(infoList);

      mockedStatic.verifyNoInteractions();
      verifyNoInteractions(processingLogic, aircraftTypeDeviationMapper);
    }
  }

  @Test
  @DisplayName("infoList が null の場合は早期 return する")
  void registerReturnsWhenInfoListNull() {
    logic.registerAircraftTypeDeviation(null);

    verifyNoInteractions(processingLogic, aircraftTypeDeviationMapper);
  }

  @Test
  @DisplayName("resolveMeta は空リストの場合に null を返す")
  void resolveMetaReturnsNullForEmptyGroup() throws Exception {
    java.lang.reflect.Method m =
        AircraftTypeDeviationLogic.class.getDeclaredMethod(
            "resolveMeta", List.class, Map.class);
    m.setAccessible(true);

    AircraftTypeDeviationInfo result =
        (AircraftTypeDeviationInfo) m.invoke(logic, List.of(), Map.of());
    assertNull(result);
  }

  @Test
  @DisplayName("computeFlightCountByRegistrationId は null/空入力で空マップを返す")
  @SuppressWarnings("unchecked")
  void computeFlightCountReturnsEmptyForNullOrEmptyGroup() throws Exception {
    java.lang.reflect.Method m =
        AircraftTypeDeviationLogic.class.getDeclaredMethod(
            "computeFlightCountByRegistrationId", List.class, Map.class);
    m.setAccessible(true);

    Map<String, Integer> resultNull =
        (Map<String, Integer>) m.invoke(logic, null, Map.of());
    assertNotNull(resultNull);
    assertTrue(resultNull.isEmpty());

    Map<String, Integer> resultEmpty =
        (Map<String, Integer>) m.invoke(logic, List.of(), Map.of());
    assertNotNull(resultEmpty);
    assertTrue(resultEmpty.isEmpty());
  }
}
