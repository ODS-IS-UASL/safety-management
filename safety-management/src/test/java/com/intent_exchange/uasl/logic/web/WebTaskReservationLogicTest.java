package com.intent_exchange.uasl.logic.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.util.PropertyUtil;

/** WebTaskReservationLogicのテストクラス */

// TestConfigとModelMapperConfigのModelMapperインスタンス作成のbeenが重複してエラーのため、Bean 定義の上書きを許可します。
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")

public class WebTaskReservationLogicTest {

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private WebTaskReservationLogic WebTaskReservationLogic;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.openMocks(this);

    String testNotifyConformityAssessmentResultUrlUrl = PropertyUtil.getProperty("put.notify.conformity.assessment.result.url");
    Field notifyConformityAssessmentResultUrlField =
        WebTaskReservationLogic.class.getDeclaredField("putNotifyConformityAssessmentResultUrl");
    notifyConformityAssessmentResultUrlField.setAccessible(true);
    notifyConformityAssessmentResultUrlField.set(WebTaskReservationLogic, testNotifyConformityAssessmentResultUrlUrl);
  }

  /** 適合性評価結果変化通知APIのURL */
  private static final String PUT_NOTIFYCONFORMIT_YASSESSMENTRESULT_URL =
      PropertyUtil.getProperty("put.notify.conformity.assessment.result.url");

  @Test
  @DisplayName("適合性評価結果変化通知_正常終了")
  public void testPutTaskResevasion1() {
    // テストデータの準備
    ConformityAssessmentResultDto dto = new ConformityAssessmentResultDto();
    dto.setUaslReservationId("123");
    dto.setMessage("Test message");
    dto.setEvaluationResults(true);

    // メソッドの実行
    WebTaskReservationLogic.putTaskReservation(dto);

    // リクエストが正しく送信されたことを確認
    verify(restTemplate).put(eq(PUT_NOTIFYCONFORMIT_YASSESSMENTRESULT_URL), any());
  }
  @Test
  @DisplayName("適合性評価結果変化通知_正常終了")
  public void testPutTaskResevasion2() {
    // テストデータの準備
    ConformityAssessmentResultDto dto = new ConformityAssessmentResultDto();
    dto.setUaslReservationId("123");
    dto.setMessage("Test message");
    dto.setEvaluationResults(false);

    // メソッドの実行
    WebTaskReservationLogic.putTaskReservation(dto);

    // リクエストが正しく送信されたことを確認
    verify(restTemplate).put(eq(PUT_NOTIFYCONFORMIT_YASSESSMENTRESULT_URL), any());
  }

  @Test
  @DisplayName("適合性評価結果変化通知_クライアントエラー")
  public void testPutTaskResevasion3() {
    // テストデータの準備
    ConformityAssessmentResultDto dto = new ConformityAssessmentResultDto();
    dto.setUaslReservationId("123");
    dto.setMessage("Test message");
    dto.setEvaluationResults(true);

    // モックのレスポンスを設定
    doThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST)).when(restTemplate)
        .put(anyString(), any());

    // メソッドの実行と例外の確認
    assertThrows(HttpClientErrorException.class, () -> {
      WebTaskReservationLogic.putTaskReservation(dto);
    });

    // リクエストが正しく送信されたことを確認
    verify(restTemplate).put(eq(PUT_NOTIFYCONFORMIT_YASSESSMENTRESULT_URL), any());
  }

  @Test
  @DisplayName("適合性評価結果変化通知_サーバエラー")
  public void testPutTaskResevasion4() {
    // テストデータの準備
    ConformityAssessmentResultDto dto = new ConformityAssessmentResultDto();
    dto.setUaslReservationId("123");
    dto.setMessage("Test message");
    dto.setEvaluationResults(true);

    // モックのレスポンスを設定
    doThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)).when(restTemplate)
        .put(anyString(), any());

    // メソッドの実行と例外の確認
    assertThrows(HttpServerErrorException.class, () -> {
      WebTaskReservationLogic.putTaskReservation(dto);
    });

    // リクエストが正しく送信されたことを確認
    verify(restTemplate).put(eq(PUT_NOTIFYCONFORMIT_YASSESSMENTRESULT_URL), any());
  }
}
