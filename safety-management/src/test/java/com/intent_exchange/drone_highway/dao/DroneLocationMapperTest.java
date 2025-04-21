package com.intent_exchange.drone_highway.dao;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.intent_exchange.drone_highway.model.CurrentLocation;
import com.intent_exchange.drone_highway.model.DroneLocation;

@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
class DroneLocationMapperTest {

  @Autowired
  private DroneLocationMapper mapper;

  @Autowired
  private static Clock clock = Clock.systemUTC();

  private static final String timestamp = String.valueOf(clock.instant());

  private DroneLocation getDroneLocation() {
    DroneLocation model = new DroneLocation();
    model.setUasId("usasId01");
    model.setGetLocationTimestamp(LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME));
    model.setSubscriptionId("subscriptionId01");
    model.setUaType("uaType01");
    model.setLatitude(43.0457375);
    model.setLongitude(141.3553984);
    model.setAboveGroundLevel(75);
    model.setTrackDirection(90);
    model.setSpeed(10.0);
    model.setVerticalSpeed(1.0);
    model.setRouteDeviationRate(0.0);
    model.setRouteDeviationRateUpdateTime(
        LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME));
    model.setReservationId("reservationId01");
    model.setAirwayId("airwayId01");
    model.setAirwaySectionId("airwaySectionId01");
    model.setOperationalStatus("NormalOperation");
    model.setOperatorId("operatorId01");
    model.setFlightTime("00:00:00.000");
    return model;
  }

  // private AirwayReservation getAirwayReservation() {
  // AirwayReservation model = new AirwayReservation();
  // model.setAirwayReservationId("reservationId01");
  // model.setStartAt(
  // LocalDateTime.parse("2024-11-13T10:41:00.000Z", DateTimeFormatter.ISO_DATE_TIME));
  // model.setEndAt(
  // LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME));
  // model.setReservedAt(
  // LocalDateTime.parse("2024-11-13T10:40:00.000Z", DateTimeFormatter.ISO_DATE_TIME));
  // List<String> airwaySectionIds = new ArrayList<String>();
  // airwaySectionIds.add("airwaySectionId01");
  // model.setAirwaySectionIds(airwaySectionIds);
  // model.setOperatorId("operatorId01");
  // model.setEvaluationResults(true);
  // model.setThirdPartyEvaluationResults(true);
  // model.setRailwayOperationEvaluationResults(true);
  // return model;
  // }

  // private CurrentLocation getCurrentLocation() {
  // CurrentLocation model = new CurrentLocation();
  // model.setReservationId("reservationId01");
  // model.setTimestamp(
  // LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME));
  // model.setLatitude(43.0457375);
  // model.setLongitude(141.3553984);
  // model.setAboveGroundLevel(75);
  // model.setAirwayId("airwayId01");
  // model.setAirwaySectionId("airwaySectionId01");
  // model.setOperationalStatus("NormalOperation");
  // model.setOperatorId("operatorId01");
  // model.setUasId("usasId01");
  // model.setUaType("uaType01");
  // model.setTrackDirection(90);
  // model.setSpeed(10.0);
  // model.setVerticalSpeed(1.0);
  // return model;
  // }

  @Test
  @Transactional
  @DisplayName("運行情報を蓄積する1")
  void testInsertSelective1() {
    DroneLocation row = getDroneLocation();

    int res = mapper.insertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @Transactional
  @DisplayName("指定した行の運行情報を更新する")
  void testUpdateByPrimaryKeySelective1() {
    DroneLocation row = getDroneLocation();

    int res = mapper.updateByPrimaryKeySelective(row);
    // assertEquals(1, res);
  }

  @Test
  @DisplayName("指定した行の運行情報の一つ前の時刻のデータを取得する")
  void testSelectPrevRowByPrimaryKey1() {
    DroneLocation row = getDroneLocation();

    DroneLocation res = mapper.selectPrevRowByPrimaryKey(row);
    // assertEquals(row, res);
  }

  @Test
  @DisplayName("運行情報の逸脱を検知する")
  void testDeviationDetect1() {
    DroneLocation row = getDroneLocation();

    DroneLocation res = mapper.deviationDetect(row);
    // assertEquals(row, res);
  }

  @Test
  @DisplayName("予約情報を取得する")
  void testGetReservationInfo1() {
    DroneLocation row = getDroneLocation();
    // AirwayReservation expectedRes = getAirwayReservation();

    AirwayReservation res = mapper.getReservationInfo(row);
    // assertEquals(expectedRes, res);
  }

  @Test
  @DisplayName("最寄りの航路区画 ID を取得する")
  void testGetNearestAirwaySectionId1() {
    DroneLocation row = getDroneLocation();

    String res = mapper.getNearestAirwaySectionId(row);
    // assertEquals("airwaySectionId01", res);
  }

  @Test
  @DisplayName("現在位置情報を取得する")
  void testGetCurrentLocation1() {
    String reservationId = "reservationId01";
    DroneLocation row = getDroneLocation();
    // CurrentLocation expectedRes = getCurrentLocation();

    CurrentLocation res = mapper.getCurrentLocation(reservationId);
    // assertEquals(expectedRes, res);
  }
}
