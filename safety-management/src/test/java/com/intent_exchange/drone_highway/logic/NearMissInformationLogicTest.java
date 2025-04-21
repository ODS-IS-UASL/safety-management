package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.drone_highway.dto.geojson.SubscriptionsGeoJsonFeatures;
import com.intent_exchange.drone_highway.dto.geojson.SubscriptionsGeoJsonGeometry;
import com.intent_exchange.drone_highway.dto.request.NearMissInformationRequestAttributesDto;
import com.intent_exchange.drone_highway.dto.request.NearMissInformationRequestSelectDto;
import com.intent_exchange.drone_highway.dto.response.AirwayDeviationDetailDto;
import com.intent_exchange.drone_highway.dto.response.MonitoringInfoDetailDto;
import com.intent_exchange.drone_highway.dto.response.ThirdPartyMonitoringInformationIntrusionsDto;
import com.intent_exchange.drone_highway.entity.CoordinateDitailEntity;
import com.intent_exchange.drone_highway.entity.CoordinateEntity;
import com.intent_exchange.drone_highway.entity.CoordinatesEntity;
import com.intent_exchange.drone_highway.entity.NearMissInformationRequestAttributesAreaInfo;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponse;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributes;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesRouteDeviationInfoInner;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner;
import com.intent_exchange.drone_highway.entity.ThirdPartyTimeEntity;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class NearMissInformationLogicTest {

  /** 航路逸脱情報取得のロジック */
  @Mock
  private AirwayDeviationSelectLogic airwayDeviationLogic;

  /** 第三者立入監視情報取得のロジック */
  @Mock
  private MonitoringInfoSelectLogic monitoringInfoLogic;

  @Mock
  private Clock clock; // Clockクラスのモック

  @Spy
  @InjectMocks
  NearMissInformationLogic logic;

  @Test
  @DisplayName("ヒヤリハット情報取得APIのレスポンス生成")
  void testNearMissInformation1() {

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      NearMissInformationRequestSelectDto nearDto = new NearMissInformationRequestSelectDto();
      mockedStatic.when(() -> ModelMapperUtil.map(any(), any())).thenReturn(nearDto);

      List<AirwayDeviationDetailDto> nermissSelectList = new ArrayList<>();
      doReturn(nermissSelectList).when(airwayDeviationLogic).nermissSelect(any());

      List<MonitoringInfoDetailDto> moniterSelectList = new ArrayList<>();
      doReturn(moniterSelectList).when(monitoringInfoLogic).moniterSelect(any());

      List<NearMissInformationResponseAttributesRouteDeviationInfoInner> routeDeviationInfos =
          new ArrayList<>();
      NearMissInformationResponseAttributesRouteDeviationInfoInner routeDeviationInfo =
          new NearMissInformationResponseAttributesRouteDeviationInfoInner();
      List<NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> routeDeviationDetectionInfos =
          new ArrayList<>();
      routeDeviationInfo.setRouteDeviationDetectionInfo(routeDeviationDetectionInfos);
      routeDeviationInfos.add(routeDeviationInfo);
      doReturn(routeDeviationInfos).when(logic).getRouteDeviationInfos(any());

      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitorings =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner thirdPartyEntryMonitoring =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner();
      thirdPartyEntryMonitorings.add(thirdPartyEntryMonitoring);
      doReturn(thirdPartyEntryMonitorings).when(logic).getThirdPartyEntryMonitoring(any());

      // レスポンス
      NearMissInformationResponse nearMisInfRes = new NearMissInformationResponse();
      NearMissInformationResponseAttributes nearMisInfResAttr =
          new NearMissInformationResponseAttributes();
      nearMisInfResAttr.setRouteDeviationInfo(routeDeviationInfos);
      nearMisInfResAttr.setThirdPartyEntryMonitoring(thirdPartyEntryMonitorings);
      nearMisInfRes.setDataModelType(NearMissInformationResponse.DataModelTypeEnum.TEST1);
      nearMisInfRes.setAttributes(nearMisInfResAttr);

      // リクエスト
      NearMissInformationRequestAttributesDto entity =
          new NearMissInformationRequestAttributesDto();
      NearMissInformationRequestAttributesAreaInfo areaInfoEntity =
          new NearMissInformationRequestAttributesAreaInfo();
      List<List<Double>> coordinatesEntity = new ArrayList<>();
      coordinatesEntity.add(Arrays.asList(135.760497, 35.012033));
      areaInfoEntity.setCoordinates(coordinatesEntity);
      entity.setAreaInfo(areaInfoEntity);


      NearMissInformationResponse response = logic.nearMissInformation(entity);

      assertNotNull(response);
      assertEquals(nearMisInfRes, response);

    }

  }

  @Test
  @DisplayName("ヒヤリハット情報の取得Logic　航路逸脱情報/第三者立入情報がnull")
  void testNearMissInformation2() {

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      NearMissInformationRequestSelectDto nearDto = new NearMissInformationRequestSelectDto();
      mockedStatic.when(() -> ModelMapperUtil.map(any(), any())).thenReturn(nearDto);

      doReturn(null).when(airwayDeviationLogic).nermissSelect(any());

      doReturn(null).when(monitoringInfoLogic).moniterSelect(any());

      NearMissInformationRequestAttributesDto entity =
          new NearMissInformationRequestAttributesDto();
      NearMissInformationRequestAttributesAreaInfo areaInfo =
          new NearMissInformationRequestAttributesAreaInfo();
      List<List<Double>> coordinates = new ArrayList<>();
      areaInfo.setCoordinates(coordinates);
      entity.setAreaInfo(areaInfo);
      NearMissInformationResponse response = logic.nearMissInformation(entity);

      assertNotNull(response);
      verify(logic, times(0)).getRouteDeviationInfos(eq(null));
      verify(logic, times(0)).getThirdPartyEntryMonitoring(eq(null));
      verify(logic, times(0)).getRouteDeviationDetectionInfo(eq(null));

    }

  }

  @Test
  @DisplayName("航路逸脱情報取得Logic")
  void testGetRouteDeviationInfos1() {

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      when(clock.getZone()).thenReturn(ZoneId.of("UTC"));

      ThirdPartyTimeEntity thirdPartyTimeEntity = new ThirdPartyTimeEntity();
      List<String> times = new ArrayList<>();
      String time = "2025-01-27 03:00:00.0";
      times.add("2025-01-27 03:00:00.0");
      thirdPartyTimeEntity.setTime(times);
      mockedStatic.when(() -> ModelMapperUtil.jsonStrToClass(any(), any()))
          .thenReturn(thirdPartyTimeEntity);


      AirwayDeviationDetailDto airwayDeviation = new AirwayDeviationDetailDto();
      List<AirwayDeviationDetailDto> airwayDeviations = new ArrayList<>();
      airwayDeviation.setAirwayId("1");
      airwayDeviation.setAirwayAdministratorId("1");
      airwayDeviation.setOperatorId("1");
      airwayDeviation.setRouteDeviationRate(1.0);
      airwayDeviation.setRouteDeviationAmount("1");
      airwayDeviation.setRouteDeviationTime(time);
      airwayDeviation.setRouteDeviationCoordinates("1");
      airwayDeviation.setAirwaySectionId("1");
      airwayDeviation.setAirwayReservationId("1");
      airwayDeviations.add(airwayDeviation);

      List<NearMissInformationResponseAttributesRouteDeviationInfoInner> routeDeviationInfos =
          new ArrayList<>();
      NearMissInformationResponseAttributesRouteDeviationInfoInner routeDeviationInfo =
          new NearMissInformationResponseAttributesRouteDeviationInfoInner();
      NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation nearMissInformation =
          new NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation();
      List<NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> routeDeviationDetectionInfo =
          new ArrayList<>();
      NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner routeDeviationDetectionInfoInner =
          new NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner();
      nearMissInformation.setRouteDeviationAmount("1");
      nearMissInformation.setRouteDeviationRate(1.0);
      routeDeviationDetectionInfo.add(routeDeviationDetectionInfoInner);
      routeDeviationInfo.setAirwayId("1");
      routeDeviationInfo.setAirwayAdministratorId("1");
      routeDeviationInfo.setOperatorId("1");
      routeDeviationInfo.setNearMissInformation(nearMissInformation);
      routeDeviationInfo.setRouteDeviationDetectionInfo(routeDeviationDetectionInfo);
      routeDeviationInfo.setTime("2025-01-27T03:00:00Z");
      routeDeviationInfos.add(routeDeviationInfo);

      doReturn(routeDeviationDetectionInfo).when(logic).getRouteDeviationDetectionInfo(any());

      List<NearMissInformationResponseAttributesRouteDeviationInfoInner> results =
          logic.getRouteDeviationInfos(airwayDeviations);

      assertNotNull(results);
      assertEquals(routeDeviationInfos, results);

    }
  }

  @SuppressWarnings("null")
  @Test
  @DisplayName("第三者立入情報取得Logic 侵入対象が車の場合")
  void testGetThirdPartyEntryMonitoring1() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      List<SubscriptionsGeoJsonFeatures> features = new ArrayList<>();
      SubscriptionsGeoJsonFeatures thirdPartyFeature = new SubscriptionsGeoJsonFeatures();
      SubscriptionsGeoJsonGeometry thirdPartyGeometry = new SubscriptionsGeoJsonGeometry();
      Map<String, Object> thirdPartyProperties = new HashMap<>();
      List<Map<String, Object>> intrusionslist = new ArrayList<>();
      List<ThirdPartyMonitoringInformationIntrusionsDto> intrusions = new ArrayList<>();
      ThirdPartyMonitoringInformationIntrusionsDto intrusion =
          new ThirdPartyMonitoringInformationIntrusionsDto();
      intrusion.setCurrentTime("2025-01-27T03:00:00Z");
      intrusion.setType(1);
      intrusion.setCount(1);
      intrusions.add(intrusion);
      thirdPartyProperties.put("areaID", "areaId");
      thirdPartyProperties.put("timestamp", "2025-01-27T03:00:00Z");
      thirdPartyProperties.put("intrusions", intrusions);
      thirdPartyFeature.setProperties(thirdPartyProperties);
      thirdPartyGeometry.setCoordinates(Arrays.asList(Arrays.asList(Arrays.asList(139.0, 35.0))));
      thirdPartyFeature.setGeometry(thirdPartyGeometry);
      features.add(thirdPartyFeature);

      mockedStatic.when(() -> ModelMapperUtil.jsonStrToListClass(any(), any()))
          .thenReturn(features);
      mockedStatic.when(() -> ModelMapperUtil.convertListMapToListDto(any(), any()))
          .thenReturn(intrusions);

      List<MonitoringInfoDetailDto> monInfors = new ArrayList<>();
      MonitoringInfoDetailDto monInfor = new MonitoringInfoDetailDto();
      monInfor.setAirwayAdministratorId("1");
      monInfor.setOperatorId("1");
      monInfor.setAirwayId("1");
      monInfor.setAirwayReservationId("1");
      monInfor.setMonitoringInformation("");
      monInfors.add(monInfor);

      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitorings =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner thirdPartyEntryMonitoring =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo thirdPartyInfo =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo();
      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner> featuresInner =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner featuresInnerInner =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry geometry =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties properties =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties();
      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner> traffics =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner traffic =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner();
      traffic.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner.TypeEnum.u);
      traffic.setCurrentTime("2025-01-27T03:00:00Z");
      traffic.setCount(1);
      traffics.add(traffic);
      geometry.setCoordinates(Arrays.asList(Arrays.asList(139.0, 35.0)));
      geometry.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry.TypeEnum.POLYGON);
      properties.setArea("areaId");
      properties.setTimestamp("2025-01-27T03:00:00Z");
      properties.setTraffics(traffics);
      featuresInnerInner.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner.TypeEnum.FEATURE);
      featuresInnerInner.setGeometry(geometry);
      featuresInnerInner.setProperties(properties);
      featuresInner.add(featuresInnerInner);
      thirdPartyInfo.setFeatures(featuresInner);
      thirdPartyInfo.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo.TypeEnum.FEATURE_COLLECTION);
      thirdPartyEntryMonitoring.setThirdPartyInfo(thirdPartyInfo);
      thirdPartyEntryMonitoring.setAirwayAdministratorId("1");
      thirdPartyEntryMonitoring.setOperatorId("1");
      thirdPartyEntryMonitoring.setAirwayId("1");
      thirdPartyEntryMonitoring.setAirwaySectionId(null);
      thirdPartyEntryMonitorings.add(thirdPartyEntryMonitoring);

      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> results =
          logic.getThirdPartyEntryMonitoring(monInfors);

      assertNotNull(results);
      assertEquals(thirdPartyEntryMonitorings, results);

    }
  }

  @SuppressWarnings("null")
  @Test
  @DisplayName("第三者立入情報取得Logic 侵入対象が自転車の場合")
  void testGetThirdPartyEntryMonitoring2() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      List<SubscriptionsGeoJsonFeatures> features = new ArrayList<>();
      SubscriptionsGeoJsonFeatures thirdPartyFeature = new SubscriptionsGeoJsonFeatures();
      SubscriptionsGeoJsonGeometry thirdPartyGeometry = new SubscriptionsGeoJsonGeometry();
      Map<String, Object> thirdPartyProperties = new HashMap<>();
      List<Map<String, Object>> intrusionslist = new ArrayList<>();
      List<ThirdPartyMonitoringInformationIntrusionsDto> intrusions = new ArrayList<>();
      ThirdPartyMonitoringInformationIntrusionsDto intrusion =
          new ThirdPartyMonitoringInformationIntrusionsDto();
      intrusion.setCurrentTime("2025-01-27T03:00:00Z");
      intrusion.setType(2);
      intrusion.setCount(1);
      intrusions.add(intrusion);
      thirdPartyProperties.put("areaID", "areaId");
      thirdPartyProperties.put("timestamp", "2025-01-27T03:00:00Z");
      thirdPartyProperties.put("intrusions", intrusions);
      thirdPartyFeature.setProperties(thirdPartyProperties);
      thirdPartyGeometry.setCoordinates(Arrays.asList(Arrays.asList(Arrays.asList(139.0, 35.0))));
      thirdPartyFeature.setGeometry(thirdPartyGeometry);
      features.add(thirdPartyFeature);

      mockedStatic.when(() -> ModelMapperUtil.jsonStrToListClass(any(), any()))
          .thenReturn(features);
      mockedStatic.when(() -> ModelMapperUtil.convertListMapToListDto(any(), any()))
          .thenReturn(intrusions);

      List<MonitoringInfoDetailDto> monInfors = new ArrayList<>();
      MonitoringInfoDetailDto monInfor = new MonitoringInfoDetailDto();
      monInfor.setAirwayAdministratorId("1");
      monInfor.setOperatorId("1");
      monInfor.setAirwayId("1");
      monInfor.setAirwayReservationId("1");
      monInfor.setMonitoringInformation("");
      monInfors.add(monInfor);

      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitorings =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner thirdPartyEntryMonitoring =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo thirdPartyInfo =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo();
      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner> featuresInner =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner featuresInnerInner =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry geometry =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties properties =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties();
      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner> traffics =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner traffic =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner();
      traffic.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner.TypeEnum.u2);
      traffic.setCurrentTime("2025-01-27T03:00:00Z");
      traffic.setCount(1);
      traffics.add(traffic);
      geometry.setCoordinates(Arrays.asList(Arrays.asList(139.0, 35.0)));
      geometry.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry.TypeEnum.POLYGON);
      properties.setArea("areaId");
      properties.setTimestamp("2025-01-27T03:00:00Z");
      properties.setTraffics(traffics);
      featuresInnerInner.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner.TypeEnum.FEATURE);
      featuresInnerInner.setGeometry(geometry);
      featuresInnerInner.setProperties(properties);
      featuresInner.add(featuresInnerInner);
      thirdPartyInfo.setFeatures(featuresInner);
      thirdPartyInfo.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo.TypeEnum.FEATURE_COLLECTION);
      thirdPartyEntryMonitoring.setThirdPartyInfo(thirdPartyInfo);
      thirdPartyEntryMonitoring.setAirwayAdministratorId("1");
      thirdPartyEntryMonitoring.setOperatorId("1");
      thirdPartyEntryMonitoring.setAirwayId("1");
      thirdPartyEntryMonitoring.setAirwaySectionId(null);
      thirdPartyEntryMonitorings.add(thirdPartyEntryMonitoring);

      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> results =
          logic.getThirdPartyEntryMonitoring(monInfors);

      assertNotNull(results);
      assertEquals(thirdPartyEntryMonitorings, results);

    }
  }

  @SuppressWarnings("null")
  @Test
  @DisplayName("第三者立入情報取得Logic 侵入対象が人の場合")
  void testGetThirdPartyEntryMonitoring3() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      List<SubscriptionsGeoJsonFeatures> features = new ArrayList<>();
      SubscriptionsGeoJsonFeatures thirdPartyFeature = new SubscriptionsGeoJsonFeatures();
      SubscriptionsGeoJsonGeometry thirdPartyGeometry = new SubscriptionsGeoJsonGeometry();
      Map<String, Object> thirdPartyProperties = new HashMap<>();
      List<Map<String, Object>> intrusionslist = new ArrayList<>();
      List<ThirdPartyMonitoringInformationIntrusionsDto> intrusions = new ArrayList<>();
      ThirdPartyMonitoringInformationIntrusionsDto intrusion =
          new ThirdPartyMonitoringInformationIntrusionsDto();
      intrusion.setCurrentTime("2025-01-27T03:00:00Z");
      intrusion.setType(3);
      intrusion.setCount(1);
      intrusions.add(intrusion);
      thirdPartyProperties.put("areaID", "areaId");
      thirdPartyProperties.put("timestamp", "2025-01-27T03:00:00Z");
      thirdPartyProperties.put("intrusions", intrusions);
      thirdPartyFeature.setProperties(thirdPartyProperties);
      thirdPartyGeometry.setCoordinates(Arrays.asList(Arrays.asList(Arrays.asList(139.0, 35.0))));
      thirdPartyFeature.setGeometry(thirdPartyGeometry);
      features.add(thirdPartyFeature);

      mockedStatic.when(() -> ModelMapperUtil.jsonStrToListClass(any(), any()))
          .thenReturn(features);
      mockedStatic.when(() -> ModelMapperUtil.convertListMapToListDto(any(), any()))
          .thenReturn(intrusions);

      List<MonitoringInfoDetailDto> monInfors = new ArrayList<>();
      MonitoringInfoDetailDto monInfor = new MonitoringInfoDetailDto();
      monInfor.setAirwayAdministratorId("1");
      monInfor.setOperatorId("1");
      monInfor.setAirwayId("1");
      monInfor.setAirwayReservationId("1");
      monInfor.setMonitoringInformation("");
      monInfors.add(monInfor);

      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitorings =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner thirdPartyEntryMonitoring =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo thirdPartyInfo =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo();
      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner> featuresInner =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner featuresInnerInner =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry geometry =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties properties =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties();
      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner> traffics =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner traffic =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner();
      traffic.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner.TypeEnum.u3);
      traffic.setCurrentTime("2025-01-27T03:00:00Z");
      traffic.setCount(1);
      traffics.add(traffic);
      geometry.setCoordinates(Arrays.asList(Arrays.asList(139.0, 35.0)));
      geometry.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry.TypeEnum.POLYGON);
      properties.setArea("areaId");
      properties.setTimestamp("2025-01-27T03:00:00Z");
      properties.setTraffics(traffics);
      featuresInnerInner.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner.TypeEnum.FEATURE);
      featuresInnerInner.setGeometry(geometry);
      featuresInnerInner.setProperties(properties);
      featuresInner.add(featuresInnerInner);
      thirdPartyInfo.setFeatures(featuresInner);
      thirdPartyInfo.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo.TypeEnum.FEATURE_COLLECTION);
      thirdPartyEntryMonitoring.setThirdPartyInfo(thirdPartyInfo);
      thirdPartyEntryMonitoring.setAirwayAdministratorId("1");
      thirdPartyEntryMonitoring.setOperatorId("1");
      thirdPartyEntryMonitoring.setAirwayId("1");
      thirdPartyEntryMonitoring.setAirwaySectionId(null);
      thirdPartyEntryMonitorings.add(thirdPartyEntryMonitoring);

      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> results =
          logic.getThirdPartyEntryMonitoring(monInfors);

      assertNotNull(results);
      assertEquals(thirdPartyEntryMonitorings, results);

    }
  }

  @SuppressWarnings("null")
  @Test
  @DisplayName("第三者立入情報取得Logic 侵入対象がバイクの場合")
  void testGetThirdPartyEntryMonitoring4() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      List<SubscriptionsGeoJsonFeatures> features = new ArrayList<>();
      SubscriptionsGeoJsonFeatures thirdPartyFeature = new SubscriptionsGeoJsonFeatures();
      SubscriptionsGeoJsonGeometry thirdPartyGeometry = new SubscriptionsGeoJsonGeometry();
      Map<String, Object> thirdPartyProperties = new HashMap<>();
      List<Map<String, Object>> intrusionslist = new ArrayList<>();
      List<ThirdPartyMonitoringInformationIntrusionsDto> intrusions = new ArrayList<>();
      ThirdPartyMonitoringInformationIntrusionsDto intrusion =
          new ThirdPartyMonitoringInformationIntrusionsDto();
      intrusion.setCurrentTime("2025-01-27T03:00:00Z");
      intrusion.setType(4);
      intrusion.setCount(1);
      intrusions.add(intrusion);
      thirdPartyProperties.put("areaID", "areaId");
      thirdPartyProperties.put("timestamp", "2025-01-27T03:00:00Z");
      thirdPartyProperties.put("intrusions", intrusions);
      thirdPartyFeature.setProperties(thirdPartyProperties);
      thirdPartyGeometry.setCoordinates(Arrays.asList(Arrays.asList(Arrays.asList(139.0, 35.0))));
      thirdPartyFeature.setGeometry(thirdPartyGeometry);
      features.add(thirdPartyFeature);

      mockedStatic.when(() -> ModelMapperUtil.jsonStrToListClass(any(), any()))
          .thenReturn(features);
      mockedStatic.when(() -> ModelMapperUtil.convertListMapToListDto(any(), any()))
          .thenReturn(intrusions);

      List<MonitoringInfoDetailDto> monInfors = new ArrayList<>();
      MonitoringInfoDetailDto monInfor = new MonitoringInfoDetailDto();
      monInfor.setAirwayAdministratorId("1");
      monInfor.setOperatorId("1");
      monInfor.setAirwayId("1");
      monInfor.setAirwayReservationId("1");
      monInfor.setMonitoringInformation("");
      monInfors.add(monInfor);

      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitorings =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner thirdPartyEntryMonitoring =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo thirdPartyInfo =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo();
      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner> featuresInner =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner featuresInnerInner =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry geometry =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties properties =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties();
      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner> traffics =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner traffic =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner();
      traffic.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner.TypeEnum.u4);
      traffic.setCurrentTime("2025-01-27T03:00:00Z");
      traffic.setCount(1);
      traffics.add(traffic);
      geometry.setCoordinates(Arrays.asList(Arrays.asList(139.0, 35.0)));
      geometry.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry.TypeEnum.POLYGON);
      properties.setArea("areaId");
      properties.setTimestamp("2025-01-27T03:00:00Z");
      properties.setTraffics(traffics);
      featuresInnerInner.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner.TypeEnum.FEATURE);
      featuresInnerInner.setGeometry(geometry);
      featuresInnerInner.setProperties(properties);
      featuresInner.add(featuresInnerInner);
      thirdPartyInfo.setFeatures(featuresInner);
      thirdPartyInfo.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo.TypeEnum.FEATURE_COLLECTION);
      thirdPartyEntryMonitoring.setThirdPartyInfo(thirdPartyInfo);
      thirdPartyEntryMonitoring.setAirwayAdministratorId("1");
      thirdPartyEntryMonitoring.setOperatorId("1");
      thirdPartyEntryMonitoring.setAirwayId("1");
      thirdPartyEntryMonitoring.setAirwaySectionId(null);
      thirdPartyEntryMonitorings.add(thirdPartyEntryMonitoring);

      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> results =
          logic.getThirdPartyEntryMonitoring(monInfors);

      assertNotNull(results);
      assertEquals(thirdPartyEntryMonitorings, results);

    }
  }

  @Test
  @DisplayName("航路逸脱検知情報取得Logic")
  void testGetRouteDeviationDetectionInfo1() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {

      CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
      mockedStatic.when(() -> ModelMapperUtil.jsonStrToClass(any(), any()))
          .thenReturn(coordinatesEntity);

      List<AirwayDeviationDetailDto> airwayDeviations = new ArrayList<>();
      AirwayDeviationDetailDto airwayDeviation = new AirwayDeviationDetailDto();
      String coodStr = "1";
      airwayDeviation.setAirwaySectionId("1");
      airwayDeviation.setRouteDeviationCoordinates(coodStr);
      airwayDeviations.add(airwayDeviation);

      CoordinateEntity coordinateEntity = new CoordinateEntity();
      CoordinateDitailEntity coordinateDitail = new CoordinateDitailEntity();
      List<CoordinateEntity> coordinates = new ArrayList<>();
      coordinateDitail.setLatitude("35.0");
      coordinateDitail.setLongitude("139.0");
      coordinateDitail.setAboveGroundLevel("0.0");
      coordinateEntity.setCoordinates(coordinateDitail);
      coordinateEntity.setAirwaySectionId("0");
      coordinates.add(coordinateEntity);
      coordinatesEntity.setCoordinates(coordinates);

      NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner routeDeviationDetectionInfo =
          new NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner();
      List<NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> routeDeviationDetectionInfos =
          new ArrayList<NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner>();
      NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates coord =
          new NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates();
      coord.setLatitude(35.0);
      coord.setLongitude(139.0);
      coord.setAboveGroundLevel(new BigDecimal("0.0"));
      routeDeviationDetectionInfo.setAirwaySectionId("0");
      routeDeviationDetectionInfo.setCoodinates(coord);
      routeDeviationDetectionInfos.add(routeDeviationDetectionInfo);

      List<NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> results =
          logic.getRouteDeviationDetectionInfo(airwayDeviations);

      assertNotNull(results);
      assertEquals(routeDeviationDetectionInfos, results);
    }
  }
}
