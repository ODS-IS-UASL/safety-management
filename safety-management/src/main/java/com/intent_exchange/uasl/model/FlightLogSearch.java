/*
 * Copyright 2026 Intent Exchange, Inc.
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

package com.intent_exchange.uasl.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * フライトログ検索用の条件モデル
 * MyBatisのMapperに渡すパラメータとして使用します。
 */
@Data
public class FlightLogSearch {
    /** 検索開始日時 */
    private LocalDateTime startTime;

    /** 検索終了日時 */
    private LocalDateTime endTime;

    /** 航路予約ID */
    private String reservationId;

    /** 航路ID */
    private String uaslId;

    /** 運航者ID */
    private String operatorId;

    /** 機体情報ID */
    private Integer aircraftInfoId;

    /** 機体種別 */
    private String uaType;
}
