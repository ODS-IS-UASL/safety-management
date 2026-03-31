package com.intent_exchange.uasl.dao;

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
import com.intent_exchange.uasl.dao.config.DatabaseTestConfig;
import com.intent_exchange.uasl.dao.config.XlsDataSetLoader;
import com.intent_exchange.uasl.model.SubscriptionData;

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
    row.setUaslReservationId("UaslReservationId01");
    row.setSubscriptionId("SubscriptionId01");
    List<Double> areaInfo = new ArrayList<Double>();
    areaInfo.add(1.0);
    row.setAreaInfo(areaInfo);
    row.setUaslId("UaslId01");

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @Transactional
  @DisplayName("サブスクリプション ID とリモート ID の紐づけ情報を作成する")
  void testUpsertSelective2() {
    SubscriptionData row = new SubscriptionData();
    row.setUaslReservationId("UaslReservationId02");
    row.setSubscriptionId("SubscriptionId02");
    List<Double> areaInfo = new ArrayList<Double>();
    areaInfo.add(2.0);
    row.setAreaInfo(areaInfo);
    row.setUaslId("UaslId02");

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @DisplayName("サブスクリプション ID とリモート ID の紐づけ情報を取得する")
  void testSelectByPrimaryKey1() {
    String uaslReservationId = "UaslReservationId01";
    SubscriptionData res = mapper.selectByPrimaryKey(uaslReservationId);
    // assertEquals(null, res);
  }

  @Test
  @DisplayName("航路 ID とエリア情報を取得する")
  void testSelectAreaInfoAndUaslIdByReservationId1() {
    String uaslReservationId = "UaslReservationId02";
    Map<String, Object> res = mapper.selectAreaInfoAndUaslIdByReservationId(uaslReservationId);
    // assertEquals(null, res);
  }

  @Test
  @Transactional
  @DisplayName("サブスクリプション ID とリモート ID の紐づけ情報を削除する")
  void testDeleteByPrimaryKey1() {
    String uaslReservationId = "UaslReservationId01";
    int res = mapper.deleteByPrimaryKey(uaslReservationId);
    // assertEquals(1, res);
  }
}
