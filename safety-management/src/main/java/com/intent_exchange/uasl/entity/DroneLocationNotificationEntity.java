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

package com.intent_exchange.uasl.entity;

import java.math.BigDecimal;
import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 運行中ドローンの位置情報を受信する API 用の entity
 */

@Schema(name = "DroneLocationNotificationEntity", description = "運行中ドローンの位置情報を受信する API 用の entity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-12-13T05:27:23.828338500Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class DroneLocationNotificationEntity {

  private String subscriptionId;

  private String reservationId;

  private DroneLocationNotificationEntityUasId uasId;

  private String uaType;

  private String timestamp;

  private BigDecimal latitude;

  private BigDecimal longitude;

  private Integer altitude;

  private Integer trackDirection;

  private BigDecimal speed;

  private BigDecimal verticalSpeed;

  public DroneLocationNotificationEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DroneLocationNotificationEntity(String timestamp, BigDecimal latitude,
      BigDecimal longitude, Integer altitude) {
    this.timestamp = timestamp;
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
  }

  public DroneLocationNotificationEntity subscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
    return this;
  }

  /**
   * USS が航路の安全管理 (SafetyManagement) に通知するためのエリア情報のサブスクリプション ID。
   * 
   * @return subscriptionId
   */

  @Schema(name = "subscriptionId", description = "USS が航路の安全管理 (SafetyManagement) に通知するためのエリア情報のサブスクリプション ID。",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("subscriptionId")
  public String getSubscriptionId() {
    return subscriptionId;
  }

  public void setSubscriptionId(String subscriptionId) {
    this.subscriptionId = subscriptionId;
  }

  public DroneLocationNotificationEntity reservationId(String reservationId) {
    this.reservationId = reservationId;
    return this;
  }

  /**
   * 航路の予約 ID。
   * 
   * @return reservationId
   */

  @Schema(name = "reservationId", description = "航路の予約 ID。",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reservationId")
  public String getReservationId() {
    return reservationId;
  }

  public void setReservationId(String reservationId) {
    this.reservationId = reservationId;
  }

  public DroneLocationNotificationEntity uasId(DroneLocationNotificationEntityUasId uasId) {
    this.uasId = uasId;
    return this;
  }

  /**
   * Get uasId
   * 
   * @return uasId
   */
  @Valid
  @Schema(name = "uasId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uasId")
  public DroneLocationNotificationEntityUasId getUasId() {
    return uasId;
  }

  public void setUasId(DroneLocationNotificationEntityUasId uasId) {
    this.uasId = uasId;
  }

  public DroneLocationNotificationEntity uaType(String uaType) {
    this.uaType = uaType;
    return this;
  }

  /**
   * ドローンの種別。
   * 
   * @return uaType
   */

  @Schema(name = "uaType", description = "ドローンの種別。",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaType")
  public String getUaType() {
    return uaType;
  }

  public void setUaType(String uaType) {
    this.uaType = uaType;
  }

  public DroneLocationNotificationEntity timestamp(String timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * テレメトリ情報取得日時 ( RFC3339 形式 (yyyy-MM-ddTHH:mm:ss.sssZ) ) 。不正な値、値が無い、不明の場合は 0xFFFF とする。
   * 
   * @return timestamp
   */
  @NotEmpty(message = "テレメトリ情報取得日時は必須です")
  @NotNull(message = "テレメトリ情報取得日時は必須です")
  @Schema(name = "timestamp",
      description = "テレメトリ情報取得日時 ( RFC3339 形式 (yyyy-MM-ddTHH:mm:ss.sssZ) ) 。不正な値、値が無い、不明の場合は 0xFFFF とする。",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("timestamp")
  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }

  public DroneLocationNotificationEntity latitude(BigDecimal latitude) {
    this.latitude = latitude;
    return this;
  }

  /**
   * ドローンの緯度 (degree)。小数点 7 桁まで。不正な値、値が無い、不明の場合は 0 deg とする。 minimum: -90.0 maximum: 90.0
   * 
   * @return latitude
   */
  @Valid
  @NotNull(message = "緯度は必須です")
  @DecimalMin(value = "-90.0", inclusive = true, message = "緯度は-90.0以上でなければなりません")
  @DecimalMax(value = "90.0", inclusive = true, message = "緯度は90.0以下でなければなりません")
  @Schema(name = "latitude", description = "ドローンの緯度 (degree)。小数点 7 桁まで。不正な値、値が無い、不明の場合は 0 deg とする。",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("latitude")
  public BigDecimal getLatitude() {
    return latitude;
  }

  public void setLatitude(BigDecimal latitude) {
    this.latitude = latitude;
  }

  public DroneLocationNotificationEntity longitude(BigDecimal longitude) {
    this.longitude = longitude;
    return this;
  }

  /**
   * ドローンの経度 (degree)。小数点 7 桁まで。不正な値、値が無い、不明の場合は 0 deg とする。 minimum: -180.0 maximum: 180.0
   * 
   * @return longitude
   */
  @Valid
  @NotNull(message = "経度は必須です")
  @DecimalMin(value = "-180.0", inclusive = true, message = "経度は-180.0以上でなければなりません")
  @DecimalMax(value = "180.0", inclusive = true, message = "経度は180.0以下でなければなりません")
  @Schema(name = "longitude",
      description = "ドローンの経度 (degree)。小数点 7 桁まで。不正な値、値が無い、不明の場合は 0 deg とする。",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("longitude")
  public BigDecimal getLongitude() {
    return longitude;
  }

  public void setLongitude(BigDecimal longitude) {
    this.longitude = longitude;
  }

  public DroneLocationNotificationEntity altitude(Integer altitude) {
    this.altitude = altitude;
    return this;
  }

  /**
   * ドローンの標高。単位は m。最小解像度は 1 m。不正な値、値が無い、不明の場合は -1000 m とする。
   * 
   * @return altitude
   */
  @NotNull(message = "標高は必須です")
  @Schema(name = "altitude",
      description = "ドローンの標高。単位は m。最小解像度は 1 m。不正な値、値が無い、不明の場合は -1000 m とする。",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("altitude")
  public Integer getAltitude() {
    return altitude;
  }

  public void setAltitude(Integer altitude) {
    this.altitude = altitude;
  }

  public DroneLocationNotificationEntity trackDirection(Integer trackDirection) {
    this.trackDirection = trackDirection;
    return this;
  }

  /**
   * ドローンの進行方向 (degree)。最小解像度は 1 deg。不正な値、値が無い、不明の場合は 361 deg とする。
   * 
   * @return trackDirection
   */

  @Schema(name = "trackDirection",
      description = "ドローンの進行方向 (degree)。最小解像度は 1 deg。不正な値、値が無い、不明の場合は 361 deg とする。",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("trackDirection")
  public Integer getTrackDirection() {
    return trackDirection;
  }

  public void setTrackDirection(Integer trackDirection) {
    this.trackDirection = trackDirection;
  }

  public DroneLocationNotificationEntity speed(BigDecimal speed) {
    this.speed = speed;
    return this;
  }

  /**
   * ドローンの速度 (m/s)。最小解像度は 0.25 m/s。不正な値、値が無い、不明の場合は 255 m/s とする。もし、254.25 m/s 以上の場合は 254.25 m/s とする。
   * 
   * @return speed
   */
  @Valid
  @Schema(name = "speed",
      description = "ドローンの速度 (m/s)。最小解像度は 0.25 m/s。不正な値、値が無い、不明の場合は 255 m/s とする。もし、254.25 m/s 以上の場合は 254.25 m/s とする。",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("speed")
  public BigDecimal getSpeed() {
    return speed;
  }

  public void setSpeed(BigDecimal speed) {
    this.speed = speed;
  }

  public DroneLocationNotificationEntity verticalSpeed(BigDecimal verticalSpeed) {
    this.verticalSpeed = verticalSpeed;
    return this;
  }

  /**
   * ドローンの垂直速度 (m/s)。不正な値、値が無い、不明の場合は 63 m/s とする。もし、62 m/s 以上の場合は 62 m/s とする。もし、-62 m/s 以下の場合は -62
   * m/s とする。
   * 
   * @return verticalSpeed
   */
  @Valid
  @Schema(name = "verticalSpeed",
      description = "ドローンの垂直速度 (m/s)。不正な値、値が無い、不明の場合は 63 m/s とする。もし、62 m/s 以上の場合は 62 m/s とする。もし、-62 m/s 以下の場合は -62 m/s とする。",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
    DroneLocationNotificationEntity droneLocationNotificationEntity =
        (DroneLocationNotificationEntity) o;
    return Objects.equals(this.subscriptionId, droneLocationNotificationEntity.subscriptionId)
        && Objects.equals(this.reservationId, droneLocationNotificationEntity.reservationId)
        && Objects.equals(this.uasId, droneLocationNotificationEntity.uasId)
        && Objects.equals(this.uaType, droneLocationNotificationEntity.uaType)
        && Objects.equals(this.timestamp, droneLocationNotificationEntity.timestamp)
        && Objects.equals(this.latitude, droneLocationNotificationEntity.latitude)
        && Objects.equals(this.longitude, droneLocationNotificationEntity.longitude)
        && Objects.equals(this.altitude, droneLocationNotificationEntity.altitude)
        && Objects.equals(this.trackDirection, droneLocationNotificationEntity.trackDirection)
        && Objects.equals(this.speed, droneLocationNotificationEntity.speed)
        && Objects.equals(this.verticalSpeed, droneLocationNotificationEntity.verticalSpeed);
  }

  @Override
  public int hashCode() {
    return Objects.hash(subscriptionId, reservationId, uasId, uaType, timestamp, latitude,
        longitude, altitude, trackDirection, speed, verticalSpeed);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DroneLocationNotificationEntity {\n");
    sb.append("    subscriptionId: ").append(toIndentedString(subscriptionId)).append("\n");
    sb.append("    reservationId: ").append(toIndentedString(reservationId)).append("\n");
    sb.append("    uasId: ").append(toIndentedString(uasId)).append("\n");
    sb.append("    uaType: ").append(toIndentedString(uaType)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    latitude: ").append(toIndentedString(latitude)).append("\n");
    sb.append("    longitude: ").append(toIndentedString(longitude)).append("\n");
    sb.append("    altitude: ").append(toIndentedString(altitude)).append("\n");
    sb.append("    trackDirection: ").append(toIndentedString(trackDirection)).append("\n");
    sb.append("    speed: ").append(toIndentedString(speed)).append("\n");
    sb.append("    verticalSpeed: ").append(toIndentedString(verticalSpeed)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


