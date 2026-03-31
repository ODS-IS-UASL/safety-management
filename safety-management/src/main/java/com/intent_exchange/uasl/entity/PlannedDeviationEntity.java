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

import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 計画的な航路逸脱の設定情報
 */

@Schema(name = "PlannedDeviationEntity", description = "計画的な航路逸脱の設定情報")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-12-02T15:00:01.104906100+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class PlannedDeviationEntity {

  private Boolean enabled;

  public PlannedDeviationEntity() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public PlannedDeviationEntity(Boolean enabled) {
    this.enabled = enabled;
  }

  public PlannedDeviationEntity enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * 計画的な航路逸脱の指示。 - true: 計画的な逸脱として扱う - false: 通常の逸脱として扱う 
   * @return enabled
   */
  @NotNull
  @Schema(name = "enabled", example = "true", description = "計画的な航路逸脱の指示。 - true: 計画的な逸脱として扱う - false: 通常の逸脱として扱う ", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlannedDeviationEntity plannedDeviationEntity = (PlannedDeviationEntity) o;
    return Objects.equals(this.enabled, plannedDeviationEntity.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlannedDeviationEntity {\n");
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
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
