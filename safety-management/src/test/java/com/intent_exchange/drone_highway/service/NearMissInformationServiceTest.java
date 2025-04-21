package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.text.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dto.request.NearMissInformationRequestAttributesDto;
import com.intent_exchange.drone_highway.entity.NearMissInformationRequest;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponse;
import com.intent_exchange.drone_highway.logic.NearMissInformationLogic;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 第三者立入監視情報変更通知のサービスのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class NearMissInformationServiceTest {
  @Mock
  private NearMissInformationLogic logic;

  @InjectMocks
  private NearMissInformationService service;

  @Test
  @DisplayName("ヒヤリハット情報取得を実施")
  void testNotifyNearMissEntry1() throws ParseException {


    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      // モックの設定
      NearMissInformationRequest entity = new NearMissInformationRequest();
      NearMissInformationResponse response = new NearMissInformationResponse();
      NearMissInformationRequestAttributesDto attributes =
          new NearMissInformationRequestAttributesDto();

      mockedStatic.when(() -> ModelMapperUtil.map(any(), any())).thenReturn(attributes);

      when(logic.nearMissInformation(any())).thenReturn(response);

      // テスト実施
      NearMissInformationResponse result = service.nearMissInformationEntry(entity);

      // 結果検証
      assertNotNull(result);
    }
  }
}
