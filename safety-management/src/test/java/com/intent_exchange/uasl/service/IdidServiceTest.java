package com.intent_exchange.uasl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.entity.LinkageInformationNotificationEntity;
import com.intent_exchange.uasl.logic.IdidLogic;
import com.intent_exchange.uasl.logic.web.WebUaslReservationLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class IdidServiceTest {

  @Mock
  private IdidLogic ididLogic;

  @Mock
  private WebUaslReservationLogic webUaslReservationLogic;

  @InjectMocks
  private IdidService service;

  @Test
  @DisplayName("機体ID(リモートID)と航路予約毎の識別IDを受信し、航路予約へ航路予約毎の識別IDを送信し予約情報を受信、機体ID(リモートID)と紐づけ保存")
  void testNotifyUaslReservation1() {
    String uaslReservationId = "abc-123";
    LinkageInformationNotificationEntity linkageInformationNotificationEntity =
        new LinkageInformationNotificationEntity();
    linkageInformationNotificationEntity.setUaslReservationId(uaslReservationId);

    LinkageInformationNotificationDto notificationDto = new LinkageInformationNotificationDto();
    notificationDto.setUaslReservationId(uaslReservationId);

    WebUaslReservationDto uaslReservationDto = new WebUaslReservationDto();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(linkageInformationNotificationEntity,
          LinkageInformationNotificationDto.class)).thenReturn(notificationDto);

      when(webUaslReservationLogic.getUaslReservationForDb(uaslReservationId))
          .thenReturn(uaslReservationDto);
      doNothing().when(ididLogic).upsert(uaslReservationDto, notificationDto);

      assertDoesNotThrow(
          () -> service.notifyUaslReservation(linkageInformationNotificationEntity));
    }
  }

}
