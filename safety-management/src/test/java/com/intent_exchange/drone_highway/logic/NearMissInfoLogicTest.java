package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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
import com.intent_exchange.drone_highway.config.TestConfig;
import com.intent_exchange.drone_highway.dao.AirwayDeviationMapper;
import com.intent_exchange.drone_highway.dao.MonitoringInformationMapper;
import com.intent_exchange.drone_highway.dto.request.AirwayDeviationDto;
import com.intent_exchange.drone_highway.dto.request.MonitoringInformationDto;
import com.intent_exchange.drone_highway.dto.request.NotifyUpdatesNearMissInfoDto;
import com.intent_exchange.drone_highway.model.AirwayDeviation;
import com.intent_exchange.drone_highway.model.MonitoringInformation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * NearMissInfoLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class NearMissInfoLogicTest extends NearMissInfoLogic {

  /** AirwayDeviationMapperクラスのMock */
  @Mock
  private AirwayDeviationMapper airwayDeviationMapper;

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
    when(airwayDeviationMapper.selectByReservationEndAt(startAt, endAt))
        .thenReturn(new ArrayList<AirwayDeviation>());
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
      // AirwayDeviationリスト→AirwayDeviationDtoリストへの変換処理をMock化
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(any(List.class), eq(AirwayDeviationDto.class)))
          .thenAnswer(invocation -> {
            List<AirwayDeviation> list = invocation.getArgument(0);
            return list.stream()
                .map(deviation -> createAirwayDeviationTestDto(deviation.getAirwayReservationId(),
                    deviation.getAirwayAdministratorId(), deviation.getAirwayId()))
                .collect(Collectors.toList());
          });
      // MonitoringInformationリスト→MonitoringInformationDtoリストへの変換処理をMock化
      mockedStatic
          .when(() -> ModelMapperUtil.mapList(any(List.class), eq(MonitoringInformationDto.class)))
          .thenAnswer(invocation -> {
            List<MonitoringInformation> list = invocation.getArgument(0);
            return list.stream()
                .map(monitoringInfo -> createMonitoringInformationTestDto(
                    monitoringInfo.getAirwayReservationId(),
                    monitoringInfo.getAirwayAdministratorId(), monitoringInfo.getAirwayId()))
                .collect(Collectors.toList());
          });
      when(clock.getZone()).thenReturn(ZoneId.of("UTC"));

      // テスト対象メソッド呼び出し用の引数
      LocalDateTime startAt = LocalDateTime.of(2024, 12, 1, 0, 0, 0);
      String assertStartAt = startAt.toString() + ":00Z";
      LocalDateTime endAt = LocalDateTime.of(2024, 12, 31, 23, 59, 59);
      String assertEndAt = endAt.toString() + "Z";

      // ヒヤリハット(航路逸脱)情報のDB取得をMock化
      List<AirwayDeviation> deviationList = new ArrayList<>();
      deviationList.add(createAirwayDeviationTestModel("001", "admin1", "airway1"));
      deviationList.add(createAirwayDeviationTestModel("002", "admin2", "airway1"));
      deviationList.add(createAirwayDeviationTestModel("003", "admin2", "airway2"));
      // ↓004は重複データ（航路運営者admin2の航路airway1の情報は、予約002と同じ。Mapに重複登録されないことの確認用。）
      deviationList.add(createAirwayDeviationTestModel("004", "admin2", "airway1"));
      when(airwayDeviationMapper.selectByReservationEndAt(startAt, endAt))
          .thenReturn(deviationList);

      // ヒヤリハット(第三者立入)情報のDB取得をMock化
      List<MonitoringInformation> monitoringInfoList = new ArrayList<>();
      monitoringInfoList.add(createMonitoringInformationTestModel("001", "admin1", "airway1"));
      monitoringInfoList.add(createMonitoringInformationTestModel("002", "admin2", "airway1"));
      monitoringInfoList.add(createMonitoringInformationTestModel("003", "admin2", "airway2"));
      // ↓004は重複データ（航路運営者admin2の航路airway1の情報は、予約002と同じ。Mapに重複登録されないことの確認用。）
      monitoringInfoList.add(createMonitoringInformationTestModel("004", "admin2", "airway1"));
      monitoringInfoList.add(createMonitoringInformationTestModel("005", "admin1", "airway2"));
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
      assertEquals(1, notifyDto1.getDeviantAirwayIds().size());
      assertEquals("airway1", notifyDto1.getDeviantAirwayIds().get(0));
      assertEquals(2, notifyDto1.getEntryAirwayIds().size());
      assertEquals("airway1", notifyDto1.getEntryAirwayIds().get(0));
      assertEquals("airway2", notifyDto1.getEntryAirwayIds().get(1));
      assertEquals(assertStartAt, notifyDto1.getReportingStartAt());
      assertEquals(assertEndAt, notifyDto1.getReportingEndAt());
      // Mapの登録内容確認（admin2の情報)
      NotifyUpdatesNearMissInfoDto notifyDto2 = resultMap.get("admin2");
      assertNotNull(notifyDto2);
      assertEquals(2, notifyDto2.getDeviantAirwayIds().size());
      assertEquals("airway1", notifyDto2.getDeviantAirwayIds().get(0));
      assertEquals("airway2", notifyDto2.getDeviantAirwayIds().get(1));
      assertEquals(2, notifyDto2.getEntryAirwayIds().size());
      assertEquals("airway1", notifyDto2.getEntryAirwayIds().get(0));
      assertEquals("airway2", notifyDto2.getEntryAirwayIds().get(1));
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
   * @param airwayId 航路ID
   * @return 航路逸脱情報のテストデータ
   */
  private AirwayDeviation createAirwayDeviationTestModel(String reservationId, String adminId,
      String airwayId) {
    AirwayDeviation dto = new AirwayDeviation();
    dto.setAirwayReservationId(reservationId);
    dto.setAirwayAdministratorId(adminId);
    dto.setAirwayId(airwayId);
    return dto;
  }

  /**
   * 引数に指定した内容で、航路逸脱情報のテストデータを生成する。
   * 
   * @param reservationId 航路予約毎の識別ID
   * @param adminId 航路運営者ID
   * @param airwayId 航路ID
   * @return 航路逸脱情報のテストデータ
   */
  private AirwayDeviationDto createAirwayDeviationTestDto(String reservationId, String adminId,
      String airwayId) {
    AirwayDeviationDto dto = new AirwayDeviationDto();
    dto.setAirwayReservationId(reservationId);
    dto.setAirwayAdministratorId(adminId);
    dto.setAirwayId(airwayId);
    return dto;
  }

  /**
   * 引数に指定した内容で、第三者立入情報のテストデータを生成する。
   * 
   * @param reservationId 航路予約毎の識別ID
   * @param adminId 航路運営者ID
   * @param airwayId 航路ID
   * @return 第三者立入情報のテストデータ
   */
  private MonitoringInformation createMonitoringInformationTestModel(String reservationId,
      String adminId, String airwayId) {
    MonitoringInformation dto = new MonitoringInformation();
    dto.setAirwayReservationId(reservationId);
    dto.setAirwayAdministratorId(adminId);
    dto.setAirwayId(airwayId);
    return dto;
  }

  /**
   * 引数に指定した内容で、第三者立入情報のテストデータを生成する。
   * 
   * @param reservationId 航路予約毎の識別ID
   * @param adminId 航路運営者ID
   * @param airwayId 航路ID
   * @return 第三者立入情報のテストデータ
   */
  private MonitoringInformationDto createMonitoringInformationTestDto(String reservationId,
      String adminId, String airwayId) {
    MonitoringInformationDto dto = new MonitoringInformationDto();
    dto.setAirwayReservationId(reservationId);
    dto.setAirwayAdministratorId(adminId);
    dto.setAirwayId(airwayId);
    return dto;
  }
}

// ↓Copilotの参考コード
// AirwayDeviationDto deviationDto = new AirwayDeviationDto();
// deviationDto.setAirwayAdministratorId("admin1");
// deviationDto.setAirwayId("airway1");
//
// MonitoringInformationDto monitoringInfoDto = new MonitoringInformationDto();
// monitoringInfoDto.setAirwayAdministratorId("admin1");
// monitoringInfoDto.setAirwayId("airway2");
//
// when(nearMissInfoLogic.getDeviationByStartEnd(startAt,
// endAt)).thenReturn(Arrays.asList(deviationDto));
// when(nearMissInfoLogic.getMonitoringInfoByStartEnd(startAt,
// endAt)).thenReturn(Arrays.asList(monitoringInfoDto));

