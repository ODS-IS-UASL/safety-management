package com.intent_exchange.drone_highway.logic;

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
import com.intent_exchange.drone_highway.dao.SubscriptionDataMapper;
import com.intent_exchange.drone_highway.dto.request.LinkageSubscriptionIdDto;
import com.intent_exchange.drone_highway.model.SubscriptionData;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

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

    dto.setAirwayReservationId("AirwayReservationId01");
    dto.setSubscriptionId("SubscriptionId01");
    List<Double> areaInfo = new ArrayList<Double>();
    areaInfo.add(1.0);
    dto.setAreaInfo(areaInfo);
    dto.setAirwayId("AirwayId01");

    SubscriptionData map = new SubscriptionData();
    map.setAirwayReservationId("AirwayReservationId01");
    map.setSubscriptionId("SubscriptionId01");
    map.setAreaInfo(areaInfo);
    map.setAirwayId("AirwayId01");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, SubscriptionData.class)).thenReturn(map);
      when(mapper.upsertSelective(map)).thenReturn(1);
      assertDoesNotThrow(() -> logic.upsert(dto));
    }
  }

  @Test
  @DisplayName("予約IDとリモートIDの紐づけ情報を削除する")
  void testDelete1() {
    String airwayReservationId = "airwayReservationId01";
    when(mapper.deleteByPrimaryKey(airwayReservationId)).thenReturn(1);
    assertDoesNotThrow(() -> logic.delete(airwayReservationId));
  }

  @Test
  @DisplayName("新しい航路予約ごとの識別 ID で航路 ID とそのエリア情報を取得する")
  void testGetAreaInfoAndAirwayId1() {
    Map<String, Object> areaInfoAndAirwayId = new HashMap<String, Object>();
    areaInfoAndAirwayId.put("airway_id", "airwayId01");
    areaInfoAndAirwayId.put("box2d", "BOX(1.0 2.0,3.0 4.0)");
    String airwayReservationId = "airwayReservationId01";
    when(mapper.selectAreaInfoAndAirwayIdByReservationId(airwayReservationId))
        .thenReturn(areaInfoAndAirwayId);
    assertDoesNotThrow(() -> logic.getAreaInfoAndAirwayId(airwayReservationId));
  }

  @Test
  @DisplayName("新しい航路予約ごとの識別 ID で航路 ID とそのエリア情報を取得する")
  void testGetSubscriptionData1() {
    String airwayReservationId = "airwayReservationId01";
    SubscriptionData map = new SubscriptionData();
    map.setAirwayReservationId("AirwayReservationId01");
    map.setSubscriptionId("SubscriptionId01");
    List<Double> areaInfo = new ArrayList<Double>();
    areaInfo.add(1.0);
    map.setAreaInfo(areaInfo);
    map.setAirwayId("AirwayId01");

    LinkageSubscriptionIdDto dto = new LinkageSubscriptionIdDto();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      when(mapper.selectByPrimaryKey(airwayReservationId)).thenReturn(map);
      mockedStatic.when(() -> ModelMapperUtil.map(map, LinkageSubscriptionIdDto.class))
          .thenReturn(dto);
      assertDoesNotThrow(() -> logic.getSubscriptionData(airwayReservationId));
    }
  }

}
