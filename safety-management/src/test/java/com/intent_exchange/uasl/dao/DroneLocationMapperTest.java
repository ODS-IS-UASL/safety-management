package com.intent_exchange.uasl.dao;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import com.intent_exchange.uasl.model.CurrentLocation;
import com.intent_exchange.uasl.model.DroneLocation;
import com.intent_exchange.uasl.model.FlightLog;
import com.intent_exchange.uasl.model.FlightLogSearch;
import com.intent_exchange.uasl.model.UaslReservation;

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
    model.setAltitude(75);
    model.setTrackDirection(90);
    model.setSpeed(10.0);
    model.setVerticalSpeed(1.0);
    model.setRouteDeviationRate(0.0);
    model.setRouteDeviationRateUpdateTime(
        LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME));
    model.setReservationId("reservationId01");
    model.setUaslId("uaslId01");
    model.setUaslSectionId("uaslSectionId01");
    model.setOperationalStatus("NormalOperation");
    model.setOperatorId("operatorId01");
    model.setFlightTime("00:00:00.000");
    model.setAircraftInfoId(123);
    return model;
  }

  // private UaslReservation getUaslReservation() {
  // UaslReservation model = new UaslReservation();
  // model.setUaslReservationId("reservationId01");
  // model.setStartAt(
  // LocalDateTime.parse("2024-11-13T10:41:00.000Z", DateTimeFormatter.ISO_DATE_TIME));
  // model.setEndAt(
  // LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_DATE_TIME));
  // model.setReservedAt(
  // LocalDateTime.parse("2024-11-13T10:40:00.000Z", DateTimeFormatter.ISO_DATE_TIME));
  // List<String> uaslSectionIds = new ArrayList<String>();
  // uaslSectionIds.add("uaslSectionId01");
  // model.setUaslSectionIds(uaslSectionIds);
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
  // model.setUaslId("uaslId01");
  // model.setUaslSectionId("uaslSectionId01");
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
    // UaslReservation expectedRes = getUaslReservation();

    UaslReservation res = mapper.getReservationInfo(row);
    // assertEquals(expectedRes, res);
  }

  @Test
  @DisplayName("最寄りの航路区画 ID を取得する")
  void testGetNearestUaslSectionId1() {
    DroneLocation row = getDroneLocation();

    String res = mapper.getNearestUaslSectionId(row);
    // assertEquals("uaslSectionId01", res);
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

  @Test
  @DisplayName("フライトログを取得する")
  @Transactional
  void testGetFlightLogs() {
    DroneLocation location = getDroneLocation();
    location.setGetLocationTimestamp(LocalDateTime.of(2024, 1, 1, 12, 0, 0));
    location.setReservationId("res001");
    location.setUaslId("uasl001");
    location.setOperatorId("op001");
    location.setAircraftInfoId(101);
    location.setUaType("TypeA");
    mapper.insertSelective(location);

    FlightLogSearch search = new FlightLogSearch();
    search.setStartTime(LocalDateTime.of(2024, 1, 1, 0, 0, 0));
    search.setEndTime(LocalDateTime.of(2024, 1, 1, 23, 59, 59));
    search.setReservationId("res001");
    search.setUaslId("uasl001");
    search.setOperatorId("op001");
    search.setAircraftInfoId(101);
    search.setUaType("TypeA");

    List<FlightLog> result = mapper.getFlightLogs(search);

    assertEquals(1, result.size());
    FlightLog actual = result.get(0);
    assertEquals("res001", actual.getReservationId());
    assertEquals("uasl001", actual.getUaslId());
    
    search.setReservationId("res002");
    result = mapper.getFlightLogs(search);
    assertEquals(0, result.size());
  }
}
