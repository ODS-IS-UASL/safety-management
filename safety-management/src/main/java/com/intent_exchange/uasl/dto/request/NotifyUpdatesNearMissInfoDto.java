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
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/** ヒヤリハット情報更新通知用DTO */
@Data
public class NotifyUpdatesNearMissInfoDto {
  
  /** 航路逸脱を検知した航路IDリスト */
  private List<String> deviantUaslIds = new ArrayList<>();
  
  /** 第三者立入を検知した航路IDリスト */
  private List<String> entryUaslIds = new ArrayList<>();
  
  /** ヒヤリハット情報収集開始日時 */
  // TODO: ModelMapperUtilでDTO→JSON文字列変換に対応したらLocalDateTime型への変更を検討
  private String reportingStartAt;
  
  /** ヒヤリハット情報収集終了日時 */
  // TODO: ModelMapperUtilでDTO→JSON文字列変換に対応したらLocalDateTime型への変更を検討
  private String reportingEndAt;

}

