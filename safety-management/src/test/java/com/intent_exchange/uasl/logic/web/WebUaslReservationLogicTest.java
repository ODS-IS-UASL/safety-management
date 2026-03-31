package com.intent_exchange.uasl.logic.web;

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
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.dto.response.WebUaslSectionsDto;
import com.intent_exchange.uasl.util.PropertyUtil;

@ExtendWith(MockitoExtension.class)
class WebUaslReservationLogicTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private WebUaslReservationLogic logic;

  @Test
  @DisplayName("指定された航路予約毎の識別IDに基づいて航路予約情報取得を取得")
  void testGetUaslReservation1() {
    String uaslReservationId = "abc-123";
    String urlTemplate =
        UriComponentsBuilder.fromHttpUrl(PropertyUtil.getProperty("get.uasl.reservation.url",
            uaslReservationId, uaslReservationId)).toUriString();
    WebUaslReservationDto uaslReservationDto = new WebUaslReservationDto();

    List<String> uaslSectionIds = new ArrayList<>();
    uaslSectionIds.add("uaslSectionIds");

    uaslReservationDto.setUaslReservationId("uaslReservationId");
    uaslReservationDto.setUaslSectionIds(uaslSectionIds);
    uaslReservationDto.setOperatorId("operatorId");
    uaslReservationDto.setStartAt("startDateTime");
    uaslReservationDto.setEndAt("endDateTime");
    uaslReservationDto.setReservedAt("reservationDateTime");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", PropertyUtil.getProperty("route_reservation.token"));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    when(restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, WebUaslReservationDto.class))
        .thenReturn(ResponseEntity.ok(uaslReservationDto));

    WebUaslReservationDto result = logic.getUaslReservation(uaslReservationId);
    assertNotNull(result);
    assertEquals("uaslReservationId", result.getUaslReservationId());
    assertEquals(uaslSectionIds, result.getUaslSectionIds());
    assertEquals("operatorId", result.getOperatorId());
    assertEquals("startDateTime", result.getStartAt());
    assertEquals("endDateTime", result.getEndAt());
    assertEquals("reservationDateTime", result.getReservedAt());
  }

  @Test
  @DisplayName("指定された航路予約毎の識別IDに基づいて航路予約情報取得を取得し,戻り値をDB登録用の値に加工")
  void testGetUaslReservationForDb1() {
    String uaslReservationId = "abc-123";
    String urlTemplate = UriComponentsBuilder
        .fromHttpUrl(PropertyUtil.getProperty("get.uasl.reservation.url", uaslReservationId))
        .toUriString();
    WebUaslReservationDto uaslReservationDto = new WebUaslReservationDto();

    uaslReservationDto.setUaslSections(new ArrayList<WebUaslSectionsDto>());
    List<String> uaslSectionIds = new ArrayList<>();
    WebUaslSectionsDto WebUaslSectionsDto = new WebUaslSectionsDto();
    WebUaslSectionsDto.setStartAt("2025-01-28T14:40:00Z");
    WebUaslSectionsDto.setEndAt("2025-01-28T14:55:00Z");
    WebUaslSectionsDto.setUaslSectionId("uaslSectionId1");
    uaslSectionIds.add("uaslSectionId1");
    uaslReservationDto.getUaslSections().add(WebUaslSectionsDto);

    WebUaslSectionsDto = new WebUaslSectionsDto();
    WebUaslSectionsDto.setStartAt("2025-01-28T13:40:00Z");
    WebUaslSectionsDto.setEndAt("2025-01-28T15:55:00Z");
    WebUaslSectionsDto.setUaslSectionId("uaslSectionId2");
    uaslSectionIds.add("uaslSectionId2");
    uaslReservationDto.getUaslSections().add(WebUaslSectionsDto);

    WebUaslSectionsDto = new WebUaslSectionsDto();
    WebUaslSectionsDto.setStartAt("2025-01-28T15:40:00Z");
    WebUaslSectionsDto.setEndAt("2025-01-28T13:55:00Z");
    WebUaslSectionsDto.setUaslSectionId("uaslSectionId3");
    uaslSectionIds.add("uaslSectionId3");
    uaslReservationDto.getUaslSections().add(WebUaslSectionsDto);

    // uaslReservationDto.getUaslSections().add(new WebUaslSectionsDto());

    uaslReservationDto.setUaslReservationId("uaslReservationId");
    uaslReservationDto.setOperatorId("operatorId");
    uaslReservationDto.setReservedAt("reservationDateTime");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", PropertyUtil.getProperty("route_reservation.token"));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    when(restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, WebUaslReservationDto.class))
        .thenReturn(ResponseEntity.ok(uaslReservationDto));

    WebUaslReservationDto result = logic.getUaslReservationForDb(uaslReservationId);
    assertNotNull(result);
    assertEquals("uaslReservationId", result.getUaslReservationId());
    assertEquals(uaslSectionIds, result.getUaslSectionIds());
    assertEquals("operatorId", result.getOperatorId());
    assertEquals("2025-01-28T13:40:00Z", result.getStartAt());
    assertEquals("2025-01-28T15:55:00Z", result.getEndAt());
    assertEquals("reservationDateTime", result.getReservedAt());
  }

  @Test
  @DisplayName("指定された航路予約毎の識別IDに基づいて航路予約情報取得を取得し,戻り値をDB登録用の値に加工：StartAt EndAtがnull")
  void testGetUaslReservationForDb2() {
    String uaslReservationId = "abc-123";
    String urlTemplate = UriComponentsBuilder
        .fromHttpUrl(PropertyUtil.getProperty("get.uasl.reservation.url", uaslReservationId))
        .toUriString();
    WebUaslReservationDto uaslReservationDto = new WebUaslReservationDto();

    uaslReservationDto.setUaslSections(new ArrayList<WebUaslSectionsDto>());
    List<String> uaslSectionIds = new ArrayList<>();
    WebUaslSectionsDto WebUaslSectionsDto = new WebUaslSectionsDto();
    WebUaslSectionsDto.setStartAt(null);
    WebUaslSectionsDto.setEndAt(null);
    WebUaslSectionsDto.setUaslSectionId(null);
    uaslSectionIds.add("uaslSectionId1");
    uaslReservationDto.getUaslSections().add(WebUaslSectionsDto);

    // uaslReservationDto.getUaslSections().add(new WebUaslSectionsDto());

    uaslReservationDto.setUaslReservationId("uaslReservationId");
    uaslReservationDto.setOperatorId("operatorId");
    uaslReservationDto.setReservedAt("reservationDateTime");
    uaslReservationDto.setStartAt("");
    uaslReservationDto.setEndAt("");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", PropertyUtil.getProperty("route_reservation.token"));
    HttpEntity<String> entity = new HttpEntity<>(headers);

    when(restTemplate.exchange(urlTemplate, HttpMethod.GET, entity, WebUaslReservationDto.class))
        .thenReturn(ResponseEntity.ok(uaslReservationDto));

    WebUaslReservationDto result = logic.getUaslReservationForDb(uaslReservationId);
    assertNotNull(result);
  }
}
