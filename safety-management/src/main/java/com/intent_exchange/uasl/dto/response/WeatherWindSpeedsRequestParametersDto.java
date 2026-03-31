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

package com.intent_exchange.uasl.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.intent_exchange.uasl.dto.request.WeatherWindSpeedConformityAssessmentDto;
import lombok.Data;

/** RequestParametersDtoは、天候・風速データのリクエストパラメータを保持するdto */
@Data
public class WeatherWindSpeedsRequestParametersDto extends WeatherWindSpeedConformityAssessmentDto {

  /** 気象予測発表時間 (UTC) */
  @JsonProperty("Basetime")
  private String basetime;

  /** 気象予測対象時間 (UTC) */
  @JsonProperty("Validtime")
  private String validtime;

  /** 分 */
  @JsonProperty("Minute")
  private String minute;

  /** 気象要素 */
  @JsonProperty("Element")
  private String element;

  /** 標高 */
  @JsonProperty("Alt_surface")
  private String altSurface;

  /** 海面高度 */
  @JsonProperty("Altitude")
  private String altitude;

  /** グリッドサイズ */
  @JsonProperty("Grid")
  private String grid;

  /** 緯度間隔 */
  @JsonProperty("Lat_interval")
  private String latInterval;

  /** 経度間隔 */
  @JsonProperty("Lon_interval")
  private String lonInterval;

  /** 北西端緯度の基準値 */
  @JsonProperty("Lat_start_base")
  private String latStartBase;

  /** 北西端経度の基準値 */
  @JsonProperty("Lon_start_base")
  private String lonStartBase;

  /** 南東端緯度の基準値 */
  @JsonProperty("Lat_end_base")
  private String latEndBase;

  /** 南東端経度の基準値 */
  @JsonProperty("Lon_end_base")
  private String lonEndBase;

  /** 1マス250mの縦マス幅 */
  @JsonProperty("Height")
  private int height;

  /** 1マス250mの横マス幅 */
  @JsonProperty("Width")
  private int width;
}

