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
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner
 */

@JsonTypeName("NearMissInformationResponse_attributes_ThirdPartyEntryMonitoring_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-18T19:35:22.332791300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner {

  private NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo thirdPartyInfo;

  private String airwayAdministratorId;

  private String operatorId;

  private String airwayId;

  private String airwaySectionId;

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner thirdPartyInfo(NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo thirdPartyInfo) {
    this.thirdPartyInfo = thirdPartyInfo;
    return this;
  }

  /**
   * Get thirdPartyInfo
   * @return thirdPartyInfo
   */
  @Valid 
  @Schema(name = "thirdPartyInfo", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("thirdPartyInfo")
  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo getThirdPartyInfo() {
    return thirdPartyInfo;
  }

  public void setThirdPartyInfo(NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo thirdPartyInfo) {
    this.thirdPartyInfo = thirdPartyInfo;
  }

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner airwayAdministratorId(String airwayAdministratorId) {
    this.airwayAdministratorId = airwayAdministratorId;
    return this;
  }

  /**
   * 航路運営者ID
   * @return airwayAdministratorId
   */
  
  @Schema(name = "airwayAdministratorId", description = "航路運営者ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayAdministratorId")
  public String getAirwayAdministratorId() {
    return airwayAdministratorId;
  }

  public void setAirwayAdministratorId(String airwayAdministratorId) {
    this.airwayAdministratorId = airwayAdministratorId;
  }

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner operatorId(String operatorId) {
    this.operatorId = operatorId;
    return this;
  }

  /**
   * 運航事業者ID
   * @return operatorId
   */
  
  @Schema(name = "operatorId", description = "運航事業者ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operatorId")
  public String getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner airwayId(String airwayId) {
    this.airwayId = airwayId;
    return this;
  }

  /**
   * 航路ID
   * @return airwayId
   */
  
  @Schema(name = "airwayId", description = "航路ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayId")
  public String getAirwayId() {
    return airwayId;
  }

  public void setAirwayId(String airwayId) {
    this.airwayId = airwayId;
  }

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner airwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
    return this;
  }

  /**
   * 航路区画のID
   * @return airwaySectionId
   */
  
  @Schema(name = "airwaySectionId", description = "航路区画のID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwaySectionId")
  public String getAirwaySectionId() {
    return airwaySectionId;
  }

  public void setAirwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner = (NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner) o;
    return Objects.equals(this.thirdPartyInfo, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner.thirdPartyInfo) &&
        Objects.equals(this.airwayAdministratorId, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner.airwayAdministratorId) &&
        Objects.equals(this.operatorId, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner.operatorId) &&
        Objects.equals(this.airwayId, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner.airwayId) &&
        Objects.equals(this.airwaySectionId, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner.airwaySectionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(thirdPartyInfo, airwayAdministratorId, operatorId, airwayId, airwaySectionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner {\n");
    sb.append("    thirdPartyInfo: ").append(toIndentedString(thirdPartyInfo)).append("\n");
    sb.append("    airwayAdministratorId: ").append(toIndentedString(airwayAdministratorId)).append("\n");
    sb.append("    operatorId: ").append(toIndentedString(operatorId)).append("\n");
    sb.append("    airwayId: ").append(toIndentedString(airwayId)).append("\n");
    sb.append("    airwaySectionId: ").append(toIndentedString(airwaySectionId)).append("\n");
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


