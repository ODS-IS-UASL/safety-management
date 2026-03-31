package com.intent_exchange.uasl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.intent_exchange.uasl.dto.request.DroneLocationNotificationDto;
import com.intent_exchange.uasl.entity.PlannedDeviationEntity;
import com.intent_exchange.uasl.exception.PlannedDeviationException;
import com.intent_exchange.uasl.logic.PlannedDeviationLogic;
import com.intent_exchange.uasl.logic.mqtt.MqttDeviationNotificationLogic;
import com.intent_exchange.uasl.model.DroneLocation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class PlannedDeviationServiceTest {

  @Mock private PlannedDeviationLogic plannedDeviationLogic;

  @Mock private MqttDeviationNotificationLogic mqttNotificationLogic;

  @InjectMocks private PlannedDeviationService service;

  @Test
  @DisplayName("計画的な航路逸脱の設定状態取得: 正常系")
  void testGetPlannedDeviation_Success() {
    String reservationId = "res001";
    Boolean plannedDeviation = true;
    when(plannedDeviationLogic.getPlannedDeviation(reservationId)).thenReturn(plannedDeviation);
    PlannedDeviationEntity result = service.getPlannedDeviation(reservationId);
    assertEquals(plannedDeviation, result.getEnabled());
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定状態取得: 異常系(データなし)")
  void testGetPlannedDeviation_NotFound() {
    String reservationId = "unknown_id";
    when(plannedDeviationLogic.getPlannedDeviation(reservationId)).thenReturn(null);
    assertThrows(PlannedDeviationException.class, () -> service.getPlannedDeviation(reservationId));
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定: 変更なしの場合は更新処理を行わない")
  void testSetPlannedDeviation_NoChange() {
    String reservationId = "res001";
    Boolean enabled = true;
    when(plannedDeviationLogic.getPlannedDeviation(reservationId)).thenReturn(true);
    assertDoesNotThrow(() -> service.setPlannedDeviation(reservationId, enabled));
    verify(plannedDeviationLogic, never()).updatePlannedDeviation(anyString(), anyBoolean());
    verify(plannedDeviationLogic, never()).getDroneLocation(anyString());
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定: 変更あり、ドローン位置情報なし")
  void testSetPlannedDeviation_Update_NoLocation() {
    String reservationId = "res001";
    Boolean enabled = true;
    when(plannedDeviationLogic.getPlannedDeviation(reservationId)).thenReturn(false);
    when(plannedDeviationLogic.getDroneLocation(reservationId)).thenReturn(null);
    assertDoesNotThrow(() -> service.setPlannedDeviation(reservationId, enabled));
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定: 計画的な航路逸脱あり & 現在逸脱中")
  void testSetPlannedDeviation_PlannedAndDeviation() {
    String reservationId = "res001";
    Boolean enabled = true;
    DroneLocation location = new DroneLocation();
    location.setReservationId(reservationId);
    location.setOperationalStatus("RouteDeviation");
    DroneLocationNotificationDto notificationDto = new DroneLocationNotificationDto();
    notificationDto.setReservationId(reservationId);
    notificationDto.setOperationalStatus("RouteDeviation");

    when(plannedDeviationLogic.getPlannedDeviation(reservationId)).thenReturn(false);
    when(plannedDeviationLogic.getDroneLocation(reservationId)).thenReturn(location);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.map(location, DroneLocationNotificationDto.class))
          .thenReturn(notificationDto);
      assertDoesNotThrow(() -> service.setPlannedDeviation(reservationId, enabled));
      verify(mqttNotificationLogic)
          .notifyUaslDeviation(
              argThat(arg -> "PlannedRouteDeviation".equals(arg.getOperationalStatus())));
    }
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定: 計画的な航路逸脱なし & 現在逸脱中")
  void testSetPlannedDeviation_NotPlannedAndDeviation() {
    String reservationId = "res001";
    Boolean enabled = false;
    DroneLocation location = new DroneLocation();
    location.setReservationId(reservationId);
    location.setOperationalStatus("RouteDeviation");
    DroneLocationNotificationDto notificationDto = new DroneLocationNotificationDto();
    notificationDto.setReservationId(reservationId);
    notificationDto.setOperationalStatus("RouteDeviation");

    when(plannedDeviationLogic.getPlannedDeviation(reservationId)).thenReturn(true);
    when(plannedDeviationLogic.getDroneLocation(reservationId)).thenReturn(location);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.map(location, DroneLocationNotificationDto.class))
          .thenReturn(notificationDto);
      assertDoesNotThrow(() -> service.setPlannedDeviation(reservationId, enabled));
    }
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定: 計画的な航路逸脱なし & 計画的な航路逸脱中")
  void testSetPlannedDeviation_NotPlannedAndPlanned() {
    String reservationId = "res001";
    Boolean enabled = false;
    DroneLocation location = new DroneLocation();
    location.setReservationId(reservationId);
    location.setOperationalStatus("PlannedRouteDeviation");
    DroneLocationNotificationDto notificationDto = new DroneLocationNotificationDto();
    notificationDto.setReservationId(reservationId);
    notificationDto.setOperationalStatus("PlannedRouteDeviation");

    when(plannedDeviationLogic.getPlannedDeviation(reservationId)).thenReturn(true);
    when(plannedDeviationLogic.getDroneLocation(reservationId)).thenReturn(location);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.map(location, DroneLocationNotificationDto.class))
          .thenReturn(notificationDto);
      assertDoesNotThrow(() -> service.setPlannedDeviation(reservationId, enabled));
      verify(mqttNotificationLogic)
          .notifyUaslDeviation(
              argThat(arg -> "RouteDeviation".equals(arg.getOperationalStatus())));
    }
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定: 計画的な航路逸脱あり & 計画的な航路逸脱中")
  void testSetPlannedDeviation_PlannedAndPlanned() {
    String reservationId = "res001";
    Boolean enabled = true;
    DroneLocation location = new DroneLocation();
    location.setReservationId(reservationId);
    location.setOperationalStatus("PlannedRouteDeviation");
    DroneLocationNotificationDto notificationDto = new DroneLocationNotificationDto();
    notificationDto.setReservationId(reservationId);
    notificationDto.setOperationalStatus("PlannedRouteDeviation");

    when(plannedDeviationLogic.getPlannedDeviation(reservationId)).thenReturn(false);
    when(plannedDeviationLogic.getDroneLocation(reservationId)).thenReturn(location);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic
          .when(() -> ModelMapperUtil.map(location, DroneLocationNotificationDto.class))
          .thenReturn(notificationDto);
      assertDoesNotThrow(() -> service.setPlannedDeviation(reservationId, enabled));
    }
  }

  @Test
  @DisplayName("計画的な航路逸脱の設定: 異常系(予約IDが存在しない)")
  void testSetPlannedDeviation_NotFound() {
    String reservationId = "unknown_id";
    Boolean enabled = true;
    when(plannedDeviationLogic.getPlannedDeviation(reservationId)).thenReturn(null);
    assertThrows(
        PlannedDeviationException.class, () -> service.setPlannedDeviation(reservationId, enabled));
  }
}
