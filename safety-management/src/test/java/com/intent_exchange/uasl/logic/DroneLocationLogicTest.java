package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.intent_exchange.uasl.dao.DroneLocationMapper;
import com.intent_exchange.uasl.dao.UaslReservationMapper;
import com.intent_exchange.uasl.dto.request.DroneLocationNotificationDto;
import com.intent_exchange.uasl.dto.response.CurrentLocationDto;
import com.intent_exchange.uasl.logic.mqtt.MqttDeviationNotificationLogic;
import com.intent_exchange.uasl.model.CurrentLocation;
import com.intent_exchange.uasl.model.DroneLocation;
import com.intent_exchange.uasl.model.UaslReservation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class DroneLocationLogicTest {

  @Mock
  private DroneLocationMapper mapper;
  
  @Mock
  private UaslReservationMapper reservationMapper;

  @Mock
  private Clock clock; // Clockクラスのモック

  @Mock
  private MqttClient mqttClient;

  @Mock
  private MqttDeviationNotificationLogic mqttDeviationNotificationLogic;

  @InjectMocks
  private DroneLocationLogic logic;

  private DroneLocationNotificationDto getDroneLocationNotificationDto() {
    DroneLocationNotificationDto dto = new DroneLocationNotificationDto();
    dto.setSubscriptionId("subscriptionId01");
    dto.setUasId("usasId01");
    dto.setUaType("uaType01");
    dto.setGetLocationTimestamp("2024-11-13T10:42:00.000Z");
    dto.setLatitude(43.0457375);
    dto.setLongitude(141.3553984);
    dto.setAltitude(75);
    dto.setTrackDirection(90);
    dto.setSpeed(10.0);
    dto.setVerticalSpeed(1.0);
    dto.setRouteDeviationRate(0.0);
    dto.setRouteDeviationRateUpdateTime("2024-11-13T10:42:00.000Z");
    return dto;
  }

  private DroneLocation getDroneLocation() {
    DroneLocation model = new DroneLocation();
    model.setUasId("usasId01");
    model.setGetLocationTimestamp(
        LocalDateTime.parse("2024-11-13T10:42:00.000Z", DateTimeFormatter.ISO_DATE_TIME));
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
        LocalDateTime.parse("2024-11-13T10:42:00.000Z", DateTimeFormatter.ISO_DATE_TIME));
    model.setReservationId("reservationId01");
    model.setUaslId("uaslId01");
    model.setUaslSectionId("uaslSectionId01");
    model.setOperationalStatus("NormalOperation");
    model.setOperatorId("operatorId01");
    model.setFlightTime("00:00:00.000");
    return model;
  }

  @Test
  @DisplayName("運行中ドローンの位置情報を格納する")
  void testInsert1() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    DroneLocation model = getDroneLocation();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.insertSelective(model)).thenReturn(1);
      assertDoesNotThrow(() -> logic.insert(dto));
    }
  }

  @Test
  @DisplayName("格納してある運行中ドローンの位置情報を格納する")
  void testUpdate1() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    DroneLocation model = getDroneLocation();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.updateByPrimaryKeySelective(model)).thenReturn(1);
      assertDoesNotThrow(() -> logic.updateDroneLocation(dto));
    }
  }

  @Test
  @DisplayName("逸脱判定で逸脱していない場合1")
  void testDeviationDetect1() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setGetLocationTimestamp("2024-11-13T10:41:00.000Z");

    DroneLocation model = getDroneLocation();
    DroneLocation prevModel = getDroneLocation();
    prevModel.setGetLocationTimestamp(
        LocalDateTime.parse("2024-11-13T10:41:00.000Z", DateTimeFormatter.ISO_DATE_TIME));
    prevModel.setRouteDeviationRateUpdateTime(
        LocalDateTime.parse("2024-11-13T10:41:00.000Z", DateTimeFormatter.ISO_DATE_TIME));

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic.when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic.when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      doReturn(1).when(mapper).updateByPrimaryKeySelective(any(DroneLocation.class));
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }

  @Test
  @DisplayName("逸脱判定で逸脱していない場合2")
  void testDeviationDetect2() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(2.0);
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setGetLocationTimestamp("2024-11-13T10:41:00.000Z");
    prevDto.setRouteDeviationRate(2.0);

    DroneLocation model = getDroneLocation();
    DroneLocation prevModel = getDroneLocation();
    prevModel.setGetLocationTimestamp(
        LocalDateTime.parse("2024-11-13T10:41:00.000Z", DateTimeFormatter.ISO_DATE_TIME));
    prevModel.setRouteDeviationRateUpdateTime(
        LocalDateTime.parse("2024-11-13T10:41:00.000Z", DateTimeFormatter.ISO_DATE_TIME));

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic.when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic.when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      doReturn(1).when(mapper).updateByPrimaryKeySelective(any(DroneLocation.class));
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }

  @Test
  @DisplayName("逸脱判定で逸脱していない場合3")
  void testDeviationDetect3() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setGetLocationTimestamp("2024-11-13T10:41:00.000Z");

    dto.setRouteDeviationRate(1.0);
    dto.setOperationalStatus("RouteApproach");
    prevDto.setRouteDeviationRate(1.0);
    prevDto.setOperationalStatus("RouteApproach");
    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(1.0);
    model.setOperationalStatus("RouteApproach");
    DroneLocation prevModel = getDroneLocation();
    prevModel.setRouteDeviationRate(1.0);
    prevModel.setOperationalStatus("RouteApproach");
    prevModel.setGetLocationTimestamp(
        LocalDateTime.parse("2024-11-13T10:41:00.000Z", DateTimeFormatter.ISO_DATE_TIME));
    prevModel.setRouteDeviationRateUpdateTime(
        LocalDateTime.parse("2024-11-13T10:41:00.000Z", DateTimeFormatter.ISO_DATE_TIME));

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic.when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic.when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      doReturn(1).when(mapper).updateByPrimaryKeySelective(any(DroneLocation.class));
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }

  @Test
  @DisplayName("逸脱検知後の publish が正常終了する場合1")
  void testDeviationDetect4() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(1.0);
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setRouteDeviationRate(0.0);

    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(1.0);
    DroneLocation prevModel = getDroneLocation();
    prevModel.setRouteDeviationRate(0.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic.when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic.when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      doReturn(1).when(mapper).updateByPrimaryKeySelective(any(DroneLocation.class));
      doNothing().when(mqttDeviationNotificationLogic).notifyUaslDeviation(dto);

      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }

  @Test
  @DisplayName("逸脱検知後の publish が正常終了する場合2")
  void testDeviationDetect5() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(0.0);
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setRouteDeviationRate(1.0);

    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(0.0);
    DroneLocation prevModel = getDroneLocation();
    prevModel.setRouteDeviationRate(1.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic.when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic.when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      doReturn(1).when(mapper).updateByPrimaryKeySelective(any(DroneLocation.class));
      doNothing().when(mqttDeviationNotificationLogic).notifyUaslDeviation(dto);

      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }

  @Test
  @DisplayName("逸脱検知で連続で逸脱していると判定が出た場合")
  void testDeviationDetect6() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(0.0);
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setRouteDeviationRate(1.0);

    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(1.0);
    DroneLocation prevModel = getDroneLocation();
    prevModel.setRouteDeviationRate(1.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic.when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic.when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }

  @Test
  @DisplayName("航路進入前の場合1")
  void testDeviationDetect7() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(1.0);
    dto.setOperationalStatus("航路進入前");
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setRouteDeviationRate(0.0);

    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(1.0);
    model.setOperationalStatus("航路進入前");
    DroneLocation prevModel = getDroneLocation();
    prevModel.setRouteDeviationRate(0.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic.when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic.when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }

  @Test
  @DisplayName("航路進入前の場合2")
  void testDeviationDetect8() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(0.0);
    dto.setOperationalStatus("RouteDeviation");
    DroneLocationNotificationDto prevDto = null;

    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(0.0);
    model.setOperationalStatus("RouteDeviation");
    DroneLocation prevModel = null;

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic.when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic.when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }


  @Test
  @DisplayName("予約情報を取得する")
  void testGetReservationInfo1() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    DroneLocation model = getDroneLocation();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.getReservationInfo(model)).thenReturn(new UaslReservation());
      assertDoesNotThrow(() -> logic.getReservationInfo(dto));
    }
  }

  @Test
  @DisplayName("予約情報を取得できなかった場合")
  void testGetReservationInfo2() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    DroneLocation model = getDroneLocation();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.getReservationInfo(model)).thenReturn(null);
      assertDoesNotThrow(() -> logic.getReservationInfo(dto));
    }
  }

  @Test
  @DisplayName("一番近い航路区画 ID を取得する")
  void testGetNearestUaslSectionId() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setUaslSectionId("uaslSectionId01");
    DroneLocation model = getDroneLocation();

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.getNearestUaslSectionId(model)).thenReturn(dto.getUaslSectionId());
      assertDoesNotThrow(() -> logic.getNearestUaslSectionId(dto));
    }
  }

  @Test
  @DisplayName("現在のドローンの位置情報を取得する　正常系1")
  void testGetCurrentLocation1() {
    String reservationId = "reservationId01";
    CurrentLocation model = new CurrentLocation();
    model.setUasId("usasId01");
    CurrentLocationDto dto = new CurrentLocationDto();
    dto.setUasId("usasId01");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(model, CurrentLocationDto.class)).thenReturn(dto);
      when(mapper.getCurrentLocation(reservationId)).thenReturn(model);
      when(reservationMapper.getPlannedDeviation(reservationId)).thenReturn(true);
      assertDoesNotThrow(() -> logic.getCurrentLocation(reservationId));
    }
  }

  @Test
  @DisplayName("現在のドローンの位置情報を取得する　正常系2")
  void testGetCurrentLocation2() {
    String reservationId = "reservationId01";
    assertDoesNotThrow(() -> logic.getCurrentLocation(reservationId));
  }

  @Test
  @DisplayName("現在のドローンの位置情報を取得する RouteDeviationで計画的")
  void testGetCurrentLocationPlanned() {
    String reservationId = "reservationId01";
    CurrentLocation model = new CurrentLocation();
    model.setUasId("usasId01");
    model.setOperationalStatus("RouteDeviation");
    CurrentLocationDto dto = new CurrentLocationDto();
    dto.setUasId("usasId01");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(model, CurrentLocationDto.class)).thenReturn(dto);
      when(mapper.getCurrentLocation(reservationId)).thenReturn(model);
      when(reservationMapper.getPlannedDeviation(reservationId)).thenReturn(true);
      assertDoesNotThrow(() -> logic.getCurrentLocation(reservationId));
    }
  }

  @Test
  @DisplayName("現在のドローンの位置情報を取得する RouteDeviationで計画的ではない")
  void testGetCurrentLocationNotPlanned() {
    String reservationId = "reservationId01";
    CurrentLocation model = new CurrentLocation();
    model.setUasId("usasId01");
    model.setOperationalStatus("RouteDeviation");
    CurrentLocationDto dto = new CurrentLocationDto();
    dto.setUasId("usasId01");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(model, CurrentLocationDto.class)).thenReturn(dto);
      when(mapper.getCurrentLocation(reservationId)).thenReturn(model);
      when(reservationMapper.getPlannedDeviation(reservationId)).thenReturn(false);
      assertDoesNotThrow(() -> logic.getCurrentLocation(reservationId));
    }
  }

  @Test
  @DisplayName("現在のドローンの位置情報を取得する PlannedRouteDeviationで計画的")
  void testGetCurrentLocationPlannedAndPlanned() {
    String reservationId = "reservationId01";
    CurrentLocation model = new CurrentLocation();
    model.setUasId("usasId01");
    model.setOperationalStatus("PlannedRouteDeviation");
    CurrentLocationDto dto = new CurrentLocationDto();
    dto.setUasId("usasId01");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(model, CurrentLocationDto.class)).thenReturn(dto);
      when(mapper.getCurrentLocation(reservationId)).thenReturn(model);
      when(reservationMapper.getPlannedDeviation(reservationId)).thenReturn(true);
      assertDoesNotThrow(() -> logic.getCurrentLocation(reservationId));
    }
  }

  @Test
  @DisplayName("現在のドローンの位置情報を取得する PlannedRouteDeviationで計画的ではない")
  void testGetCurrentLocationPlannedAndNotPlanned() {
    String reservationId = "reservationId01";
    CurrentLocation model = new CurrentLocation();
    model.setUasId("usasId01");
    model.setOperationalStatus("PlannedRouteDeviation");
    CurrentLocationDto dto = new CurrentLocationDto();
    dto.setUasId("usasId01");

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(model, CurrentLocationDto.class)).thenReturn(dto);
      when(mapper.getCurrentLocation(reservationId)).thenReturn(model);
      when(reservationMapper.getPlannedDeviation(reservationId)).thenReturn(false);
      assertDoesNotThrow(() -> logic.getCurrentLocation(reservationId));
    }
  }

  @Test
  @DisplayName("逸脱状況変更：　現在逸脱なし")
  void testChangeDeviationNormal() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(0.0);
    dto.setPlannedDeviation(true);
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setRouteDeviationRate(1.0);
    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(0.0);
    DroneLocation prevModel = getDroneLocation();
    prevDto.setRouteDeviationRate(1.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic
          .when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic
          .when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }

  @Test
  @DisplayName("逸脱状況変更：　現在逸脱あり、計画的ではない")
  void testChangeDeviationNotPlanned() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(1.0);
    dto.setPlannedDeviation(false);
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setRouteDeviationRate(0.0);
    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(1.0);
    DroneLocation prevModel = getDroneLocation();
    prevDto.setRouteDeviationRate(0.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic
          .when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic
          .when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
      verify(mqttDeviationNotificationLogic)
          .notifyUaslDeviation(argThat(arg -> "RouteDeviation".equals(arg.getOperationalStatus())));
    }
  }

  @Test
  @DisplayName("逸脱状況変更：　現在逸脱あり、計画的である")
  void testChangeDeviationPlanned() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(1.0);
    dto.setPlannedDeviation(true);
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setRouteDeviationRate(0.0);
    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(1.0);
    DroneLocation prevModel = getDroneLocation();
    prevDto.setRouteDeviationRate(0.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic
          .when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic
          .when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
      verify(mqttDeviationNotificationLogic)
          .notifyUaslDeviation(
              argThat(arg -> "PlannedRouteDeviation".equals(arg.getOperationalStatus())));
    }
  }

  @Test
  @DisplayName("逸脱状態継続: 計画的ではない")
  void testContinuedDeviationNotPlanned() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(1.0);
    dto.setPlannedDeviation(false);
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setRouteDeviationRate(1.0);
    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(1.0);
    DroneLocation prevModel = getDroneLocation();
    prevDto.setRouteDeviationRate(1.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic
          .when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic
          .when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }

  @Test
  @DisplayName("逸脱状態継続: 計画的である")
  void testContinuedDeviationPlanned() {
    DroneLocationNotificationDto dto = getDroneLocationNotificationDto();
    dto.setRouteDeviationRate(1.0);
    dto.setPlannedDeviation(true);
    DroneLocationNotificationDto prevDto = getDroneLocationNotificationDto();
    prevDto.setRouteDeviationRate(1.0);
    DroneLocation model = getDroneLocation();
    model.setRouteDeviationRate(1.0);
    DroneLocation prevModel = getDroneLocation();
    prevDto.setRouteDeviationRate(1.0);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(dto, DroneLocation.class)).thenReturn(model);
      when(mapper.deviationDetect(model)).thenReturn(model);
      when(mapper.selectPrevRowByPrimaryKey(model)).thenReturn(prevModel);
      mockedStatic
          .when(() -> ModelMapperUtil.map(model, DroneLocationNotificationDto.class))
          .thenReturn(dto);
      mockedStatic
          .when(() -> ModelMapperUtil.map(prevModel, DroneLocationNotificationDto.class))
          .thenReturn(prevDto);
      assertDoesNotThrow(() -> logic.deviationDetect(dto));
    }
  }
}
