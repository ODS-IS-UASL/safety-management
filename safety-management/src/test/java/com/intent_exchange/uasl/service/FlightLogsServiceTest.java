package com.intent_exchange.uasl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.intent_exchange.uasl.dto.request.FlightLogSearchDto;
import com.intent_exchange.uasl.entity.FlightLogListResponse;
import com.intent_exchange.uasl.entity.FlightLogResponse;
import com.intent_exchange.uasl.entity.FlightLogResponse.OperationalStatusEnum;
import com.intent_exchange.uasl.logic.FlightLogsLogic;
import com.intent_exchange.uasl.model.DroneLocation;
import com.intent_exchange.uasl.model.FlightLog;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class FlightLogsServiceTest {

  @Mock private FlightLogsLogic logic;

  @InjectMocks private FlightLogsService service;

  private static MockedStatic<ModelMapperUtil> modelMapperUtilMock;

  @BeforeAll
  static void setUpBeforeClass() {
    modelMapperUtilMock = Mockito.mockStatic(ModelMapperUtil.class);
  }

  @AfterAll
  static void tearDownAfterClass() {
    modelMapperUtilMock.close();
  }

  @Test
  @DisplayName("フライトログ取得 正常系")
  void testGetFlightLogs_Success() {
    FlightLogSearchDto searchDto =
        FlightLogSearchDto.builder()
            .startTime(LocalDateTime.of(2024, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2024, 1, 31, 23, 59))
            .build();
    String operationalStatus = "RouteDeviation";

    FlightLog entity = new FlightLog();
    entity.setOperationalStatus(operationalStatus);

    List<FlightLog> entities = List.of(entity);
    when(logic.getFlightLogs(searchDto)).thenReturn(entities);

    FlightLogResponse mappedResponse = new FlightLogResponse();
    // ModelMapperUtil.mapのモック化
    modelMapperUtilMock
        .when(() -> ModelMapperUtil.map(entity, FlightLogResponse.class))
        .thenReturn(mappedResponse);

    FlightLogListResponse result = service.getFlightLogs(searchDto);

    assertNotNull(result);
    assertEquals(1, result.getTotalCount());
    assertEquals(1, result.getFlightLogs().size());
    assertEquals(
        OperationalStatusEnum.fromValue(operationalStatus),
        result.getFlightLogs().get(0).getOperationalStatus());
    verify(logic, times(1)).getFlightLogs(searchDto);
  }

  @Test
  @DisplayName("フライトログ取得 0件")
  void testGetFlightLogs_Empty() {
    FlightLogSearchDto searchDto =
        FlightLogSearchDto.builder()
            .startTime(LocalDateTime.of(2024, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2024, 1, 31, 23, 59))
            .build();
    when(logic.getFlightLogs(searchDto)).thenReturn(Collections.emptyList());

    FlightLogListResponse result = service.getFlightLogs(searchDto);

    assertNotNull(result);
    assertEquals(0, result.getTotalCount());
    assertTrue(result.getFlightLogs().isEmpty());
  }

  @Test
  @DisplayName("フライトログ取得 異常系：日付範囲エラー")
  void testGetFlightLogs_DateRangeError() {
    FlightLogSearchDto searchDto =
        FlightLogSearchDto.builder()
            .startTime(LocalDateTime.of(2024, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2024, 2, 5, 0, 0)) // 31日超え
            .build();

    assertThrows(IllegalArgumentException.class, () -> service.getFlightLogs(searchDto));
  }

  @Test
  @DisplayName("フライトログ取得 検索結果がnullの場合")
  void testGetFlightLogs_NullResult() {
    FlightLogSearchDto searchDto =
        FlightLogSearchDto.builder()
            .startTime(LocalDateTime.of(2024, 1, 1, 0, 0))
            .endTime(LocalDateTime.of(2024, 1, 31, 23, 59))
            .build();

    when(logic.getFlightLogs(searchDto)).thenReturn(null);

    FlightLogListResponse result = service.getFlightLogs(searchDto);

    assertNotNull(result);
    assertEquals(0, result.getTotalCount());
    assertTrue(result.getFlightLogs().isEmpty());
    verify(logic, times(1)).getFlightLogs(searchDto);
  }
}
