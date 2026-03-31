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
import com.intent_exchange.uasl.dto.request.MonitoringNotificationDestinationRequestDto;
import com.intent_exchange.uasl.entity.MonitoringNotificationDestinationRequestEntity;
import com.intent_exchange.uasl.logic.web.WebMonitoringNotificationDestinationLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 安全管理支援(SafetyManagement) 第三者立入監視情報関連サービスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class MonitoringNotificationServiceTest {

  @Mock
  private WebMonitoringNotificationDestinationLogic webMonitoringNotificationDestinationLogic;

  @InjectMocks
  private MonitoringNotificationService service;

  @Test
  @DisplayName("変更があった場合に通知(Subscribe)を行うため、航路の安全管理支援(SafetyManagement)の通知先をデータプロバイダに登録する")
  void testRegisterMonitoringNotificationDestination1() {
    // 通知先
    String endpoint = "https://****/++++/";
    // 通知先情報Entity
    MonitoringNotificationDestinationRequestEntity monitoringNotificationDestinationRequestEntity =
        new MonitoringNotificationDestinationRequestEntity();
    monitoringNotificationDestinationRequestEntity.setEndPoint(endpoint);
    // 通知先情報DTO
    MonitoringNotificationDestinationRequestDto monitoringNotificationDestinationRequestDto =
        new MonitoringNotificationDestinationRequestDto();
    monitoringNotificationDestinationRequestDto.setEndPoint(endpoint);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      // Logicの処理実行時のEntity→DTO変換処理をMock化
      mockedStatic
          .when(() -> ModelMapperUtil.map(monitoringNotificationDestinationRequestEntity,
              MonitoringNotificationDestinationRequestDto.class))
          .thenReturn(monitoringNotificationDestinationRequestDto);
      // Logicの処理実行をMock化（戻り値無しのためdoNothing）
      doNothing().when(webMonitoringNotificationDestinationLogic)
          .registerMonitoringNotificationDestination(monitoringNotificationDestinationRequestDto);
      // エラーのthrow無く終了することを確認
      assertDoesNotThrow(() -> service.registerMonitoringNotificationDestination(
          monitoringNotificationDestinationRequestEntity));
    }
  }

  @Test
  @DisplayName("データプロバイダに登録した情報変更通知先を変更する")
  void testChangeMonitoringNotificationDestination1() {
    // 通知先
    String endpoint = "https://****/++++/";
    // 通知先情報Entity
    MonitoringNotificationDestinationRequestEntity monitoringNotificationDestinationRequestEntity =
        new MonitoringNotificationDestinationRequestEntity();
    monitoringNotificationDestinationRequestEntity.setEndPoint(endpoint);
    // 通知先情報DTO
    MonitoringNotificationDestinationRequestDto monitoringNotificationDestinationRequestDto =
        new MonitoringNotificationDestinationRequestDto();
    monitoringNotificationDestinationRequestDto.setEndPoint(endpoint);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      // Logicの処理実行時のEntity→DTO変換処理をMock化
      mockedStatic
          .when(() -> ModelMapperUtil.map(monitoringNotificationDestinationRequestEntity,
              MonitoringNotificationDestinationRequestDto.class))
          .thenReturn(monitoringNotificationDestinationRequestDto);
      // Logicの処理実行をMock化（戻り値無しのためdoNothing）
      doNothing().when(webMonitoringNotificationDestinationLogic)
          .changeMonitoringNotificationDestination(monitoringNotificationDestinationRequestDto);
      // エラーのthrow無く終了することを確認
      assertDoesNotThrow(() -> service
          .changeMonitoringNotificationDestination(monitoringNotificationDestinationRequestEntity));
    }
  }

  @Test
  @DisplayName("データプロバイダに登録した情報変更通知先を削除する")
  void testDeleteMonitoringNotificationDestination1() {
    // Logicの処理実行をMock化（戻り値無しのためdoNothing）
    doNothing().when(webMonitoringNotificationDestinationLogic)
        .deleteMonitoringNotificationDestination();
    // エラーのthrow無く終了することを確認
    assertDoesNotThrow(() -> service.deleteMonitoringNotificationDestination());
  }
}
