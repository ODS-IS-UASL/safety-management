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
import com.intent_exchange.drone_highway.entity.MonitoringNotificationDestinationRequestEntity;
import com.intent_exchange.drone_highway.service.MonitoringNotificationService;

/**
 * 安全管理支援(A-1-4) 第三者立入監視情報コントローラのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MonitoringNotificationController.class)
class MonitoringNotificationControllerTest {

  @MockBean
  private MonitoringNotificationService service;

  @InjectMocks
  private MonitoringNotificationController controller;

  @Test
  @DisplayName("第三者立入監視情報の通知先の登録")
  void testRegisterMonitoringNotificationDestination1() {
    // リクエストEntity
    MonitoringNotificationDestinationRequestEntity monitoringNotificationDestinationRequestEntity =
        new MonitoringNotificationDestinationRequestEntity();
    // Serviceの処理をMock化（戻り値無しのためdoNothing）
    doNothing().when(service)
        .registerMonitoringNotificationDestination(monitoringNotificationDestinationRequestEntity);
    // テスト対象のメソッド実行
    ResponseEntity<Void> response = controller
        .registerMonitoringNotificationDestination(monitoringNotificationDestinationRequestEntity);
    // 結果確認
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  @DisplayName("第三者立入監視情報の通知先の変更")
  void testChangeMonitoringNotificationDestination1() {
    // リクエストEntity
    MonitoringNotificationDestinationRequestEntity monitoringNotificationDestinationRequestEntity =
        new MonitoringNotificationDestinationRequestEntity();
    // Serviceの処理をMock化（戻り値無しのためdoNothing）
    doNothing().when(service)
        .changeMonitoringNotificationDestination(monitoringNotificationDestinationRequestEntity);
    // テスト対象のメソッド実行
    ResponseEntity<Void> response = controller
        .changeMonitoringNotificationDestination(monitoringNotificationDestinationRequestEntity);
    // 結果確認
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @DisplayName("第三者立入監視情報の通知先の削除")
  void testDeleteMonitoringNotificationDestination1() {
    // Serviceの処理をMock化（戻り値無しのためdoNothing）
    doNothing().when(service).deleteMonitoringNotificationDestination();
    // テスト対象のメソッド実行
    ResponseEntity<Void> response = controller.deleteMonitoringNotificationDestination();
    // 結果確認
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }
}
