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
import com.fasterxml.jackson.annotation.JsonValue;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner
 */

@JsonTypeName("NearMissInformationResponse_attributes_ThirdPartyEntryMonitoring_inner_thirdPartyInfo_features_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-18T19:35:22.332791300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner {

  /**
   * Gets or Sets type
   */
  public enum TypeEnum {
    FEATURE("feature");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry geometry;

  private NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties properties;

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Get type
   * @return type
   */
  
  @Schema(name = "type", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner geometry(NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry geometry) {
    this.geometry = geometry;
    return this;
  }

  /**
   * Get geometry
   * @return geometry
   */
  @Valid 
  @Schema(name = "geometry", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("geometry")
  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry getGeometry() {
    return geometry;
  }

  public void setGeometry(NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerGeometry geometry) {
    this.geometry = geometry;
  }

  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner properties(NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties properties) {
    this.properties = properties;
    return this;
  }

  /**
   * Get properties
   * @return properties
   */
  @Valid 
  @Schema(name = "properties", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("properties")
  public NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties getProperties() {
    return properties;
  }

  public void setProperties(NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInnerProperties properties) {
    this.properties = properties;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner nearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner = (NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner) o;
    return Objects.equals(this.type, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner.type) &&
        Objects.equals(this.geometry, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner.geometry) &&
        Objects.equals(this.properties, nearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, geometry, properties);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NearMissInformationResponseAttributesThirdPartyEntryMonitoringInnerThirdPartyInfoFeaturesInner {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    geometry: ").append(toIndentedString(geometry)).append("\n");
    sb.append("    properties: ").append(toIndentedString(properties)).append("\n");
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


