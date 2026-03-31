package com.intent_exchange.uasl.logic;

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
import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationDto;
import com.intent_exchange.uasl.dto.request.LinkageInformationNotificationUasIdDto;
import com.intent_exchange.uasl.dto.request.LinkageSubscriptionIdDto;
import com.intent_exchange.uasl.dto.request.WebSubscriptionRegistrationDto;
import com.intent_exchange.uasl.dto.response.WebUaslReservationDto;
import com.intent_exchange.uasl.logic.web.WebSubscriptionRegistrationAndDeletionLogic;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(IdidLogic.class)
class IdidLogicTest {

  @MockBean
  private UaslReservationLogic uaslReservationLogic;

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
    WebUaslReservationDto webUaslReservationDto = new WebUaslReservationDto();

    List<String> uaslSectionIds = new ArrayList<>();
    uaslSectionIds.add("uaslSectionIds");

    webUaslReservationDto.setUaslReservationId("uaslReservationId");
    webUaslReservationDto.setUaslSectionIds(uaslSectionIds);
    webUaslReservationDto.setOperatorId("operatorId");
    webUaslReservationDto.setStartAt("startDateTime");
    webUaslReservationDto.setEndAt("endDateTime");
    webUaslReservationDto.setReservedAt("reservationDateTime");

    LinkageInformationNotificationDto linkageInformationNotificationDto =
        new LinkageInformationNotificationDto();
    LinkageInformationNotificationUasIdDto uasId = new LinkageInformationNotificationUasIdDto();

    uasId.setSerialNumber("serialNumber01");
    uasId.setRegistrationId("registrationId01");
    uasId.setUtmId("utmId01");
    uasId.setSpecificSessionId("specificSessionId01");

    linkageInformationNotificationDto.setUaslReservationId("uaslReservationId01");
    linkageInformationNotificationDto.setUasId(uasId);

    LinkageSubscriptionIdDto linkageSubscriptionIdDto = new LinkageSubscriptionIdDto();
    when(subscriptionDataLogic.getAreaInfoAndUaslId(anyString()))
        .thenReturn(linkageSubscriptionIdDto);

    Map<String, String> subscriptionId = new HashMap<>();
    subscriptionId.put("subscription_id", "test");
    when(webSubscriptionRegistrationAndDeletionLogic
        .registerSubscription(any(WebSubscriptionRegistrationDto.class)))
            .thenReturn(subscriptionId);

    doNothing().when(uaslReservationLogic).upsert(webUaslReservationDto);
    doNothing().when(remoteDataLogic).upsert(linkageInformationNotificationDto);
    doNothing().when(subscriptionDataLogic).upsert(linkageSubscriptionIdDto);
    assertDoesNotThrow(
        () -> logic.upsert(webUaslReservationDto, linkageInformationNotificationDto));
  }

  @Test
  @DisplayName("DBから情報を削除する")
  void testDelete1() {
    String uaslReservationId = "uaslReservationId01";
    doNothing().when(remoteDataLogic).delete(uaslReservationId);
    LinkageSubscriptionIdDto linkageSubscriptionIdDto = new LinkageSubscriptionIdDto();
    linkageSubscriptionIdDto.setSubscriptionId("subscriptionId01");
    when(subscriptionDataLogic.getSubscriptionData(uaslReservationId))
        .thenReturn(linkageSubscriptionIdDto);
    doNothing().when(webSubscriptionRegistrationAndDeletionLogic)
        .deleteSubscription(linkageSubscriptionIdDto.getSubscriptionId());
    doNothing().when(subscriptionDataLogic).delete(uaslReservationId);
    doNothing().when(uaslReservationLogic).delete(uaslReservationId);
    assertDoesNotThrow(() -> logic.delete(uaslReservationId));
  }

}
