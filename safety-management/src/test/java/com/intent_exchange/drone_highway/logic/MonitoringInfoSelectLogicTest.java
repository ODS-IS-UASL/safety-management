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
import com.intent_exchange.drone_highway.dao.MonitoringInformationMapper;
import com.intent_exchange.drone_highway.dto.request.NearMissInformationRequestSelectDto;
import com.intent_exchange.drone_highway.dto.response.MonitoringInfoDetailDto;
import com.intent_exchange.drone_highway.model.MonitoringInformation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * MonitoringInfoSelectLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class MonitoringInfoSelectLogicTest {

  @Mock
  private MonitoringInformationMapper mapper;

  @InjectMocks
  private MonitoringInfoSelectLogic logic;

  @Test
  @DisplayName("第三者立入監視情報リストを取得")
  void testMoniterSelect1() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      NearMissInformationRequestSelectDto nearDto = new NearMissInformationRequestSelectDto();
      MonitoringInformation map = new MonitoringInformation();
      List<MonitoringInformation> list = new ArrayList<>();
      List<MonitoringInfoDetailDto> MonitoringInfoDetailDtoList = new ArrayList<>();

      mockedStatic.when(() -> ModelMapperUtil.map(nearDto, MonitoringInformation.class))
          .thenReturn(map);
      mockedStatic.when(() -> ModelMapperUtil.mapList(any(), eq(MonitoringInfoDetailDto.class)))
          .thenReturn(MonitoringInfoDetailDtoList);
      when(mapper.moniterSelect(map)).thenReturn(list);

      assertDoesNotThrow(() -> logic.moniterSelect(nearDto));

    }
  }
}
