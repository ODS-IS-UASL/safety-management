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

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.intent_exchange.uasl.entity.LinkageInformationNotificationEntityUasId;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * 機体ID(リモートID)と航路予約毎の識別IDを受信し、予約情報を保存API用のentity
 */

@Schema(name = "LinkageInformationNotificationEntity", description = "機体ID(リモートID)と航路予約毎の識別IDを受信し、予約情報を保存API用のentity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-06T18:33:27.346222500+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class LinkageInformationNotificationEntity {

  private String uaslReservationId;

  private LinkageInformationNotificationEntityUasId uasId;

  private Integer aircraftInfoId;

  public LinkageInformationNotificationEntity uaslReservationId(String uaslReservationId) {
    this.uaslReservationId = uaslReservationId;
    return this;
  }

  /**
   * 航路予約毎の識別ID
   * @return uaslReservationId
   */
  
  @Schema(name = "uaslReservationId", description = "航路予約毎の識別ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslReservationId")
  public String getUaslReservationId() {
    return uaslReservationId;
  }

  public void setUaslReservationId(String uaslReservationId) {
    this.uaslReservationId = uaslReservationId;
  }

  public LinkageInformationNotificationEntity uasId(LinkageInformationNotificationEntityUasId uasId) {
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
  public LinkageInformationNotificationEntityUasId getUasId() {
    return uasId;
  }

  public void setUasId(LinkageInformationNotificationEntityUasId uasId) {
    this.uasId = uasId;
  }

  public LinkageInformationNotificationEntity aircraftInfoId(Integer aircraftInfoId) {
    this.aircraftInfoId = aircraftInfoId;
    return this;
  }

  /**
   * 機体情報ID
   * @return aircraftInfoId
   */
  
  @Schema(name = "aircraftInfoId", description = "機体情報ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aircraftInfoId")
  public Integer getAircraftInfoId() {
    return aircraftInfoId;
  }

  public void setAircraftInfoId(Integer aircraftInfoId) {
    this.aircraftInfoId = aircraftInfoId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkageInformationNotificationEntity linkageInformationNotificationEntity = (LinkageInformationNotificationEntity) o;
    return Objects.equals(this.uaslReservationId, linkageInformationNotificationEntity.uaslReservationId) &&
        Objects.equals(this.uasId, linkageInformationNotificationEntity.uasId) &&
        Objects.equals(this.aircraftInfoId, linkageInformationNotificationEntity.aircraftInfoId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uaslReservationId, uasId, aircraftInfoId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LinkageInformationNotificationEntity {\n");
    sb.append("    uaslReservationId: ").append(toIndentedString(uaslReservationId)).append("\n");
    sb.append("    uasId: ").append(toIndentedString(uasId)).append("\n");
    sb.append("    aircraftInfoId: ").append(toIndentedString(aircraftInfoId)).append("\n");
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


