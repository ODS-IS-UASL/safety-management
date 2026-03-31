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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;

/**
 * NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner
 */

@JsonTypeName("NearMissInformationResponse_attributes_ThirdPartyEntryMonitoring_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-18T19:35:22.332791300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner {

  private NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfo thirdPartyInfo;

  private String uaslAdministratorId;

  private String operatorId;

  private String uaslId;

  private String uaslSectionId;

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

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner uaslAdministratorId(String uaslAdministratorId) {
    this.uaslAdministratorId = uaslAdministratorId;
    return this;
  }

  /**
   * 航路運営者ID
   * @return uaslAdministratorId
   */
  
  @Schema(name = "uaslAdministratorId", description = "航路運営者ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslAdministratorId")
  public String getUaslAdministratorId() {
    return uaslAdministratorId;
  }

  public void setUaslAdministratorId(String uaslAdministratorId) {
    this.uaslAdministratorId = uaslAdministratorId;
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

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner uaslId(String uaslId) {
    this.uaslId = uaslId;
    return this;
  }

  /**
   * 航路ID
   * @return uaslId
   */
  
  @Schema(name = "uaslId", description = "航路ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslId")
  public String getUaslId() {
    return uaslId;
  }

  public void setUaslId(String uaslId) {
    this.uaslId = uaslId;
  }

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner uaslSectionId(String uaslSectionId) {
    this.uaslSectionId = uaslSectionId;
    return this;
  }

  /**
   * 航路区画のID
   * @return uaslSectionId
   */
  
  @Schema(name = "uaslSectionId", description = "航路区画のID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslSectionId")
  public String getUaslSectionId() {
    return uaslSectionId;
  }

  public void setUaslSectionId(String uaslSectionId) {
    this.uaslSectionId = uaslSectionId;
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
        Objects.equals(this.uaslAdministratorId, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner.uaslAdministratorId) &&
        Objects.equals(this.operatorId, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner.operatorId) &&
        Objects.equals(this.uaslId, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner.uaslId) &&
        Objects.equals(this.uaslSectionId, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInner.uaslSectionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(thirdPartyInfo, uaslAdministratorId, operatorId, uaslId, uaslSectionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner {\n");
    sb.append("    thirdPartyInfo: ").append(toIndentedString(thirdPartyInfo)).append("\n");
    sb.append("    uaslAdministratorId: ").append(toIndentedString(uaslAdministratorId)).append("\n");
    sb.append("    operatorId: ").append(toIndentedString(operatorId)).append("\n");
    sb.append("    uaslId: ").append(toIndentedString(uaslId)).append("\n");
    sb.append("    uaslSectionId: ").append(toIndentedString(uaslSectionId)).append("\n");
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


