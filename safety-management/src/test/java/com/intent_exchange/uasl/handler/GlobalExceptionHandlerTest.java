package com.intent_exchange.uasl.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;

import com.intent_exchange.uasl.entity.ErrorResponseEntity;
import com.intent_exchange.uasl.util.PropertyUtil;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

  @InjectMocks
  private GlobalExceptionHandler globalExceptionHandler;

  @Test
  @DisplayName("必須リクエストパラメータ欠落例外（MissingServletRequestParameterException）のハンドリングテスト")
  void testHandleMissingServletRequestParameterException() {
    // Arrange
    String parameterName = "reservationId";
    String parameterType = "String";
    MissingServletRequestParameterException exception =
        new MissingServletRequestParameterException(parameterName, parameterType);

    String expectedErrorCode = String.valueOf(HttpStatus.BAD_REQUEST.value());
    String expectedErrorMessage = "Bad Request Error";

    // PropertyUtilのstaticメソッドをモック化
    try (MockedStatic<PropertyUtil> mockedPropertyUtil = mockStatic(PropertyUtil.class)) {
      mockedPropertyUtil.when(() -> PropertyUtil.getProperty("400.error.message"))
          .thenReturn(expectedErrorMessage);

      // Act
      ResponseEntity<ErrorResponseEntity> responseEntity =
          globalExceptionHandler.handleMissingServletRequestParameterException(exception);

      // Assert
      assertNotNull(responseEntity, "レスポンスはnullであってはなりません");
      assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "HTTPステータスコードは400である必要があります");

      ErrorResponseEntity body = responseEntity.getBody();
      assertNotNull(body, "レスポンスボディはnullであってはなりません");
      assertEquals(expectedErrorCode, body.getCode(), "エラーコードが一致しません");
      assertEquals(expectedErrorMessage, body.getMessage(), "エラーメッセージが一致しません");
      assertEquals(exception.getMessage(), body.getDescription(), "エラー詳細（description）が例外メッセージと一致しません");

      // モックの検証
      mockedPropertyUtil.verify(() -> PropertyUtil.getProperty("400.error.message"), times(1));
    }
  }
}