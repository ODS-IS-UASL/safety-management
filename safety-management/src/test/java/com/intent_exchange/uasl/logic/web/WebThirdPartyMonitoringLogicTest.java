package com.intent_exchange.uasl.logic.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.uasl.dto.request.ThirdPartyEntryRequestDto;
import com.intent_exchange.uasl.dto.response.ThirdPartyMonitoringInformationResponseDto;
import com.intent_exchange.uasl.util.PropertyUtil;

/**
 * 空域デジタルツイン 第三者立入監視情報への通信用ロジックのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class WebThirdPartyMonitoringLogicTest {

  @Mock
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  @Mock
  private Clock clock; // Clockクラスのモック

  @InjectMocks
  private WebThirdPartyMonitoringLogic logic;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    String testThirdPartyEntryNotificationRegistrationUrl =
        PropertyUtil.getProperty("put.third.party.entry.notification.registration.url");
    Field thirdPartyEntryNotificationRegistrationUrlField =
        WebThirdPartyMonitoringLogic.class.getDeclaredField(
            "putThirdPartyEntryNotificationRegistrationUrl");
    thirdPartyEntryNotificationRegistrationUrlField.setAccessible(true);
    thirdPartyEntryNotificationRegistrationUrlField.set(logic, testThirdPartyEntryNotificationRegistrationUrl);

    String testThirdPartyMonitoringInformationUrl =
        PropertyUtil.getProperty("get.third.party.monitoring.information.url");
    Field thirdPartyMonitoringInformationUrlField =
        WebThirdPartyMonitoringLogic.class.getDeclaredField(
            "getThirdPartyMonitoringInformationUrl");
    thirdPartyMonitoringInformationUrlField.setAccessible(true);
    thirdPartyMonitoringInformationUrlField.set(logic, testThirdPartyMonitoringInformationUrl);

    String testPostThirdPartyEntryNotificationRegistrationUrl =
        PropertyUtil.getProperty("post.third.party.entry.notification.registration.url");
    Field postThirdPartyEntryNotificationRegistrationUrlField =
        WebThirdPartyMonitoringLogic.class.getDeclaredField(
            "postThirdPartyEntryNotificationRegistrationUrl");
    postThirdPartyEntryNotificationRegistrationUrlField.setAccessible(true);
    postThirdPartyEntryNotificationRegistrationUrlField.set(logic, testPostThirdPartyEntryNotificationRegistrationUrl);

    String testDeleteThirdPartyEntryNotificationRegistrationUrl =
        PropertyUtil.getProperty("delete.third.party.entry.notification.registration.url");
    Field deleteThirdPartyEntryNotificationRegistrationUrlField =
        WebThirdPartyMonitoringLogic.class.getDeclaredField(
            "deleteThirdPartyEntryNotificationRegistrationUrl");
    deleteThirdPartyEntryNotificationRegistrationUrlField.setAccessible(true);
    deleteThirdPartyEntryNotificationRegistrationUrlField.set(logic, testDeleteThirdPartyEntryNotificationRegistrationUrl);
  }

  @Test
  @DisplayName("変更があった場合に通知(Subscribe)を行うため、航路の安全管理支援(SafetyManagement)の通知先をデータプロバイダに登録 正常終了")
  void testRegistrationThirdPartyEntryNotification1() {
    // 通知先情報DTO
    String endpoint = "https://****/++++/";
    ThirdPartyEntryRequestDto thirdPartyEntryRequestDto = new ThirdPartyEntryRequestDto();
    thirdPartyEntryRequestDto.setEndPoint(endpoint);
    // 通知先の登録リクエストのレスポンス(201:正常終了)
    ResponseEntity<Void> res = ResponseEntity.status(HttpStatus.CREATED).build();
    // 通知先の登録リクエストURL
    String url = PropertyUtil.getProperty("post.third.party.entry.notification.registration.url");
    // 通知先の登録リクエスト送信処理のMock化
    when(restTemplate.postForObject(url, thirdPartyEntryRequestDto, ResponseEntity.class))
        .thenReturn(res);
    // エラーのthrow無く終了することを確認
    assertDoesNotThrow(
        () -> logic.registrationThirdPartyEntryNotification(thirdPartyEntryRequestDto));
  }

  @Test
  @DisplayName("データプロバイダに登録した情報変更通知先を変更する 正常終了")
  void testChangeThirdPartyEntryNotification1() {
    // 通知先情報DTO
    String endpoint = "https://****/++++/";
    ThirdPartyEntryRequestDto thirdPartyEntryRequestDto = new ThirdPartyEntryRequestDto();
    thirdPartyEntryRequestDto.setEndPoint(endpoint);
    // 通知先の変更リクエストURL
    String url = PropertyUtil.getProperty("put.third.party.entry.notification.registration.url");
    // 通知先の変更リクエスト送信処理のMock化（戻り値無しのためdoNothing）
    doNothing().when(restTemplate).put(url, thirdPartyEntryRequestDto);
    // エラーのthrow無く終了することを確認
    assertDoesNotThrow(() -> logic.changeThirdPartyEntryNotification(thirdPartyEntryRequestDto));
  }

  @Test
  @DisplayName("データプロバイダに登録した情報変更通知先を削除する 正常終了")
  void testDeletionThirdPartyEntryNotification1() {
    // 通知先の変更リクエストURL
    String url = PropertyUtil.getProperty("delete.third.party.entry.notification.registration.url");
    // 通知先の変更リクエスト送信処理のMock化（戻り値無しのためdoNothing）
    doNothing().when(restTemplate).delete(url);
    // エラーのthrow無く終了することを確認
    assertDoesNotThrow(() -> logic.deletionThirdPartyEntryNotification());
  }

  @Test
  @DisplayName("指定されたエリア情報と予約日時に基づいて第三者立入監視情報を取得する 正常終了")
  void testGetMonitoringInformation1() {
    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2025, 2, 11, 13, 30, 0, 0, ZoneId.of("UTC"));
    when(clock.getZone()).thenReturn(mockZonedNow.getZone());

    // URL生成
    LocalDateTime reservationDateTime = LocalDateTime.of(2024, 12, 17, 12, 0);
    String url = PropertyUtil.getProperty("get.third.party.monitoring.information.url");
    String latStart = "23.4567";
    String latEnd = "89.0123";
    String lonEnd = "123.8901";
    String lonStart = "123.4567";
    int maxItemCount = 1;
    String apiKey = "QKRBRxWv7k4Tujh3UgFYjvEbcCr4NcD8zsbgi0pc";
    String contentType = "application/json";
    String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("lat1", latStart)
        .queryParam("lon1", lonStart)
        .queryParam("lat2", latEnd)
        .queryParam("lon2", lonEnd)
        .queryParam("timestamp",
            reservationDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
        .queryParam("maxItemCount", maxItemCount)
        .toUriString();
    HttpHeaders headers = new HttpHeaders();
    headers.set("x-api-key", apiKey);
    headers.set("Content-Type", contentType);
    HttpEntity<String> entity = new HttpEntity<>(headers);
    // 第三者立入監視情報のレスポンス
    ThirdPartyMonitoringInformationResponseDto thirdPartyMonitoringInformationResponseDto =
        new ThirdPartyMonitoringInformationResponseDto();
    List<Object> features = new ArrayList<>();
    features.add("feature1");
    thirdPartyMonitoringInformationResponseDto.setFeatures(features);

    ResponseEntity<ThirdPartyMonitoringInformationResponseDto> responseEntity =
        ResponseEntity.ok(thirdPartyMonitoringInformationResponseDto);
    when(restTemplate.exchange(urlTemplate, HttpMethod.GET, entity,
        ThirdPartyMonitoringInformationResponseDto.class)).thenReturn(responseEntity);

    // テスト実行
    ThirdPartyMonitoringInformationResponseDto result = logic.getMonitoringInformation(latStart,
        lonStart, latEnd, lonEnd, reservationDateTime, maxItemCount, apiKey);

    // 結果確認 レスポンスのfeaturesに"feature1"が含まれていることを確認
    assertNotNull(result);
    assertEquals("feature1", result.getFeatures().get(0));
  }
}
