package com.intent_exchange.uasl.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.OffsetDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.intent_exchange.uasl.dto.request.FlightLogSearchDto;
import com.intent_exchange.uasl.entity.FlightLogListResponse;
import com.intent_exchange.uasl.service.FlightLogsService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FlightLogsController.class)
class FlightLogsControllerTest {

  @MockBean
  private FlightLogsService service;

  @InjectMocks
  private FlightLogsController controller;

  @Test
  @DisplayName("フライトログ取得 正常系")
  void testGetFlightLogs_Success() {
    String startTime = "2024-01-01T00:00:00Z";
    String endTime = "2024-01-01T23:59:59Z";
    String reservationId = "res001";
    String uaslId = "uasl001";
    String operatorId = "op001";
    Integer aircraftInfoId = 123;
    String uaType = "TypeA";

    FlightLogListResponse mockResponse = new FlightLogListResponse();

    when(service.getFlightLogs(any(FlightLogSearchDto.class))).thenReturn(mockResponse);

    ResponseEntity<FlightLogListResponse> result =
        controller.getFlightLogs(startTime, endTime, reservationId, uaslId, operatorId, aircraftInfoId, uaType);

    assertNotNull(result);
    assertEquals(HttpStatus.OK, result.getStatusCode());
    assertEquals(mockResponse, result.getBody());

    ArgumentCaptor<FlightLogSearchDto> captor = ArgumentCaptor.forClass(FlightLogSearchDto.class);
    verify(service, times(1)).getFlightLogs(captor.capture());

    FlightLogSearchDto capturedDto = captor.getValue();
    assertEquals(OffsetDateTime.parse(startTime).toLocalDateTime(), capturedDto.getStartTime());
    assertEquals(OffsetDateTime.parse(endTime).toLocalDateTime(), capturedDto.getEndTime());
    assertEquals(reservationId, capturedDto.getReservationId());
    assertEquals(uaslId, capturedDto.getUaslId());
    assertEquals(operatorId, capturedDto.getOperatorId());
    assertEquals(aircraftInfoId, capturedDto.getAircraftInfoId());
    assertEquals(uaType, capturedDto.getUaType());
  }
}