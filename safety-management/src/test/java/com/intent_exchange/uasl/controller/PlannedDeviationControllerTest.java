package com.intent_exchange.uasl.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.intent_exchange.uasl.entity.PlannedDeviationEntity;
import com.intent_exchange.uasl.exception.PlannedDeviationException;
import com.intent_exchange.uasl.service.PlannedDeviationService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(PlannedDeviationController.class)
class PlannedDeviationControllerTest {

  @MockBean private PlannedDeviationService service;

  @InjectMocks private PlannedDeviationController controller;

  @Test
  @DisplayName("計画的な航路逸脱の設定状態取得 正常系")
  void testGetPlannedDeviation_Success() throws Exception {
    String reservationId = "res001";
    PlannedDeviationEntity entity = new PlannedDeviationEntity();
    entity.setEnabled(true);
    when(service.getPlannedDeviation(reservationId)).thenReturn(entity);
    ResponseEntity<PlannedDeviationEntity> response = controller.getPlannedDeviation(reservationId);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定状態取得 異常系: 存在しない予約ID")
  void testGetPlannedDeviation_NotFound() throws Exception {
    String reservationId = "unknown_id";
    when(service.getPlannedDeviation(reservationId))
        .thenThrow(new PlannedDeviationException("Reservation not found"));
    assertThrows(
        PlannedDeviationException.class,
        () -> {
          controller.getPlannedDeviation(reservationId);
        });
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定 正常系")
  void testSetPlannedDeviation_Success() throws Exception {
    String reservationId = "res001";
    Boolean enabled = true;
    doNothing().when(service).setPlannedDeviation(reservationId, enabled);
    ResponseEntity<Void> response = controller.setPlannedDeviation(reservationId, enabled);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定 異常系: 存在しない予約ID")
  void testSetPlannedDeviation_NotFound() throws Exception {
    String reservationId = "unknown_id";
    Boolean enabled = true;
    doThrow(new PlannedDeviationException("Reservation not found"))
        .when(service)
        .setPlannedDeviation(eq(reservationId), eq(enabled));
    assertThrows(
        PlannedDeviationException.class,
        () -> {
          controller.setPlannedDeviation(reservationId, enabled);
        });
  }
}
