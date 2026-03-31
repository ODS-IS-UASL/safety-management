package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.intent_exchange.uasl.dao.DroneLocationMapper;
import com.intent_exchange.uasl.dao.UaslReservationMapper;
import com.intent_exchange.uasl.dto.request.DroneLocationNotificationDto;
import com.intent_exchange.uasl.model.DroneLocation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class PlannedDeviationLogicTest {

  @Mock private Clock clock;

  @Mock private UaslReservationMapper mapperReservation;

  @Mock private DroneLocationMapper mapperLocation;

  @InjectMocks private PlannedDeviationLogic logic;

  @Test
  @DisplayName("計画的な航路逸脱フラグを取得する")
  void testGetPlannedDeviation() {
    String reservationId = "res001";
    Boolean plannedDeviation = true;
    when(mapperReservation.getPlannedDeviation(reservationId)).thenReturn(plannedDeviation);
    Boolean result = logic.getPlannedDeviation(reservationId);
    assertEquals(plannedDeviation, result);
  }

  @Test
  @DisplayName("最新のドローンの位置情報を取得する")
  void testGetDroneLocation() {
    String reservationId = "res001";
    DroneLocation expectedLocation = new DroneLocation();
    String instantExpected = "2025-01-01T10:00:00Z";
    Clock fixedClock = Clock.fixed(Instant.parse(instantExpected), ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(fixedClock.instant());
    when(clock.getZone()).thenReturn(fixedClock.getZone());
    when(mapperLocation.selectPrevRowByPrimaryKey(any(DroneLocation.class)))
        .thenReturn(expectedLocation);
    DroneLocation result = logic.getDroneLocation(reservationId);
    assertEquals(expectedLocation, result);
  }

  @Test
  @DisplayName("計画的な航路逸脱フラグを更新する")
  void testUpdatePlannedDeviation() {
    String reservationId = "res001";
    Boolean enabled = true;
    logic.updatePlannedDeviation(reservationId, enabled);
    verify(mapperReservation, times(1))
        .updateByPrimaryKeySelective(
            argThat(
                arg ->
                    reservationId.equals(arg.getUaslReservationId())
                        && enabled.equals(arg.getPlannedDeviation())));
  }

  @Test
  @DisplayName("運行中ドローンの位置情報を更新する")
  void testUpdateDroneLocation() {
    DroneLocationNotificationDto dto = new DroneLocationNotificationDto();
    DroneLocation model = new DroneLocation();
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      logic.updateDroneLocation(dto);
      verify(mapperLocation).updateByPrimaryKeySelective(model);
    }
  }
}
