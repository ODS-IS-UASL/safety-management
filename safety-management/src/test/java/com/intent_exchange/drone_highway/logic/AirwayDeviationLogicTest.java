package com.intent_exchange.drone_highway.logic;

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
import com.intent_exchange.drone_highway.dao.AirwayDeviationMapper;
import com.intent_exchange.drone_highway.dto.request.AirwayDesignAreaInfoDeviationDto;
import com.intent_exchange.drone_highway.dto.request.AirwayDeviationDto;
import com.intent_exchange.drone_highway.model.AirwayDeviation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 航路逸脱情報の登録に関連するロジックのテストを提供します。
 */
@ExtendWith(MockitoExtension.class)
class AirwayDeviationLogicTest {

  @Mock
  private AirwayDeviationMapper airwayDeviationMapper;

  @Mock
  private AirwayDeviationProcessingLogic airwayDeviationProcessingLogic;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private AirwayDeviationLogic airwayDeviationLogic;

  @Test
  @DisplayName("航路逸脱情報を登録する")
  void testRegisterRouteDeviationInfo() {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssX");
    // モックのセットアップ
    List<AirwayDesignAreaInfoDeviationDto> deviations = new ArrayList<>();
    AirwayDesignAreaInfoDeviationDto airwayDesignAreaInfoDeviationDto =
        new AirwayDesignAreaInfoDeviationDto();
    airwayDesignAreaInfoDeviationDto.setReservationId("reservation1");
    try {
      airwayDesignAreaInfoDeviationDto.setGetLocationTimestamp(
          new Timestamp(dateFormat.parse("2025-02-12 15:59:59+00").getTime()));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    airwayDesignAreaInfoDeviationDto.setLatitude(35.6895);
    airwayDesignAreaInfoDeviationDto.setLongitude(139.6917);
    airwayDesignAreaInfoDeviationDto.setAboveGroundLevel(100.0);
    airwayDesignAreaInfoDeviationDto.setRouteDeviationRate(5.0);
    airwayDesignAreaInfoDeviationDto.setHorizontalDeviation(10.0);
    airwayDesignAreaInfoDeviationDto.setVerticalDeviation(2.0);
    airwayDesignAreaInfoDeviationDto.setAirwayId("airway789");
    airwayDesignAreaInfoDeviationDto.setAirwaySectionId("section456");
    airwayDesignAreaInfoDeviationDto.setOperatorId("operator123");
    airwayDesignAreaInfoDeviationDto.setAircraftInfoId(1);
    airwayDesignAreaInfoDeviationDto.setAirwayAdministratorId("admin456");
    airwayDesignAreaInfoDeviationDto.setOperationalStatus("RouteDeviation");
    
    deviations.add(airwayDesignAreaInfoDeviationDto);
    Map<String, List<AirwayDesignAreaInfoDeviationDto>> groupedByReservationIdMap = new HashMap<>();
    groupedByReservationIdMap.put("reservation1", deviations);

    when(airwayDeviationProcessingLogic.getGroupedByReservationIdMap(any()))
        .thenReturn(groupedByReservationIdMap);
    when(airwayDeviationProcessingLogic.getDeviationRate(any())).thenReturn(0.1);
    when(airwayDeviationProcessingLogic.getHorizontalPercentile(any())).thenReturn("10.10");
    when(airwayDeviationProcessingLogic.getVerticalPercentile(any())).thenReturn("20.200");
    when(airwayDeviationProcessingLogic.getJsonTimestamps(any())).thenReturn(
        "{\"time\": [\"2025-02-12 00:59:59.0\", \"2025-02-12 10:00:00.0\", \"2025-02-12 10:00:02.0\", \"2025-02-12 10:00:03.0\"]}");
    when(airwayDeviationProcessingLogic.getJsonCoordinates(any())).thenReturn(
        "{\"coordinates\": [{\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0002, \"pressureAltitude\": 30.0}, \"airwaySectionId\": \"1\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0003, \"pressureAltitude\": 30.0}, \"airwaySectionId\": \"1\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0001, \"pressureAltitude\": 30.0}, \"airwaySectionId\": \"2\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0001, \"pressureAltitude\": 30.0}, \"airwaySectionId\": \"2\"}]}");
  
    // modelMapperのモックの実行動作
    when(modelMapper.map(any(AirwayDeviationDto.class), eq(AirwayDeviation.class)))
        .thenReturn(new AirwayDeviation());

    // ModelMapperUtil.mapメソッドのモックの実行動作
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(any(AirwayDeviationDto.class),
          eq(AirwayDeviation.class))).thenAnswer(invocation -> {
            AirwayDeviationDto dto = invocation.getArgument(0);
            return modelMapper.map(dto, AirwayDeviation.class);
          });
    
      // メソッドを呼び出し
      airwayDeviationLogic.registerRouteDeviationInfo(deviations);

      // モックの呼び出しを検証
      verify(airwayDeviationProcessingLogic).getGroupedByReservationIdMap(deviations);
      verify(airwayDeviationProcessingLogic).getDeviationRate(deviations);
      verify(airwayDeviationProcessingLogic).getHorizontalPercentile(deviations);
      verify(airwayDeviationProcessingLogic).getVerticalPercentile(deviations);
      verify(airwayDeviationProcessingLogic).getJsonTimestamps(deviations);
      verify(airwayDeviationProcessingLogic).getJsonCoordinates(deviations);
      verify(airwayDeviationMapper).insertBatchSelective(anyList());

      // modelMapperのmapメソッドが呼び出されたことを確認
      verify(modelMapper, atLeastOnce()).map(any(AirwayDeviationDto.class),
          eq(AirwayDeviation.class));

      // 結果を検証
      assertDoesNotThrow(() -> airwayDeviationLogic.registerRouteDeviationInfo(deviations));
    }
  }
}