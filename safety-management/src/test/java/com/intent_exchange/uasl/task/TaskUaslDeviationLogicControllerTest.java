package com.intent_exchange.uasl.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.service.UaslDeviationService;
import com.intent_exchange.uasl.service.AircraftTypeDeviationService;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/*  
 * 航路逸脱情報の登録に関するコントローラクラスのテストを提供します。
 */
@ExtendWith(MockitoExtension.class)
class TaskUaslDeviationLogicControllerTest {

  @InjectMocks
  private TaskUaslDeviationLogicController controller;

  @Mock
  private UaslDeviationService service;

  @Mock
  private AircraftTypeDeviationService aircraftTypeDeviationService;

  @Test
  @DisplayName("performTaskメソッドのテスト_呼び出されている")
  void testPerformTask() {

    // タスクの実行
    controller.performTask();

    // service.conformityAssessmentが呼び出されていう事を確認する
    verify(service).registerRouteDeviationInfo();
  }

  @Test
  @DisplayName("performAircraftTypeDeviationTaskメソッドのテスト_前月の期間で呼び出される")
  void testPerformAircraftTypeDeviationTask() {
    ZonedDateTime nowUtc = ZonedDateTime.now(ZoneOffset.UTC);
    ZonedDateTime startOfMonth = nowUtc.withDayOfMonth(1).truncatedTo(ChronoUnit.DAYS);
    ZonedDateTime startOfPrevMonth = startOfMonth.minus(1, ChronoUnit.MONTHS);

    controller.performAircraftTypeDeviationTask();

    ArgumentCaptor<Instant> startCaptor = ArgumentCaptor.forClass(Instant.class);
    ArgumentCaptor<Instant> endCaptor = ArgumentCaptor.forClass(Instant.class);
    verify(aircraftTypeDeviationService)
        .registerAircraftTypeDeviationForPeriod(startCaptor.capture(), endCaptor.capture());

    assertEquals(startOfPrevMonth.toInstant(), startCaptor.getValue());
    assertEquals(startOfMonth.toInstant(), endCaptor.getValue());
  }

}
