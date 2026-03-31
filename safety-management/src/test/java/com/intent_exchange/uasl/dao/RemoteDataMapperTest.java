package com.intent_exchange.uasl.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.intent_exchange.uasl.dao.config.DatabaseTestConfig;
import com.intent_exchange.uasl.dao.config.XlsDataSetLoader;
import com.intent_exchange.uasl.model.RemoteData;

@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
// @DatabaseSetup(value = {"classpath:test-t_remote_data-data.xlsx"},
// type = DatabaseOperation.CLEAN_INSERT)
class RemoteDataMapperTest {

  @Autowired
  private RemoteDataMapper mapper;

  // TODO:excel読み込み時カラムが大文字になる件調査中のためコメントアウト
  @Test
  @Transactional
  @DisplayName("予約IDとリモートIDの紐づけ情報を更新する")
  // @ExpectedDatabase(value = "classpath:expected-t_remote_data-upsert01.xlsx",
  // assertionMode = DatabaseAssertionMode.NON_STRICT)
  void testUpsertSelective1() {
    RemoteData row = new RemoteData();
    row.setUaslReservationId("uaslReservationId01");
    row.setSerialNumber("serialNumber02");
    row.setRegistrationId("registrationId02");
    row.setUtmId("utmId02");
    row.setSpecificSessionId("specificSessionId02");
    row.setAircraftInfoId(12345);

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @Transactional
  @DisplayName("予約IDとリモートIDの紐づけ情報を作成する")
  // @ExpectedDatabase(value = "classpath:expected-t_remote_data-upsert02.xlsx",
  // assertionMode = DatabaseAssertionMode.NON_STRICT)
  void testUpsertSelective2() {
    RemoteData row = new RemoteData();
    row.setUaslReservationId("uaslReservationId02");
    row.setSerialNumber("serialNumber02");
    row.setRegistrationId("registrationId02");
    row.setUtmId("utmId02");
    row.setSpecificSessionId("specificSessionId02");
    row.setAircraftInfoId(12345);

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @Transactional
  @DisplayName("予約IDとリモートIDの紐づけ情報を更新する（requestIdあり）")
  void testUpsertSelective3_withRequestId() {
    RemoteData row = new RemoteData();
    row.setUaslReservationId("uaslReservationId03");
    row.setSerialNumber("serialNumber03");
    row.setRegistrationId("registrationId03");
    row.setUtmId("utmId03");
    row.setSpecificSessionId("specificSessionId03");
    row.setAircraftInfoId(99);
    row.setRequestId("REQ-0003"); // L45: requestId column added

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @Transactional
  @DisplayName("予約IDとリモートIDの紐づけ情報を更新する（requestIdがnull）")
  void testUpsertSelective4_nullRequestId() {
    RemoteData row = new RemoteData();
    row.setUaslReservationId("uaslReservationId04");
    row.setRegistrationId("registrationId04");
    row.setAircraftInfoId(88);
    row.setRequestId(null); // requestId is nullable

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @Transactional
  @DisplayName("予約IDとリモートIDの紐づけ情報を削除する")
  // @ExpectedDatabase(value = "classpath:expected-t_remote_data-delete01.xlsx",
  // assertionMode = DatabaseAssertionMode.NON_STRICT)
  void testDeleteByPrimaryKey1() {
    String uaslReservationId = "uaslReservationId01";
    int res = mapper.deleteByPrimaryKey(uaslReservationId);
    // assertEquals(1, res);
  }
}
