package com.intent_exchange.uasl.logic.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.time.Clock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.intent_exchange.uasl.dto.request.MonitoringNotificationDestinationRequestDto;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * 空域デジタルツイン 第三者立入監視情報への通信用ロジックのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class WebMonitoringNotificationDestinationLogicTest {

  @Mock
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  @Mock
  private Clock clock; // Clockクラスのモック

  @InjectMocks
  private WebMonitoringNotificationDestinationLogic logic;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    String testDeleteMonitoringNotificationDestinationDeletionUrl = PropertyUtil.getProperty("delete.monitoring.notification.destination.deletion.url");
    Field monitoringNotificationDestinationDeletionUrlField =
        WebMonitoringNotificationDestinationLogic.class.getDeclaredField("deleteMonitoringNotificationDestinationDeletionUrl");
    monitoringNotificationDestinationDeletionUrlField.setAccessible(true);
    monitoringNotificationDestinationDeletionUrlField.set(logic, testDeleteMonitoringNotificationDestinationDeletionUrl);

    String testMonitoringNotificationDestinationRegistrationUrl = PropertyUtil.getProperty("post.monitoring.notification.destination.registration.url");
    Field monitoringNotificationDestinationRegistrationUrlField =
        WebMonitoringNotificationDestinationLogic.class.getDeclaredField("postMonitoringNotificationDestinationRegistrationUrl");
    monitoringNotificationDestinationRegistrationUrlField.setAccessible(true);
    monitoringNotificationDestinationRegistrationUrlField.set(logic, testMonitoringNotificationDestinationRegistrationUrl);

    String testMonitoringNotificationDestinationChangeUrl = PropertyUtil.getProperty("put.monitoring.notification.destination.change.url");
    Field monitoringNotificationDestinationChangeUrlField =
        WebMonitoringNotificationDestinationLogic.class.getDeclaredField("putMonitoringNotificationDestinationChangeUrl");
    monitoringNotificationDestinationChangeUrlField.setAccessible(true);
    monitoringNotificationDestinationChangeUrlField.set(logic, testMonitoringNotificationDestinationChangeUrl);
  }

  @Test
  @DisplayName("変更があった場合に通知(Subscribe)を行うため、航路の安全管理支援(SafetyManagement)の通知先をデータプロバイダに登録 正常終了")
  void testRegisterMonitoringNotificationDestination1() {
    // 通知先情報DTO
    String endpoint = "https://****/++++/";
    MonitoringNotificationDestinationRequestDto monitoringNotificationDestinationRequestDto =
        new MonitoringNotificationDestinationRequestDto();
    monitoringNotificationDestinationRequestDto.setEndPoint(endpoint);
    // 通知先の登録リクエストのレスポンス(201:正常終了)
    ResponseEntity<Void> res = ResponseEntity.status(HttpStatus.CREATED).build();
    // 通知先の登録リクエストURL
    String url =
        PropertyUtil.getProperty("post.monitoring.notification.destination.registration.url");
    // 通知先の登録リクエスト送信処理のMock化
    when(restTemplate.postForObject(url, monitoringNotificationDestinationRequestDto,
        ResponseEntity.class)).thenReturn(res);
    // エラーのthrow無く終了することを確認
    assertDoesNotThrow(() -> logic
        .registerMonitoringNotificationDestination(monitoringNotificationDestinationRequestDto));
  }

  @Test
  @DisplayName("データプロバイダに登録した情報変更通知先を変更する 正常終了")
  void testChangeMonitoringNotificationDestination1() {
    // 通知先情報DTO
    String endpoint = "https://****/++++/";
    MonitoringNotificationDestinationRequestDto monitoringNotificationDestinationRequestDto =
        new MonitoringNotificationDestinationRequestDto();
    monitoringNotificationDestinationRequestDto.setEndPoint(endpoint);
    // 通知先の変更リクエストURL
    String url = PropertyUtil.getProperty("put.monitoring.notification.destination.change.url");
    // 通知先の変更リクエスト送信処理のMock化（戻り値無しのためdoNothing）
    doNothing().when(restTemplate).put(url, monitoringNotificationDestinationRequestDto);
    // エラーのthrow無く終了することを確認
    assertDoesNotThrow(() -> logic
        .changeMonitoringNotificationDestination(monitoringNotificationDestinationRequestDto));
  }

  @Test
  @DisplayName("データプロバイダに登録した情報変更通知先を削除する 正常終了")
  void testDeleteMonitoringNotificationDestination1() {
    // 通知先の変更リクエストURL
    String url =
        PropertyUtil.getProperty("delete.monitoring.notification.destination.deletion.url");
    // 通知先の変更リクエスト送信処理のMock化（戻り値無しのためdoNothing）
    doNothing().when(restTemplate).delete(url);
    // エラーのthrow無く終了することを確認
    assertDoesNotThrow(() -> logic.deleteMonitoringNotificationDestination());
  }

}
