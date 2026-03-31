package com.intent_exchange.uasl.dao;

import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.intent_exchange.uasl.dao.config.DatabaseTestConfig;
import com.intent_exchange.uasl.dao.config.XlsDataSetLoader;
import com.intent_exchange.uasl.model.UaslDeviation;

@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
// Excelからのテストデータ読み込み時、カラム名が大文字に変換されてうまく動作しないためコメントアウト。取得データの検証はスキップ。
//@DatabaseSetup(value = {"classpath:test-t_deviation-select-updatesNearMiss.xlsx"}, type = DatabaseOperation.CLEAN_INSERT)
class UaslDeviationMapperTest {

  /** ヒヤリハット情報（航路逸脱情報）操作用Mapper */
  @Autowired
  private UaslDeviationMapper mapper;
  
  @Test
  @Transactional
  @DisplayName("指定の期間内に終了日時を迎えた予約のヒヤリハット（航路逸脱）情報を抽出する")
  void testSelectByReservationEndAt1() {
    LocalDateTime startAt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    LocalDateTime endAt = LocalDateTime.of(2025, 1, 31, 23, 59, 59);
    List<UaslDeviation> deviationList = mapper.selectByReservationEndAt(startAt, endAt);
    // assertEquals(4, deviationList.size());
  }

  @Test
  @Transactional
  @DisplayName("insertBatchSelectiveメソッドのテスト")
  void testInsertBatchSelective() {
    List<UaslDeviation> deviations = new ArrayList<>();
    UaslDeviation uaslDeviation = new UaslDeviation();
    uaslDeviation.setUaslReservationId("reservationId01");
    uaslDeviation.setRouteDeviationRate(66.66666666666666);
    uaslDeviation.setRouteDeviationAmount("{\"horizontal\": 13.48, \"vertical\": 25.00}");
    uaslDeviation.setRouteDeviationTime(
        "{\"time\": [\"2025-02-12 00:59:59.0\", \"2025-02-12 10:00:00.0\", \"2025-02-12 10:00:02.0\", \"2025-02-12 10:00:03.0\"]}");
    uaslDeviation.setUaslAdministratorId("admin123");
    uaslDeviation.setOperatorId("operator456");
    uaslDeviation.setUaslId("1");
    uaslDeviation.setAircraftInfoId(123);
    uaslDeviation.setRouteDeviationCoordinates(
        "{\"coordinates\": [{\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0002, \"pressureAltitude\": 30.0}, \"uaslSectionId\": \"1\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0003, \"pressureAltitude\": 30.0}, \"uaslSectionId\": \"1\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0001, \"pressureAltitude\": 30.0}, \"uaslSectionId\": \"2\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0001, \"pressureAltitude\": 30.0}, \"uaslSectionId\": \"2\"}]}");
    deviations.add(uaslDeviation);

    uaslDeviation = new UaslDeviation();
    uaslDeviation.setUaslReservationId("reservationId02");
    uaslDeviation.setRouteDeviationRate(100.0);
    uaslDeviation.setRouteDeviationAmount("{\"horizontal\": 36.25, \"vertical\": 10.00}");
    uaslDeviation.setRouteDeviationTime("{\"horizontal\": 36.25, \"vertical\": 10.00}");
    uaslDeviation.setUaslAdministratorId("admin789");
    uaslDeviation.setOperatorId("operator123");
    uaslDeviation.setUaslId("2");
    uaslDeviation.setAircraftInfoId(456);
    uaslDeviation.setRouteDeviationCoordinates(
        "{\"coordinates\": [{\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0004, \"pressureAltitude\": 40.0}, \"uaslSectionId\": \"1\"}]}");
    deviations.add(uaslDeviation);

    int insertedRows = mapper.insertBatchSelective(deviations);
    assertEquals(2, insertedRows);
  }
}
