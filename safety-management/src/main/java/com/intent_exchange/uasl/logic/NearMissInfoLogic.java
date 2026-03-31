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

package com.intent_exchange.uasl.logic;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.intent_exchange.uasl.dao.UaslDeviationMapper;
import com.intent_exchange.uasl.dao.MonitoringInformationMapper;
import com.intent_exchange.uasl.dto.request.UaslDeviationDto;
import com.intent_exchange.uasl.dto.request.MonitoringInformationDto;
import com.intent_exchange.uasl.dto.request.NotifyUpdatesNearMissInfoDto;
import com.intent_exchange.uasl.model.UaslDeviation;
import com.intent_exchange.uasl.model.MonitoringInformation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 航路の安全管理（SafetyManagement）管理のヒヤリハット情報に関するロジック<br>
 * TODO:
 * （ヒヤリハット情報登録）、（ヒヤリハット情報取得）の実装とコンフリクトするかもしれない。ヒヤリハット情報操作ロジックとしてまとめるなど、マージ時要検討。<br>
 */
@Component
public class NearMissInfoLogic {

  /**
   * ヒヤリハット情報（航路逸脱情報）操作用Mapper
   */
  @Autowired
  private UaslDeviationMapper uaslDeviationMapper;

  /**
   * ヒヤリハット情報（第三者立入情報）操作用Mapper
   */
  @Autowired
  private MonitoringInformationMapper monitoringInformationMapper;

  // Clockインスタンス。現在の時刻を取得するために使用します。
  @Autowired
  private Clock clock;

  /**
   * 指定期間内のヒヤリハット情報有無を確認し、<br>
   * key:航路運営者ID value:ヒヤリハット情報更新通知用DTO のMapで返す<br>
   * 
   * @param startAt 抽出対象期間（開始日時）
   * @param endAt 抽出対象期間（終了日時）
   * @return key:航路運営者ID value:ヒヤリハット情報更新通知用DTO のMap
   */
  public Map<String, NotifyUpdatesNearMissInfoDto> getAdminIdNearMissInfoMap(LocalDateTime startAt,
      LocalDateTime endAt) {
    // key:航路運営者ID value:ヒヤリハット情報更新通知用DTO のMap
    Map<String, NotifyUpdatesNearMissInfoDto> adminIdNearMissInfoMap = new HashMap<>();

    // 航路逸脱情報
    List<UaslDeviationDto> deviationList = getDeviationByStartEnd(startAt, endAt);
    deviationList.forEach(deviation -> {
      // 航路運営者IDをキーにMapから通知用DTOを取得
      NotifyUpdatesNearMissInfoDto nearMissInfoDto = getNotifyDtoValueFromMap(
          adminIdNearMissInfoMap, deviation.getUaslAdministratorId(), startAt, endAt);
      // 航路逸脱を検知した航路IDリストを更新（未登録のIDの場合に追加）
      if (!nearMissInfoDto.getDeviantUaslIds().contains(deviation.getUaslId())) {
        nearMissInfoDto.getDeviantUaslIds().add(deviation.getUaslId());
      }
    });

    // 第三者立入情報
    List<MonitoringInformationDto> monitoringInfoList = getMonitoringInfoByStartEnd(startAt, endAt);
    monitoringInfoList.forEach(monitoringInfo -> {
      // 航路運営者IDでMapから通知用DTOを取得
      NotifyUpdatesNearMissInfoDto nearMissInfoDto = getNotifyDtoValueFromMap(
          adminIdNearMissInfoMap, monitoringInfo.getUaslAdministratorId(), startAt, endAt);
      // 第三者立入を検知した航路IDリストを更新（未登録のIDの場合に追加）
      if (!nearMissInfoDto.getEntryUaslIds().contains(monitoringInfo.getUaslId())) {
        nearMissInfoDto.getEntryUaslIds().add(monitoringInfo.getUaslId());
      }
    });

    return adminIdNearMissInfoMap;
  }

  /**
   * 引数に指定した航路運営者IDをキーに、Mapから通知用DTOを取得。<br>
   * Mapに存在しない場合は新規登録したものを返却。
   * 
   * @param adminIdNearMissInfoMap 航路運営者IDをキーとするヒヤリハット情報更新通知用DTOのマップ
   * @param adminId 航路運営者ID
   * @param startAt ヒヤリハット情報収集開始日時 ※Mapに存在しない場合に新規DTOに設定
   * @param endAt ヒヤリハット情報収集終了日時 ※Mapに存在しない場合に新規DTOに設定
   * @return ヒヤリハット情報更新通知用DTO
   */
  private NotifyUpdatesNearMissInfoDto getNotifyDtoValueFromMap(
      Map<String, NotifyUpdatesNearMissInfoDto> adminIdNearMissInfoMap, String adminId,
      LocalDateTime startAt, LocalDateTime endAt) {
    // 航路運営者IDでMapから通知用DTOを取得
    NotifyUpdatesNearMissInfoDto nearMissInfoDto = adminIdNearMissInfoMap.get(adminId);
    // Mapに存在しない場合は通知用DTOを新規作成
    if (nearMissInfoDto == null) {
      nearMissInfoDto = new NotifyUpdatesNearMissInfoDto();
      nearMissInfoDto.setReportingStartAt(
          startAt.atZone(clock.getZone()).format(DateTimeFormatter.ISO_INSTANT));
      nearMissInfoDto
          .setReportingEndAt(endAt.atZone(clock.getZone()).format(DateTimeFormatter.ISO_INSTANT));
      adminIdNearMissInfoMap.put(adminId, nearMissInfoDto);
    }
    return nearMissInfoDto;
  }

  /**
   * 指定期間中に運航が完了した（※）予約のヒヤリハット（航路逸脱）情報を抽出する<br>
   * ※航路予約情報の予約終了日時が指定の期間内である<br>
   * TODO: ヒヤリハット情報テーブル操作用としてLogic切り出しが必要か要検討。
   *
   * @param startAt 抽出対象期間（開始日時）
   * @param endAt 抽出対象期間（終了日時）
   * @return ヒヤリハット情報（航路逸脱情報）DTOリスト
   */
  @Transactional
  public List<UaslDeviationDto> getDeviationByStartEnd(LocalDateTime startAt,
      LocalDateTime endAt) {
    List<UaslDeviation> deviationList =
        uaslDeviationMapper.selectByReservationEndAt(startAt, endAt);
    // EntityリストからDTOリストに変換
    return ModelMapperUtil.mapList(deviationList, UaslDeviationDto.class);
  }

  /**
   * 指定期間中に運航が完了した（※）予約のヒヤリハット（第三者立入）情報を抽出する<br>
   * ※航路予約情報の予約終了日時が指定の期間内である<br>
   * TODO: ヒヤリハット情報テーブル操作用としてLogic切り出しが必要か要検討。
   * 
   * @param startAt 抽出対象期間（開始日時）
   * @param endAt 抽出対象期間（終了日時）
   * @return ヒヤリハット情報（第三者立入情報）DTOリスト
   */
  @Transactional
  public List<MonitoringInformationDto> getMonitoringInfoByStartEnd(LocalDateTime startAt,
      LocalDateTime endAt) {
    List<MonitoringInformation> monitoringInfoList =
        monitoringInformationMapper.selectByReservationEndAt(startAt, endAt);
    // EntityリストからDTOリストに変換
    return ModelMapperUtil.mapList(monitoringInfoList, MonitoringInformationDto.class);
  }
}

