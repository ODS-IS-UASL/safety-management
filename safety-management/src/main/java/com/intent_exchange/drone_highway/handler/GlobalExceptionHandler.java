/*
 * Copyright 2025 Intent Exchange, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.intent_exchange.drone_highway.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.intent_exchange.drone_highway.entity.ErrorResponseEntity;
import com.intent_exchange.drone_highway.exception.ConformityAssessmentException;
import com.intent_exchange.drone_highway.exception.CurrentLocationException;
import com.intent_exchange.drone_highway.exception.DroneHighwayException;
import com.intent_exchange.drone_highway.exception.WeatherWindSpeedException;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/** グローバル例外ハンドラークラス。 アプリケーション全体で発生する例外を処理し、カスタムエラーレスポンスを返します。 */
@ControllerAdvice
public class GlobalExceptionHandler {

  /** ロガー */
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * ハンドラが見つからない場合の例外を処理します。
   *
   * @param ex NoHandlerFoundException
   * @return カスタムエラーレスポンスを含むResponseEntity
   */
  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorResponseEntity> handleNoHandlerFoundException(
      NoHandlerFoundException ex) {
    ErrorResponseEntity errorResponse =
        createResultEntity(Integer.toString(HttpStatus.NOT_FOUND.value()),
            PropertyUtil.getProperty("404.error.message"), "");

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * バリデーション例外を処理します。
   *
   * @param ex MethodArgumentNotValidException
   * @return バリデーションエラーメッセージを含むカスタムエラーレスポンスを返すResponseEntity
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseEntity> handleValidationException(
      MethodArgumentNotValidException ex) {
    ErrorResponseEntity result =
        createResultEntity(Integer.toString(HttpStatus.BAD_REQUEST.value()),
            PropertyUtil.getProperty("400.error.message"),
            ex.getBindingResult().getFieldError().getDefaultMessage());
    return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
  }

  /**
   * 現在のドローンの位置情報取得に関連する例外を処理します。
   *
   * @param ex CurrentLocationException
   * @return カスタムエラーレスポンスを含むResponseEntity
   */
  @ExceptionHandler(CurrentLocationException.class)
  public ResponseEntity<ErrorResponseEntity> handleCurrentLocationException(
      CurrentLocationException ex) {
    logger.error(ex.getMessage());
    ErrorResponseEntity errorResponse = createResultEntity(
        Integer.toString(HttpStatus.NOT_FOUND.value()), ex.getMessage(),
        PropertyUtil.getProperty("error.get.current.location.description"));

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  /**
   * 適合性評価に関連する例外を処理します。
   *
   * @param ex ConformityAssessmentException
   * @return カスタムエラーレスポンスを含むResponseEntity
   */
  @ExceptionHandler(ConformityAssessmentException.class)
  public ResponseEntity<ErrorResponseEntity> handleConformityAssessmentException(
      ConformityAssessmentException ex) {
    logger.error(ex.getMessage());
    ErrorResponseEntity errorResponse =
        createResultEntity(Integer.toString(HttpStatus.BAD_REQUEST.value()), ex.getMessage(), "");

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * 天候・風速情報取得に関連する例外を処理します。
   *
   * @param ex WeatherWindSpeedException
   * @return カスタムエラーレスポンスを含むResponseEntity
   */
  @ExceptionHandler(WeatherWindSpeedException.class)
  public ResponseEntity<ErrorResponseEntity> handleWeatherWindSpeedException(
      WeatherWindSpeedException ex) {
    logger.error(ex.getMessage(), ex);
    ErrorResponseEntity errorResponse =
        createResultEntity(Integer.toString(HttpStatus.BAD_REQUEST.value()),
            PropertyUtil.getProperty("400.error.message"), ex.getMessage());

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  /**
   * ドローン航路用の例外を処理します。
   *
   * @param ex MethodArgumentNotValidException
   * @return バリデーションエラーメッセージを含むカスタムエラーレスポンスを返すResponseEntity
   */
  @ExceptionHandler(DroneHighwayException.class)
  public ResponseEntity<ErrorResponseEntity> handleDroneHighwayException(DroneHighwayException ex) {
    logger.error(ex.getMessage(), ex);
    if (ex.getEntity() == null) {
      ex.setEntity(createResultEntity(Integer.toString(HttpStatus.BAD_REQUEST.value()),
          PropertyUtil.getProperty("400.error.message"),
          ex.getMessage() + ex.getCause() == null ? "" : " : " + ex.getCause().getMessage()));
    }
    return new ResponseEntity<>(ex.getEntity(), HttpStatus.BAD_REQUEST);
  }

  /**
   * 一般的な例外を処理します。
   *
   * @param ex Exception
   * @return カスタムエラーレスポンスを含むResponseEntity
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponseEntity> handleException(Exception ex) {
    logger.error("Internal server error: ", ex);
    ErrorResponseEntity errorResponse =
        createResultEntity(Integer.toString(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            PropertyUtil.getProperty("500.error.message"), ex.getMessage());

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }


  /**
   * ErrorResponseEntityオブジェクトを作成します。
   *
   * @param code エラーコード
   * @param message エラーメッセージ
   * @param description エラーの詳細説明
   * @return SampleResultEntityオブジェクト
   */
  private ErrorResponseEntity createResultEntity(String code, String message, String description) {
    ErrorResponseEntity rtn = new ErrorResponseEntity();
    rtn.setCode(code);
    rtn.setMessage(message);
    rtn.setDescription(description);
    return rtn;
  }
}

