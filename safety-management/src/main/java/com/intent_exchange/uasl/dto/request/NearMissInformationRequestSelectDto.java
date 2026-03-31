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

package com.intent_exchange.uasl.dto.request;

import java.time.LocalDateTime;
import java.util.List;
import com.intent_exchange.uasl.entity.NearMissInformationRequestAttributesAreaInfo;
import lombok.Data;

/**
 * 対象予約、航路情報取得条件
 */
@Data
public class NearMissInformationRequestSelectDto {
  /**
   * エリア情報
   */
  private NearMissInformationRequestAttributesAreaInfo areaInfo;

  /**
   * 開始日時
   */
  private LocalDateTime startAt;

  /**
   * 終了日時
   */
  private LocalDateTime endAt;

  /**
   * 航路運営者ID
   */
  private String uaslAdministratorId;

  /**
   * 運航事業者ID
   */
  private String operatorId;

  /**
   * 航路ID
   */
  private String uaslId;

  /**
   * 航路区画のID
   */
  private String uaslSectionId;

  /**
   * 機体種別
   */
  private String uaType;

  /**
   * 座標
   */
  private String coordinates;

  /**
   * 座標情報からGeomTextへ変更
   * 
   * @param coord
   */
  public void setCoordinates(List<List<List<Double>>> coord) {
    if (coord == null || coord.size() == 0) {
      coordinates = "";
      return;
    }
    StringBuilder sb = new StringBuilder();
    sb.append("POLYGON((");

    for (List<List<Double>> ring : coord) {
      for (List<Double> point : ring) {
        sb.append(point.get(0)).append(" ").append(point.get(1)).append(", ");
      }
      // Remove the last comma and space
      sb.setLength(sb.length() - 2);
      sb.append("), (");
    }
    // Remove the last comma, space, and opening parenthesis
    sb.setLength(sb.length() - 3);
    sb.append(")");

    coordinates = sb.toString();
  }
}

