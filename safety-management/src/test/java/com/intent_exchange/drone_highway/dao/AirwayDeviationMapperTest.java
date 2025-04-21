package com.intent_exchange.drone_highway.dao;

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
import com.intent_exchange.drone_highway.dao.config.DatabaseTestConfig;
import com.intent_exchange.drone_highway.dao.config.XlsDataSetLoader;
import com.intent_exchange.drone_highway.model.AirwayDeviation;

@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
// Excelからのテストデータ読み込み時、カラム名が大文字に変換されてうまく動作しないためコメントアウト。取得データの検証はスキップ。
//@DatabaseSetup(value = {"classpath:test-t_deviation-select-updatesNearMiss.xlsx"}, type = DatabaseOperation.CLEAN_INSERT)
class AirwayDeviationMapperTest {

  /** ヒヤリハット情報（航路逸脱情報）操作用Mapper */
  @Autowired
  private AirwayDeviationMapper mapper;
  
  @Test
  @Transactional
  @DisplayName("指定の期間内に終了日時を迎えた予約のヒヤリハット（航路逸脱）情報を抽出する")
  void testSelectByReservationEndAt1() {
    LocalDateTime startAt = LocalDateTime.of(2025, 1, 1, 0, 0, 0);
    LocalDateTime endAt = LocalDateTime.of(2025, 1, 31, 23, 59, 59);
    List<AirwayDeviation> deviationList = mapper.selectByReservationEndAt(startAt, endAt);
    // assertEquals(4, deviationList.size());
  }

  @Test
  @Transactional
  @DisplayName("insertBatchSelectiveメソッドのテスト")
  void testInsertBatchSelective() {
    List<AirwayDeviation> deviations = new ArrayList<>();
    AirwayDeviation airwayDeviation = new AirwayDeviation();
    airwayDeviation.setAirwayReservationId("reservationId01");
    airwayDeviation.setRouteDeviationRate(66.66666666666666);
    airwayDeviation.setRouteDeviationAmount("{\"horizontal\": 13.48, \"vertical\": 25.00}");
    airwayDeviation.setRouteDeviationTime(
        "{\"time\": [\"2025-02-12 00:59:59.0\", \"2025-02-12 10:00:00.0\", \"2025-02-12 10:00:02.0\", \"2025-02-12 10:00:03.0\"]}");
    airwayDeviation.setAirwayAdministratorId("admin123");
    airwayDeviation.setOperatorId("operator456");
    airwayDeviation.setAirwayId("1");
    airwayDeviation.setAircraftInfoId(123);
    airwayDeviation.setRouteDeviationCoordinates(
        "{\"coordinates\": [{\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0002, \"pressureAltitude\": 30.0}, \"airwaySectionId\": \"1\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0003, \"pressureAltitude\": 30.0}, \"airwaySectionId\": \"1\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0001, \"pressureAltitude\": 30.0}, \"airwaySectionId\": \"2\"}, {\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0001, \"pressureAltitude\": 30.0}, \"airwaySectionId\": \"2\"}]}");
    deviations.add(airwayDeviation);

    airwayDeviation = new AirwayDeviation();
    airwayDeviation.setAirwayReservationId("reservationId02");
    airwayDeviation.setRouteDeviationRate(100.0);
    airwayDeviation.setRouteDeviationAmount("{\"horizontal\": 36.25, \"vertical\": 10.00}");
    airwayDeviation.setRouteDeviationTime("{\"horizontal\": 36.25, \"vertical\": 10.00}");
    airwayDeviation.setAirwayAdministratorId("admin789");
    airwayDeviation.setOperatorId("operator123");
    airwayDeviation.setAirwayId("2");
    airwayDeviation.setAircraftInfoId(456);
    airwayDeviation.setRouteDeviationCoordinates(
        "{\"coordinates\": [{\"Coordinates\": {\"latitude\": 35.0, \"longitude\": 136.0004, \"pressureAltitude\": 40.0}, \"airwaySectionId\": \"1\"}]}");
    deviations.add(airwayDeviation);

    int insertedRows = mapper.insertBatchSelective(deviations);
    assertEquals(2, insertedRows);
  }
}
