package com.intent_exchange.uasl.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.intent_exchange.uasl.dto.request.SearchActiveUaslReservationsRequestDto;
import com.intent_exchange.uasl.dto.response.ActiveReservationResponseDto;
import com.intent_exchange.uasl.dto.response.ActiveReservationSummaryDto;
import com.intent_exchange.uasl.entity.ActiveReservationResponse;
import com.intent_exchange.uasl.entity.SearchActiveUaslReservationsRequest;
import com.intent_exchange.uasl.logic.ActiveUaslReservationsLogic;
import com.intent_exchange.uasl.model.UaslReservation;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ActiveUaslReservationsServiceTest {

  private ActiveUaslReservationsService service;

  @Mock private ActiveUaslReservationsLogic logic;

  private Clock clock;

  @BeforeEach
  void setUp() {
    service = new ActiveUaslReservationsService();
    clock = Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneOffset.UTC);
    ReflectionTestUtils.setField(service, "logic", logic);
    ReflectionTestUtils.setField(service, "clock", clock);

    ModelMapper mapper = new ModelMapper();
    ModelMapperUtil.setModelMapper(mapper);
  }

  @Test
  @DisplayName("searchActiveUaslReservationsはロジックの結果をレスポンスにマッピングする")
  void searchActiveUaslReservations_mapsResult() {
    SearchActiveUaslReservationsRequest request =
        new SearchActiveUaslReservationsRequest(List.of("sec-1"));

    UaslReservation reservation = buildReservation("r-1", List.of("sec-1"));
    when(logic.selectActiveReservations(eq(List.of("sec-1")), any(LocalDateTime.class)))
        .thenReturn(List.of(reservation));

    ActiveReservationResponse response = service.searchActiveUaslReservations(request);

    verify(logic).selectActiveReservations(eq(List.of("sec-1")), any(LocalDateTime.class));
    assertThat(response.getReservations()).hasSize(1);
    assertThat(response.getReservations().get(0).getUaslReservationId()).isEqualTo("r-1");
    assertThat(response.getCheckedAt()).isNotNull();
  }

  @Test
  @DisplayName("toRequestDtoはリクエストがnullの場合に空リストを返す")
  void toRequestDto_nullRequest_returnsEmptyList() {
    SearchActiveUaslReservationsRequestDto dto =
        ReflectionTestUtils.invokeMethod(service, "toRequestDto", new Object[] {null});

    assertThat(dto.getUaslSectionIds()).isEmpty();
  }

  @Test
  @DisplayName("toRequestDtoはsectionIdが指定された場合に同じリストを返す")
  void toRequestDto_withIds_returnsSameList() {
    SearchActiveUaslReservationsRequest request =
        new SearchActiveUaslReservationsRequest(List.of("a", "b"));

    SearchActiveUaslReservationsRequestDto dto =
        ReflectionTestUtils.invokeMethod(service, "toRequestDto", request);

    assertThat(dto.getUaslSectionIds()).containsExactly("a", "b");
  }

  @Test
  @DisplayName("mapReservationsは入力がnullまたは空の場合に空リストを返す")
  void mapReservations_nullOrEmpty_returnsEmptyList() {
    List<ActiveReservationSummaryDto> nullResult =
        ReflectionTestUtils.invokeMethod(service, "mapReservations", new Object[] {null});
    List<ActiveReservationSummaryDto> emptyResult =
        ReflectionTestUtils.invokeMethod(service, "mapReservations", Collections.emptyList());

    assertThat(nullResult).isEmpty();
    assertThat(emptyResult).isEmpty();
  }

  @Test
  @DisplayName("mapReservationsは予約をサマリーDTOに変換する")
  void mapReservations_convertsReservations() {
    UaslReservation reservation = buildReservation("r-2", List.of("x", "y"));

    List<ActiveReservationSummaryDto> result =
        ReflectionTestUtils.invokeMethod(service, "mapReservations", List.of(reservation));

    assertThat(result).hasSize(1);
    ActiveReservationSummaryDto dto = result.get(0);
    assertThat(dto.getUaslReservationId()).isEqualTo("r-2");
    assertThat(dto.getUaslSectionIds()).containsExactly("x", "y");
    assertThat(dto.getStartAt()).isNotNull();
    assertThat(dto.getEndAt()).isNotNull();
  }

  @Test
  @DisplayName("resolveSectionIdsはnullまたは空リストの場合に空リストを返す")
  void resolveSectionIds_handlesNullAndEmpty() {
    UaslReservation nullReservation = new UaslReservation();
    UaslReservation emptyReservation = new UaslReservation();
    emptyReservation.setUaslSectionIds(Collections.emptyList());

    List<String> nullResult =
        ReflectionTestUtils.invokeMethod(service, "resolveSectionIds", nullReservation);
    List<String> emptyResult =
        ReflectionTestUtils.invokeMethod(service, "resolveSectionIds", emptyReservation);

    assertThat(nullResult).isEmpty();
    assertThat(emptyResult).isEmpty();
  }

  @Test
  @DisplayName("toSectionIdListは生のList要素を文字列に変換する")
  void toSectionIdList_convertsRawList() {
    List<String> sectionIds =
        ReflectionTestUtils.invokeMethod(service, "toSectionIdList", Arrays.asList("1", 2, null));

    assertThat(sectionIds).containsExactly("1", "2");
  }

  @Test
  @DisplayName("toSectionIdListは値がListでない場合に空リストを返す")
  void toSectionIdList_nonList_returnsEmpty() {
    List<String> sectionIds =
        ReflectionTestUtils.invokeMethod(service, "toSectionIdList", "not-a-list");

    assertThat(sectionIds).isEmpty();
  }

  @Test
  @DisplayName("toDateは入力がnullの場合にnullを返す")
  void toDate_nullInput_returnsNull() {
    Date result = ReflectionTestUtils.invokeMethod(service, "toDate", new Object[] {null});

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("toDateはLocalDateTimeをUTCで変換する")
  void toDate_convertsUsingUtc() {
    LocalDateTime dateTime = LocalDateTime.of(2025, 1, 1, 12, 0);

    Date result = ReflectionTestUtils.invokeMethod(service, "toDate", dateTime);

    assertThat(result.toInstant()).isEqualTo(dateTime.toInstant(ZoneOffset.UTC));
  }

  @Test
  @DisplayName("toResponseEntityはreservationsリストがnullの場合に空リストを返す")
  void toResponseEntity_nullReservations_returnsEmptyList() {
    ActiveReservationResponseDto dto = new ActiveReservationResponseDto();
    dto.setCheckedAt(new Date());
    dto.setReservations(null);

    ActiveReservationResponse response =
        ReflectionTestUtils.invokeMethod(service, "toResponseEntity", dto);

    assertThat(response.getCheckedAt()).isEqualTo(dto.getCheckedAt());
    assertThat(response.getReservations()).isEmpty();
  }

  @Test
  @DisplayName("toResponseEntityは予約サマリーをマッピングする")
  void toResponseEntity_mapsSummaries() {
    ActiveReservationSummaryDto summaryDto = new ActiveReservationSummaryDto();
    summaryDto.setUaslReservationId("summary-1");
    summaryDto.setStartAt(new Date());
    summaryDto.setEndAt(new Date());
    summaryDto.setOperatorId("op");
    summaryDto.setUaslSectionIds(List.of("sec"));

    ActiveReservationResponseDto dto = new ActiveReservationResponseDto();
    dto.setCheckedAt(new Date());
    dto.setReservations(List.of(summaryDto));

    ActiveReservationResponse response =
        ReflectionTestUtils.invokeMethod(service, "toResponseEntity", dto);

    assertThat(response.getReservations()).hasSize(1);
    assertThat(response.getReservations().get(0).getUaslReservationId()).isEqualTo("summary-1");
  }

  private UaslReservation buildReservation(String id, List<String> sectionIds) {
    UaslReservation reservation = new UaslReservation();
    reservation.setUaslReservationId(id);
    reservation.setStartAt(LocalDateTime.of(2025, 1, 1, 10, 0));
    reservation.setEndAt(LocalDateTime.of(2025, 1, 1, 11, 0));
    reservation.setOperatorId("operator");
    reservation.setUaslSectionIds(sectionIds);
    return reservation;
  }
}
