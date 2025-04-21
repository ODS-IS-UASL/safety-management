package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dao.AirwayReservationMapper;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.model.AirwayReservation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class AirwayReservationLogicTest {

  @Mock
  private AirwayReservationMapper mapper;

  @InjectMocks
  private AirwayReservationLogic logic;

  @Test
  @DisplayName("新しい航路予約情報を作成する")
  void testUpsert1() {
    WebAirwayReservationDto dto = new WebAirwayReservationDto();

    List<String> airwaySectionIds = new ArrayList<>();
    airwaySectionIds.add("airwaySectionIds");

    dto.setAirwayReservationId("airwayReservationId01");
    dto.setStartAt("2024-11-13T10:42:00");
    dto.setEndAt("2024-11-13T10:43:00");
    dto.setReservedAt("2024-11-13T10:41:00");
    dto.setOperatorId("operatorId01");
    dto.setAirwaySectionIds(airwaySectionIds);

    AirwayReservation model = new AirwayReservation();
    model.setAirwayReservationId("reservationId01");
    model.setStartAt(LocalDateTime.parse("2024-11-13T10:42:00", DateTimeFormatter.ISO_DATE_TIME));
    model.setEndAt(LocalDateTime.parse("2024-11-13T10:43:00", DateTimeFormatter.ISO_DATE_TIME));
    model
        .setReservedAt(LocalDateTime.parse("2024-11-13T10:41:00", DateTimeFormatter.ISO_DATE_TIME));
    model.setOperatorId("areaInfo02");
    model.setAirwaySectionIds(airwaySectionIds);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, AirwayReservation.class)).thenReturn(model);
      when(mapper.upsertSelective(model)).thenReturn(1);
      assertDoesNotThrow(() -> logic.upsert(dto));
    }
  }

  @Test
  @DisplayName("航路予約情報を削除する")
  void testDelete1() {
    String airwayReservationId = "airwayReservationId01";
    when(mapper.deleteByPrimaryKey(airwayReservationId)).thenReturn(1);
    assertDoesNotThrow(() -> logic.delete(airwayReservationId));
  }

  @Test
  @DisplayName("航路予約情報を更新する")
  void testUpdate1() {
    WebAirwayReservationDto dto = new WebAirwayReservationDto();
    dto.setAirwayReservationId("airwayReservationId01");
    dto.setEvaluationResults(false);

    AirwayReservation model = new AirwayReservation();
    model.setAirwayReservationId("reservationId01");
    model.setEvaluationResults(false);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, AirwayReservation.class)).thenReturn(model);
      when(mapper.updateByPrimaryKeySelective(model)).thenReturn(1);
      assertDoesNotThrow(() -> logic.update(dto));
    }
  }
}
