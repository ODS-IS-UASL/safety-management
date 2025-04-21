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

import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * データモデル要素
 */

@Schema(name = "NearMissInformationRequest_attributes", description = "データモデル要素")
@JsonTypeName("NearMissInformationRequest_attributes")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-18T19:35:22.332791300+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class NearMissInformationRequestAttributes {

  private NearMissInformationRequestAttributesAreaInfo areaInfo;
  
  // openApiGeneratorがString型で扱うことを想定した設定となっているため、時刻情報はString型で定義。
  // ※Near_miss_information.yamlからopenApiGeneratorでコード生成するとDate型となります。
  private String startAt;
  private String endAt;

  private String airwayAdministratorId;

  private String operatorId;

  private String airwayId;

  private String airwaySectionId;

  public NearMissInformationRequestAttributes() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public NearMissInformationRequestAttributes(NearMissInformationRequestAttributesAreaInfo areaInfo, String startAt, String endAt) {
    this.areaInfo = areaInfo;
    this.startAt = startAt;
    this.endAt = endAt;
  }

  public NearMissInformationRequestAttributes areaInfo(NearMissInformationRequestAttributesAreaInfo areaInfo) {
    this.areaInfo = areaInfo;
    return this;
  }

  /**
   * Get areaInfo
   * @return areaInfo
   */
  @NotNull@Valid 
  @Schema(name = "areaInfo", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("areaInfo")
  public NearMissInformationRequestAttributesAreaInfo getAreaInfo() {
    return areaInfo;
  }

  public void setAreaInfo(NearMissInformationRequestAttributesAreaInfo areaInfo) {
    this.areaInfo = areaInfo;
  }

  public NearMissInformationRequestAttributes startAt(String startAt) {
    this.startAt = startAt;
    return this;
  }

  /**
   * 開始日時
   * @return startAt
   */
  @NotNull@Valid 
  @Schema(name = "startAt", description = "開始日時", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("startAt")
  public String getStartAt() {
    return startAt;
  }

  public void setStartAt(String startAt) {
    this.startAt = startAt;
  }

  public NearMissInformationRequestAttributes endAt(String endAt) {
    this.endAt = endAt;
    return this;
  }

  /**
   * 終了日時
   * @return endAt
   */
  @NotNull@Valid 
  @Schema(name = "endAt", description = "終了日時", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("endAt")
  public String getEndAt() {
    return endAt;
  }

  public void setEndAt(String endAt) {
    this.endAt = endAt;
  }

  public NearMissInformationRequestAttributes airwayAdministratorId(String airwayAdministratorId) {
    this.airwayAdministratorId = airwayAdministratorId;
    return this;
  }

  /**
   * 航路運営者ID
   * @return airwayAdministratorId
   */
  
  @Schema(name = "airwayAdministratorId", example = "123e4567-e89b-12d3-a456-426614174000", description = "航路運営者ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayAdministratorId")
  public String getAirwayAdministratorId() {
    return airwayAdministratorId;
  }

  public void setAirwayAdministratorId(String airwayAdministratorId) {
    this.airwayAdministratorId = airwayAdministratorId;
  }

  public NearMissInformationRequestAttributes operatorId(String operatorId) {
    this.operatorId = operatorId;
    return this;
  }

  /**
   * 運航事業者ID
   * @return operatorId
   */
  
  @Schema(name = "operatorId", example = "123e4567-e89b-12d3-a456-426614174000", description = "運航事業者ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("operatorId")
  public String getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(String operatorId) {
    this.operatorId = operatorId;
  }

  public NearMissInformationRequestAttributes airwayId(String airwayId) {
    this.airwayId = airwayId;
    return this;
  }

  /**
   * 航路ID
   * @return airwayId
   */
  
  @Schema(name = "airwayId", example = "123e4567-e89b-12d3-a456-426614174000", description = "航路ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("airwayId")
  public String getAirwayId() {
    return airwayId;
  }

  public void setAirwayId(String airwayId) {
    this.airwayId = airwayId;
  }

  public NearMissInformationRequestAttributes airwaySectionId(String airwaySectionId) {
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    NearMissInformationRequestAttributes nearMissInformationRequestAttributes = (NearMissInformationRequestAttributes) o;
    return Objects.equals(this.areaInfo, nearMissInformationRequestAttributes.areaInfo) &&
        Objects.equals(this.startAt, nearMissInformationRequestAttributes.startAt) &&
        Objects.equals(this.endAt, nearMissInformationRequestAttributes.endAt) &&
        Objects.equals(this.airwayAdministratorId, nearMissInformationRequestAttributes.airwayAdministratorId) &&
        Objects.equals(this.operatorId, nearMissInformationRequestAttributes.operatorId) &&
        Objects.equals(this.airwayId, nearMissInformationRequestAttributes.airwayId) &&
        Objects.equals(this.airwaySectionId, nearMissInformationRequestAttributes.airwaySectionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(areaInfo, startAt, endAt, airwayAdministratorId, operatorId, airwayId, airwaySectionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NearMissInformationRequestAttributes {\n");
    sb.append("    areaInfo: ").append(toIndentedString(areaInfo)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    airwayAdministratorId: ").append(toIndentedString(airwayAdministratorId)).append("\n");
    sb.append("    operatorId: ").append(toIndentedString(operatorId)).append("\n");
    sb.append("    airwayId: ").append(toIndentedString(airwayId)).append("\n");
    sb.append("    airwaySectionId: ").append(toIndentedString(airwaySectionId)).append("\n");
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


