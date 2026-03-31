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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

/**
 * NearMissInformationResponseAttributesRouteDeviationInfoInner
 */

@JsonTypeName("NearMissInformationResponse_attributes_RouteDeviationInfo_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2025-02-18T19:35:22.332791300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class NearMissInformationResponseAttributesRouteDeviationInfoInner {

  private NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation nearMissInformation;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private String time;

  private String uaslAdministratorId;

  private String operatorId;

  private String uaslId;

  private NearMissInformationResponseAttributesRouteDeviationInfoInnerUasId uasId;

  @Valid
  private List<@Valid NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> routeDeviationDetectionInfo =
      new ArrayList<>();

  public NearMissInformationResponseAttributesRouteDeviationInfoInner nearMissInformation(
      NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation nearMissInformation) {
    this.nearMissInformation = nearMissInformation;
    return this;
  }

  /**
   * Get nearMissInformation
   * 
   * @return nearMissInformation
   */
  @Valid
  @Schema(name = "NearMissInformation", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("NearMissInformation")
  public NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation getNearMissInformation() {
    return nearMissInformation;
  }

  public void setNearMissInformation(
      NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation nearMissInformation) {
    this.nearMissInformation = nearMissInformation;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInner time(String time) {
    this.time = time;
    return this;
  }

  /**
   * 逸脱検知時刻
   * 
   * @return time
   */
  @Valid
  @Schema(name = "time", description = "逸脱検知時刻", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("time")
  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInner uaslAdministratorId(
      String uaslAdministratorId) {
    this.uaslAdministratorId = uaslAdministratorId;
    return this;
  }

  /**
   * 航路運営者ID
   * 
   * @return uaslAdministratorId
   */

  @Schema(name = "uaslAdministratorId", example = "123e4567-e89b-12d3-a456-426614174000",
      description = "航路運営者ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslAdministratorId")
  public String getUaslAdministratorId() {
    return uaslAdministratorId;
  }

  public void setUaslAdministratorId(String uaslAdministratorId) {
    this.uaslAdministratorId = uaslAdministratorId;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInner operatorId(
      String operatorId) {
    this.operatorId = operatorId;
    return this;
  }

  /**
   * 運航事業者ID
   * 
   * @return operatorId
   */

  @Schema(name = "operatorId", example = "123e4567-e89b-12d3-a456-426614174000",
      description = "運航事業者ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operatorId")
  public String getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInner uaslId(String uaslId) {
    this.uaslId = uaslId;
    return this;
  }

  /**
   * 航路ID
   * 
   * @return uaslId
   */

  @Schema(name = "uaslId", example = "123e4567-e89b-12d3-a456-426614174000", description = "航路ID",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslId")
  public String getUaslId() {
    return uaslId;
  }

  public void setUaslId(String uaslId) {
    this.uaslId = uaslId;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInner uasId(
      NearMissInformationResponseAttributesRouteDeviationInfoInnerUasId uasId) {
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
  public NearMissInformationResponseAttributesRouteDeviationInfoInnerUasId getUasId() {
    return uasId;
  }

  public void setUasId(NearMissInformationResponseAttributesRouteDeviationInfoInnerUasId uasId) {
    this.uasId = uasId;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInner routeDeviationDetectionInfo(
      List<@Valid NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> routeDeviationDetectionInfo) {
    this.routeDeviationDetectionInfo = routeDeviationDetectionInfo;
    return this;
  }


  public NearMissInformationResponseAttributesRouteDeviationInfoInner addRouteDeviationDetectionInfoItem(
      NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner routeDeviationDetectionInfoItem) {
    if (this.routeDeviationDetectionInfo == null) {
      this.routeDeviationDetectionInfo = new ArrayList<>();
    }
    this.routeDeviationDetectionInfo.add(routeDeviationDetectionInfoItem);
    return this;
  }

  /**
   * 逸脱検知情報のリスト
   * 
   * @return routeDeviationDetectionInfo
   */
  @Valid
  @Size(min = 1, max = 99)
  @Schema(name = "RouteDeviationDetectionInfo", description = "逸脱検知情報のリスト",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("RouteDeviationDetectionInfo")
  public List<@Valid NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> getRouteDeviationDetectionInfo() {
    return routeDeviationDetectionInfo;
  }

  public void setRouteDeviationDetectionInfo(
      List<@Valid NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner> routeDeviationDetectionInfo) {
    this.routeDeviationDetectionInfo = routeDeviationDetectionInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NearMissInformationResponseAttributesRouteDeviationInfoInner nearMissInformationResponseAttributesRouteDeviationInfoInner =
        (NearMissInformationResponseAttributesRouteDeviationInfoInner) o;
    return Objects.equals(this.nearMissInformation,
        nearMissInformationResponseAttributesRouteDeviationInfoInner.nearMissInformation)
        && Objects.equals(this.time,
            nearMissInformationResponseAttributesRouteDeviationInfoInner.time)
        && Objects.equals(this.uaslAdministratorId,
            nearMissInformationResponseAttributesRouteDeviationInfoInner.uaslAdministratorId)
        && Objects.equals(this.operatorId,
            nearMissInformationResponseAttributesRouteDeviationInfoInner.operatorId)
        && Objects.equals(this.uaslId,
            nearMissInformationResponseAttributesRouteDeviationInfoInner.uaslId)
        && Objects.equals(
                this.uasId, nearMissInformationResponseAttributesRouteDeviationInfoInner.uasId)
        && Objects.equals(this.routeDeviationDetectionInfo,
            nearMissInformationResponseAttributesRouteDeviationInfoInner.routeDeviationDetectionInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nearMissInformation, time, uaslAdministratorId, operatorId, uaslId,
        uasId, routeDeviationDetectionInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NearMissInformationResponseAttributesRouteDeviationInfoInner {\n");
    sb.append("    nearMissInformation: ")
        .append(toIndentedString(nearMissInformation))
        .append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    uaslAdministratorId: ")
        .append(toIndentedString(uaslAdministratorId))
        .append("\n");
    sb.append("    operatorId: ").append(toIndentedString(operatorId)).append("\n");
    sb.append("    uaslId: ").append(toIndentedString(uaslId)).append("\n");
    sb.append("    uasId: ").append(toIndentedString(uasId)).append("\n");
    sb.append("    routeDeviationDetectionInfo: ")
        .append(toIndentedString(routeDeviationDetectionInfo))
        .append("\n");
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


