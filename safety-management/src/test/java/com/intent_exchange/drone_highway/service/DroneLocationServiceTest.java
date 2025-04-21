package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dto.request.DroneLocationNotificationDto;
import com.intent_exchange.drone_highway.dto.response.CurrentLocationDto;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.entity.CurrentLocationEntity;
import com.intent_exchange.drone_highway.entity.DroneLocationNotificationEntity;
import com.intent_exchange.drone_highway.entity.DroneLocationNotificationEntityUasId;
import com.intent_exchange.drone_highway.logic.DroneLocationLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class DroneLocationServiceTest {

  @Mock
  private DroneLocationLogic droneLocationLogic;

  @InjectMocks
  private DroneLocationService service;

  @Test
  @DisplayName("運行中ドローンの位置情報を受信し、保存後に逸脱判定")
  void testNotifyDroneLocation1() {
    Double latitude = 43.0457375;
    Double longitude = 141.3553984;
    int aboveGroundLevel = 175;
    String airwaySectionId = "airwaySectionId01";

    DroneLocationNotificationEntity droneLocationNotificationEntity =
        new DroneLocationNotificationEntity();
    droneLocationNotificationEntity.setLatitude(new BigDecimal(latitude));
    droneLocationNotificationEntity.setLongitude(new BigDecimal(longitude));
    droneLocationNotificationEntity.setAboveGroundLevel(aboveGroundLevel);
    DroneLocationNotificationEntityUasId uasId = new DroneLocationNotificationEntityUasId();
    uasId.setRegistrationId("registrationId01");
    droneLocationNotificationEntity.setUasId(uasId);
    droneLocationNotificationEntity.setSpeed(new BigDecimal(0));
    droneLocationNotificationEntity.setVerticalSpeed(new BigDecimal(0));
    DroneLocationNotificationDto notificationDto = new DroneLocationNotificationDto();
    notificationDto.setLatitude(latitude);
    notificationDto.setLongitude(longitude);
    notificationDto.setAboveGroundLevel(aboveGroundLevel);
    notificationDto.setUasId("registrationId01");
    notificationDto.setSpeed(new BigDecimal(0).doubleValue());
    notificationDto.setVerticalSpeed(new BigDecimal(0).doubleValue());

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(droneLocationNotificationEntity,
          DroneLocationNotificationDto.class)).thenReturn(notificationDto);
      when(droneLocationLogic.getReservationInfo(notificationDto))
          .thenReturn(new WebAirwayReservationDto());

      doNothing().when(droneLocationLogic).insert(any(DroneLocationNotificationDto.class));
      when(droneLocationLogic.getNearestAirwaySectionId(notificationDto))
          .thenReturn(airwaySectionId);
      doNothing().when(droneLocationLogic).deviationDetect(any(DroneLocationNotificationDto.class));

      assertDoesNotThrow(() -> service.notifyDroneLocation(droneLocationNotificationEntity));
    }
  }

  @Test
  @DisplayName("受信した運行中ドローンの位置情報が予約情報に存在しなかった場合")
  void testNotifyDroneLocation2() {
    Double latitude = 43.0457375;
    Double longitude = 141.3553984;
    int aboveGroundLevel = 175;

    DroneLocationNotificationEntity droneLocationNotificationEntity =
        new DroneLocationNotificationEntity();
    droneLocationNotificationEntity.setLatitude(new BigDecimal(latitude));
    droneLocationNotificationEntity.setLongitude(new BigDecimal(longitude));
    droneLocationNotificationEntity.setAboveGroundLevel(aboveGroundLevel);
    DroneLocationNotificationDto notificationDto = new DroneLocationNotificationDto();
    notificationDto.setLatitude(latitude);
    notificationDto.setLongitude(longitude);
    notificationDto.setAboveGroundLevel(aboveGroundLevel);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(droneLocationNotificationEntity,
          DroneLocationNotificationDto.class)).thenReturn(notificationDto);
      when(droneLocationLogic.getReservationInfo(notificationDto)).thenReturn(null);

      assertDoesNotThrow(() -> service.notifyDroneLocation(droneLocationNotificationEntity));
    }
  }

  @Test
  @DisplayName("航路予約毎の識別 ID に該当するドローンの現在の位置情報を取得　正常系1")
  void testGetCurrentLocation1() {
    String reservationId = "reservationId01";

    when(droneLocationLogic.getCurrentLocation(reservationId)).thenReturn(null);

    assertNull(service.getCurrentLocation(reservationId));
  }

  @Test
  @DisplayName("航路予約毎の識別 ID に該当するドローンの現在の位置情報を取得　正常系2")
  void testGetCurrentLocation2() {
    String reservationId = "reservationId01";
    CurrentLocationDto currentLocationDto = new CurrentLocationDto();
    currentLocationDto.setUasId("uasId01");
    CurrentLocationEntity currentLocationEntity = new CurrentLocationEntity();
    currentLocationEntity.setUasId("uasId01");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(currentLocationDto, CurrentLocationEntity.class))
          .thenReturn(currentLocationEntity);
      when(droneLocationLogic.getCurrentLocation(reservationId)).thenReturn(currentLocationDto);
      assertDoesNotThrow(() -> service.getCurrentLocation(reservationId));
    }
  }
}
