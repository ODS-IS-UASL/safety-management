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

package com.intent_exchange.drone_highway.entity;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * CurrentLocationEntity
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-05T09:08:25.925635900Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class CurrentLocationEntity {

  private String timestamp;

  private BigDecimal latitude;

  private BigDecimal longitude;

  private Integer aboveGroundLevel;

  private String airwayId;

  private String airwaySectionId;

  private String operationalStatus;

  private String operatorId;

  private String uasId;

  private String uaType;

  private Integer trackDirection;

  private BigDecimal speed;

  private BigDecimal verticalSpeed;

  public CurrentLocationEntity timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * テレメトリ情報取得日時 ( RFC3339 形式 (yyyy-MM-ddTHH:mm:ss.sssZ) )
   * @return timestamp
   */
  
  @Schema(name = "timestamp", description = "テレメトリ情報取得日時 ( RFC3339 形式 (yyyy-MM-ddTHH:mm:ss.sssZ) )", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public CurrentLocationEntity latitude(BigDecimal latitude) {
    this.latitude = latitude;
    return this;
  }

  /**
   * ドローンの緯度 (degree)
   * @return latitude
   */
  @Valid 
  @Schema(name = "latitude", description = "ドローンの緯度 (degree)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("latitude")
  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public CurrentLocationEntity longitude(BigDecimal longitude) {
    this.longitude = longitude;
    return this;
  }

  /**
   * ドローンの経度 (degree)
   * @return longitude
   */
  @Valid 
  @Schema(name = "longitude", description = "ドローンの経度 (degree)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("longitude")
  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  public CurrentLocationEntity aboveGroundLevel(Integer aboveGroundLevel) {
    this.aboveGroundLevel = aboveGroundLevel;
    return this;
  }

  /**
   * ドローンの対地高度（m）   
   * 不正値の場合あり。その場合は、-1000 を設定する予定。 
   * @return aboveGroundLevel
   */
  
  @Schema(name = "aboveGroundLevel", description = "ドローンの対地高度（m）   不正値の場合あり。その場合は、-1000 を設定する予定。 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aboveGroundLevel")
  public Integer getAboveGroundLevel() {
    return aboveGroundLevel;
  }

  public void setAboveGroundLevel(Integer aboveGroundLevel) {
    this.aboveGroundLevel = aboveGroundLevel;
  }

  public CurrentLocationEntity airwayId(String airwayId) {
    this.airwayId = airwayId;
    return this;
  }

  /**
   * 航路 ID
   * @return airwayId
   */
  
  @Schema(name = "airwayId", description = "航路 ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayId")
  public String getAirwayId() {
    return airwayId;
  }

  public void setAirwayId(String airwayId) {
    this.airwayId = airwayId;
  }

  public CurrentLocationEntity airwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
    return this;
  }

  /**
   * 航路区画 ID
   * @return airwaySectionId
   */
  
  @Schema(name = "airwaySectionId", description = "航路区画 ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwaySectionId")
  public String getAirwaySectionId() {
    return airwaySectionId;
  }

  public void setAirwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
  }

  public CurrentLocationEntity operationalStatus(String operationalStatus) {
    this.operationalStatus = operationalStatus;
    return this;
  }

  /**
   * ドローンの運航状況。「RouteApproach」「NormalOperation」「RouteDeviation」のいずれか。   
   * RouteApproach・・・航路進入前。まだ飛んでいない場合、航路に一度も入っていない場合。   
   * NormalOperation・・・運航中。一度航路に入った後、逸脱判定によって逸脱していない場合。   
   * RouteDeviation・・・航路逸脱。逸脱している場合、逸脱してポートに向かっている、また、ポートに着陸した場合。   
   * @return operationalStatus
   */
  
  @Schema(name = "operationalStatus", description = "ドローンの運航状況。「RouteApproach」「NormalOperation」「RouteDeviation」のいずれか。   RouteApproach・・・航路進入前。まだ飛んでいない場合、航路に一度も入っていない場合。   NormalOperation・・・運航中。一度航路に入った後、逸脱判定によって逸脱していない場合。   RouteDeviation・・・航路逸脱。逸脱している場合、逸脱してポートに向かっている、また、ポートに着陸した場合。   ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operationalStatus")
  public String getOperationalStatus() {
    return operationalStatus;
  }

  public void setOperationalStatus(String operationalStatus) {
    this.operationalStatus = operationalStatus;
  }

  public CurrentLocationEntity operatorId(String operatorId) {
    this.operatorId = operatorId;
    return this;
  }

  /**
   * 運航者 ID
   * @return operatorId
   */
  
  @Schema(name = "operatorId", description = "運航者 ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operatorId")
  public String getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }

  public CurrentLocationEntity uasId(String uasId) {
    this.uasId = uasId;
    return this;
  }

  /**
   * 機体の登録 ID   
   * GCS の場合はなし   
   * @return uasId
   */
  
  @Schema(name = "uasId", description = "機体の登録 ID   GCS の場合はなし   ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uasId")
  public String getUasId() {
    return uasId;
  }

  public void setUasId(String uasId) {
    this.uasId = uasId;
  }

  public CurrentLocationEntity uaType(String uaType) {
    this.uaType = uaType;
    return this;
  }

  /**
   * ドローンの種別   
   * 任意の値のため、UTM・GCS 側で設定されない場合はなし 
   * @return uaType
   */
  
  @Schema(name = "uaType", description = "ドローンの種別   任意の値のため、UTM・GCS 側で設定されない場合はなし ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaType")
  public String getUaType() {
    return uaType;
  }

  public void setUaType(String uaType) {
    this.uaType = uaType;
  }

  public CurrentLocationEntity trackDirection(Integer trackDirection) {
    this.trackDirection = trackDirection;
    return this;
  }

  /**
   * ドローンの進行方向 (degree)   
   * GCS の場合はなし 
   * @return trackDirection
   */
  
  @Schema(name = "trackDirection", description = "ドローンの進行方向 (degree)   GCS の場合はなし ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trackDirection")
  public Integer getTrackDirection() {
    return trackDirection;
  }

  public void setTrackDirection(Integer trackDirection) {
    this.trackDirection = trackDirection;
  }

  public CurrentLocationEntity speed(BigDecimal speed) {
    this.speed = speed;
    return this;
  }

  /**
   * ドローンの速度 (m/s)   
   * 任意の値のため、UTM・GCS 側で設定されない場合はなし   
   * @return speed
   */
  @Valid 
  @Schema(name = "speed", description = "ドローンの速度 (m/s)   任意の値のため、UTM・GCS 側で設定されない場合はなし   ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speed")
  public BigDecimal getSpeed() {
    return speed;
  }

  public void setSpeed(BigDecimal speed) {
    this.speed = speed;
  }

  public CurrentLocationEntity verticalSpeed(BigDecimal verticalSpeed) {
    this.verticalSpeed = verticalSpeed;
    return this;
  }

  /**
   * ドローンの垂直速度 (m/s)   
   * 任意の値のため、UTM・GCS 側で設定されない場合はなし   
   * @return verticalSpeed
   */
  @Valid 
  @Schema(name = "verticalSpeed", description = "ドローンの垂直速度 (m/s)   任意の値のため、UTM・GCS 側で設定されない場合はなし   ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("verticalSpeed")
  public BigDecimal getVerticalSpeed() {
    return verticalSpeed;
  }

  public void setVerticalSpeed(BigDecimal verticalSpeed) {
    this.verticalSpeed = verticalSpeed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CurrentLocationEntity currentLocationEntity = (CurrentLocationEntity) o;
    return Objects.equals(this.timestamp, currentLocationEntity.timestamp) &&
        Objects.equals(this.latitude, currentLocationEntity.latitude) &&
        Objects.equals(this.longitude, currentLocationEntity.longitude) &&
        Objects.equals(this.aboveGroundLevel, currentLocationEntity.aboveGroundLevel) &&
        Objects.equals(this.airwayId, currentLocationEntity.airwayId) &&
        Objects.equals(this.airwaySectionId, currentLocationEntity.airwaySectionId) &&
        Objects.equals(this.operationalStatus, currentLocationEntity.operationalStatus) &&
        Objects.equals(this.operatorId, currentLocationEntity.operatorId) &&
        Objects.equals(this.uasId, currentLocationEntity.uasId) &&
        Objects.equals(this.uaType, currentLocationEntity.uaType) &&
        Objects.equals(this.trackDirection, currentLocationEntity.trackDirection) &&
        Objects.equals(this.speed, currentLocationEntity.speed) &&
        Objects.equals(this.verticalSpeed, currentLocationEntity.verticalSpeed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, latitude, longitude, aboveGroundLevel, airwayId, airwaySectionId, operationalStatus, operatorId, uasId, uaType, trackDirection, speed, verticalSpeed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CurrentLocationEntity {\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    aboveGroundLevel: ").append(toIndentedString(aboveGroundLevel)).append("\n");
    sb.append("    airwayId: ").append(toIndentedString(airwayId)).append("\n");
    sb.append("    airwaySectionId: ").append(toIndentedString(airwaySectionId)).append("\n");
    sb.append("    operationalStatus: ").append(toIndentedString(operationalStatus)).append("\n");
    sb.append("    operatorId: ").append(toIndentedString(operatorId)).append("\n");
    sb.append("    uasId: ").append(toIndentedString(uasId)).append("\n");
    sb.append("    uaType: ").append(toIndentedString(uaType)).append("\n");
    sb.append("    trackDirection: ").append(toIndentedString(trackDirection)).append("\n");
    sb.append("    speed: ").append(toIndentedString(speed)).append("\n");
    sb.append("    verticalSpeed: ").append(toIndentedString(verticalSpeed)).append("\n");
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


