package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
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
import com.intent_exchange.uasl.dao.SubscriptionDataMapper;
import com.intent_exchange.uasl.dto.request.LinkageSubscriptionIdDto;
import com.intent_exchange.uasl.model.SubscriptionData;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class SubscriptionDataLogicTest {

  @Mock
  private SubscriptionDataMapper mapper;

  @InjectMocks
  private SubscriptionDataLogic logic;

  @Test
  @DisplayName("新しい予約IDとリモートIDの紐づけ情報を登録する")
  void testUpsert1() {
    LinkageSubscriptionIdDto dto = new LinkageSubscriptionIdDto();

    dto.setUaslReservationId("UaslReservationId01");
    dto.setSubscriptionId("SubscriptionId01");
    List<Double> areaInfo = new ArrayList<Double>();
    areaInfo.add(1.0);
    dto.setAreaInfo(areaInfo);
    dto.setUaslId("UaslId01");

    SubscriptionData map = new SubscriptionData();
    map.setUaslReservationId("UaslReservationId01");
    map.setSubscriptionId("SubscriptionId01");
    map.setAreaInfo(areaInfo);
    map.setUaslId("UaslId01");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, SubscriptionData.class)).thenReturn(map);
      when(mapper.upsertSelective(map)).thenReturn(1);
      assertDoesNotThrow(() -> logic.upsert(dto));
    }
  }

  @Test
  @DisplayName("予約IDとリモートIDの紐づけ情報を削除する")
  void testDelete1() {
    String uaslReservationId = "uaslReservationId01";
    when(mapper.deleteByPrimaryKey(uaslReservationId)).thenReturn(1);
    assertDoesNotThrow(() -> logic.delete(uaslReservationId));
  }

  @Test
  @DisplayName("新しい航路予約ごとの識別 ID で航路 ID とそのエリア情報を取得する")
  void testGetAreaInfoAndUaslId1() {
    Map<String, Object> areaInfoAndUaslId = new HashMap<String, Object>();
    areaInfoAndUaslId.put("uasl_id", "uaslId01");
    areaInfoAndUaslId.put("box2d", "BOX(1.0 2.0,3.0 4.0)");
    String uaslReservationId = "uaslReservationId01";
    when(mapper.selectAreaInfoAndUaslIdByReservationId(uaslReservationId))
        .thenReturn(areaInfoAndUaslId);
    assertDoesNotThrow(() -> logic.getAreaInfoAndUaslId(uaslReservationId));
  }

  @Test
  @DisplayName("新しい航路予約ごとの識別 ID で航路 ID とそのエリア情報を取得する")
  void testGetSubscriptionData1() {
    String uaslReservationId = "uaslReservationId01";
    SubscriptionData map = new SubscriptionData();
    map.setUaslReservationId("UaslReservationId01");
    map.setSubscriptionId("SubscriptionId01");
    List<Double> areaInfo = new ArrayList<Double>();
    areaInfo.add(1.0);
    map.setAreaInfo(areaInfo);
    map.setUaslId("UaslId01");

    LinkageSubscriptionIdDto dto = new LinkageSubscriptionIdDto();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      when(mapper.selectByPrimaryKey(uaslReservationId)).thenReturn(map);
      mockedStatic.when(() -> ModelMapperUtil.map(map, LinkageSubscriptionIdDto.class))
          .thenReturn(dto);
      assertDoesNotThrow(() -> logic.getSubscriptionData(uaslReservationId));
    }
  }

}
