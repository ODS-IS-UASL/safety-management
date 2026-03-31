package com.intent_exchange.uasl.logic;

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
import com.intent_exchange.uasl.dto.geojson.SubscriptionsGeoJsonFeatures;
import com.intent_exchange.uasl.dto.geojson.SubscriptionsGeoJsonGeometry;
import com.intent_exchange.uasl.dto.request.NearMissInformationRequestAttributesDto;
import com.intent_exchange.uasl.dto.request.NearMissInformationRequestSelectDto;
import com.intent_exchange.uasl.dto.response.UaslDeviationDetailDto;
import com.intent_exchange.uasl.dto.response.MonitoringInfoDetailDto;
import com.intent_exchange.uasl.dto.response.ThirdPartyMonitoringInformationIntrusionsDto;
import com.intent_exchange.uasl.entity.CoordinateDitailEntity;
import com.intent_exchange.uasl.entity.CoordinateEntity;
import com.intent_exchange.uasl.entity.CoordinatesEntity;
import com.intent_exchange.uasl.entity.NearMissInformationRequestAttributesAreaInfo;
import com.intent_exchange.uasl.entity.NearMissInformationResponse;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributes;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesRouteDeviationInfoInner;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesRouteDeviationInfoInnerUasId;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner;
import com.intent_exchange.uasl.entity.ThirdPartyTimeEntity;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class NearMissInformationLogicTest {

  /** 航路逸脱情報取得のロジック */
  @Mock
  private UaslDeviationSelectLogic uaslDeviationLogic;

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

      List<UaslDeviationDetailDto> nermissSelectList = new ArrayList<>();
      doReturn(nermissSelectList).when(uaslDeviationLogic).nermissSelect(any());

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

      doReturn(null).when(uaslDeviationLogic).nermissSelect(any());

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


      UaslDeviationDetailDto uaslDeviation = new UaslDeviationDetailDto();
      List<UaslDeviationDetailDto> uaslDeviations = new ArrayList<>();
      uaslDeviation.setUaslId("1");
      uaslDeviation.setUaslAdministratorId("1");
      uaslDeviation.setOperatorId("1");
      uaslDeviation.setRouteDeviationRate(1.0);
      uaslDeviation.setRouteDeviationAmount("1");
      uaslDeviation.setRouteDeviationTime(time);
      uaslDeviation.setRouteDeviationCoordinates("1");
      uaslDeviation.setUaslSectionId("1");
      uaslDeviation.setUaslReservationId("1");
      uaslDeviation.setSerialNumber("1");
      uaslDeviation.setRegistrationId("1");
      uaslDeviation.setUtmId("1");
      uaslDeviation.setSpecificSessionId("1");
      uaslDeviations.add(uaslDeviation);

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
      NearMissInformationResponseAttributesRouteDeviationInfoInnerUasId uasId =
          new NearMissInformationResponseAttributesRouteDeviationInfoInnerUasId();
      uasId.setSerialNumber("1");
      uasId.setRegistrationId("1");
      uasId.setUtmId("1");
      uasId.setSpecificSessionId("1");
      nearMissInformation.setRouteDeviationAmount("1");
      nearMissInformation.setRouteDeviationRate(1.0);
      routeDeviationDetectionInfo.add(routeDeviationDetectionInfoInner);
      routeDeviationInfo.setUaslId("1");
      routeDeviationInfo.setUaslAdministratorId("1");
      routeDeviationInfo.setOperatorId("1");
      routeDeviationInfo.setNearMissInformation(nearMissInformation);
      routeDeviationInfo.setRouteDeviationDetectionInfo(routeDeviationDetectionInfo);
      routeDeviationInfo.setTime("2025-01-27T03:00:00Z");
      routeDeviationInfo.setUasId(uasId);
      routeDeviationInfos.add(routeDeviationInfo);

      doReturn(routeDeviationDetectionInfo).when(logic).getRouteDeviationDetectionInfo(any());

      List<NearMissInformationResponseAttributesRouteDeviationInfoInner> results =
          logic.getRouteDeviationInfos(uaslDeviations);

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
      monInfor.setUaslAdministratorId("1");
      monInfor.setOperatorId("1");
      monInfor.setUaslId("1");
      monInfor.setUaslReservationId("1");
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
      thirdPartyEntryMonitoring.setUaslAdministratorId("1");
      thirdPartyEntryMonitoring.setOperatorId("1");
      thirdPartyEntryMonitoring.setUaslId("1");
      thirdPartyEntryMonitoring.setUaslSectionId(null);
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
      monInfor.setUaslAdministratorId("1");
      monInfor.setOperatorId("1");
      monInfor.setUaslId("1");
      monInfor.setUaslReservationId("1");
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
      thirdPartyEntryMonitoring.setUaslAdministratorId("1");
      thirdPartyEntryMonitoring.setOperatorId("1");
      thirdPartyEntryMonitoring.setUaslId("1");
      thirdPartyEntryMonitoring.setUaslSectionId(null);
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
      monInfor.setUaslAdministratorId("1");
      monInfor.setOperatorId("1");
      monInfor.setUaslId("1");
      monInfor.setUaslReservationId("1");
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
      thirdPartyEntryMonitoring.setUaslAdministratorId("1");
      thirdPartyEntryMonitoring.setOperatorId("1");
      thirdPartyEntryMonitoring.setUaslId("1");
      thirdPartyEntryMonitoring.setUaslSectionId(null);
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
      monInfor.setUaslAdministratorId("1");
      monInfor.setOperatorId("1");
      monInfor.setUaslId("1");
      monInfor.setUaslReservationId("1");
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
      thirdPartyEntryMonitoring.setUaslAdministratorId("1");
      thirdPartyEntryMonitoring.setOperatorId("1");
      thirdPartyEntryMonitoring.setUaslId("1");
      thirdPartyEntryMonitoring.setUaslSectionId(null);
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

      List<UaslDeviationDetailDto> uaslDeviations = new ArrayList<>();
      UaslDeviationDetailDto uaslDeviation = new UaslDeviationDetailDto();
      String coodStr = "1";
      uaslDeviation.setUaslSectionId("1");
      uaslDeviation.setRouteDeviationCoordinates(coodStr);
      uaslDeviations.add(uaslDeviation);

      CoordinateEntity coordinateEntity = new CoordinateEntity();
      CoordinateDitailEntity coordinateDitail = new CoordinateDitailEntity();
      List<CoordinateEntity> coordinates = new ArrayList<>();
      coordinateDitail.setLatitude("35.0");
      coordinateDitail.setLongitude("139.0");
      coordinateDitail.setAltitude("10.0");
      coordinateEntity.setCoordinates(coordinateDitail);
      coordinateEntity.setUaslSectionId("0");
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
      coord.setAltitude(10.0);
      routeDeviationDetectionInfo.setUaslSectionId("0");
      routeDeviationDetectionInfo.setCoodinates(coord);
      routeDeviationDetectionInfos.add(routeDeviationDetectionInfo);

      List<NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> results =
          logic.getRouteDeviationDetectionInfo(uaslDeviations);

      assertNotNull(results);
      assertEquals(routeDeviationDetectionInfos, results);
    }
  }
}
