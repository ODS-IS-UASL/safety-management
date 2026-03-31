package com.intent_exchange.uasl.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.uasl.config.ModelMapperConfig;
import com.intent_exchange.uasl.dao.UaslReservationMapper;
import com.intent_exchange.uasl.entity.SearchActiveUaslReservationsRequest;
import com.intent_exchange.uasl.logic.ActiveUaslReservationsLogic;
import com.intent_exchange.uasl.model.UaslReservation;
import com.intent_exchange.uasl.service.ActiveUaslReservationsService;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SuppressWarnings("null")
@WebMvcTest(ActiveUaslReservationsController.class)
@Import({
  ActiveUaslReservationsControllerTest.TestConfig.class,
  ActiveUaslReservationsService.class,
  ActiveUaslReservationsLogic.class,
  ModelMapperConfig.class
})
class ActiveUaslReservationsControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private UaslReservationMapper reservationMapper;

  @Test
  @DisplayName("予約サマリを返却し、開始日時昇順で並ぶ")
  void searchActiveUaslReservations_returnsSummaries() throws Exception {
    List<String> sectionIds = List.of("s1", "s2");
    SearchActiveUaslReservationsRequest request =
        new SearchActiveUaslReservationsRequest(sectionIds);

    LocalDateTime start1 = LocalDateTime.of(2025, 1, 2, 10, 0);
    LocalDateTime end1 = LocalDateTime.of(2025, 1, 2, 11, 0);
    LocalDateTime start2 = LocalDateTime.of(2025, 1, 3, 9, 0);
    LocalDateTime end2 = LocalDateTime.of(2025, 1, 3, 10, 0);

    UaslReservation first = buildReservation("r1", start1, end1, List.of("a", "b"), "op-1");
    UaslReservation second = buildReservation("r2", start2, end2, List.of("c"), "op-2");

    when(reservationMapper.selectActiveBySectionIds(eq(sectionIds), any(LocalDateTime.class)))
        .thenReturn(List.of(first, second));

    mockMvc
        .perform(
            post("/active-uasl-reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.reservations", hasSize(2)))
        .andExpect(jsonPath("$.reservations[0].uaslReservationId", is("r1")))
        .andExpect(jsonPath("$.reservations[1].uaslReservationId", is("r2")))
        .andExpect(jsonPath("$.reservations[0].operatorId", is("op-1")))
        .andExpect(jsonPath("$.reservations[1].operatorId", is("op-2")))
        .andExpect(jsonPath("$.reservations[0].uaslSectionIds", contains("a", "b")))
        .andExpect(jsonPath("$.reservations[1].uaslSectionIds", contains("c")))
        .andExpect(jsonPath("$.reservations[0].startAt", startsWith("2025-01-02T10:00:00")))
        .andExpect(jsonPath("$.reservations[1].startAt", startsWith("2025-01-03T09:00:00")))
        .andExpect(jsonPath("$.reservations[0].endAt", startsWith("2025-01-02T11:00:00")))
        .andExpect(jsonPath("$.reservations[1].endAt", startsWith("2025-01-03T10:00:00")))
        .andExpect(jsonPath("$.checkedAt", notNullValue()));
  }

  @Test
  @DisplayName("予約なしの場合は空配列を返す")
  void searchActiveUaslReservations_returnsEmpty() throws Exception {
    List<String> sectionIds = List.of("s1");
    SearchActiveUaslReservationsRequest request =
        new SearchActiveUaslReservationsRequest(sectionIds);

    when(reservationMapper.selectActiveBySectionIds(eq(sectionIds), any(LocalDateTime.class)))
        .thenReturn(List.of());

    mockMvc
        .perform(
            post("/active-uasl-reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.reservations", hasSize(0)))
        .andExpect(jsonPath("$.checkedAt", notNullValue()));
  }

  @Test
  @DisplayName("uaslSectionIds未指定は400")
  void searchActiveUaslReservations_missingSectionIds() throws Exception {
    mockMvc
        .perform(
            post("/active-uasl-reservations").contentType(MediaType.APPLICATION_JSON).content("{}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is("400")))
        .andExpect(jsonPath("$.message", is("入力チェックエラー")));
  }

  @Test
  @DisplayName("uaslSectionIds空配列は400")
  void searchActiveUaslReservations_emptySectionIds() throws Exception {
    mockMvc
        .perform(
            post("/active-uasl-reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"uaslSectionIds\":[]}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is("400")))
        .andExpect(jsonPath("$.message", is("入力チェックエラー")));
  }

  @Test
  @DisplayName("JSONパースエラーは400")
  void searchActiveUaslReservations_jsonParseError() throws Exception {
    mockMvc
        .perform(
            post("/active-uasl-reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"uaslSectionIds\":["))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code", is("400")))
        .andExpect(jsonPath("$.message", is("入力チェックエラー")));
  }

  private UaslReservation buildReservation(
      String id,
      LocalDateTime start,
      LocalDateTime end,
      List<String> sectionIds,
      String operatorId) {
    UaslReservation reservation = new UaslReservation();
    reservation.setUaslReservationId(id);
    reservation.setStartAt(start);
    reservation.setEndAt(end);
    reservation.setUaslSectionIds(sectionIds);
    reservation.setOperatorId(operatorId);
    reservation.setReservedAt(start.minusHours(1));
    reservation.setEvaluationResults(true);
    reservation.setThirdPartyEvaluationResults(true);
    reservation.setRailwayOperationEvaluationResults(true);
    reservation.setPlannedDeviation(false);
    return reservation;
  }

  @TestConfiguration
  static class TestConfig {
    @Bean
    UaslReservationMapper reservationMapper() {
      return mock(UaslReservationMapper.class);
    }

    @Bean
    Clock clock() {
      return Clock.fixed(Instant.parse("2025-01-01T00:00:00Z"), ZoneOffset.UTC);
    }
  }
}
