package com.intent_exchange.drone_highway.controller;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.intent_exchange.drone_highway.entity.LinkageInformationNotificationEntity;
import com.intent_exchange.drone_highway.service.IdidService;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(IdidController.class)
class IdidControllerTest {

  @MockBean
  private IdidService service;

  @InjectMocks
  private IdidController controller;

  @Test
  @DisplayName("航路予約情報の通知 正常系")
  void testNotifyLinkageInformation1() {
    LinkageInformationNotificationEntity linkageInformationNotificationEntity =
        new LinkageInformationNotificationEntity();

    ResponseEntity<Void> response =
        controller.notifyLinkageInformation(linkageInformationNotificationEntity);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

}
