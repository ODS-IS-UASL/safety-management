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
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * 通知先情報
 */

@Schema(name = "MonitoringNotificationDestinationRequestEntity", description = "通知先情報")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-12-16T10:32:09.677292+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class MonitoringNotificationDestinationRequestEntity {

  private String endPoint;

  public MonitoringNotificationDestinationRequestEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public MonitoringNotificationDestinationRequestEntity(String endPoint) {
    this.endPoint = endPoint;
  }

  public MonitoringNotificationDestinationRequestEntity endPoint(String endPoint) {
    this.endPoint = endPoint;
    return this;
  }

  /**
   * Get endPoint
   * @return endPoint
   */
  @NotNull@NotEmpty 
  @Schema(name = "endPoint", example = "https://_****_/++++/", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endPoint")
  public String getEndPoint() {
    return endPoint;
  }

  public void setEndPoint(String endPoint) {
    this.endPoint = endPoint;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MonitoringNotificationDestinationRequestEntity monitoringNotificationDestinationRequestEntity = (MonitoringNotificationDestinationRequestEntity) o;
    return Objects.equals(this.endPoint, monitoringNotificationDestinationRequestEntity.endPoint);
  }

  @Override
  public int hashCode() {
    return Objects.hash(endPoint);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MonitoringNotificationDestinationRequestEntity {\n");
    sb.append("    endPoint: ").append(toIndentedString(endPoint)).append("\n");
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


