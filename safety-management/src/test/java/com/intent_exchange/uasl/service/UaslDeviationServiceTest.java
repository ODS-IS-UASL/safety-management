package com.intent_exchange.uasl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.UaslDesignAreaInfoDeviationDto;
import com.intent_exchange.uasl.logic.UaslDeviationLogic;
import com.intent_exchange.uasl.logic.UaslDeviationSelectRouteDeviationInfoLogic;

/**
 * 航路逸脱情報サービスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class UaslDeviationServiceTest {

  @Mock
  private UaslDeviationSelectRouteDeviationInfoLogic logic;

  @Mock
  private UaslDeviationLogic uaslDeviationLogic;

  @InjectMocks
  private UaslDeviationService service;

  @Test
  @DisplayName("航路逸脱情報を登録")
  void testregisterRouteDeviationInfo1() {

    // 航路逸脱情報のリストをモック
    List<UaslDesignAreaInfoDeviationDto> deviations = new ArrayList<>();
    UaslDesignAreaInfoDeviationDto deviation = new UaslDesignAreaInfoDeviationDto();
    deviations.add(deviation);

    // モックの動作を定義
    when(logic.get()).thenReturn(deviations);
    doNothing().when(uaslDeviationLogic).registerRouteDeviationInfo(deviations);

    // メソッドを呼び出し
    service.registerRouteDeviationInfo();

    // モックの呼び出しを検証
    verify(logic).get();
    verify(uaslDeviationLogic).registerRouteDeviationInfo(deviations);

    // 例外が発生しないことを検証
    assertDoesNotThrow(() -> service.registerRouteDeviationInfo());
  }

  @Test
  @DisplayName("航路逸脱情報が空の場合")
  void testregisterRouteDeviationInfo2() {

    // 空の航路逸脱情報のリストをモック
    List<UaslDesignAreaInfoDeviationDto> deviations = new ArrayList<>();

    // モックの動作を定義
    when(logic.get()).thenReturn(deviations);

    // メソッドを呼び出し
    service.registerRouteDeviationInfo();

    // モックの呼び出しを検証
    verify(logic).get();
    verify(uaslDeviationLogic, never()).registerRouteDeviationInfo(anyList());

    // 例外が発生しないことを検証
    assertDoesNotThrow(() -> service.registerRouteDeviationInfo());
  }
  
  @Test
  @DisplayName("航路逸脱情報がnullの場合")
  void testregisterRouteDeviationInfo3() {

    // 空の航路逸脱情報のリストをモック
    List<UaslDesignAreaInfoDeviationDto> deviations = null;

    // モックの動作を定義
    when(logic.get()).thenReturn(deviations);

    // メソッドを呼び出し
    service.registerRouteDeviationInfo();

    // モックの呼び出しを検証
    verify(logic).get();
    verify(uaslDeviationLogic, never()).registerRouteDeviationInfo(anyList());

    // 例外が発生しないことを検証
    assertDoesNotThrow(() -> service.registerRouteDeviationInfo());
  }
}
