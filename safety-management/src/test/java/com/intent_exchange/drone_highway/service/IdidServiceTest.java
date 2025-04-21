package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.entity.LinkageInformationNotificationEntity;
import com.intent_exchange.drone_highway.logic.IdidLogic;
import com.intent_exchange.drone_highway.logic.web.WebAirwayReservationLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class IdidServiceTest {

  @Mock
  private IdidLogic ididLogic;

  @Mock
  private WebAirwayReservationLogic webAirwayReservationLogic;

  @InjectMocks
  private IdidService service;

  @Test
  @DisplayName("機体ID(リモートID)と航路予約毎の識別IDを受信し、航路予約(A-1-3)へ航路予約毎の識別IDを送信し予約情報を受信、機体ID(リモートID)と紐づけ保存")
  void testNotifyAirwayReservation1() {
    String airwayReservationId = "abc-123";
    LinkageInformationNotificationEntity linkageInformationNotificationEntity =
        new LinkageInformationNotificationEntity();
    linkageInformationNotificationEntity.setAirwayReservationId(airwayReservationId);

    LinkageInformationNotificationDto notificationDto = new LinkageInformationNotificationDto();
    notificationDto.setAirwayReservationId(airwayReservationId);

    WebAirwayReservationDto airwayReservationDto = new WebAirwayReservationDto();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(linkageInformationNotificationEntity,
          LinkageInformationNotificationDto.class)).thenReturn(notificationDto);

      when(webAirwayReservationLogic.getAirwayReservationForDb(airwayReservationId))
          .thenReturn(airwayReservationDto);
      doNothing().when(ididLogic).upsert(airwayReservationDto, notificationDto);

      assertDoesNotThrow(
          () -> service.notifyAirwayReservation(linkageInformationNotificationEntity));
    }
  }

}
