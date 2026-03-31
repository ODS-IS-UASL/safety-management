package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import com.intent_exchange.uasl.dao.UaslDeviationMapper;
import com.intent_exchange.uasl.dto.request.UaslDesignAreaInfoDeviationDto;
import com.intent_exchange.uasl.dto.request.UaslDeviationDto;
import com.intent_exchange.uasl.model.UaslDeviation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 航路逸脱情報の登録に関連するロジックのテストを提供します。
 */
@ExtendWith(MockitoExtension.class)
class UaslDeviationLogicTest {

  @Mock
  private UaslDeviationMapper uaslDeviationMapper;

  @Mock
  private UaslDeviationProcessingLogic uaslDeviationProcessingLogic;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private UaslDeviationLogic uaslDeviationLogic;

  @Test
  @DisplayName("航路逸脱情報を登録する")
  void testRegisterRouteDeviationInfo() {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");
    // モックのセットアップ
    List<UaslDesignAreaInfoDeviationDto> deviations = new ArrayList<>();
    UaslDesignAreaInfoDeviationDto uaslDesignAreaInfoDeviationDto =
        new UaslDesignAreaInfoDeviationDto();
    uaslDesignAreaInfoDeviationDto.setReservationId("reservation1");
    try {
      uaslDesignAreaInfoDeviationDto.setGetLocationTimestamp(
          new Timestamp(dateFormat.parse("2025-02-12 15:59:59+00").getTime()));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    uaslDesignAreaInfoDeviationDto.setLatitude(35.6895);
    uaslDesignAreaInfoDeviationDto.setLongitude(139.6917);
    uaslDesignAreaInfoDeviationDto.setAltitude(100.0);
    uaslDesignAreaInfoDeviationDto.setRouteDeviationRate(5.0);
    uaslDesignAreaInfoDeviationDto.setHorizontalDeviation(10.0);
    uaslDesignAreaInfoDeviationDto.setVerticalDeviation(2.0);
    uaslDesignAreaInfoDeviationDto.setUaslId("uasl789");
    uaslDesignAreaInfoDeviationDto.setUaslSectionId("section456");
    uaslDesignAreaInfoDeviationDto.setOperatorId("operator123");
    uaslDesignAreaInfoDeviationDto.setAircraftInfoId(1);
    uaslDesignAreaInfoDeviationDto.setUaslAdministratorId("admin456");
    uaslDesignAreaInfoDeviationDto.setOperationalStatus("RouteDeviation");
    
    deviations.add(uaslDesignAreaInfoDeviationDto);
    Map<String, List<UaslDesignAreaInfoDeviationDto>> groupedByReservationIdMap = new HashMap<>();
    groupedByReservationIdMap.put("reservation1", deviations);

    when(uaslDeviationProcessingLogic.getGroupedByReservationIdMap(any()))
        .thenReturn(groupedByReservationIdMap);
    when(uaslDeviationProcessingLogic.getDeviationRate(any())).thenReturn(0.1);
    when(uaslDeviationProcessingLogic.getHorizontalPercentile(any())).thenReturn("10.10");
    when(uaslDeviationProcessingLogic.getVerticalPercentile(any())).thenReturn("20.200");
    when(uaslDeviationProcessingLogic.getJsonTimestamps(any())).thenReturn(
        "{\"time\": [\"2025-02-12 00:59:59.0\", \"2025-02-12 10:00:00.0\", \"2025-02-12 10:00:02.0\", \"2025-02-12 10:00:03.0\"]}");
    when(uaslDeviationProcessingLogic.getJsonCoordinates(any())).thenReturn(
        "{\"coordinates\": [{\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0002, \"pressureAltitude\": 30.0}, \"uaslSectionId\": \"1\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0003, \"pressureAltitude\": 30.0}, \"uaslSectionId\": \"1\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0001, \"pressureAltitude\": 30.0}, \"uaslSectionId\": \"2\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0001, \"pressureAltitude\": 30.0}, \"uaslSectionId\": \"2\"}]}");
  
    // modelMapperのモックの実行動作
    when(modelMapper.map(any(UaslDeviationDto.class), eq(UaslDeviation.class)))
        .thenReturn(new UaslDeviation());

    // ModelMapperUtil.mapメソッドのモックの実行動作
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(any(UaslDeviationDto.class),
          eq(UaslDeviation.class))).thenAnswer(invocation -> {
            UaslDeviationDto dto = invocation.getArgument(0);
            return modelMapper.map(dto, UaslDeviation.class);
          });
    
      // メソッドを呼び出し
      uaslDeviationLogic.registerRouteDeviationInfo(deviations);

      // モックの呼び出しを検証
      verify(uaslDeviationProcessingLogic).getGroupedByReservationIdMap(deviations);
      verify(uaslDeviationProcessingLogic).getDeviationRate(deviations);
      verify(uaslDeviationProcessingLogic).getHorizontalPercentile(deviations);
      verify(uaslDeviationProcessingLogic).getVerticalPercentile(deviations);
      verify(uaslDeviationProcessingLogic).getJsonTimestamps(deviations);
      verify(uaslDeviationProcessingLogic).getJsonCoordinates(deviations);
      verify(uaslDeviationMapper).insertBatchSelective(anyList());

      // modelMapperのmapメソッドが呼び出されたことを確認
      verify(modelMapper, atLeastOnce()).map(any(UaslDeviationDto.class),
          eq(UaslDeviation.class));

      // 結果を検証
      assertDoesNotThrow(() -> uaslDeviationLogic.registerRouteDeviationInfo(deviations));
    }
  }
  
  @Test
  @DisplayName("航路逸脱情報を登録しない_逸脱割合が0の場合")
  void testRegisterRouteDeviationInfo_ZeroDeviationRate() {
    // モックのセットアップ
    List<UaslDesignAreaInfoDeviationDto> deviations = new ArrayList<>();
    UaslDesignAreaInfoDeviationDto uaslDesignAreaInfoDeviationDto = new UaslDesignAreaInfoDeviationDto();
    uaslDesignAreaInfoDeviationDto.setReservationId("reservation1");
    // その他の必要なプロパティ設定（省略可、ロジック依存による）
    deviations.add(uaslDesignAreaInfoDeviationDto);

    Map<String, List<UaslDesignAreaInfoDeviationDto>> groupedByReservationIdMap = new HashMap<>();
    groupedByReservationIdMap.put("reservation1", deviations);

    // getGroupedByReservationIdMapが呼ばれたら、用意したMapを返す
    when(uaslDeviationProcessingLogic.getGroupedByReservationIdMap(any())).thenReturn(
        groupedByReservationIdMap);

    // getDeviationRateが0.0を返すように設定
    when(uaslDeviationProcessingLogic.getDeviationRate(any())).thenReturn(0.0);

    // テスト対象メソッドを実行
    uaslDeviationLogic.registerRouteDeviationInfo(deviations);

    // 検証
    // getDeviationRateまでは呼ばれることを確認
    verify(uaslDeviationProcessingLogic).getGroupedByReservationIdMap(deviations);
    verify(uaslDeviationProcessingLogic).getDeviationRate(deviations);

    // 0.0の場合はcontinueするため、それ以降の処理（getHorizontalPercentileなど）やDB登録は呼ばれないことを確認
    verify(uaslDeviationProcessingLogic, never()).getHorizontalPercentile(any());
    verify(uaslDeviationProcessingLogic, never()).getVerticalPercentile(any());
    verify(uaslDeviationMapper, never()).insertBatchSelective(anyList());
  }
}
