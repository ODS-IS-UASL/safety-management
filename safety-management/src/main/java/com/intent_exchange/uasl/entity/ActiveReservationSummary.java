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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * ActiveReservationSummary
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-09T14:24:35.115008009+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class ActiveReservationSummary {

  private String uaslReservationId;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date startAt;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date endAt;

  private String operatorId;

  @Valid
  private List<String> uaslSectionIds = new ArrayList<>();

  public ActiveReservationSummary() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActiveReservationSummary(String uaslReservationId, Date startAt, Date endAt, String operatorId, List<String> uaslSectionIds) {
    this.uaslReservationId = uaslReservationId;
    this.startAt = startAt;
    this.endAt = endAt;
    this.operatorId = operatorId;
    this.uaslSectionIds = uaslSectionIds;
  }

  public ActiveReservationSummary uaslReservationId(String uaslReservationId) {
    this.uaslReservationId = uaslReservationId;
    return this;
  }

  /**
   * 航路予約ID
   * @return uaslReservationId
   */
  @NotNull@NotEmpty 
  @Schema(name = "uaslReservationId", example = "123e4567-e89b-12d3-a456-426614174000", description = "航路予約ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uaslReservationId")
  public String getUaslReservationId() {
    return uaslReservationId;
  }

  public void setUaslReservationId(String uaslReservationId) {
    this.uaslReservationId = uaslReservationId;
  }

  public ActiveReservationSummary startAt(Date startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * 予約開始日時
   * @return startAt
   */
  @NotNull@Valid 
  @Schema(name = "startAt", example = "2025-12-25T10:00Z", description = "予約開始日時", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startAt")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
  public Date getStartAt() {
    return startAt;
  }

  public void setStartAt(Date startAt) {
    this.startAt = startAt;
  }

  public ActiveReservationSummary endAt(Date endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * 予約終了日時
   * @return endAt
   */
  @NotNull@Valid 
  @Schema(name = "endAt", example = "2025-12-25T14:00Z", description = "予約終了日時", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endAt")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
  public Date getEndAt() {
    return endAt;
  }

  public void setEndAt(Date endAt) {
    this.endAt = endAt;
  }

  public ActiveReservationSummary operatorId(String operatorId) {
    this.operatorId = operatorId;
    return this;
  }

  /**
   * 運航事業者ID
   * @return operatorId
   */
  @NotNull@NotEmpty 
  @Schema(name = "operatorId", example = "OP-001", description = "運航事業者ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("operatorId")
  public String getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }

  public ActiveReservationSummary uaslSectionIds(List<String> uaslSectionIds) {
    this.uaslSectionIds = uaslSectionIds;
    return this;
  }

  public ActiveReservationSummary addUaslSectionIdsItem(String uaslSectionIdsItem) {
    if (this.uaslSectionIds == null) {
      this.uaslSectionIds = new ArrayList<>();
    }
    this.uaslSectionIds.add(uaslSectionIdsItem);
    return this;
  }

  /**
   * 航路区画IDリスト
   * @return uaslSectionIds
   */
  @NotNull
  @Schema(name = "uaslSectionIds", example = "[\"c2462ed8-00ef-4072-8fa0-3d5c93148ad4\",\"c2462ed8-00ef-4072-8fa0-3d5c93148ad5\"]", description = "航路区画IDリスト", requiredMode = Schema.RequiredMode.REQUIRED)
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
    ActiveReservationSummary activeReservationSummary = (ActiveReservationSummary) o;
    return Objects.equals(this.uaslReservationId, activeReservationSummary.uaslReservationId) &&
        Objects.equals(this.startAt, activeReservationSummary.startAt) &&
        Objects.equals(this.endAt, activeReservationSummary.endAt) &&
        Objects.equals(this.operatorId, activeReservationSummary.operatorId) &&
        Objects.equals(this.uaslSectionIds, activeReservationSummary.uaslSectionIds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uaslReservationId, startAt, endAt, operatorId, uaslSectionIds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActiveReservationSummary {\n");
    sb.append("    uaslReservationId: ").append(toIndentedString(uaslReservationId)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    operatorId: ").append(toIndentedString(operatorId)).append("\n");
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

