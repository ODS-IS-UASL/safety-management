package com.intent_exchange.drone_highway.service;

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
import com.intent_exchange.drone_highway.dto.request.AirwayDesignAreaInfoDeviationDto;
import com.intent_exchange.drone_highway.logic.AirwayDeviationLogic;
import com.intent_exchange.drone_highway.logic.AirwayDeviationSelectRouteDeviationInfoLogic;

/**
 * 航路逸脱情報サービスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class AirwayDeviationServiceTest {

  @Mock
  private AirwayDeviationSelectRouteDeviationInfoLogic logic;

  @Mock
  private AirwayDeviationLogic airwayDeviationLogic;

  @InjectMocks
  private AirwayDeviationService service;

  @Test
  @DisplayName("航路逸脱情報を登録")
  void testregisterRouteDeviationInfo1() {

    // 航路逸脱情報のリストをモック
    List<AirwayDesignAreaInfoDeviationDto> deviations = new ArrayList<>();
    AirwayDesignAreaInfoDeviationDto deviation = new AirwayDesignAreaInfoDeviationDto();
    deviations.add(deviation);

    // モックの動作を定義
    when(logic.get()).thenReturn(deviations);
    doNothing().when(airwayDeviationLogic).registerRouteDeviationInfo(deviations);

    // メソッドを呼び出し
    service.registerRouteDeviationInfo();

    // モックの呼び出しを検証
    verify(logic).get();
    verify(airwayDeviationLogic).registerRouteDeviationInfo(deviations);

    // 例外が発生しないことを検証
    assertDoesNotThrow(() -> service.registerRouteDeviationInfo());
  }

  @Test
  @DisplayName("航路逸脱情報が空の場合")
  void testregisterRouteDeviationInfo2() {

    // 空の航路逸脱情報のリストをモック
    List<AirwayDesignAreaInfoDeviationDto> deviations = new ArrayList<>();

    // モックの動作を定義
    when(logic.get()).thenReturn(deviations);

    // メソッドを呼び出し
    service.registerRouteDeviationInfo();

    // モックの呼び出しを検証
    verify(logic).get();
    verify(airwayDeviationLogic, never()).registerRouteDeviationInfo(anyList());

    // 例外が発生しないことを検証
    assertDoesNotThrow(() -> service.registerRouteDeviationInfo());
  }
  
  @Test
  @DisplayName("航路逸脱情報がnullの場合")
  void testregisterRouteDeviationInfo3() {

    // 空の航路逸脱情報のリストをモック
    List<AirwayDesignAreaInfoDeviationDto> deviations = null;

    // モックの動作を定義
    when(logic.get()).thenReturn(deviations);

    // メソッドを呼び出し
    service.registerRouteDeviationInfo();

    // モックの呼び出しを検証
    verify(logic).get();
    verify(airwayDeviationLogic, never()).registerRouteDeviationInfo(anyList());

    // 例外が発生しないことを検証
    assertDoesNotThrow(() -> service.registerRouteDeviationInfo());
  }
}