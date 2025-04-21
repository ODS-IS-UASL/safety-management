package com.intent_exchange.drone_highway.logic;

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
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dao.AirwayDeviationMapper;
import com.intent_exchange.drone_highway.dto.request.NearMissInformationRequestSelectDto;
import com.intent_exchange.drone_highway.dto.response.AirwayDeviationDetailDto;
import com.intent_exchange.drone_highway.model.AirwayDeviation;
import com.intent_exchange.drone_highway.model.AirwayDeviationSelect;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * MonitoringInfoSelectLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class AirwayDeviationSelectLogicTest {

  @Mock
  private AirwayDeviationMapper mapper;

  @InjectMocks
  private AirwayDeviationSelectLogic logic;

  @Test
  @DisplayName("第三者立入監視情報リストを取得")
  void testNermissSelect1() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      NearMissInformationRequestSelectDto nearDto = new NearMissInformationRequestSelectDto();
      AirwayDeviationSelect map = new AirwayDeviationSelect();
      List<AirwayDeviation> list = new ArrayList<>();
      List<AirwayDeviationDetailDto> airwayDeviationDetailDtoList = new ArrayList<>();

      mockedStatic.when(() -> ModelMapperUtil.map(nearDto, AirwayDeviationSelect.class))
          .thenReturn(map);
      mockedStatic.when(() -> ModelMapperUtil.mapList(any(), eq(AirwayDeviationDetailDto.class)))
          .thenReturn(airwayDeviationDetailDtoList);
      when(mapper.nermissSelect(map)).thenReturn(list);

      assertDoesNotThrow(() -> logic.nermissSelect(nearDto));

    }
  }
}
