/*
 * Copyright 2025 Intent Exchange, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.intent_exchange.drone_highway.logic;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dto.geojson.SubscriptionsGeoJsonFeatures;
import com.intent_exchange.drone_highway.dto.request.NearMissInformationRequestAttributesDto;
import com.intent_exchange.drone_highway.dto.request.NearMissInformationRequestSelectDto;
import com.intent_exchange.drone_highway.dto.response.AirwayDeviationDetailDto;
import com.intent_exchange.drone_highway.dto.response.MonitoringInfoDetailDto;
import com.intent_exchange.drone_highway.dto.response.ThirdPartyMonitoringInformationIntrusionsDto;
import com.intent_exchange.drone_highway.entity.CoordinateEntity;
import com.intent_exchange.drone_highway.entity.CoordinatesEntity;
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

/**
 * 安全管理支援(A-1-4)のDBに関連する処理をします。
 */
@Component
public class NearMissInformationLogic {

  /** 航路逸脱情報取得のロジック */
  @Autowired
  private AirwayDeviationSelectLogic airwayDeviationLogic;

  /** 第三者立入監視情報取得のロジック */
  @Autowired
  private MonitoringInfoSelectLogic monitoringInfoLogic;

  // Clockインスタンス。現在の時刻を取得するために使用します。
  @Autowired
  private Clock clock;

  /**
   * ヒヤリハット情報取得APIのレスポンスを生成します。
   * 
   * @param dto ヒヤリハット情報取得APIのリクエストデータモデル
   * @return ヒヤリハット情報取得APIのレスポンスデータモデル
   */
  public NearMissInformationResponse nearMissInformation(
      NearMissInformationRequestAttributesDto dto) {

    NearMissInformationRequestSelectDto selectDto =
        ModelMapperUtil.map(dto, NearMissInformationRequestSelectDto.class);
    List<List<List<Double>>> coordinates = new ArrayList<>();
    coordinates.add(dto.getAreaInfo().getCoordinates());
    selectDto.setCoordinates(coordinates);
    // データベースリード(select)
    List<AirwayDeviationDetailDto> airwayDeviations = airwayDeviationLogic.nermissSelect(selectDto); // ヒヤリハット情報
    if (airwayDeviations == null) {
      airwayDeviations = new ArrayList<AirwayDeviationDetailDto>();
    }
    List<MonitoringInfoDetailDto> monInfors = monitoringInfoLogic.moniterSelect(selectDto); // 第三者立ち入り情報
    if (monInfors == null) {
      monInfors = new ArrayList<MonitoringInfoDetailDto>();
    }

    // ヒヤリハットレスポンス生成
    NearMissInformationResponse nearMisInfRes = new NearMissInformationResponse();
    NearMissInformationResponseAttributes nearMisInfResAttr =
        new NearMissInformationResponseAttributes();

    List<NearMissInformationResponseAttributesRouteDeviationInfoInner> routeDeviationInfos =
        getRouteDeviationInfos(airwayDeviations);
    List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitorings =
        getThirdPartyEntryMonitoring(monInfors);

    // レスポンスに登録
    nearMisInfResAttr.routeDeviationInfo(routeDeviationInfos);
    nearMisInfResAttr.thirdPartyEntryMonitoring(thirdPartyEntryMonitorings);
    // nearMisInfResAttr.routeDeviationDetectionInfo(routeDeviationDetectionInfos);
    nearMisInfRes.setDataModelType(NearMissInformationResponse.DataModelTypeEnum.TEST1);
    nearMisInfRes.setAttributes(nearMisInfResAttr);

    return nearMisInfRes;
  }

