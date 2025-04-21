package com.intent_exchange.drone_highway.controller;

import static org.junit.jupiter.api.Assertions.*;
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
import com.intent_exchange.drone_highway.entity.CurrentLocationEntity;
import com.intent_exchange.drone_highway.entity.DroneLocationNotificationEntity;
import com.intent_exchange.drone_highway.entity.DroneLocationNotificationEntityUasId;
import com.intent_exchange.drone_highway.exception.CurrentLocationException;
import com.intent_exchange.drone_highway.service.DroneLocationService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(DroneLocationController.class)
class DroneLocationControllerTest {

  @MockBean
  private DroneLocationService service;

  @InjectMocks
  private DroneLocationController controller;

  @Test
  @DisplayName("運航中ドローンの位置情報通知 正常系1")
  void testNotifyDroneLocation1() {
    DroneLocationNotificationEntity droneLocationNotificationEntity =
        new DroneLocationNotificationEntity();

    ResponseEntity<Void> response = controller.notifyDroneLocation(droneLocationNotificationEntity);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @DisplayName("運航中ドローンの位置情報通知 正常系2")
  void testNotifyDroneLocation2() {
    DroneLocationNotificationEntity droneLocationNotificationEntity =
        new DroneLocationNotificationEntity();
    droneLocationNotificationEntity.setReservationId("test");

    ResponseEntity<Void> response = controller.notifyDroneLocation(droneLocationNotificationEntity);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

  }

  @Test
  @DisplayName("運航中ドローンの位置情報通知 正常系3")
  void testNotifyDroneLocation3() {
    DroneLocationNotificationEntity droneLocationNotificationEntity =
        new DroneLocationNotificationEntity();
    DroneLocationNotificationEntityUasId droneLocationNotificationEntityUasId =
        new DroneLocationNotificationEntityUasId();
    droneLocationNotificationEntityUasId.setRegistrationId("test");
    droneLocationNotificationEntity.setUasId(droneLocationNotificationEntityUasId);

    ResponseEntity<Void> response = controller.notifyDroneLocation(droneLocationNotificationEntity);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

  }

  @Test
  @DisplayName("現在のドローンの位置情報取得 正常系1")
  void testGetCurrentLocation1() {
    String airwayReservationId = "test";
    CurrentLocationEntity currentLocationEntity = new CurrentLocationEntity();
    currentLocationEntity.setUasId(airwayReservationId);
    when(service.getCurrentLocation(airwayReservationId)).thenReturn(currentLocationEntity);

    ResponseEntity<CurrentLocationEntity> response =
        controller.getCurrentLocation(airwayReservationId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  @DisplayName("現在のドローンの位置情報取得 正常系2")
  void testGetCurrentLocation2() {
    String airwayReservationId = "test";
    assertThrows(CurrentLocationException.class, () -> {
      controller.getCurrentLocation(airwayReservationId);
    });
  }
}
