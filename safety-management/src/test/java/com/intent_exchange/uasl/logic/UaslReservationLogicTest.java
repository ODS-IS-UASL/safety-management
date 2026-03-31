package com.intent_exchange.uasl.logic;

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
import com.intent_exchange.uasl.dao.UaslReservationMapper;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.model.UaslReservation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class UaslReservationLogicTest {

  @Mock
  private UaslReservationMapper mapper;

  @InjectMocks
  private UaslReservationLogic logic;

  @Test
  @DisplayName("新しい航路予約情報を作成する")
  void testUpsert1() {
    WebUaslReservationDto dto = new WebUaslReservationDto();

    List<String> uaslSectionIds = new ArrayList<>();
    uaslSectionIds.add("uaslSectionIds");

    dto.setUaslReservationId("uaslReservationId01");
    dto.setStartAt("2024-11-13T10:42:00");
    dto.setEndAt("2024-11-13T10:43:00");
    dto.setReservedAt("2024-11-13T10:41:00");
    dto.setOperatorId("operatorId01");
    dto.setUaslSectionIds(uaslSectionIds);

    UaslReservation model = new UaslReservation();
    model.setUaslReservationId("reservationId01");
    model.setStartAt(LocalDateTime.parse("2024-11-13T10:42:00", DateTimeFormatter.ISO_DATE_TIME));
    model.setEndAt(LocalDateTime.parse("2024-11-13T10:43:00", DateTimeFormatter.ISO_DATE_TIME));
    model
        .setReservedAt(LocalDateTime.parse("2024-11-13T10:41:00", DateTimeFormatter.ISO_DATE_TIME));
    model.setOperatorId("areaInfo02");
    model.setUaslSectionIds(uaslSectionIds);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, UaslReservation.class)).thenReturn(model);
      when(mapper.upsertSelective(model)).thenReturn(1);
      assertDoesNotThrow(() -> logic.upsert(dto));
    }
  }

  @Test
  @DisplayName("航路予約情報を削除する")
  void testDelete1() {
    String uaslReservationId = "uaslReservationId01";
    when(mapper.deleteByPrimaryKey(uaslReservationId)).thenReturn(1);
    assertDoesNotThrow(() -> logic.delete(uaslReservationId));
  }

  @Test
  @DisplayName("航路予約情報を更新する")
  void testUpdate1() {
    WebUaslReservationDto dto = new WebUaslReservationDto();
    dto.setUaslReservationId("uaslReservationId01");
    dto.setEvaluationResults(false);

    UaslReservation model = new UaslReservation();
    model.setUaslReservationId("reservationId01");
    model.setEvaluationResults(false);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, UaslReservation.class)).thenReturn(model);
      when(mapper.updateByPrimaryKeySelective(model)).thenReturn(1);
      assertDoesNotThrow(() -> logic.update(dto));
    }
  }
}
