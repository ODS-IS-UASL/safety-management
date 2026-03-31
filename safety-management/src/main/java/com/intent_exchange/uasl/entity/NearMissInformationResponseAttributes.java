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
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesRouteDeviationInfoInner;
import com.intent_exchange.uasl.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * データモデル要素
 */

@Schema(name = "NearMissInformationResponse_attributes", description = "データモデル要素")
@JsonTypeName("NearMissInformationResponse_attributes")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-18T19:35:22.332791300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class NearMissInformationResponseAttributes {

  @Valid
  private List<@Valid NearMissInformationResponseAttributesRouteDeviationInfoInner> routeDeviationInfo = new ArrayList<>();

  @Valid
  private List<@Valid NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitoring = new ArrayList<>();

  public NearMissInformationResponseAttributes() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NearMissInformationResponseAttributes(List<@Valid NearMissInformationResponseAttributesRouteDeviationInfoInner> routeDeviationInfo, List<@Valid NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitoring) {
    this.routeDeviationInfo = routeDeviationInfo;
    this.thirdPartyEntryMonitoring = thirdPartyEntryMonitoring;
  }

  public NearMissInformationResponseAttributes routeDeviationInfo(List<@Valid NearMissInformationResponseAttributesRouteDeviationInfoInner> routeDeviationInfo) {
    this.routeDeviationInfo = routeDeviationInfo;
    return this;
  }

  public NearMissInformationResponseAttributes addRouteDeviationInfoItem(NearMissInformationResponseAttributesRouteDeviationInfoInner routeDeviationInfoItem) {
    if (this.routeDeviationInfo == null) {
      this.routeDeviationInfo = new ArrayList<>();
    }
    this.routeDeviationInfo.add(routeDeviationInfoItem);
    return this;
  }

  /**
   * 航路逸脱情報のリスト
   * @return routeDeviationInfo
   */
  @NotNull@Valid 
  @Schema(name = "RouteDeviationInfo", description = "航路逸脱情報のリスト", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("RouteDeviationInfo")
  public List<@Valid NearMissInformationResponseAttributesRouteDeviationInfoInner> getRouteDeviationInfo() {
    return routeDeviationInfo;
  }

  public void setRouteDeviationInfo(List<@Valid NearMissInformationResponseAttributesRouteDeviationInfoInner> routeDeviationInfo) {
    this.routeDeviationInfo = routeDeviationInfo;
  }

  public NearMissInformationResponseAttributes thirdPartyEntryMonitoring(List<@Valid NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitoring) {
    this.thirdPartyEntryMonitoring = thirdPartyEntryMonitoring;
    return this;
  }

  public NearMissInformationResponseAttributes addThirdPartyEntryMonitoringItem(NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner thirdPartyEntryMonitoringItem) {
    if (this.thirdPartyEntryMonitoring == null) {
      this.thirdPartyEntryMonitoring = new ArrayList<>();
    }
    this.thirdPartyEntryMonitoring.add(thirdPartyEntryMonitoringItem);
    return this;
  }

  /**
   * 第三者立入監視情報のリスト
   * @return thirdPartyEntryMonitoring
   */
  @NotNull@Valid 
  @Schema(name = "ThirdPartyEntryMonitoring", description = "第三者立入監視情報のリスト", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("ThirdPartyEntryMonitoring")
  public List<@Valid NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> getThirdPartyEntryMonitoring() {
    return thirdPartyEntryMonitoring;
  }

  public void setThirdPartyEntryMonitoring(List<@Valid NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitoring) {
    this.thirdPartyEntryMonitoring = thirdPartyEntryMonitoring;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NearMissInformationResponseAttributes nearMissInformationResponseAttributes = (NearMissInformationResponseAttributes) o;
    return Objects.equals(this.routeDeviationInfo, nearMissInformationResponseAttributes.routeDeviationInfo) &&
        Objects.equals(this.thirdPartyEntryMonitoring, nearMissInformationResponseAttributes.thirdPartyEntryMonitoring);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routeDeviationInfo, thirdPartyEntryMonitoring);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NearMissInformationResponseAttributes {\n");
    sb.append("    routeDeviationInfo: ").append(toIndentedString(routeDeviationInfo)).append("\n");
    sb.append("    thirdPartyEntryMonitoring: ").append(toIndentedString(thirdPartyEntryMonitoring)).append("\n");
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


