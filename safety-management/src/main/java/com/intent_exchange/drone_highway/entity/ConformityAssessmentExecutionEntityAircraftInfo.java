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
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * 機体情報
 */

@Schema(name = "ConformityAssessmentExecutionEntity_aircraftInfo", description = "機体情報")
@JsonTypeName("ConformityAssessmentExecutionEntity_aircraftInfo")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-05T16:53:06.485800400+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class ConformityAssessmentExecutionEntityAircraftInfo {

  private String maker;

  private String modelNumber;

  private String name;

  private String type;

  private BigDecimal length;

  public ConformityAssessmentExecutionEntityAircraftInfo maker(String maker) {
    this.maker = maker;
    return this;
  }

  /**
   * 製造メーカー名
   * @return maker
   */
  
  @Schema(name = "maker", description = "製造メーカー名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("maker")
  public String getMaker() {
    return maker;
  }

  public void setMaker(String maker) {
    this.maker = maker;
  }

  public ConformityAssessmentExecutionEntityAircraftInfo modelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
    return this;
  }

  /**
   * 型式（モデル）
   * @return modelNumber
   */
  
  @Schema(name = "modelNumber", description = "型式（モデル）", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("modelNumber")
  public String getModelNumber() {
    return modelNumber;
  }

  public void setModelNumber(String modelNumber) {
    this.modelNumber = modelNumber;
  }

  public ConformityAssessmentExecutionEntityAircraftInfo name(String name) {
    this.name = name;
    return this;
  }

  /**
   * 機種名
   * @return name
   */
  
  @Schema(name = "name", description = "機種名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ConformityAssessmentExecutionEntityAircraftInfo type(String type) {
    this.type = type;
    return this;
  }

  /**
   * 機体種別
   * @return type
   */
  
  @Schema(name = "type", description = "機体種別", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ConformityAssessmentExecutionEntityAircraftInfo length(BigDecimal length) {
    this.length = length;
    return this;
  }

  /**
   * 機体長
   * @return length
   */
  @Valid 
  @Schema(name = "length", description = "機体長", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("length")
  public BigDecimal getLength() {
    return length;
  }

  public void setLength(BigDecimal length) {
    this.length = length;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConformityAssessmentExecutionEntityAircraftInfo conformityAssessmentExecutionEntityAircraftInfo = (ConformityAssessmentExecutionEntityAircraftInfo) o;
    return Objects.equals(this.maker, conformityAssessmentExecutionEntityAircraftInfo.maker) &&
        Objects.equals(this.modelNumber, conformityAssessmentExecutionEntityAircraftInfo.modelNumber) &&
        Objects.equals(this.name, conformityAssessmentExecutionEntityAircraftInfo.name) &&
        Objects.equals(this.type, conformityAssessmentExecutionEntityAircraftInfo.type) &&
        Objects.equals(this.length, conformityAssessmentExecutionEntityAircraftInfo.length);
  }

  @Override
  public int hashCode() {
    return Objects.hash(maker, modelNumber, name, type, length);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConformityAssessmentExecutionEntityAircraftInfo {\n");
    sb.append("    maker: ").append(toIndentedString(maker)).append("\n");
    sb.append("    modelNumber: ").append(toIndentedString(modelNumber)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    length: ").append(toIndentedString(length)).append("\n");
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


