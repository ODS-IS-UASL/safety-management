package com.intent_exchange.drone_highway.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.intent_exchange.drone_highway.service.AirwayReservationService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AirwayReservationController.class)
class AirwayReservationControllerTest {

  @MockBean
  private AirwayReservationService service;

  @InjectMocks
  private AirwayReservationController controller;

  @Test
  @DisplayName("航路予約情報の削除")
  void testDeleteRouteReservation1() {
    String airwayReservationId = "1";

    ResponseEntity<Void> response = controller.deleteAirwayReservation(airwayReservationId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }
}
