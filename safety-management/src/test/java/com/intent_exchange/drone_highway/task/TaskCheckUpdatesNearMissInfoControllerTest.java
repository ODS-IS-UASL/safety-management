package com.intent_exchange.drone_highway.task;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.service.CheckUpdatesMearMissInfoTaskService;

/**
 * TaskCheckUpdatesNearMissInfoControllerのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class TaskCheckUpdatesNearMissInfoControllerTest {

  /** ClockクラスのMock */
  @Mock
  private Clock clock;
  
  /** CheckUpdatesMearMissInfoTaskServiceクラスのMock */
  @Mock
  private CheckUpdatesMearMissInfoTaskService service;
  
  /** テスト対象クラス */
  @InjectMocks
  private TaskCheckUpdatesNearMissInfoController controller;
  
  @Test
  @DisplayName("ヒヤリハット情報の更新確認に関するタスクを実行")
  void testPerformTask1() {
    
    // モックされた現在の時刻を設定(2025-1-1 00:00:00)
    ZonedDateTime mockZonedDateTime = ZonedDateTime.of(2025, 1, 1, 0, 0, 0, 0, ZoneId.of("UTC"));
    when(clock.instant()).thenReturn(mockZonedDateTime.toInstant());
    when(clock.getZone()).thenReturn(mockZonedDateTime.getZone());
    
    // メソッドの実行
    controller.performTask();
    
    // 結果の検証
    // 前月中(2024-12-1 00:00:00～2024-12-31 23:59:59）を対象にヒヤリハット情報の更新確認が実施される
    verify(service).checkUpdatesNearMissInfo(
        eq(LocalDateTime.of(2024, 12, 1, 0, 0, 0)),
        eq(LocalDateTime.of(2024, 12, 31, 23, 59, 59)));
  }

}