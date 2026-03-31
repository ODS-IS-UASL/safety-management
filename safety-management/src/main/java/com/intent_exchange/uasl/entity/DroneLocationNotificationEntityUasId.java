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

import java.util.Objects;
import jakarta.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 運航中ドローンを表す Json オブジェクト。
 */

@Schema(name = "DroneLocationNotificationEntity_uasId", description = "運航中ドローンを表す Json オブジェクト。")
@JsonTypeName("DroneLocationNotificationEntity_uasId")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2024-12-13T05:27:23.828338500Z[Etc/UTC]", comments = "Generator version: 7.8.0")
public class DroneLocationNotificationEntityUasId {

  private String serialNumber;

  private String registrationId;

  private String utmId;

  private String specificSessionId;

  private String uasId;

  public DroneLocationNotificationEntityUasId serialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
    return this;
  }

  /**
   * ドローンのシリアルナンバー。CTA-2063-A シリアルナンバーフォーマットで表現する。
   * 
   * @return serialNumber
   */

  @Schema(name = "serialNumber", description = "ドローンのシリアルナンバー。CTA-2063-A シリアルナンバーフォーマットで表現する。",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("serialNumber")
  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public DroneLocationNotificationEntityUasId registrationId(String registrationId) {
    this.registrationId = registrationId;
    return this;
  }

  /**
   * ドローンの登録 ID。フォーマットは <ICAO 国籍マーク>.<CAA 割り当て ID>。ASCII
   * エンコードされており、大文字の英字（A-Z）、ドット（.）、および数字（0-9）のみが許可される。(例) N.123456
   * 
   * @return registrationId
   */

  @Schema(name = "registrationId",
      description = "ドローンの登録 ID。フォーマットは <ICAO 国籍マーク>.<CAA 割り当て ID>。ASCII エンコードされており、大文字の英字（A-Z）、ドット（.）、および数字（0-9）のみが許可される。(例) N.123456",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("registrationId")
  public String getRegistrationId() {
    return registrationId;
  }

  public void setRegistrationId(String registrationId) {
    this.registrationId = registrationId;
  }

  public DroneLocationNotificationEntityUasId utmId(String utmId) {
    this.utmId = utmId;
    return this;
  }

  /**
   * 「セッションID」として機能します。
   * 
   * @return utmId
   */

  @Schema(name = "utmId", description = "「セッションID」として機能します。",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("utmId")
  public String getUtmId() {
    return utmId;
  }

  public void setUtmId(String utmId) {
    this.utmId = utmId;
  }

  public DroneLocationNotificationEntityUasId specificSessionId(String specificSessionId) {
    this.specificSessionId = specificSessionId;
    return this;
  }

  /**
   * 特定のフライト (セッション) を識別するためのユニークな 20 バイトの ID。
   * 
   * @return specificSessionId
   */

  @Schema(name = "specificSessionId", description = "特定のフライト (セッション) を識別するためのユニークな 20 バイトの ID。",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("specificSessionId")
  public String getSpecificSessionId() {
    return specificSessionId;
  }

  public void setSpecificSessionId(String specificSessionId) {
    this.specificSessionId = specificSessionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DroneLocationNotificationEntityUasId droneLocationNotificationEntityUasId =
        (DroneLocationNotificationEntityUasId) o;
    return Objects.equals(this.serialNumber, droneLocationNotificationEntityUasId.serialNumber)
        && Objects.equals(this.registrationId, droneLocationNotificationEntityUasId.registrationId)
        && Objects.equals(this.utmId, droneLocationNotificationEntityUasId.utmId) && Objects.equals(
            this.specificSessionId, droneLocationNotificationEntityUasId.specificSessionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(serialNumber, registrationId, utmId, specificSessionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DroneLocationNotificationEntityUasId {\n");
    sb.append("    serialNumber: ").append(toIndentedString(serialNumber)).append("\n");
    sb.append("    registrationId: ").append(toIndentedString(registrationId)).append("\n");
    sb.append("    utmId: ").append(toIndentedString(utmId)).append("\n");
    sb.append("    specificSessionId: ").append(toIndentedString(specificSessionId)).append("\n");
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

  public String getUasId() {
    if (registrationId != null && !registrationId.isEmpty()) {
      this.uasId = registrationId;
      return this.uasId;
    } else if (serialNumber != null && !serialNumber.isEmpty()) {
      this.uasId = serialNumber;
      return this.uasId;
    } else if (utmId != null && !utmId.isEmpty()) {
      this.uasId = utmId;
      return this.uasId;
    } else if (specificSessionId != null && !specificSessionId.isEmpty()) {
      this.uasId = specificSessionId;
      return this.uasId;
    } else {
      this.uasId = null; // すべてのフィールドが null または空の場合
      return this.uasId;
    }
  }
}


