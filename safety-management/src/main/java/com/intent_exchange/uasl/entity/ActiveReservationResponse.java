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
import com.intent_exchange.uasl.entity.ActiveReservationSummary;
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
 * ActiveReservationResponse
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2026-01-09T14:24:35.115008009+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class ActiveReservationResponse {

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Date checkedAt;

  @Valid
  private List<@Valid ActiveReservationSummary> reservations = new ArrayList<>();

  public ActiveReservationResponse() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ActiveReservationResponse(Date checkedAt, List<@Valid ActiveReservationSummary> reservations) {
    this.checkedAt = checkedAt;
    this.reservations = reservations;
  }

  public ActiveReservationResponse checkedAt(Date checkedAt) {
    this.checkedAt = checkedAt;
    return this;
  }

  /**
   * サーバ側で判定に使用した現在時刻（ISO 8601形式・UTC）。   
   * @return checkedAt
   */
  @NotNull@Valid 
  @Schema(name = "checkedAt", example = "2025-12-25T12:00Z", description = "サーバ側で判定に使用した現在時刻（ISO 8601形式・UTC）。   ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("checkedAt")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", timezone = "UTC")
  public Date getCheckedAt() {
    return checkedAt;
  }

  public void setCheckedAt(Date checkedAt) {
    this.checkedAt = checkedAt;
  }

  public ActiveReservationResponse reservations(List<@Valid ActiveReservationSummary> reservations) {
    this.reservations = reservations;
    return this;
  }

  public ActiveReservationResponse addReservationsItem(ActiveReservationSummary reservationsItem) {
    if (this.reservations == null) {
      this.reservations = new ArrayList<>();
    }
    this.reservations.add(reservationsItem);
    return this;
  }

  /**
   * 条件に合致した有効な予約の一覧。   有効な予約情報がない場合は空配列を返す。 
   * @return reservations
   */
  @NotNull@Valid 
  @Schema(name = "reservations", description = "条件に合致した有効な予約の一覧。   有効な予約情報がない場合は空配列を返す。 ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("reservations")
  public List<@Valid ActiveReservationSummary> getReservations() {
    return reservations;
  }

  public void setReservations(List<@Valid ActiveReservationSummary> reservations) {
    this.reservations = reservations;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ActiveReservationResponse activeReservationResponse = (ActiveReservationResponse) o;
    return Objects.equals(this.checkedAt, activeReservationResponse.checkedAt) &&
        Objects.equals(this.reservations, activeReservationResponse.reservations);
  }

  @Override
  public int hashCode() {
    return Objects.hash(checkedAt, reservations);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ActiveReservationResponse {\n");
    sb.append("    checkedAt: ").append(toIndentedString(checkedAt)).append("\n");
    sb.append("    reservations: ").append(toIndentedString(reservations)).append("\n");
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

