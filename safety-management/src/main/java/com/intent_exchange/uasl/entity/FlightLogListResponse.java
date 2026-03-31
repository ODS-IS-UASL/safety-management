/*
 * Copyright 2026 Intent Exchange, Inc.
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
import com.intent_exchange.uasl.entity.FlightLogResponse;
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
 * FlightLogListResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-28T13:43:13.092416900+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class FlightLogListResponse {

  @Valid
  private List<@Valid FlightLogResponse> flightLogs = new ArrayList<>();

  private Integer totalCount;

  public FlightLogListResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public FlightLogListResponse(List<@Valid FlightLogResponse> flightLogs, Integer totalCount) {
    this.flightLogs = flightLogs;
    this.totalCount = totalCount;
  }

  public FlightLogListResponse flightLogs(List<@Valid FlightLogResponse> flightLogs) {
    this.flightLogs = flightLogs;
    return this;
  }

  public FlightLogListResponse addFlightLogsItem(FlightLogResponse flightLogsItem) {
    if (this.flightLogs == null) {
      this.flightLogs = new ArrayList<>();
    }
    this.flightLogs.add(flightLogsItem);
    return this;
  }

  /**
   * フライトログリスト
   * @return flightLogs
   */
  @NotNull@Valid 
  @Schema(name = "flightLogs", description = "フライトログリスト", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("flightLogs")
  public List<@Valid FlightLogResponse> getFlightLogs() {
    return flightLogs;
  }

  public void setFlightLogs(List<@Valid FlightLogResponse> flightLogs) {
    this.flightLogs = flightLogs;
  }

  public FlightLogListResponse totalCount(Integer totalCount) {
    this.totalCount = totalCount;
    return this;
  }

  /**
   * 検索条件に一致した件数
   * @return totalCount
   */
  @NotNull
  @Schema(name = "totalCount", example = "20", description = "検索条件に一致した件数", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("totalCount")
  public Integer getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(Integer totalCount) {
    this.totalCount = totalCount;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FlightLogListResponse flightLogListResponse = (FlightLogListResponse) o;
    return Objects.equals(this.flightLogs, flightLogListResponse.flightLogs) &&
        Objects.equals(this.totalCount, flightLogListResponse.totalCount);
  }

  @Override
  public int hashCode() {
    return Objects.hash(flightLogs, totalCount);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FlightLogListResponse {\n");
    sb.append("    flightLogs: ").append(toIndentedString(flightLogs)).append("\n");
    sb.append("    totalCount: ").append(toIndentedString(totalCount)).append("\n");
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

