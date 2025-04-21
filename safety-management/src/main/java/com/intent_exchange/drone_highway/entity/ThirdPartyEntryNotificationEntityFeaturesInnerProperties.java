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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import jakarta.annotation.Generated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.intent_exchange.drone_highway.annotation.DateFormat;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ThirdPartyEntryNotificationEntityFeaturesInnerProperties
 */

@JsonTypeName("ThirdPartyEntryNotificationEntity_features_inner_properties")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen",
    date = "2025-02-07T18:14:02.470725+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class ThirdPartyEntryNotificationEntityFeaturesInnerProperties {

  private String area;

  @DateFormat
  private Date timestamp;

  /**
   * 侵入状態（0：侵入なし、1：侵入あり）
   */
  public enum IntrusionStatusEnum {
    NUMBER_0(0),

    NUMBER_1(1);

    private Integer value;

    IntrusionStatusEnum(Integer value) {
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
    public static IntrusionStatusEnum fromValue(Integer value) {
      for (IntrusionStatusEnum b : IntrusionStatusEnum.values()) {
        if (b.value.equals(value)) {
          return b;
        }
      }
      throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
  }

  private IntrusionStatusEnum intrusionStatus;

  @Valid
  private List<@Valid ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner> traffics =
      new ArrayList<>();

  public ThirdPartyEntryNotificationEntityFeaturesInnerProperties area(String area) {
    this.area = area;
    return this;
  }

  /**
   * 監視エリア名
   * 
   * @return area
   */
  @Size(min = 1, max = 64)
  @Schema(name = "area", description = "監視エリア名", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("area")
  public String getArea() {
    return area;
  }

  public void setArea(String area) {
    this.area = area;
  }

  public ThirdPartyEntryNotificationEntityFeaturesInnerProperties timestamp(Date timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * 送信時刻
   * 
   * @return timestamp
   */
  @Valid
  @Schema(name = "timestamp", description = "送信時刻", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timestamp")
  public Date getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Date timestamp) {
    this.timestamp = timestamp;
  }

  public ThirdPartyEntryNotificationEntityFeaturesInnerProperties intrusionStatus(
      IntrusionStatusEnum intrusionStatus) {
    this.intrusionStatus = intrusionStatus;
    return this;
  }

  /**
   * 侵入状態（0：侵入なし、1：侵入あり）
   * 
   * @return intrusionStatus
   */

  @Schema(name = "intrusionStatus", description = "侵入状態（0：侵入なし、1：侵入あり）",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("intrusionStatus")
  public IntrusionStatusEnum getIntrusionStatus() {
    return intrusionStatus;
  }

  public void setIntrusionStatus(IntrusionStatusEnum intrusionStatus) {
    this.intrusionStatus = intrusionStatus;
  }

  public ThirdPartyEntryNotificationEntityFeaturesInnerProperties traffics(
      List<@Valid ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner> traffics) {
    this.traffics = traffics;
    return this;
  }

  public ThirdPartyEntryNotificationEntityFeaturesInnerProperties addTrafficsItem(
      ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner trafficsItem) {
    if (this.traffics == null) {
      this.traffics = new ArrayList<>();
    }
    this.traffics.add(trafficsItem);
    return this;
  }

  /**
   * 侵入検知対象の情報を配列で指定する。 ■送信条件： ・侵入ありの場合：継続的に送信を続けます。 ・侵入なしの場合： - 内部では traffics.type:0 を設定（内部保持用） -
   * 外部送信時の挙動： 1. 侵入状態が1→0に変化した際、最後に検知された種別(1,2,3,4)で count:0 を1回だけ送信 2. その後、新たな侵入が検知されるまでデータは送信されない
   * 
   * @return traffics
   */
  @Valid
  @Schema(name = "traffics",
      description = "侵入検知対象の情報を配列で指定する。 ■送信条件： ・侵入ありの場合：継続的に送信を続けます。 ・侵入なしの場合：   - 内部では traffics.type:0 を設定（内部保持用）   - 外部送信時の挙動：     1. 侵入状態が1→0に変化した際、最後に検知された種別(1,2,3,4)で count:0 を1回だけ送信     2. その後、新たな侵入が検知されるまでデータは送信されない ",
      requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("traffics")
  public List<@Valid ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner> getTraffics() {
    return traffics;
  }

  public void setTraffics(
      List<@Valid ThirdPartyEntryNotificationEntityFeaturesInnerPropertiesTrafficsInner> traffics) {
    this.traffics = traffics;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ThirdPartyEntryNotificationEntityFeaturesInnerProperties thirdPartyEntryNotificationEntityFeaturesInnerProperties =
        (ThirdPartyEntryNotificationEntityFeaturesInnerProperties) o;
    return Objects.equals(this.area, thirdPartyEntryNotificationEntityFeaturesInnerProperties.area)
        && Objects.equals(this.timestamp,
            thirdPartyEntryNotificationEntityFeaturesInnerProperties.timestamp)
        && Objects.equals(this.intrusionStatus,
            thirdPartyEntryNotificationEntityFeaturesInnerProperties.intrusionStatus)
        && Objects.equals(this.traffics,
            thirdPartyEntryNotificationEntityFeaturesInnerProperties.traffics);
  }

  @Override
  public int hashCode() {
    return Objects.hash(area, timestamp, intrusionStatus, traffics);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ThirdPartyEntryNotificationEntityFeaturesInnerProperties {\n");
    sb.append("    area: ").append(toIndentedString(area)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    intrusionStatus: ").append(toIndentedString(intrusionStatus)).append("\n");
    sb.append("    traffics: ").append(toIndentedString(traffics)).append("\n");
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


