package com.intent_exchange.drone_highway.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.intent_exchange.drone_highway.entity.ThirdPartyEntryNotificationEntity;
import com.intent_exchange.drone_highway.service.ThirdPartyEntryNotificationService;

/**
 * 第三者立入監視情報変更通知のコントローラのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ThirdPartyEntryNotificationController.class)
class ThirdPartyEntryNotificationControllerTest {


  @MockBean
  private ThirdPartyEntryNotificationService service;

  @InjectMocks
  private ThirdPartyEntryNotificationController controller;

  @Test
  @DisplayName("第三者立入監視情報変更通知")
  void testNotifyThirdPartyEntry() {
    // リクエストEntity
    ThirdPartyEntryNotificationEntity thirdPartyEntryNotificationEntity =
        new ThirdPartyEntryNotificationEntity();
    // Serviceの処理をMock化（戻り値無しのためdoNothing）
    doNothing().when(service).notifyThirdPartyEntry(thirdPartyEntryNotificationEntity);
    // テスト対象のメソッド実行
    ResponseEntity<Void> response =
        controller.notifyThirdPartyEntry(thirdPartyEntryNotificationEntity);
    // 結果確認
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

}
