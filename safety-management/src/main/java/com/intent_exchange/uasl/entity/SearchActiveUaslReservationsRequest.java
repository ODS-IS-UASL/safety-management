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
 * SearchActiveUaslReservationsRequest
 */

@JsonTypeName("searchActiveUaslReservations_request")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-09T14:24:35.115008009+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class SearchActiveUaslReservationsRequest {

  @Valid
  private List<String> uaslSectionIds;

  public SearchActiveUaslReservationsRequest() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public SearchActiveUaslReservationsRequest(List<String> uaslSectionIds) {
    this.uaslSectionIds = uaslSectionIds;
  }

  public SearchActiveUaslReservationsRequest uaslSectionIds(List<String> uaslSectionIds) {
    this.uaslSectionIds = uaslSectionIds;
    return this;
  }

  public SearchActiveUaslReservationsRequest addUaslSectionIdsItem(String uaslSectionIdsItem) {
    if (this.uaslSectionIds == null) {
      this.uaslSectionIds = new ArrayList<>();
    }
    this.uaslSectionIds.add(uaslSectionIdsItem);
    return this;
  }

  /**
   * 航路区画IDのリスト。 
   * @return uaslSectionIds
   */
  @NotNull(message = "uaslSectionIds is required")
  @NotEmpty(message = "uaslSectionIds is required")
  @Schema(name = "uaslSectionIds", example = "[\"c2462ed8-00ef-4072-8fa0-3d5c93148ad4\",\"c2462ed8-00ef-4072-8fa0-3d5c93148ad5\"]", description = "航路区画IDのリスト。 ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uaslSectionIds")
  public List<String> getUaslSectionIds() {
    return uaslSectionIds;
  }

  public void setUaslSectionIds(List<String> uaslSectionIds) {
    this.uaslSectionIds = uaslSectionIds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SearchActiveUaslReservationsRequest searchActiveUaslReservationsRequest = (SearchActiveUaslReservationsRequest) o;
    return Objects.equals(this.uaslSectionIds, searchActiveUaslReservationsRequest.uaslSectionIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uaslSectionIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SearchActiveUaslReservationsRequest {\n");
    sb.append("    uaslSectionIds: ").append(toIndentedString(uaslSectionIds)).append("\n");
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

