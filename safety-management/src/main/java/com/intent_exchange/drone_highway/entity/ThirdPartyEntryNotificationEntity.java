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
import com.intent_exchange.drone_highway.entity.ThirdPartyEntryNotificationEntityFeaturesInner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * 第三者立入監視情報のエンティティ
 */

@Schema(name = "ThirdPartyEntryNotificationEntity", description = "第三者立入監視情報のエンティティ")
@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-01-31T09:51:12.548410800+09:00[Asia/Tokyo]", comments = "Generator version: 7.8.0")
public class ThirdPartyEntryNotificationEntity {

  private String type;

  @Valid
  private List<@Valid ThirdPartyEntryNotificationEntityFeaturesInner> features = new ArrayList<>();

  public ThirdPartyEntryNotificationEntity type(String type) {
    this.type = type;
    return this;
  }

  /**
   * FeatureCollectionを指定
   * @return type
   */
  
  @Schema(name = "type", description = "FeatureCollectionを指定", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("type")
  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public ThirdPartyEntryNotificationEntity features(List<@Valid ThirdPartyEntryNotificationEntityFeaturesInner> features) {
    this.features = features;
    return this;
  }

  public ThirdPartyEntryNotificationEntity addFeaturesItem(ThirdPartyEntryNotificationEntityFeaturesInner featuresItem) {
    if (this.features == null) {
      this.features = new ArrayList<>();
    }
    this.features.add(featuresItem);
    return this;
  }

  /**
   * 監視エリアおよび侵入検知対象の情報
   * @return features
   */
  @Valid @Size(min = 1, max = 99) 
  @Schema(name = "features", description = "監視エリアおよび侵入検知対象の情報", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("features")
  public List<@Valid ThirdPartyEntryNotificationEntityFeaturesInner> getFeatures() {
    return features;
  }

  public void setFeatures(List<@Valid ThirdPartyEntryNotificationEntityFeaturesInner> features) {
    this.features = features;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ThirdPartyEntryNotificationEntity thirdPartyEntryNotificationEntity = (ThirdPartyEntryNotificationEntity) o;
    return Objects.equals(this.type, thirdPartyEntryNotificationEntity.type) &&
        Objects.equals(this.features, thirdPartyEntryNotificationEntity.features);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, features);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ThirdPartyEntryNotificationEntity {\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    features: ").append(toIndentedString(features)).append("\n");
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


