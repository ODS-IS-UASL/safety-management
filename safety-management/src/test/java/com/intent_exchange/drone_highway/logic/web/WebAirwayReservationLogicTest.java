package com.intent_exchange.drone_highway.logic.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.dto.response.WebAirwaySectionsDto;
import com.intent_exchange.drone_highway.util.PropertyUtil;

@ExtendWith(MockitoExtension.class)
class WebAirwayReservationLogicTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private WebAirwayReservationLogic logic;

  @Test
  @DisplayName("指定された航路予約毎の識別IDに基づいて航路予約情報取得を取得")
  void testGetAirwayReservation1() {
    String airwayReservationId = "abc-123";
    String urlTemplate =
        UriComponentsBuilder.fromHttpUrl(PropertyUtil.getProperty("get.airway.reservation.url",
            airwayReservationId, airwayReservationId)).toUriString();
    WebAirwayReservationDto airwayReservationDto = new WebAirwayReservationDto();

    List<String> airwaySectionIds = new ArrayList<>();
    airwaySectionIds.add("airwaySectionIds");

    airwayReservationDto.setAirwayReservationId("airwayReservationId");
    airwayReservationDto.setAirwaySectionIds(airwaySectionIds);
    airwayReservationDto.setOperatorId("operatorId");
    airwayReservationDto.setStartAt("startDateTime");
    airwayReservationDto.setEndAt("endDateTime");
    airwayReservationDto.setReservedAt("reservationDateTime");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", PropertyUtil.getProperty("route_reservation.token"));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    when(restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, WebAirwayReservationDto.class))
        .thenReturn(ResponseEntity.ok(airwayReservationDto));

    WebAirwayReservationDto result = logic.getAirwayReservation(airwayReservationId);
    assertNotNull(result);
    assertEquals("airwayReservationId", result.getAirwayReservationId());
    assertEquals(airwaySectionIds, result.getAirwaySectionIds());
    assertEquals("operatorId", result.getOperatorId());
    assertEquals("startDateTime", result.getStartAt());
    assertEquals("endDateTime", result.getEndAt());
    assertEquals("reservationDateTime", result.getReservedAt());
  }

  @Test
  @DisplayName("指定された航路予約毎の識別IDに基づいて航路予約情報取得を取得し,戻り値をDB登録用の値に加工")
  void testGetAirwayReservationForDb1() {
    String airwayReservationId = "abc-123";
    String urlTemplate = UriComponentsBuilder
        .fromHttpUrl(PropertyUtil.getProperty("get.airway.reservation.url", airwayReservationId))
        .toUriString();
    WebAirwayReservationDto airwayReservationDto = new WebAirwayReservationDto();

    airwayReservationDto.setAirwaySections(new ArrayList<WebAirwaySectionsDto>());
    List<String> airwaySectionIds = new ArrayList<>();
    WebAirwaySectionsDto WebAirwaySectionsDto = new WebAirwaySectionsDto();
    WebAirwaySectionsDto.setStartAt("2025-01-28T14:40:00Z");
    WebAirwaySectionsDto.setEndAt("2025-01-28T14:55:00Z");
    WebAirwaySectionsDto.setAirwaySectionId("airwaySectionId1");
    airwaySectionIds.add("airwaySectionId1");
    airwayReservationDto.getAirwaySections().add(WebAirwaySectionsDto);

    WebAirwaySectionsDto = new WebAirwaySectionsDto();
    WebAirwaySectionsDto.setStartAt("2025-01-28T13:40:00Z");
    WebAirwaySectionsDto.setEndAt("2025-01-28T15:55:00Z");
    WebAirwaySectionsDto.setAirwaySectionId("airwaySectionId2");
    airwaySectionIds.add("airwaySectionId2");
    airwayReservationDto.getAirwaySections().add(WebAirwaySectionsDto);

    WebAirwaySectionsDto = new WebAirwaySectionsDto();
    WebAirwaySectionsDto.setStartAt("2025-01-28T15:40:00Z");
    WebAirwaySectionsDto.setEndAt("2025-01-28T13:55:00Z");
    WebAirwaySectionsDto.setAirwaySectionId("airwaySectionId3");
    airwaySectionIds.add("airwaySectionId3");
    airwayReservationDto.getAirwaySections().add(WebAirwaySectionsDto);

    // airwayReservationDto.getAirwaySections().add(new WebAirwaySectionsDto());

    airwayReservationDto.setAirwayReservationId("airwayReservationId");
    airwayReservationDto.setOperatorId("operatorId");
    airwayReservationDto.setReservedAt("reservationDateTime");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", PropertyUtil.getProperty("route_reservation.token"));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    when(restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, WebAirwayReservationDto.class))
        .thenReturn(ResponseEntity.ok(airwayReservationDto));

    WebAirwayReservationDto result = logic.getAirwayReservationForDb(airwayReservationId);
    assertNotNull(result);
    assertEquals("airwayReservationId", result.getAirwayReservationId());
    assertEquals(airwaySectionIds, result.getAirwaySectionIds());
    assertEquals("operatorId", result.getOperatorId());
    assertEquals("2025-01-28T13:40:00Z", result.getStartAt());
    assertEquals("2025-01-28T15:55:00Z", result.getEndAt());
    assertEquals("reservationDateTime", result.getReservedAt());
  }

  @Test
  @DisplayName("指定された航路予約毎の識別IDに基づいて航路予約情報取得を取得し,戻り値をDB登録用の値に加工：StartAt EndAtがnull")
  void testGetAirwayReservationForDb2() {
    String airwayReservationId = "abc-123";
    String urlTemplate = UriComponentsBuilder
        .fromHttpUrl(PropertyUtil.getProperty("get.airway.reservation.url", airwayReservationId))
        .toUriString();
    WebAirwayReservationDto airwayReservationDto = new WebAirwayReservationDto();

    airwayReservationDto.setAirwaySections(new ArrayList<WebAirwaySectionsDto>());
    List<String> airwaySectionIds = new ArrayList<>();
    WebAirwaySectionsDto WebAirwaySectionsDto = new WebAirwaySectionsDto();
    WebAirwaySectionsDto.setStartAt(null);
    WebAirwaySectionsDto.setEndAt(null);
    WebAirwaySectionsDto.setAirwaySectionId(null);
    airwaySectionIds.add("airwaySectionId1");
    airwayReservationDto.getAirwaySections().add(WebAirwaySectionsDto);

    // airwayReservationDto.getAirwaySections().add(new WebAirwaySectionsDto());

    airwayReservationDto.setAirwayReservationId("airwayReservationId");
    airwayReservationDto.setOperatorId("operatorId");
    airwayReservationDto.setReservedAt("reservationDateTime");
    airwayReservationDto.setStartAt("");
    airwayReservationDto.setEndAt("");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", PropertyUtil.getProperty("route_reservation.token"));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    when(restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, WebAirwayReservationDto.class))
        .thenReturn(ResponseEntity.ok(airwayReservationDto));

    WebAirwayReservationDto result = logic.getAirwayReservationForDb(airwayReservationId);
    assertNotNull(result);
  }
}
