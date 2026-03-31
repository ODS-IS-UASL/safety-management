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

package com.intent_exchange.uasl.service;

import com.intent_exchange.uasl.dto.request.SearchActiveUaslReservationsRequestDto;
import com.intent_exchange.uasl.dto.response.ActiveReservationResponseDto;
import com.intent_exchange.uasl.dto.response.ActiveReservationSummaryDto;
import com.intent_exchange.uasl.entity.ActiveReservationResponse;
import com.intent_exchange.uasl.entity.SearchActiveUaslReservationsRequest;
import com.intent_exchange.uasl.logic.ActiveUaslReservationsLogic;
import com.intent_exchange.uasl.model.UaslReservation;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** L25 有効な予約情報の取得に関するサービス。 */
@Service
public class ActiveUaslReservationsService {

  @Autowired private ActiveUaslReservationsLogic logic;

  @Autowired private Clock clock;

  /**
   * 航路区画IDリストで有効な予約情報を検索し、レスポンスを生成します。
   *
   * @param request 検索リクエスト
   * @return レスポンス
   */
  public ActiveReservationResponse searchActiveUaslReservations(
      SearchActiveUaslReservationsRequest request) {
    SearchActiveUaslReservationsRequestDto requestDto = toRequestDto(request);
    LocalDateTime now = LocalDateTime.now(clock).atZone(ZoneOffset.UTC).toLocalDateTime();

    List<UaslReservation> reservations =
        logic.selectActiveReservations(requestDto.getUaslSectionIds(), now);
    List<ActiveReservationSummaryDto> summaryDtos = mapReservations(reservations);

    ActiveReservationResponseDto responseDto = new ActiveReservationResponseDto();
    responseDto.setCheckedAt(toDate(now));
    responseDto.setReservations(summaryDtos);
    return toResponseEntity(responseDto);
  }

  private SearchActiveUaslReservationsRequestDto toRequestDto(
      SearchActiveUaslReservationsRequest request) {
    SearchActiveUaslReservationsRequestDto dto = new SearchActiveUaslReservationsRequestDto();
    if (request != null && request.getUaslSectionIds() != null) {
      dto.setUaslSectionIds(request.getUaslSectionIds());
    } else {
      dto.setUaslSectionIds(Collections.emptyList());
    }
    return dto;
  }

  private List<ActiveReservationSummaryDto> mapReservations(List<UaslReservation> reservations) {
    if (reservations == null || reservations.isEmpty()) {
      return Collections.emptyList();
    }

    return reservations.stream()
        .filter(Objects::nonNull)
        .map(reservation -> toSummaryDto(reservation, resolveSectionIds(reservation)))
        .collect(Collectors.toList());
  }

  private ActiveReservationSummaryDto toSummaryDto(
      UaslReservation reservation, List<String> sectionIds) {
    ActiveReservationSummaryDto dto = new ActiveReservationSummaryDto();
    dto.setUaslReservationId(reservation.getUaslReservationId());
    dto.setStartAt(toDate(reservation.getStartAt()));
    dto.setEndAt(toDate(reservation.getEndAt()));
    dto.setOperatorId(reservation.getOperatorId());
    dto.setUaslSectionIds(sectionIds);
    return dto;
  }

  /**
   * UaslReservationから区画IDリストを抽出し、すべての要素をString型に変換して返します。
   *
   * <p>注意: UaslReservation#getUaslSectionIds()はジェネリクス指定のないraw List型を返すため、
   * このメソッドでは型安全性を担保するために防御的な型チェックと変換を行っています。 リストがnullまたは空、あるいはList型でない場合は空リストを返します。
   * この実装は、元データがDBや外部からのマッピング・デシリアライズ等で要素型が不明・混在する可能性があるため、 下流処理で必ずList<String>として扱えるようにする目的です。
   *
   * @param reservation 区画ID（raw Listの可能性あり）を保持する予約エンティティ
   * @return 区画IDのList<String>。取得不可やList型でない場合は空リスト
   */
  private List<String> resolveSectionIds(UaslReservation reservation) {
    Object sectionIdsObj = reservation == null ? null : reservation.getUaslSectionIds();
    return toSectionIdList(sectionIdsObj);
  }

  private List<String> toSectionIdList(Object sectionIdsObj) {
    if (!(sectionIdsObj instanceof List<?> rawList) || rawList.isEmpty()) {
      return Collections.emptyList();
    }
    return rawList.stream().filter(Objects::nonNull).map(Object::toString).toList();
  }

  private Date toDate(LocalDateTime dateTime) {
    if (dateTime == null) {
      return null;
    }
    return Date.from(dateTime.toInstant(ZoneOffset.UTC));
  }

  private ActiveReservationResponse toResponseEntity(ActiveReservationResponseDto dto) {
    ActiveReservationResponse response = ModelMapperUtil.map(dto, ActiveReservationResponse.class);
    if (response.getReservations() == null) {
      response.setReservations(Collections.emptyList());
    }
    return response;
  }
}
