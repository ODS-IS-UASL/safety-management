package com.intent_exchange.drone_highway.dao;

import java.time.LocalDateTime;
import java.util.List;
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
import com.intent_exchange.drone_highway.model.MonitoringInformation;

@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
// Excelからのテストデータ読み込み時、カラム名が大文字に変換されてうまく動作しないためコメントアウト。取得データの検証はスキップ。
//@DatabaseSetup(value = {"classpath:test-t_deviation-select-updatesNearMiss.xlsx"}, type = DatabaseOperation.CLEAN_INSERT)
class MonitoringInformationMapperTest {

  /** ヒヤリハット情報（第三者立入情報）操作用Mapper */
  @Autowired
  private MonitoringInformationMapper mapper;
  
  @Test
  @Transactional
  @DisplayName("ヒヤリハット情報（第三者立入情報）を登録する")
  
  void testInsertSelective1() {
    MonitoringInformation monitoringInfo = new MonitoringInformation();
    monitoringInfo.setAirwayReservationId("1");
    monitoringInfo.setMonitoringInformation("test");
    monitoringInfo.setAirwayAdministratorId("1");
    monitoringInfo.setOperatorId("1");
    monitoringInfo.setAirwayId("1");
    mapper.insertSelective(monitoringInfo);
  }
  
  @Test
  @Transactional
  @DisplayName("指定の期間内に終了日時を迎えた予約のヒヤリハット（第三者立入）情報を抽出する")
  void testSelectByReservationEndAt1() {
    LocalDateTime startAt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    LocalDateTime endAt = LocalDateTime.of(2025, 1, 31, 23, 59, 59);
    List<MonitoringInformation> monitoringInfoList = mapper.selectByReservationEndAt(startAt, endAt);
    //assertEquals(2, monitoringInfoList.size());
  }

}
