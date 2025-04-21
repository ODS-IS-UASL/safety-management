package com.intent_exchange.drone_highway.dao;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.intent_exchange.drone_highway.model.AirwayReservation;

@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
// @DatabaseSetup(value = {"classpath:test-t_airway_reservation-data.xlsx"},
// type = DatabaseOperation.CLEAN_INSERT)
class AirwayReservationMapperTest {

  @Autowired
  private AirwayReservationMapper mapper;

  // TODO:excel読み込み時カラムが大文字になる件調査中のためコメントアウト
  @Test
  @Transactional
  @DisplayName("航路予約情報を作成する")
  // @ExpectedDatabase(value = "classpath:expected-t_airway_reservation-upsert01.xlsx",
  // assertionMode = DatabaseAssertionMode.NON_STRICT)
  void testUpsertSelective1() {
    AirwayReservation row = new AirwayReservation();
    List<String> airwaySectionIds = new ArrayList<>();
    airwaySectionIds.add("airwaySectionIds02");
    row.setAirwayReservationId("airwayReservationId02");
    row.setAirwaySectionIds(airwaySectionIds);
    row.setOperatorId("operatorId02");
    row.setStartAt(LocalDateTime.parse("2024-11-13T10:41:00", DateTimeFormatter.ISO_DATE_TIME));
    row.setEndAt(LocalDateTime.parse("2024-11-13T10:42:00", DateTimeFormatter.ISO_DATE_TIME));
    row.setReservedAt(LocalDateTime.parse("2024-11-13T10:43:00", DateTimeFormatter.ISO_DATE_TIME));

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @Transactional
  @DisplayName("航路予約情報を更新する")
  // @ExpectedDatabase(value = "classpath:expected-t_airway_reservation-upsert02.xlsx",
  // assertionMode = DatabaseAssertionMode.NON_STRICT)
  void testUpsertSelective2() {
    AirwayReservation row = new AirwayReservation();
    List<String> airwaySectionIds = new ArrayList<>();
    airwaySectionIds.add("airwaySectionIds01");
    airwaySectionIds.add("airwaySectionIds02");
    row.setAirwayReservationId("airwayReservationId01");
    row.setAirwaySectionIds(airwaySectionIds);
    row.setOperatorId("operatorId02");
    row.setStartAt(LocalDateTime.parse("2024-11-13T10:41:00", DateTimeFormatter.ISO_DATE_TIME));
    row.setEndAt(LocalDateTime.parse("2024-11-13T10:42:00", DateTimeFormatter.ISO_DATE_TIME));
    row.setReservedAt(LocalDateTime.parse("2024-11-13T10:43:00", DateTimeFormatter.ISO_DATE_TIME));

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }
}
