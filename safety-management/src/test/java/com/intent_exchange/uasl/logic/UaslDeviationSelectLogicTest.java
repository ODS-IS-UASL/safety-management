package com.intent_exchange.uasl.logic;

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
import com.intent_exchange.uasl.dao.UaslDeviationMapper;
import com.intent_exchange.uasl.dto.request.NearMissInformationRequestSelectDto;
import com.intent_exchange.uasl.dto.response.UaslDeviationDetailDto;
import com.intent_exchange.uasl.model.UaslDeviation;
import com.intent_exchange.uasl.model.UaslDeviationSelect;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * MonitoringInfoSelectLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class UaslDeviationSelectLogicTest {

  @Mock
  private UaslDeviationMapper mapper;

  @InjectMocks
  private UaslDeviationSelectLogic logic;

  @Test
  @DisplayName("第三者立入監視情報リストを取得")
  void testNermissSelect1() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      NearMissInformationRequestSelectDto nearDto = new NearMissInformationRequestSelectDto();
      UaslDeviationSelect map = new UaslDeviationSelect();
      List<UaslDeviation> list = new ArrayList<>();
      List<UaslDeviationDetailDto> uaslDeviationDetailDtoList = new ArrayList<>();

      mockedStatic.when(() -> ModelMapperUtil.map(nearDto, UaslDeviationSelect.class))
          .thenReturn(map);
      mockedStatic.when(() -> ModelMapperUtil.mapList(any(), eq(UaslDeviationDetailDto.class)))
          .thenReturn(uaslDeviationDetailDtoList);
      when(mapper.nermissSelect(map)).thenReturn(list);

      assertDoesNotThrow(() -> logic.nermissSelect(nearDto));

    }
  }
}
