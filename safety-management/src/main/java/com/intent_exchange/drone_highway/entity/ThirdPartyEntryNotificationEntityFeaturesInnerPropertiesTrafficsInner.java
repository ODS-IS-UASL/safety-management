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

import java.util.Date;
import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.intent_exchange.drone_highway.annotation.DateFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner
 */

@JsonTypeName("ThirdPartyEntryNotificationEntity_features_inner_properties_traffics_inner")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2025-02-07T18:14:02.470725+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner {

  @DateFormat
  private Date currentTime;

  /**
   * 侵入検知対象の種別: 無：0 （内部保持用。外部送信時は使用しない） 車：1 自転車：2 人：3 バイク：4 ※ 侵入なしの状態では、最後に検知された種別(1,2,3,4)を使用し
   * count:0 として送信
   */
  public enum TypeEnum {
    NUMBER_0(0),

    NUMBER_1(1),

    NUMBER_2(2),

    NUMBER_3(3),

    NUMBER_4(4);

    private Integer value;

    TypeEnum(Integer value) {
      this.value = value;
    }

    @JsonValue
    public Integer getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(Integer value) {
      for (TypeEnum b : TypeEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private TypeEnum type;

  private Integer count;

  public ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner currentTime(
      Date currentTime) {
    this.currentTime = currentTime;
    return this;
  }

  /**
   * 侵入検知対象がいる時刻
   * 
   * @return currentTime
   */
  @Valid
  @Schema(name = "currentTime", description = "侵入検知対象がいる時刻",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentTime")
  public Date getCurrentTime() {
    return currentTime;
  }

  public void setCurrentTime(Date currentTime) {
    this.currentTime = currentTime;
  }

  public ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * 侵入検知対象の種別: 無：0 （内部保持用。外部送信時は使用しない） 車：1 自転車：2 人：3 バイク：4 ※ 侵入なしの状態では、最後に検知された種別(1,2,3,4)を使用し
   * count:0 として送信
   * 
   * @return type
   */

  @Schema(name = "type",
      description = "侵入検知対象の種別: 無：0 （内部保持用。外部送信時は使用しない） 車：1 自転車：2 人：3 バイク：4 ※ 侵入なしの状態では、最後に検知された種別(1,2,3,4)を使用し count:0 として送信 ",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner count(
      Integer count) {
    this.count = count;
    return this;
  }

  /**
   * 侵入検知対象の検出数 minimum: 0 maximum: 100
   * 
   * @return count
   */
  @Min(0)
  @Max(100)
  @Schema(name = "count", description = "侵入検知対象の検出数",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("count")
  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner thirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner =
        (ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner) o;
    return Objects.equals(this.currentTime,
        thirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner.currentTime)
        && Objects.equals(this.type,
            thirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner.type)
        && Objects.equals(this.count,
            thirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentTime, type, count);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner {\n");
    sb.append("    currentTime: ").append(toIndentedString(currentTime)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
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


