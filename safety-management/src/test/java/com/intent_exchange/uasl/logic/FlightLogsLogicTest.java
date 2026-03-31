package com.intent_exchange.uasl.logic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.intent_exchange.uasl.dao.DroneLocationMapper;
import com.intent_exchange.uasl.dto.request.FlightLogSearchDto;
import com.intent_exchange.uasl.model.FlightLog;
import com.intent_exchange.uasl.model.FlightLogSearch;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * FlightLogsLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class FlightLogsLogicTest {

  @Mock
  private DroneLocationMapper mapper;

  @InjectMocks
  private FlightLogsLogic logic;

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
  void testGetFlightLogs() {
    FlightLogSearchDto searchDto = FlightLogSearchDto.builder().build();
    FlightLogSearch mappedSearch = new FlightLogSearch();
    List<FlightLog> expectedList = Collections.emptyList();

    modelMapperUtilMock.when(() -> ModelMapperUtil.map(searchDto, FlightLogSearch.class))
        .thenReturn(mappedSearch);

    when(mapper.getFlightLogs(mappedSearch)).thenReturn(expectedList);

    List<FlightLog> result = logic.getFlightLogs(searchDto);

    verify(mapper, times(1)).getFlightLogs(mappedSearch);
    modelMapperUtilMock.verify(() -> ModelMapperUtil.map(searchDto, FlightLogSearch.class), times(1));
  }
}
