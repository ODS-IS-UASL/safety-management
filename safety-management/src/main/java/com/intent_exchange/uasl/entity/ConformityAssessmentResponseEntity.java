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
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * 適合性評価実行結果
 */

@Schema(name = "ConformityAssessmentResponseEntity", description = "適合性評価実行結果")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-02-05T16:53:06.485800400+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class ConformityAssessmentResponseEntity {

  private String evaluationResults;

  /**
   * 適合性評価結果がNGの場合の種別 \"weather\": 天候・風速条件によるNG \"event\": 規制／イベントによるNG \"railway\": 鉄道運行によるNG \"intrusion\": 第三者立入によるNG ※evaluationResultsがfalseの場合に参照 
   */
  public enum TypeEnum {
    WEATHER("weather"),
    
    EVENT("event"),
    
    RAILWAY("railway"),
    
    INTRUSION("intrusion"),
    
    NULL("null");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private String reasons;

  public ConformityAssessmentResponseEntity evaluationResults(String evaluationResults) {
    this.evaluationResults = evaluationResults;
    return this;
  }

  /**
   * 適合性評価結果 true : 適合性評価結果OK false: 適合性評価結果NG 
   * @return evaluationResults
   */
  
  @Schema(name = "evaluationResults", example = "false", description = "適合性評価結果 true : 適合性評価結果OK false: 適合性評価結果NG ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("evaluationResults")
  public String getEvaluationResults() {
    return evaluationResults;
  }

  public void setEvaluationResults(String evaluationResults) {
    this.evaluationResults = evaluationResults;
  }

  public ConformityAssessmentResponseEntity type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * 適合性評価結果がNGの場合の種別 \"weather\": 天候・風速条件によるNG \"event\": 規制／イベントによるNG \"railway\": 鉄道運行によるNG \"intrusion\": 第三者立入によるNG ※evaluationResultsがfalseの場合に参照 
   * @return type
   */
  
  @Schema(name = "type", example = "weather", description = "適合性評価結果がNGの場合の種別 \"weather\": 天候・風速条件によるNG \"event\": 規制／イベントによるNG \"railway\": 鉄道運行によるNG \"intrusion\": 第三者立入によるNG ※evaluationResultsがfalseの場合に参照 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public ConformityAssessmentResponseEntity reasons(String reasons) {
    this.reasons = reasons;
    return this;
  }

  /**
   * 適合性評価結果がNGの場合の詳細 ※evaluationResultsがfalseの場合に参照 
   * @return reasons
   */
  
  @Schema(name = "reasons", example = "風速条件範囲内判定エラー", description = "適合性評価結果がNGの場合の詳細 ※evaluationResultsがfalseの場合に参照 ", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("reasons")
  public String getReasons() {
    return reasons;
  }

  public void setReasons(String reasons) {
    this.reasons = reasons;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ConformityAssessmentResponseEntity conformityAssessmentResponseEntity = (ConformityAssessmentResponseEntity) o;
    return Objects.equals(this.evaluationResults, conformityAssessmentResponseEntity.evaluationResults) &&
        Objects.equals(this.type, conformityAssessmentResponseEntity.type) &&
        Objects.equals(this.reasons, conformityAssessmentResponseEntity.reasons);
  }

  @Override
  public int hashCode() {
    return Objects.hash(evaluationResults, type, reasons);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ConformityAssessmentResponseEntity {\n");
    sb.append("    evaluationResults: ").append(toIndentedString(evaluationResults)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    reasons: ").append(toIndentedString(reasons)).append("\n");
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


