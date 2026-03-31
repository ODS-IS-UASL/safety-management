/*
 * Copyright 2026 Intent Exchange, Inc.
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

package com.intent_exchange.uasl.entity;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.intent_exchange.uasl.entity.FlightLogResponseUasId;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * フライトログ（1件分のデータ）
 */

@Schema(name = "FlightLogResponse", description = "フライトログ（1件分のデータ）")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-28T13:43:13.092416900+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class FlightLogResponse {

  private String subscriptionId = null;

  private Integer aircraftInfoId = null;

  private String uaType = null;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private String getLocationTimestamp = null;

  private Double latitude = null;

  private Double longitude = null;

  private Integer altitude = null;

  private Integer trackDirection = null;

  private Double speed = null;

  private Double verticalSpeed = null;

  private Double routeDeviationRate = null;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private String routeDeviationRateUpdateTime = null;

  private String reservationId = null;

  private String uaslId = null;

  private String uaslSectionId = null;

  /**
   * 運航状況 - RouteApproach: 航路進入中 - NormalOperation: 正常運航中 - RouteDeviation: 航路逸脱中 - PlannedRouteDeviation: 計画された航路逸脱 
   */
  public enum OperationalStatusEnum {
    ROUTE_APPROACH("RouteApproach"),
    
    NORMAL_OPERATION("NormalOperation"),
    
    ROUTE_DEVIATION("RouteDeviation"),
    
    PLANNED_ROUTE_DEVIATION("PlannedRouteDeviation");

    private String value;

    OperationalStatusEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static OperationalStatusEnum fromValue(String value) {
      for (OperationalStatusEnum b : OperationalStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      return null;
    }
  }

  private OperationalStatusEnum operationalStatus = null;

  private String operatorId = null;

  private String flightTime = null;

  private FlightLogResponseUasId uasId;

  public FlightLogResponse subscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

  /**
   * サブスクリプションID
   * @return subscriptionId
   */
  @Size(max = 255) 
  @Schema(name = "subscriptionId", example = "SUB001", description = "サブスクリプションID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subscriptionId")
  public String getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public FlightLogResponse aircraftInfoId(Integer aircraftInfoId) {
    this.aircraftInfoId = aircraftInfoId;
    return this;
  }

  /**
   * 機体情報ID
   * @return aircraftInfoId
   */
  
  @Schema(name = "aircraftInfoId", example = "10", description = "機体情報ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aircraftInfoId")
  public Integer getAircraftInfoId() {
    return aircraftInfoId;
  }

  public void setAircraftInfoId(Integer aircraftInfoId) {
    this.aircraftInfoId = aircraftInfoId;
  }

  public FlightLogResponse uaType(String uaType) {
    this.uaType = uaType;
    return this;
  }

  /**
   * 機体種別
   * @return uaType
   */
  @Size(max = 255) 
  @Schema(name = "uaType", example = "multirotor", description = "機体種別", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaType")
  public String getUaType() {
    return uaType;
  }

  public void setUaType(String uaType) {
    this.uaType = uaType;
  }

  public FlightLogResponse getLocationTimestamp(String getLocationTimestamp) {
    this.getLocationTimestamp = getLocationTimestamp;
    return this;
  }

  /**
   * テレメトリ情報取得日時（ISO 8601形式）
   * @return getLocationTimestamp
   */
  @Valid 
  @Schema(name = "getLocationTimestamp", example = "2025-11-27T10:30:45Z", description = "テレメトリ情報取得日時（ISO 8601形式）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("getLocationTimestamp")
  public String getGetLocationTimestamp() {
    return getLocationTimestamp;
  }

  public void setGetLocationTimestamp(String getLocationTimestamp) {
    this.getLocationTimestamp = getLocationTimestamp;
  }

  public FlightLogResponse latitude(Double latitude) {
    this.latitude = latitude;
    return this;
  }

  /**
   * 緯度、小数点 7 桁まで。
   * minimum: -90
   * maximum: 90
   * @return latitude
   */
  @DecimalMin("-90") @DecimalMax("90") 
  @Schema(name = "latitude", example = "35.6812", description = "緯度、小数点 7 桁まで。", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latitude")
  public Double getLatitude() {
    return latitude;
  }

  public void setLatitude(Double latitude) {
    this.latitude = latitude;
  }

  public FlightLogResponse longitude(Double longitude) {
    this.longitude = longitude;
    return this;
  }

  /**
   * 経度、小数点 7 桁まで。
   * minimum: -180
   * maximum: 180
   * @return longitude
   */
  @DecimalMin("-180") @DecimalMax("180") 
  @Schema(name = "longitude", example = "139.7671", description = "経度、小数点 7 桁まで。", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("longitude")
  public Double getLongitude() {
    return longitude;
  }

  public void setLongitude(Double longitude) {
    this.longitude = longitude;
  }

  public FlightLogResponse altitude(Integer altitude) {
    this.altitude = altitude;
    return this;
  }

  /**
   * 標高（メートル）
   * @return altitude
   */
  
  @Schema(name = "altitude", example = "50", description = "標高（メートル）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("altitude")
  public Integer getAltitude() {
    return altitude;
  }

  public void setAltitude(Integer altitude) {
    this.altitude = altitude;
  }

  public FlightLogResponse trackDirection(Integer trackDirection) {
    this.trackDirection = trackDirection;
    return this;
  }

  /**
   * 機体の進行方向（度、0-359）
   * minimum: 0
   * maximum: 359
   * @return trackDirection
   */
  @Min(0) @Max(359) 
  @Schema(name = "trackDirection", example = "90", description = "機体の進行方向（度、0-359）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trackDirection")
  public Integer getTrackDirection() {
    return trackDirection;
  }

  public void setTrackDirection(Integer trackDirection) {
    this.trackDirection = trackDirection;
  }

  public FlightLogResponse speed(Double speed) {
    this.speed = speed;
    return this;
  }

  /**
   * 機体の速度（m/s）
   * @return speed
   */
  
  @Schema(name = "speed", example = "10.5", description = "機体の速度（m/s）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speed")
  public Double getSpeed() {
    return speed;
  }

  public void setSpeed(Double speed) {
    this.speed = speed;
  }

  public FlightLogResponse verticalSpeed(Double verticalSpeed) {
    this.verticalSpeed = verticalSpeed;
    return this;
  }

  /**
   * 機体の垂直速度（m/s、正の値は上昇）
   * @return verticalSpeed
   */
  
  @Schema(name = "verticalSpeed", example = "0.5", description = "機体の垂直速度（m/s、正の値は上昇）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("verticalSpeed")
  public Double getVerticalSpeed() {
    return verticalSpeed;
  }

  public void setVerticalSpeed(Double verticalSpeed) {
    this.verticalSpeed = verticalSpeed;
  }

  public FlightLogResponse routeDeviationRate(Double routeDeviationRate) {
    this.routeDeviationRate = routeDeviationRate;
    return this;
  }

  /**
   * 航路逸脱割合 - 0.0: 航路内 - 1.0: 航路逸脱中 
   * @return routeDeviationRate
   */
  
  @Schema(name = "routeDeviationRate", example = "0.0", description = "航路逸脱割合 - 0.0: 航路内 - 1.0: 航路逸脱中 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("routeDeviationRate")
  public Double getRouteDeviationRate() {
    return routeDeviationRate;
  }

  public void setRouteDeviationRate(Double routeDeviationRate) {
    this.routeDeviationRate = routeDeviationRate;
  }

  public FlightLogResponse routeDeviationRateUpdateTime(String routeDeviationRateUpdateTime) {
    this.routeDeviationRateUpdateTime = routeDeviationRateUpdateTime;
    return this;
  }

  /**
   * 航路逸脱割合更新時刻（ISO 8601形式）
   * @return routeDeviationRateUpdateTime
   */
  @Valid 
  @Schema(name = "routeDeviationRateUpdateTime", example = "2025-11-27T10:30:45Z", description = "航路逸脱割合更新時刻（ISO 8601形式）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("routeDeviationRateUpdateTime")
  public String getRouteDeviationRateUpdateTime() {
    return routeDeviationRateUpdateTime;
  }

  public void setRouteDeviationRateUpdateTime(String routeDeviationRateUpdateTime) {
    this.routeDeviationRateUpdateTime = routeDeviationRateUpdateTime;
  }

  public FlightLogResponse reservationId(String reservationId) {
    this.reservationId = reservationId;
    return this;
  }

  /**
   * 航路予約ID
   * @return reservationId
   */
  @Size(max = 255) 
  @Schema(name = "reservationId", example = "RES20251127001", description = "航路予約ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reservationId")
  public String getReservationId() {
    return reservationId;
  }

  public void setReservationId(String reservationId) {
    this.reservationId = reservationId;
  }

  public FlightLogResponse uaslId(String uaslId) {
    this.uaslId = uaslId;
    return this;
  }

  /**
   * 航路ID
   * @return uaslId
   */
  @Size(max = 255) 
  @Schema(name = "uaslId", example = "UASL001", description = "航路ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslId")
  public String getUaslId() {
    return uaslId;
  }

  public void setUaslId(String uaslId) {
    this.uaslId = uaslId;
  }

  public FlightLogResponse uaslSectionId(String uaslSectionId) {
    this.uaslSectionId = uaslSectionId;
    return this;
  }

  /**
   * 航路区画ID
   * @return uaslSectionId
   */
  @Size(max = 255) 
  @Schema(name = "uaslSectionId", example = "SEC001", description = "航路区画ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslSectionId")
  public String getUaslSectionId() {
    return uaslSectionId;
  }

  public void setUaslSectionId(String uaslSectionId) {
    this.uaslSectionId = uaslSectionId;
  }

  public FlightLogResponse operationalStatus(OperationalStatusEnum operationalStatus) {
    this.operationalStatus = operationalStatus;
    return this;
  }

  /**
   * 運航状況 - RouteApproach: 航路進入中 - NormalOperation: 正常運航中 - RouteDeviation: 航路逸脱中 - PlannedRouteDeviation: 計画された航路逸脱 
   * @return operationalStatus
   */
  
  @Schema(name = "operationalStatus", example = "NormalOperation", description = "運航状況 - RouteApproach: 航路進入中 - NormalOperation: 正常運航中 - RouteDeviation: 航路逸脱中 - PlannedRouteDeviation: 計画された航路逸脱 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operationalStatus")
  public OperationalStatusEnum getOperationalStatus() {
    return operationalStatus;
  }

  public void setOperationalStatus(OperationalStatusEnum operationalStatus) {
    this.operationalStatus = operationalStatus;
  }

  public FlightLogResponse operatorId(String operatorId) {
    this.operatorId = operatorId;
    return this;
  }

  /**
   * 運航者ID
   * @return operatorId
   */
  @Size(max = 255) 
  @Schema(name = "operatorId", example = "OP12345", description = "運航者ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operatorId")
  public String getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }

  public FlightLogResponse flightTime(String flightTime) {
    this.flightTime = flightTime;
    return this;
  }

  /**
   * 飛行時間（HH:mm:ss.SSS形式）   UTM(DSS)の制限により現状は24時間未満の範囲となります。 
   * @return flightTime
   */
  
  @Schema(name = "flightTime", example = "00:15:30.000", description = "飛行時間（HH:mm:ss.SSS形式）   UTM(DSS)の制限により現状は24時間未満の範囲となります。 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("flightTime")
  public String getFlightTime() {
    return flightTime;
  }

  public void setFlightTime(String flightTime) {
    this.flightTime = flightTime;
  }

  public FlightLogResponse uasId(FlightLogResponseUasId uasId) {
    this.uasId = uasId;
    return this;
  }

  /**
   * Get uasId
   * @return uasId
   */
  @Valid 
  @Schema(name = "uasId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uasId")
  public FlightLogResponseUasId getUasId() {
    return uasId;
  }

  public void setUasId(FlightLogResponseUasId uasId) {
    this.uasId = uasId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlightLogResponse flightLogResponse = (FlightLogResponse) o;
    return Objects.equals(this.subscriptionId, flightLogResponse.subscriptionId) &&
        Objects.equals(this.aircraftInfoId, flightLogResponse.aircraftInfoId) &&
        Objects.equals(this.uaType, flightLogResponse.uaType) &&
        Objects.equals(this.getLocationTimestamp, flightLogResponse.getLocationTimestamp) &&
        Objects.equals(this.latitude, flightLogResponse.latitude) &&
        Objects.equals(this.longitude, flightLogResponse.longitude) &&
        Objects.equals(this.altitude, flightLogResponse.altitude) &&
        Objects.equals(this.trackDirection, flightLogResponse.trackDirection) &&
        Objects.equals(this.speed, flightLogResponse.speed) &&
        Objects.equals(this.verticalSpeed, flightLogResponse.verticalSpeed) &&
        Objects.equals(this.routeDeviationRate, flightLogResponse.routeDeviationRate) &&
        Objects.equals(this.routeDeviationRateUpdateTime, flightLogResponse.routeDeviationRateUpdateTime) &&
        Objects.equals(this.reservationId, flightLogResponse.reservationId) &&
        Objects.equals(this.uaslId, flightLogResponse.uaslId) &&
        Objects.equals(this.uaslSectionId, flightLogResponse.uaslSectionId) &&
        Objects.equals(this.operationalStatus, flightLogResponse.operationalStatus) &&
        Objects.equals(this.operatorId, flightLogResponse.operatorId) &&
        Objects.equals(this.flightTime, flightLogResponse.flightTime) &&
        Objects.equals(this.uasId, flightLogResponse.uasId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriptionId, aircraftInfoId, uaType, getLocationTimestamp, latitude, longitude, altitude, trackDirection, speed, verticalSpeed, routeDeviationRate, routeDeviationRateUpdateTime, reservationId, uaslId, uaslSectionId, operationalStatus, operatorId, flightTime, uasId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlightLogResponse {\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    aircraftInfoId: ").append(toIndentedString(aircraftInfoId)).append("\n");
    sb.append("    uaType: ").append(toIndentedString(uaType)).append("\n");
    sb.append("    getLocationTimestamp: ").append(toIndentedString(getLocationTimestamp)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    altitude: ").append(toIndentedString(altitude)).append("\n");
    sb.append("    trackDirection: ").append(toIndentedString(trackDirection)).append("\n");
    sb.append("    speed: ").append(toIndentedString(speed)).append("\n");
    sb.append("    verticalSpeed: ").append(toIndentedString(verticalSpeed)).append("\n");
    sb.append("    routeDeviationRate: ").append(toIndentedString(routeDeviationRate)).append("\n");
    sb.append("    routeDeviationRateUpdateTime: ").append(toIndentedString(routeDeviationRateUpdateTime)).append("\n");
    sb.append("    reservationId: ").append(toIndentedString(reservationId)).append("\n");
    sb.append("    uaslId: ").append(toIndentedString(uaslId)).append("\n");
    sb.append("    uaslSectionId: ").append(toIndentedString(uaslSectionId)).append("\n");
    sb.append("    operationalStatus: ").append(toIndentedString(operationalStatus)).append("\n");
    sb.append("    operatorId: ").append(toIndentedString(operatorId)).append("\n");
    sb.append("    flightTime: ").append(toIndentedString(flightTime)).append("\n");
    sb.append("    uasId: ").append(toIndentedString(uasId)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

