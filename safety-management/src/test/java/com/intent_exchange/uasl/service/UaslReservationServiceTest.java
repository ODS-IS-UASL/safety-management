package com.intent_exchange.uasl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.uasl.logic.IdidLogic;

@ExtendWith(MockitoExtension.class)
class UaslReservationServiceTest {

  @Mock
  private IdidLogic ididLogic;

  @InjectMocks
  private UaslReservationService service;


  @Test
  @DisplayName("予約情報の削除")
  void testDeleteUaslReservation1() {
    String uaslReservationId = "abc-123";
    doNothing().when(ididLogic).delete(uaslReservationId);

    assertDoesNotThrow(() -> service.deleteUaslReservation(uaslReservationId));
  }

  @Test
  @DisplayName("予約情報の保存 → IdidLogic#upsert に委譲される")
  void testSaveReservation1() {
    WebUaslReservationDto reservationDto = new WebUaslReservationDto();
    reservationDto.setUaslReservationId("RES-001");
    reservationDto.setOperatorId("OP-1");

    LinkageInformationNotificationDto linkageDto = new LinkageInformationNotificationDto();
    linkageDto.setUaslReservationId("RES-001");
    linkageDto.setRequestId("REQ-001");
    linkageDto.setAircraftInfoId(1);

    doNothing().when(ididLogic).upsert(reservationDto, linkageDto);

    assertDoesNotThrow(() -> service.saveReservation(reservationDto, linkageDto));
    verify(ididLogic, times(1)).upsert(reservationDto, linkageDto);
  }

  @Test
  @DisplayName("予約情報の保存 requestIdがnullでも保存できる")
  void testSaveReservation2_nullRequestId() {
    WebUaslReservationDto reservationDto = new WebUaslReservationDto();
    reservationDto.setUaslReservationId("RES-002");

    LinkageInformationNotificationDto linkageDto = new LinkageInformationNotificationDto();
    linkageDto.setUaslReservationId("RES-002");
    linkageDto.setRequestId(null); // requestId is nullable
    linkageDto.setAircraftInfoId(2);

    doNothing().when(ididLogic).upsert(reservationDto, linkageDto);

    assertDoesNotThrow(() -> service.saveReservation(reservationDto, linkageDto));
    verify(ididLogic, times(1)).upsert(reservationDto, linkageDto);
  }
}

