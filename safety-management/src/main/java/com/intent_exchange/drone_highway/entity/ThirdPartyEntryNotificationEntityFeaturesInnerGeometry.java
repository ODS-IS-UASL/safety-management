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
 * ThirdPartyEntryNotificationEntityFeaturesInnerGeometry
 */

@JsonTypeName("ThirdPartyEntryNotificationEntity_features_inner_geometry")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-07T20:16:56.585409900+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class ThirdPartyEntryNotificationEntityFeaturesInnerGeometry {

  private String type;

  @Valid
  private List<List<List<@DecimalMin(value = "-180.0", inclusive = true) @DecimalMax(value = "180.0", inclusive = true)Double>>> coordinates = new ArrayList<>();

  public ThirdPartyEntryNotificationEntityFeaturesInnerGeometry type(String type) {
    this.type = type;
    return this;
  }

  /**
   * Polygonのみを指定
   * @return type
   */
  
  @Schema(name = "type", description = "Polygonのみを指定", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ThirdPartyEntryNotificationEntityFeaturesInnerGeometry coordinates(List<List<List<@DecimalMin(value = "-180.0", inclusive = true) @DecimalMax(value = "180.0", inclusive = true)Double>>> coordinates) {
    this.coordinates = coordinates;
    return this;
  }

  public ThirdPartyEntryNotificationEntityFeaturesInnerGeometry addCoordinatesItem(List<List<@DecimalMin(value = "-180.0", inclusive = true) @DecimalMax(value = "180.0", inclusive = true)Double>> coordinatesItem) {
    if (this.coordinates == null) {
      this.coordinates = new ArrayList<>();
    }
    this.coordinates.add(coordinatesItem);
    return this;
  }

  /**
   * Get coordinates
   * @return coordinates
   */
  @Valid @Size(min = 1, max = 1) 
  @Schema(name = "coordinates", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("coordinates")
  public List<List<List<@DecimalMin(value = "-180.0", inclusive = true) @DecimalMax(value = "180.0", inclusive = true)Double>>> getCoordinates() {
    return coordinates;
  }

  public void setCoordinates(List<List<List<@DecimalMin(value = "-180.0", inclusive = true) @DecimalMax(value = "180.0", inclusive = true)Double>>> coordinates) {
    this.coordinates = coordinates;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ThirdPartyEntryNotificationEntityFeaturesInnerGeometry thirdPartyEntryNotificationEntityFeaturesInnerGeometry = (ThirdPartyEntryNotificationEntityFeaturesInnerGeometry) o;
    return Objects.equals(this.type, thirdPartyEntryNotificationEntityFeaturesInnerGeometry.type) &&
        Objects.equals(this.coordinates, thirdPartyEntryNotificationEntityFeaturesInnerGeometry.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, coordinates);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ThirdPartyEntryNotificationEntityFeaturesInnerGeometry {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    coordinates: ").append(toIndentedString(coordinates)).append("\n");
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


