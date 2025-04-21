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

package com.intent_exchange.drone_highway.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 適合性評価に使用する航路設計(A-1-2)より取得する鉄道との交点情報のDTOです。
 */
@Data
public class AirwayDesignRailwayCrossingInfoDto {

  /** 航路ID */
  @JsonProperty("airway_id")
  private String airwayId;

  /** 駅名1 */
  private String station1;

  /** 駅名2 */
  private String station2;

  /** 相対値 */
  @JsonProperty("relative_value")
  private String relativeValue;

  /** 登録日 */
  @JsonProperty("created_at")
  private String createdAt;

  /** 更新日 */
  @JsonProperty("updated_at")
  private String updatedAt;

}

