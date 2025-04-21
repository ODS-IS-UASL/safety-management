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
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * 航路逸脱情報
 */

@Schema(name = "NearMissInformationResponse_attributes_RouteDeviationInfo_inner_NearMissInformation", description = "航路逸脱情報")
@JsonTypeName("NearMissInformationResponse_attributes_RouteDeviationInfo_inner_NearMissInformation")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-18T19:35:22.332791300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation {

  private Double routeDeviationRate;

  private String routeDeviationAmount;

  public NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation(Double routeDeviationRate, String routeDeviationAmount) {
    this.routeDeviationRate = routeDeviationRate;
    this.routeDeviationAmount = routeDeviationAmount;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation routeDeviationRate(Double routeDeviationRate) {
    this.routeDeviationRate = routeDeviationRate;
    return this;
  }

  /**
   * 逸脱割合(%)
   * minimum: 0
   * maximum: 100
   * @return routeDeviationRate
   */
  @NotNull@DecimalMin("0") @DecimalMax("100") 
  @Schema(name = "routeDeviationRate", example = "5.8", description = "逸脱割合(%)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("routeDeviationRate")
  public Double getRouteDeviationRate() {
    return routeDeviationRate;
  }

  public void setRouteDeviationRate(Double routeDeviationRate) {
    this.routeDeviationRate = routeDeviationRate;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation routeDeviationAmount(String routeDeviationAmount) {
    this.routeDeviationAmount = routeDeviationAmount;
    return this;
  }

  /**
   * 逸脱量(m)
   * @return routeDeviationAmount
   */
  @NotNull@NotEmpty @Size(min = 1, max = 128) 
  @Schema(name = "routeDeviationAmount", example = "5", description = "逸脱量(m)", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("routeDeviationAmount")
  public String getRouteDeviationAmount() {
    return routeDeviationAmount;
  }

  public void setRouteDeviationAmount(String routeDeviationAmount) {
    this.routeDeviationAmount = routeDeviationAmount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation nearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation = (NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation) o;
    return Objects.equals(this.routeDeviationRate, nearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation.routeDeviationRate) &&
        Objects.equals(this.routeDeviationAmount, nearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation.routeDeviationAmount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(routeDeviationRate, routeDeviationAmount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NearMissInformationResponseAttributesRouteDeviationInfoInnerNearMissInformation {\n");
    sb.append("    routeDeviationRate: ").append(toIndentedString(routeDeviationRate)).append("\n");
    sb.append("    routeDeviationAmount: ").append(toIndentedString(routeDeviationAmount)).append("\n");
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


