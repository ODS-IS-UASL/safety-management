package com.intent_exchange.uasl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dao.AircraftTypeDeviationMapper;
import com.intent_exchange.uasl.logic.AircraftTypeDeviationLogic;
import com.intent_exchange.uasl.model.AircraftTypeDeviationInfo;

/**
 * 機体種別別逸脱集計サービスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class AircraftTypeDeviationServiceTest {

  @Mock
  private AircraftTypeDeviationLogic logic;

  @Mock
  private AircraftTypeDeviationMapper mapper;

  @InjectMocks
  private AircraftTypeDeviationService service;

  @Test
  @DisplayName("期間指定の集計をページングで実行する")
  void testRegisterAircraftTypeDeviationForPeriodPaged() {
    Instant start = Instant.parse("2026-01-01T00:00:00Z");
    Instant end = Instant.parse("2026-02-01T00:00:00Z");

    List<AircraftTypeDeviationInfo> page1 =
        new ArrayList<>(java.util.Collections.nCopies(5000, new AircraftTypeDeviationInfo()));
    List<AircraftTypeDeviationInfo> page2 =
        List.of(new AircraftTypeDeviationInfo());

    when(mapper.selectRouteDeviationInfoForPeriodPaged(any(), any(), anyInt(), anyInt()))
        .thenReturn(page1)
        .thenReturn(page2);

    service.registerAircraftTypeDeviationForPeriod(start, end);

    verify(logic).registerAircraftTypeDeviation(page1);
    verify(logic).registerAircraftTypeDeviation(page2);

    InOrder inOrder = inOrder(mapper);
    inOrder.verify(mapper)
        .selectRouteDeviationInfoForPeriodPaged(any(), any(), eq(5000), eq(0));
    inOrder.verify(mapper)
        .selectRouteDeviationInfoForPeriodPaged(any(), any(), eq(5000), eq(5000));

    ArgumentCaptor<Timestamp> startCaptor = ArgumentCaptor.forClass(Timestamp.class);
    ArgumentCaptor<Timestamp> endCaptor = ArgumentCaptor.forClass(Timestamp.class);
    verify(mapper, times(2))
        .selectRouteDeviationInfoForPeriodPaged(startCaptor.capture(), endCaptor.capture(),
            eq(5000), anyInt());

    Timestamp expectedStart = Timestamp.from(start);
    Timestamp expectedEnd = Timestamp.from(end);
    assertTrue(startCaptor.getAllValues().stream().allMatch(expectedStart::equals));
    assertTrue(endCaptor.getAllValues().stream().allMatch(expectedEnd::equals));
  }
}
