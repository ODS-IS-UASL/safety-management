package com.intent_exchange.uasl.logic.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.uasl.dao.SubscriptionDataMapper;
import com.intent_exchange.uasl.dto.request.WebSubscriptionRegistrationDto;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * UTM への通信用ロジックのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class WebSubscriptionRegistrationAndDeletionLogicTest {

  @Mock
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  @Mock
  private SubscriptionDataMapper subscriptionDataMapper;

  @InjectMocks
  private WebSubscriptionRegistrationAndDeletionLogic logic;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    String testSubscriptionRegistrationAndDeletionUrl = PropertyUtil.getProperty("subscription.registration.and.deletion.url");
    Field subscriptionRegistrationAndDeletionUrlField =
        WebSubscriptionRegistrationAndDeletionLogic.class.getDeclaredField("subscriptionRegistrationAndDeletionUrl");
    subscriptionRegistrationAndDeletionUrlField.setAccessible(true);
    subscriptionRegistrationAndDeletionUrlField.set(logic, testSubscriptionRegistrationAndDeletionUrl);
  }

  @Test
  @DisplayName("通知(Subscribe)を行うため、航路の安全管理支援(SafetyManagement)の通知先を UTM に登録 正常終了")
  void testRegisterSubscription1() {
    // 通知先情報DTO
    String endpoint = "https://****/++++/";
    WebSubscriptionRegistrationDto subscriptionRegistrationDto =
        new WebSubscriptionRegistrationDto();
    subscriptionRegistrationDto.setUasl_base_url(endpoint);
    // 通知先の登録リクエストのレスポンス(201:正常終了)
    Map<String, String> res = new HashMap<>();
    res.put("subscriptionId", "subscriptionId01");
    // 通知先の登録リクエストURL
    String url = PropertyUtil.getProperty("subscription.registration.and.deletion.url");
    // 通知先の登録リクエスト送信処理のMock化
    when(restTemplate.postForObject(url, subscriptionRegistrationDto, Map.class)).thenReturn(res);
    // エラーのthrow無く終了することを確認
    assertDoesNotThrow(() -> logic.registerSubscription(subscriptionRegistrationDto));
  }

  @Test
  @DisplayName("UTM に登録した通知先を削除する 正常終了")
  void testDeleteMonitoringNotificationDestination1() {
    String subscriptionId = "subscriptionId01";
    // 通知先の変更リクエストURL
    String url = PropertyUtil.getProperty("subscription.registration.and.deletion.url");
    String urlTemplate =
        UriComponentsBuilder.fromHttpUrl(url).pathSegment(subscriptionId).toUriString();
    // 通知先の変更リクエスト送信処理のMock化（戻り値無しのためdoNothing）
    doNothing().when(restTemplate).delete(urlTemplate);
    // エラーのthrow無く終了することを確認
    assertDoesNotThrow(() -> logic.deleteSubscription(subscriptionId));
  }
}
