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
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * 航路逸脱検知情報のエンティティ
 */

@Schema(name = "NearMissInformationResponse_attributes_RouteDeviationInfo_inner_RouteDeviationDetectionInfo_inner", description = "航路逸脱検知情報のエンティティ")
@JsonTypeName("NearMissInformationResponse_attributes_RouteDeviationInfo_inner_RouteDeviationDetectionInfo_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-18T19:35:22.332791300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner {

  private String airwaySectionId;

  private NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates coodinates;

  public NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner(NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates coodinates) {
    this.coodinates = coodinates;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner airwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
    return this;
  }

  /**
   * 航路区画のID
   * @return airwaySectionId
   */
  
  @Schema(name = "airwaySectionId", example = "123e4567-e89b-12d3-a456-426614174000", description = "航路区画のID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwaySectionId")
  public String getAirwaySectionId() {
    return airwaySectionId;
  }

  public void setAirwaySectionId(String airwaySectionId) {
    this.airwaySectionId = airwaySectionId;
  }

  public NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner coodinates(NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates coodinates) {
    this.coodinates = coodinates;
    return this;
  }

  /**
   * Get coodinates
   * @return coodinates
   */
  @NotNull@Valid 
  @Schema(name = "coodinates", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("coodinates")
  public NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates getCoodinates() {
    return coodinates;
  }

  public void setCoodinates(NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInnerCoodinates coodinates) {
    this.coodinates = coodinates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner nearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner = (NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner) o;
    return Objects.equals(this.airwaySectionId, nearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner.airwaySectionId) &&
        Objects.equals(this.coodinates, nearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner.coodinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(airwaySectionId, coodinates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NearMissInformationResponseAttributesRouteDeviationInfoInnerRouteDeviationDetectionInfoInner {\n");
    sb.append("    airwaySectionId: ").append(toIndentedString(airwaySectionId)).append("\n");
    sb.append("    coodinates: ").append(toIndentedString(coodinates)).append("\n");
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


