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

import java.util.Date;
import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.intent_exchange.uasl.annotation.DateFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 航路の適合性評価実行API用のentity
 */

@Schema(name = "ConformityAssessmentExecutionEntity", description = "航路の適合性評価実行API用のentity")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2025-02-05T16:53:06.485800400+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class ConformityAssessmentExecutionEntity {

  private String uaslSectionId;

  @DateFormat
  private Date startAt;

  @DateFormat
  private Date endAt;

  private ConformityAssessmentExecutionEntityAircraftInfo aircraftInfo;

  public ConformityAssessmentExecutionEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public ConformityAssessmentExecutionEntity(String uaslSectionId, Date startAt, Date endAt,
      ConformityAssessmentExecutionEntityAircraftInfo aircraftInfo) {
    this.uaslSectionId = uaslSectionId;
    this.startAt = startAt;
    this.endAt = endAt;
    this.aircraftInfo = aircraftInfo;
  }

  public ConformityAssessmentExecutionEntity uaslSectionId(String uaslSectionId) {
    this.uaslSectionId = uaslSectionId;
    return this;
  }

  /**
   * 航路区画ID
   * 
   * @return uaslSectionId
   */
  @NotNull
  @NotEmpty
  @Schema(name = "uaslSectionId", description = "航路区画ID",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("uaslSectionId")
  public String getUaslSectionId() {
    return uaslSectionId;
  }

  public void setUaslSectionId(String uaslSectionId) {
    this.uaslSectionId = uaslSectionId;
  }

  public ConformityAssessmentExecutionEntity startAt(Date startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * 予約開始日時
   * 
   * @return startAt
   */
  @NotNull
  @Valid
  @Schema(name = "startAt", description = "予約開始日時", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startAt")
  public Date getStartAt() {
    return startAt;
  }

  public void setStartAt(Date startAt) {
    this.startAt = startAt;
  }

  public ConformityAssessmentExecutionEntity endAt(Date endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * 予約終了日時
   * 
   * @return endAt
   */
  @NotNull
  @Valid
  @Schema(name = "endAt", description = "予約終了日時", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endAt")
  public Date getEndAt() {
    return endAt;
  }

  public void setEndAt(Date endAt) {
    this.endAt = endAt;
  }

  public ConformityAssessmentExecutionEntity aircraftInfo(
      ConformityAssessmentExecutionEntityAircraftInfo aircraftInfo) {
    this.aircraftInfo = aircraftInfo;
    return this;
  }

  /**
   * Get aircraftInfo
   * 
   * @return aircraftInfo
   */
  @NotNull
  @Valid
  @Schema(name = "aircraftInfo", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("aircraftInfo")
  public ConformityAssessmentExecutionEntityAircraftInfo getAircraftInfo() {
    return aircraftInfo;
  }

  public void setAircraftInfo(ConformityAssessmentExecutionEntityAircraftInfo aircraftInfo) {
    this.aircraftInfo = aircraftInfo;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConformityAssessmentExecutionEntity conformityAssessmentExecutionEntity =
        (ConformityAssessmentExecutionEntity) o;
    return Objects.equals(this.uaslSectionId, conformityAssessmentExecutionEntity.uaslSectionId)
        && Objects.equals(this.startAt, conformityAssessmentExecutionEntity.startAt)
        && Objects.equals(this.endAt, conformityAssessmentExecutionEntity.endAt)
        && Objects.equals(this.aircraftInfo, conformityAssessmentExecutionEntity.aircraftInfo);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uaslSectionId, startAt, endAt, aircraftInfo);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConformityAssessmentExecutionEntity {\n");
    sb.append("    uaslSectionId: ").append(toIndentedString(uaslSectionId)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    aircraftInfo: ").append(toIndentedString(aircraftInfo)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}


