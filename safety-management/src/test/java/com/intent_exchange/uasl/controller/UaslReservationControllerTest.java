package com.intent_exchange.uasl.controller;

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
import com.intent_exchange.uasl.service.UaslReservationService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(UaslReservationController.class)
class UaslReservationControllerTest {

  @MockBean
  private UaslReservationService service;

  @InjectMocks
  private UaslReservationController controller;

  @Test
  @DisplayName("航路予約情報の削除")
  void testDeleteRouteReservation1() {
    String uaslReservationId = "1";

    ResponseEntity<Void> response = controller.deleteUaslReservation(uaslReservationId);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }
}
