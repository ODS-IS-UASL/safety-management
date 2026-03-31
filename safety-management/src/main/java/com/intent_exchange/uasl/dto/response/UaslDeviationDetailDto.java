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

import java.io.Serializable;
import lombok.Data;
/** 航路逸脱情報を返すdto */
@Data
public class UaslDeviationDetailDto implements Serializable {

	/**
	 * 航路予約毎の識別ID
	 */
    private String uaslReservationId;
	/**
	 * 航路逸脱情報.逸脱割合
	 */
    private double routeDeviationRate;
    /**
     * 航路逸脱情報.逸脱量
     */
	private String routeDeviationAmount;
	/**
	 * 逸脱検知時刻
	 */
	private String routeDeviationTime;
	/**
	 * 航路運営者ID
	 */
	private String uaslAdministratorId;
	/**
	 * 運航事業者ID
	 */
	private String operatorId;
	/*
	 * 航路ID 
	 */
	private String uaslId;
	/*
	 *航路区画 ID 
	 */
	private String uaslSectionId;
	/*
	 * シリアルナンバー
	 */
	private String serialNumber;
	/*
	 * 登録ID
	 */
	private String registrationId;
	/*
	 * セッションID
	 */
	private String utmId;
	/*
	 * フライト識別ID
	 */
	private String specificSessionId;
	/**
	 * 逸脱検知情報.航路区画ID.逸脱発生地点の座標
	 */
	private String routeDeviationCoordinates;
	/**
	 * 固有のバージョン番号
	 */
	private static final long serialVersionUID = 1L;
}