  /***
   * 航路逸脱情報を取得します。
   * 
   * @param airwayDeviations 航路逸脱情報
   * @return レスポンス用航路逸脱情報
   */
  public List<NearMissInformationResponseAttributesRouteDeviationInfoInner> getRouteDeviationInfos(
      List<AirwayDeviationDetailDto> airwayDeviations) {
    List<NearMissInformationResponseAttributesRouteDeviationInfoInner> routeDeviationInfos =
        new ArrayList<>();

    // 航路逸脱情報
    for (AirwayDeviationDetailDto airwayDeviation : airwayDeviations) {
      NearMissInformationResponseAttributesRouteDeviationInfoInner routeDeviationInfo =
          new NearMissInformationResponseAttributesRouteDeviationInfoInner();

      ThirdPartyTimeEntity times = new ThirdPartyTimeEntity();
      String timesData = airwayDeviation.getRouteDeviationTime();
      times = (ThirdPartyTimeEntity) ModelMapperUtil.jsonStrToClass(timesData,
          ThirdPartyTimeEntity.class);
      String time = LocalDateTime
          .parse(times.getTime().get(0), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"))
          .atZone(clock.getZone())
          .format(DateTimeFormatter.ISO_INSTANT);

      NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation nearMissInformation =
          new NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation(
              airwayDeviation.getRouteDeviationRate(), airwayDeviation.getRouteDeviationAmount());


      List<NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> routeDeviationDetectionInfos =
          getRouteDeviationDetectionInfo(airwayDeviations);

      // 航路逸脱情報設定
      routeDeviationInfo.setAirwayId(airwayDeviation.getAirwayId());
      routeDeviationInfo.setAirwayAdministratorId(airwayDeviation.getAirwayAdministratorId());
      routeDeviationInfo.setNearMissInformation(nearMissInformation);
      routeDeviationInfo.setOperatorId(airwayDeviation.getOperatorId());
      routeDeviationInfo.setTime(time);
      routeDeviationInfo.setRouteDeviationDetectionInfo(routeDeviationDetectionInfos);

      // リストの登録
      routeDeviationInfos.add(routeDeviationInfo);
    }
    return routeDeviationInfos;
  }

  /**
   * 第三者立入監視情報を取得します。
   * 
   * @param monInfors 第三者立入監視情報
   * @return レスポンス用第三者立入監視情報
   */
  @SuppressWarnings("unchecked")
  public List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> getThirdPartyEntryMonitoring(
      List<MonitoringInfoDetailDto> monInfors) {
    List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitorings =
        new ArrayList<>();
    NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo thirdPartyInfo =
        new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo();

    // 第三者立ち入り情報の取り出し
    for (MonitoringInfoDetailDto monInfor : monInfors) {
      List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner> featureInners =
          new ArrayList<>();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner thirdPartyEntryMonitoring =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner();
      NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner featureInner =
          new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner();

      List<SubscriptionsGeoJsonFeatures> features = ModelMapperUtil.jsonStrToListClass(
          monInfor.getMonitoringInformation(), SubscriptionsGeoJsonFeatures.class);

      for (SubscriptionsGeoJsonFeatures thirdPartyFeature : features) {
        NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner feature =
            new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner();
        feature.setType(
            NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner.TypeEnum.FEATURE);

        // 第三者立入監視情報取得APIで取得されるgeometryは、3重配列(geojson形式)だが、
        // レスポンスが2重配列のため、ここで変換して格納する
        NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry gmap =
            new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry();
        gmap.setType(
            NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry.TypeEnum.POLYGON);
        gmap.setCoordinates(thirdPartyFeature.getGeometry().getCoordinates().get(0));
        // ModelMapperUtil.map(thirdPartyFeature.getGeometry(),
        // NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry.class);
        feature.setGeometry(gmap);

        // propertiesの取り出し

        NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties pmap =
            new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties();
        pmap.setArea(thirdPartyFeature.getProperties().get("areaID").toString());
        pmap.setTimestamp(thirdPartyFeature.getProperties().get("timestamp").toString());
        List<Map<String, Object>> intrusions =
            (List<Map<String, Object>>) thirdPartyFeature.getProperties().get("intrusions");
        List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner> traffics =
            new ArrayList<>();
        List<ThirdPartyMonitoringInformationIntrusionsDto> intrusionslist =
            ModelMapperUtil.convertListMapToListDto(intrusions,
                ThirdPartyMonitoringInformationIntrusionsDto.class);
        for (ThirdPartyMonitoringInformationIntrusionsDto intrusion : intrusionslist) {

          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner traffic =
              new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner();
          traffic.setCurrentTime(intrusion.getCurrentTime());
          int type = intrusion.getType();
          // 第三者立入監視情報として保存されている侵入対象はintのため、対応するEnumに変換し格納する
          switch (type) {
            case 1:
              // 車
              traffic.setType(
                  NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner.TypeEnum.u);
              break;
            case 2:
              // 自転車
              traffic.setType(
                  NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner.TypeEnum.u2);
              break;
            case 3:
              // 人
              traffic.setType(
                  NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner.TypeEnum.u3);
              break;
            case 4:
              // バイク
              traffic.setType(
                  NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerPropertiesTrafficsInner.TypeEnum.u4);
              break;
            default:
              break;
          }
          traffic.setCount(intrusion.getCount());
          traffics.add(traffic);
        }

        pmap.setTraffics(traffics);
        feature.setProperties(pmap);

        featureInners.add(feature);
        thirdPartyInfo.setFeatures(featureInners);
      }
      thirdPartyInfo.setType(
          NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo.TypeEnum.FEATURE_COLLECTION);

      // 第三者立ち入り情報の登録
      thirdPartyEntryMonitoring.setThirdPartyInfo(thirdPartyInfo);
      thirdPartyEntryMonitoring.setAirwayAdministratorId(monInfor.getAirwayAdministratorId());
      thirdPartyEntryMonitoring.setAirwayId(monInfor.getAirwayId());
      thirdPartyEntryMonitoring.setAirwaySectionId(null); // 現時点では第三者立入情報に航路区画IDは保存されていないためNULLとする
      thirdPartyEntryMonitoring.setOperatorId(monInfor.getOperatorId());

      // リストの登録
      thirdPartyEntryMonitorings.add(thirdPartyEntryMonitoring);
    }
    return thirdPartyEntryMonitorings;
  }

  /**
   * 
   * 航路逸脱情報の航路逸脱検知情報を取得します。
   * 
   * @param airwayDeviations 航路逸脱情報
   * @return レスポンス用航路逸脱検知情報
   */
  public List<NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> getRouteDeviationDetectionInfo(
      List<AirwayDeviationDetailDto> airwayDeviations) {
    List<NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> routeDeviationDetectionInfos =
        new ArrayList<>();

    for (AirwayDeviationDetailDto airwayDeviation : airwayDeviations) {
      CoordinatesEntity coodinates = new CoordinatesEntity();
      String coodStr = airwayDeviation.getRouteDeviationCoordinates();

      coodinates =
          (CoordinatesEntity) ModelMapperUtil.jsonStrToClass(coodStr, CoordinatesEntity.class);

      for (CoordinateEntity coordinateEntity : coodinates.getCoordinates()) {
        NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner routeDeviationDetectionInfo =
            new NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner();
        NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates coord =
            new NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates();

        coord.setLatitude(Double.valueOf(coordinateEntity.getCoordinates().getLatitude()));
        coord.setLongitude(Double.valueOf(coordinateEntity.getCoordinates().getLongitude()));
        coord.setAboveGroundLevel(
            new BigDecimal(coordinateEntity.getCoordinates().getAboveGroundLevel()));

        // 航路逸脱情報を登録
        routeDeviationDetectionInfo.setAirwaySectionId(coordinateEntity.getAirwaySectionId());
        routeDeviationDetectionInfo.setCoodinates(coord);

        // リストに登録
        routeDeviationDetectionInfos.add(routeDeviationDetectionInfo);
      }
    }
    return routeDeviationDetectionInfos;
  }
}

