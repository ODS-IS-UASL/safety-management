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

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

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

  private String uaslAdministratorId;

  private String operatorId;

  private String uaslId;

  private String uaslSectionId;

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

  public NearMissInformationRequestAttributes uaslAdministratorId(String uaslAdministratorId) {
    this.uaslAdministratorId = uaslAdministratorId;
    return this;
  }

  /**
   * 航路運営者ID
   * @return uaslAdministratorId
   */
  
  @Schema(name = "uaslAdministratorId", example = "123e4567-e89b-12d3-a456-426614174000", description = "航路運営者ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslAdministratorId")
  public String getUaslAdministratorId() {
    return uaslAdministratorId;
  }

  public void setUaslAdministratorId(String uaslAdministratorId) {
    this.uaslAdministratorId = uaslAdministratorId;
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

  public NearMissInformationRequestAttributes uaslId(String uaslId) {
    this.uaslId = uaslId;
    return this;
  }

  /**
   * 航路ID
   * @return uaslId
   */
  
  @Schema(name = "uaslId", example = "123e4567-e89b-12d3-a456-426614174000", description = "航路ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslId")
  public String getUaslId() {
    return uaslId;
  }

  public void setUaslId(String uaslId) {
    this.uaslId = uaslId;
  }

  public NearMissInformationRequestAttributes uaslSectionId(String uaslSectionId) {
    this.uaslSectionId = uaslSectionId;
    return this;
  }

  /**
   * 航路区画のID
   * @return uaslSectionId
   */
  
  @Schema(name = "uaslSectionId", example = "123e4567-e89b-12d3-a456-426614174000", description = "航路区画のID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("uaslSectionId")
  public String getUaslSectionId() {
    return uaslSectionId;
  }

  public void setUaslSectionId(String uaslSectionId) {
    this.uaslSectionId = uaslSectionId;
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
        Objects.equals(this.uaslAdministratorId, nearMissInformationRequestAttributes.uaslAdministratorId) &&
        Objects.equals(this.operatorId, nearMissInformationRequestAttributes.operatorId) &&
        Objects.equals(this.uaslId, nearMissInformationRequestAttributes.uaslId) &&
        Objects.equals(this.uaslSectionId, nearMissInformationRequestAttributes.uaslSectionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(areaInfo, startAt, endAt, uaslAdministratorId, operatorId, uaslId, uaslSectionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class NearMissInformationRequestAttributes {\n");
    sb.append("    areaInfo: ").append(toIndentedString(areaInfo)).append("\n");
    sb.append("    startAt: ").append(toIndentedString(startAt)).append("\n");
    sb.append("    endAt: ").append(toIndentedString(endAt)).append("\n");
    sb.append("    uaslAdministratorId: ").append(toIndentedString(uaslAdministratorId)).append("\n");
    sb.append("    operatorId: ").append(toIndentedString(operatorId)).append("\n");
    sb.append("    uaslId: ").append(toIndentedString(uaslId)).append("\n");
    sb.append("    uaslSectionId: ").append(toIndentedString(uaslSectionId)).append("\n");
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


