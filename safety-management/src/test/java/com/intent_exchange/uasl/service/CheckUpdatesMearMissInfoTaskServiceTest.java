package com.intent_exchange.uasl.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.NotifyUpdatesNearMissInfoDto;
import com.intent_exchange.uasl.logic.NearMissInfoLogic;
import com.intent_exchange.uasl.logic.mqtt.MqttUpdatesNearMissInfoNotificationLogic;

/**
 * CheckUpdatesMearMissInfoTaskServiceのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class CheckUpdatesMearMissInfoTaskServiceTest {

  /** NearMissInfoLogicクラスのMock */
  @Mock
  private NearMissInfoLogic nearMissInfoLogic;
  
  /** MqttUpdatesNearMissInfoNotificationLogicクラスのMock */
  @Mock
  private MqttUpdatesNearMissInfoNotificationLogic mqttUpdatesNearMissInfoNotification;
  
  /** テスト対象クラス */
  @InjectMocks
  private CheckUpdatesMearMissInfoTaskService service;
  
  @Test
  @DisplayName("ヒヤリハット情報無し")
  void testCheckUpdatesNearMissInfo1() {
    // テスト対象メソッド呼び出し用の引数
    LocalDateTime startAt = LocalDateTime.of(2024, 12, 1, 0, 0, 0);
    LocalDateTime endAt = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
  
    // ヒヤリハット情報集計ロジック呼び出しをMock化（空のMapを返す）
    Map<String, NotifyUpdatesNearMissInfoDto> adminIdNearMissInfoMap = new HashMap<>();
    when(nearMissInfoLogic.getAdminIdNearMissInfoMap(startAt, endAt)).thenReturn(adminIdNearMissInfoMap);
    
    // メソッドの実行
    service.checkUpdatesNearMissInfo(startAt, endAt);
    
    // 結果の検証
    // ヒヤリハット情報が無い場合、MQTT通信を行わない
    verify(mqttUpdatesNearMissInfoNotification, never())
      .notifyUpdatesNearMissInfo(anyString(), any(NotifyUpdatesNearMissInfoDto.class));
  }
  
  @Test
  @DisplayName("ヒヤリハット情報有り(2件)")
  void testCheckUpdatesNearMissInfo2() {
    // テスト対象メソッド呼び出し用の引数
    LocalDateTime startAt = LocalDateTime.of(2024, 12, 1, 0, 0, 0);
    LocalDateTime endAt = LocalDateTime.of(2024, 12, 31, 23, 59, 59);

    // ヒヤリハット情報集計ロジック呼び出しをMock化（2件分の更新情報を設定したMapを返す）
    Map<String, NotifyUpdatesNearMissInfoDto> adminIdNearMissInfoMap = new HashMap<>();
    adminIdNearMissInfoMap.put("admin1", new NotifyUpdatesNearMissInfoDto());
    adminIdNearMissInfoMap.put("admin2", new NotifyUpdatesNearMissInfoDto());
    when(nearMissInfoLogic.getAdminIdNearMissInfoMap(startAt, endAt)).thenReturn(adminIdNearMissInfoMap);
    
    // メソッドの実行
    service.checkUpdatesNearMissInfo(startAt, endAt);
    
    // 結果の検証
    // 2件分の更新通知処理が実施される
    verify(mqttUpdatesNearMissInfoNotification, times(1))
      .notifyUpdatesNearMissInfo(eq("admin1"), eq(adminIdNearMissInfoMap.get("admin1")));
    verify(mqttUpdatesNearMissInfoNotification, times(1))
      .notifyUpdatesNearMissInfo(eq("admin2"), eq(adminIdNearMissInfoMap.get("admin2")));
  }

}
