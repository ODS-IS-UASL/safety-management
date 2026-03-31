package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;

import com.intent_exchange.uasl.config.TestConfig;
import com.intent_exchange.uasl.dao.MonitoringInformationMapper;
import com.intent_exchange.uasl.dao.UaslDeviationMapper;
import com.intent_exchange.uasl.dto.request.MonitoringInformationDto;
import com.intent_exchange.uasl.dto.request.NotifyUpdatesNearMissInfoDto;
import com.intent_exchange.uasl.dto.request.UaslDeviationDto;
import com.intent_exchange.uasl.model.MonitoringInformation;
import com.intent_exchange.uasl.model.UaslDeviation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * NearMissInfoLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class NearMissInfoLogicTest extends NearMissInfoLogic {

  /** UaslDeviationMapperクラスのMock */
  @Mock
  private UaslDeviationMapper uaslDeviationMapper;

  /** MonitoringInformationMapperクラスのMock */
  @Mock
  private MonitoringInformationMapper monitoringInformationMapper;

  @Mock
  private Clock clock; // Clockクラスのモック

  /** テスト対象クラス */
  @InjectMocks
  private NearMissInfoLogic nearMissInfoLogic;

  @Test
  @DisplayName("ヒヤリハット情報無し")
  void testGetAdminIdNearMissInfoMap1() {
    // テスト対象メソッド呼び出し用の引数
    LocalDateTime startAt = LocalDateTime.of(2024, 12, 1, 0, 0, 0);
    LocalDateTime endAt = LocalDateTime.of(2024, 12, 31, 23, 59, 59);

    // ヒヤリハット(航路逸脱)情報のDB取得をMock化（空の航路逸脱情報を返す）
    when(uaslDeviationMapper.selectByReservationEndAt(startAt, endAt))
        .thenReturn(new ArrayList<UaslDeviation>());
    // ヒヤリハット(第三者立入)情報のDB取得をMock化（空の第三者立入情報を返す）
    when(monitoringInformationMapper.selectByReservationEndAt(startAt, endAt))
        .thenReturn(new ArrayList<MonitoringInformation>());

    // テスト対象メソッドの実行
    Map<String, NotifyUpdatesNearMissInfoDto> result =
        nearMissInfoLogic.getAdminIdNearMissInfoMap(startAt, endAt);

    // 結果の検証
    // ヒヤリハット情報が無いため、空のMapが返却される
    assertNotNull(result);
    assertEquals(0, result.size());
  }

  @Test
  @DisplayName("ヒヤリハット情報有り")
  void testGetAdminIdNearMissInfoMap2() {

    // TODO: TestConfigを正しく適用できれば、ModelMapperUtilのMock化が不要となる可能性あり。
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      // ModelMapperUtilのmapListメソッドをMock化
      // UaslDeviationリスト→UaslDeviationDtoリストへの変換処理をMock化
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(any(List.class), eq(UaslDeviationDto.class)))
          .thenAnswer(invocation -> {
            List<UaslDeviation> list = invocation.getArgument(0);
            return list.stream()
                .map(deviation -> createUaslDeviationTestDto(deviation.getUaslReservationId(),
                    deviation.getUaslAdministratorId(), deviation.getUaslId()))
                .collect(Collectors.toList());
          });
      // MonitoringInformationリスト→MonitoringInformationDtoリストへの変換処理をMock化
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(any(List.class), eq(MonitoringInformationDto.class)))
          .thenAnswer(invocation -> {
            List<MonitoringInformation> list = invocation.getArgument(0);
            return list.stream()
                .map(monitoringInfo -> createMonitoringInformationTestDto(
                    monitoringInfo.getUaslReservationId(),
                    monitoringInfo.getUaslAdministratorId(), monitoringInfo.getUaslId()))
                .collect(Collectors.toList());
          });
      when(clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // テスト対象メソッド呼び出し用の引数
      LocalDateTime startAt = LocalDateTime.of(2024, 12, 1, 0, 0, 0);
      String assertStartAt = startAt.toString() + ":00Z";
      LocalDateTime endAt = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
      String assertEndAt = endAt.toString() + "Z";

      // ヒヤリハット(航路逸脱)情報のDB取得をMock化
      List<UaslDeviation> deviationList = new ArrayList<>();
      deviationList.add(createUaslDeviationTestModel("001", "admin1", "uasl1"));
      deviationList.add(createUaslDeviationTestModel("002", "admin2", "uasl1"));
      deviationList.add(createUaslDeviationTestModel("003", "admin2", "uasl2"));
      // ↓004は重複データ（航路運営者admin2の航路uasl1の情報は、予約002と同じ。Mapに重複登録されないことの確認用。）
      deviationList.add(createUaslDeviationTestModel("004", "admin2", "uasl1"));
      when(uaslDeviationMapper.selectByReservationEndAt(startAt, endAt))
          .thenReturn(deviationList);

      // ヒヤリハット(第三者立入)情報のDB取得をMock化
      List<MonitoringInformation> monitoringInfoList = new ArrayList<>();
      monitoringInfoList.add(createMonitoringInformationTestModel("001", "admin1", "uasl1"));
      monitoringInfoList.add(createMonitoringInformationTestModel("002", "admin2", "uasl1"));
      monitoringInfoList.add(createMonitoringInformationTestModel("003", "admin2", "uasl2"));
      // ↓004は重複データ（航路運営者admin2の航路uasl1の情報は、予約002と同じ。Mapに重複登録されないことの確認用。）
      monitoringInfoList.add(createMonitoringInformationTestModel("004", "admin2", "uasl1"));
      monitoringInfoList.add(createMonitoringInformationTestModel("005", "admin1", "uasl2"));
      when(monitoringInformationMapper.selectByReservationEndAt(startAt, endAt))
          .thenReturn(monitoringInfoList);

      // テスト対象メソッドの実行
      Map<String, NotifyUpdatesNearMissInfoDto> resultMap =
          nearMissInfoLogic.getAdminIdNearMissInfoMap(startAt, endAt);

      // 結果の検証
      // 航路運営者(admin1とadmin2の2件)分のヒヤリハット情報がMapに登録される
      assertNotNull(resultMap);
      assertEquals(2, resultMap.size());
      // Mapの登録内容確認（admin1の情報)
      NotifyUpdatesNearMissInfoDto notifyDto1 = resultMap.get("admin1");
      assertNotNull(notifyDto1);
      assertEquals(1, notifyDto1.getDeviantUaslIds().size());
      assertEquals("uasl1", notifyDto1.getDeviantUaslIds().get(0));
      assertEquals(2, notifyDto1.getEntryUaslIds().size());
      assertEquals("uasl1", notifyDto1.getEntryUaslIds().get(0));
      assertEquals("uasl2", notifyDto1.getEntryUaslIds().get(1));
      assertEquals(assertStartAt, notifyDto1.getReportingStartAt());
      assertEquals(assertEndAt, notifyDto1.getReportingEndAt());
      // Mapの登録内容確認（admin2の情報)
      NotifyUpdatesNearMissInfoDto notifyDto2 = resultMap.get("admin2");
      assertNotNull(notifyDto2);
      assertEquals(2, notifyDto2.getDeviantUaslIds().size());
      assertEquals("uasl1", notifyDto2.getDeviantUaslIds().get(0));
      assertEquals("uasl2", notifyDto2.getDeviantUaslIds().get(1));
      assertEquals(2, notifyDto2.getEntryUaslIds().size());
      assertEquals("uasl1", notifyDto2.getEntryUaslIds().get(0));
      assertEquals("uasl2", notifyDto2.getEntryUaslIds().get(1));
      assertEquals(assertStartAt, notifyDto2.getReportingStartAt());
      assertEquals(assertEndAt, notifyDto2.getReportingEndAt());
    }
  }

  // 以下はテスト用データ生成用メソッド

  /**
   * 引数に指定した内容で、航路逸脱情報のテストデータを生成する。
   * 
   * @param reservationId 航路予約毎の識別ID
   * @param adminId 航路運営者ID
   * @param uaslId 航路ID
   * @return 航路逸脱情報のテストデータ
   */
  private UaslDeviation createUaslDeviationTestModel(String reservationId, String adminId,
      String uaslId) {
    UaslDeviation dto = new UaslDeviation();
    dto.setUaslReservationId(reservationId);
    dto.setUaslAdministratorId(adminId);
    dto.setUaslId(uaslId);
    return dto;
  }

  /**
   * 引数に指定した内容で、航路逸脱情報のテストデータを生成する。
   * 
   * @param reservationId 航路予約毎の識別ID
   * @param adminId 航路運営者ID
   * @param uaslId 航路ID
   * @return 航路逸脱情報のテストデータ
   */
  private UaslDeviationDto createUaslDeviationTestDto(String reservationId, String adminId,
      String uaslId) {
    UaslDeviationDto dto = new UaslDeviationDto();
    dto.setUaslReservationId(reservationId);
    dto.setUaslAdministratorId(adminId);
    dto.setUaslId(uaslId);
    return dto;
  }

  /**
   * 引数に指定した内容で、第三者立入情報のテストデータを生成する。
   * 
   * @param reservationId 航路予約毎の識別ID
   * @param adminId 航路運営者ID
   * @param uaslId 航路ID
   * @return 第三者立入情報のテストデータ
   */
  private MonitoringInformation createMonitoringInformationTestModel(String reservationId,
      String adminId, String uaslId) {
    MonitoringInformation dto = new MonitoringInformation();
    dto.setUaslReservationId(reservationId);
    dto.setUaslAdministratorId(adminId);
    dto.setUaslId(uaslId);
    return dto;
  }

  /**
   * 引数に指定した内容で、第三者立入情報のテストデータを生成する。
   * 
   * @param reservationId 航路予約毎の識別ID
   * @param adminId 航路運営者ID
   * @param uaslId 航路ID
   * @return 第三者立入情報のテストデータ
   */
  private MonitoringInformationDto createMonitoringInformationTestDto(String reservationId,
      String adminId, String uaslId) {
    MonitoringInformationDto dto = new MonitoringInformationDto();
    dto.setUaslReservationId(reservationId);
    dto.setUaslAdministratorId(adminId);
    dto.setUaslId(uaslId);
    return dto;
  }
}

// ↓Copilotの参考コード
// UaslDeviationDto deviationDto = new UaslDeviationDto();
// deviationDto.setUaslAdministratorId("admin1");
// deviationDto.setUaslId("uasl1");
//
// MonitoringInformationDto monitoringInfoDto = new MonitoringInformationDto();
// monitoringInfoDto.setUaslAdministratorId("admin1");
// monitoringInfoDto.setUaslId("uasl2");
//
// when(nearMissInfoLogic.getDeviationByStartEnd(startAt,
// endAt)).thenReturn(Arrays.asList(deviationDto));
// when(nearMissInfoLogic.getMonitoringInfoByStartEnd(startAt,
// endAt)).thenReturn(Arrays.asList(monitoringInfoDto));

