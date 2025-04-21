package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.intent_exchange.drone_highway.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.drone_highway.dto.request.LinkageInformationNotificationUasIdDto;
import com.intent_exchange.drone_highway.dto.request.LinkageSubscriptionIdDto;
import com.intent_exchange.drone_highway.dto.request.WebSubscriptionRegistrationDto;
import com.intent_exchange.drone_highway.dto.response.WebAirwayReservationDto;
import com.intent_exchange.drone_highway.logic.web.WebSubscriptionRegistrationAndDeletionLogic;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(IdidLogic.class)
class IdidLogicTest {

  @MockBean
  private AirwayReservationLogic airwayReservationLogic;

  @MockBean
  private RemoteDataLogic remoteDataLogic;

  @MockBean
  private SubscriptionDataLogic subscriptionDataLogic;

  @MockBean
  private WebSubscriptionRegistrationAndDeletionLogic webSubscriptionRegistrationAndDeletionLogic;

  @InjectMocks
  private IdidLogic logic;

  @Test
  @DisplayName("新しい情報を作成する")
  void testUpsert1() {
    WebAirwayReservationDto webAirwayReservationDto = new WebAirwayReservationDto();

    List<String> airwaySectionIds = new ArrayList<>();
    airwaySectionIds.add("airwaySectionIds");

    webAirwayReservationDto.setAirwayReservationId("airwayReservationId");
    webAirwayReservationDto.setAirwaySectionIds(airwaySectionIds);
    webAirwayReservationDto.setOperatorId("operatorId");
    webAirwayReservationDto.setStartAt("startDateTime");
    webAirwayReservationDto.setEndAt("endDateTime");
    webAirwayReservationDto.setReservedAt("reservationDateTime");

    LinkageInformationNotificationDto linkageInformationNotificationDto =
        new LinkageInformationNotificationDto();
    LinkageInformationNotificationUasIdDto uasId = new LinkageInformationNotificationUasIdDto();

    uasId.setSerialNumber("serialNumber01");
    uasId.setRegistrationId("registrationId01");
    uasId.setUtmId("utmId01");
    uasId.setSpecificSessoionId("specificSessionId01");

    linkageInformationNotificationDto.setAirwayReservationId("airwayReservationId01");
    linkageInformationNotificationDto.setUasId(uasId);

    LinkageSubscriptionIdDto linkageSubscriptionIdDto = new LinkageSubscriptionIdDto();
    when(subscriptionDataLogic.getAreaInfoAndAirwayId(anyString()))
        .thenReturn(linkageSubscriptionIdDto);

    Map<String, String> subscriptionId = new HashMap<>();
    subscriptionId.put("subscription_id", "test");
    when(webSubscriptionRegistrationAndDeletionLogic
        .registerSubscription(any(WebSubscriptionRegistrationDto.class)))
            .thenReturn(subscriptionId);

    doNothing().when(airwayReservationLogic).upsert(webAirwayReservationDto);
    doNothing().when(remoteDataLogic).upsert(linkageInformationNotificationDto);
    doNothing().when(subscriptionDataLogic).upsert(linkageSubscriptionIdDto);
    assertDoesNotThrow(
        () -> logic.upsert(webAirwayReservationDto, linkageInformationNotificationDto));
  }

  @Test
  @DisplayName("DBから情報を削除する")
  void testDelete1() {
    String airwayReservationId = "airwayReservationId01";
    doNothing().when(remoteDataLogic).delete(airwayReservationId);
    LinkageSubscriptionIdDto linkageSubscriptionIdDto = new LinkageSubscriptionIdDto();
    linkageSubscriptionIdDto.setSubscriptionId("subscriptionId01");
    when(subscriptionDataLogic.getSubscriptionData(airwayReservationId))
        .thenReturn(linkageSubscriptionIdDto);
    doNothing().when(webSubscriptionRegistrationAndDeletionLogic)
        .deleteSubscription(linkageSubscriptionIdDto.getSubscriptionId());
    doNothing().when(subscriptionDataLogic).delete(airwayReservationId);
    doNothing().when(airwayReservationLogic).delete(airwayReservationId);
    assertDoesNotThrow(() -> logic.delete(airwayReservationId));
  }

}
