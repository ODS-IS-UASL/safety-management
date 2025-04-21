package com.intent_exchange.drone_highway.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.intent_exchange.drone_highway.dao.config.DatabaseTestConfig;
import com.intent_exchange.drone_highway.dao.config.XlsDataSetLoader;
import com.intent_exchange.drone_highway.model.SubscriptionData;

@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SubscriptionDataMapperTest {

  @Autowired
  private SubscriptionDataMapper mapper;

  @Test
  @Transactional
  @DisplayName("サブスクリプション ID とリモート ID の紐づけ情報を更新する")
  void testUpsertSelective1() {
    SubscriptionData row = new SubscriptionData();
    row.setAirwayReservationId("AirwayReservationId01");
    row.setSubscriptionId("SubscriptionId01");
    List<Double> areaInfo = new ArrayList<Double>();
    areaInfo.add(1.0);
    row.setAreaInfo(areaInfo);
    row.setAirwayId("AirwayId01");

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @Transactional
  @DisplayName("サブスクリプション ID とリモート ID の紐づけ情報を作成する")
  void testUpsertSelective2() {
    SubscriptionData row = new SubscriptionData();
    row.setAirwayReservationId("AirwayReservationId02");
    row.setSubscriptionId("SubscriptionId02");
    List<Double> areaInfo = new ArrayList<Double>();
    areaInfo.add(2.0);
    row.setAreaInfo(areaInfo);
    row.setAirwayId("AirwayId02");

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @DisplayName("サブスクリプション ID とリモート ID の紐づけ情報を取得する")
  void testSelectByPrimaryKey1() {
    String airwayReservationId = "AirwayReservationId01";
    SubscriptionData res = mapper.selectByPrimaryKey(airwayReservationId);
    // assertEquals(null, res);
  }

  @Test
  @DisplayName("航路 ID とエリア情報を取得する")
  void testSelectAreaInfoAndAirwayIdByReservationId1() {
    String airwayReservationId = "AirwayReservationId02";
    Map<String, Object> res = mapper.selectAreaInfoAndAirwayIdByReservationId(airwayReservationId);
    // assertEquals(null, res);
  }

  @Test
  @Transactional
  @DisplayName("サブスクリプション ID とリモート ID の紐づけ情報を削除する")
  void testDeleteByPrimaryKey1() {
    String airwayReservationId = "AirwayReservationId01";
    int res = mapper.deleteByPrimaryKey(airwayReservationId);
    // assertEquals(1, res);
  }
}
