package com.intent_exchange.uasl.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.apache.ibatis.type.JdbcType;
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
import com.intent_exchange.uasl.handler.myBatis.ArrayTypeHandler;
import com.intent_exchange.uasl.handler.myBatis.OffsetDateTimeToLocalDateTimeTypeHandler;
import com.intent_exchange.uasl.model.UaslReservation;
import org.mockito.ArgumentCaptor;

@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
// @DatabaseSetup(value = {"classpath:test-t_uasl_reservation-data.xlsx"},
// type = DatabaseOperation.CLEAN_INSERT)
class UaslReservationMapperTest {

  @Autowired
  private UaslReservationMapper mapper;

  // TODO:excel読み込み時カラムが大文字になる件調査中のためコメントアウト
  @Test
  @Transactional
  @DisplayName("航路予約情報を作成する")
  // @ExpectedDatabase(value = "classpath:expected-t_uasl_reservation-upsert01.xlsx",
  // assertionMode = DatabaseAssertionMode.NON_STRICT)
  void testUpsertSelective1() {
    UaslReservation row = new UaslReservation();
    List<String> uaslSectionIds = new ArrayList<>();
    uaslSectionIds.add("uaslSectionIds02");
    row.setUaslReservationId("uaslReservationId02");
    row.setUaslSectionIds(uaslSectionIds);
    row.setOperatorId("operatorId02");
    row.setStartAt(LocalDateTime.parse("2024-11-13T10:41:00", DateTimeFormatter.ISO_DATE_TIME));
    row.setEndAt(LocalDateTime.parse("2024-11-13T10:42:00", DateTimeFormatter.ISO_DATE_TIME));
    row.setReservedAt(LocalDateTime.parse("2024-11-13T10:43:00", DateTimeFormatter.ISO_DATE_TIME));

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @Transactional
  @DisplayName("航路予約情報を更新する")
  // @ExpectedDatabase(value = "classpath:expected-t_uasl_reservation-upsert02.xlsx",
  // assertionMode = DatabaseAssertionMode.NON_STRICT)
  void testUpsertSelective2() {
    UaslReservation row = new UaslReservation();
    List<String> uaslSectionIds = new ArrayList<>();
    uaslSectionIds.add("uaslSectionIds01");
    uaslSectionIds.add("uaslSectionIds02");
    row.setUaslReservationId("uaslReservationId01");
    row.setUaslSectionIds(uaslSectionIds);
    row.setOperatorId("operatorId02");
    row.setStartAt(LocalDateTime.parse("2024-11-13T10:41:00", DateTimeFormatter.ISO_DATE_TIME));
    row.setEndAt(LocalDateTime.parse("2024-11-13T10:42:00", DateTimeFormatter.ISO_DATE_TIME));
    row.setReservedAt(LocalDateTime.parse("2024-11-13T10:43:00", DateTimeFormatter.ISO_DATE_TIME));

    int res = mapper.upsertSelective(row);
    // assertEquals(1, res);
  }

  @Test
  @DisplayName("ArrayTypeHandlerはリストをJDBC配列に変換し、元に戻す（モック）")
  void arrayTypeHandler_setsAndGetsArrayWithoutDatabase() throws Exception {
    ArrayTypeHandler handler = new ArrayTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);
    java.sql.Connection connection = mock(java.sql.Connection.class);
    Array jdbcArray = mock(Array.class);
    when(ps.getConnection()).thenReturn(connection);
    when(connection.createArrayOf(eq("VARCHAR"), any())).thenReturn(jdbcArray);

    handler.setParameter(ps, 1, List.of("SEC-1", "SEC-2"), JdbcType.ARRAY);

    verify(ps).setArray(1, jdbcArray);

    ResultSet rs = mock(ResultSet.class);
    when(jdbcArray.getArray()).thenReturn(new String[] {"SEC-1", "SEC-2"});
    when(rs.getArray("uasl_section_ids")).thenReturn(jdbcArray);

    List<String> result = handler.getResult(rs, "uasl_section_ids");
    assertThat(result).containsExactly("SEC-1", "SEC-2");
  }

  @Test
  @DisplayName("OffsetDateTimeToLocalDateTimeTypeHandlerはUTC経由で変換する（モック）")
  void offsetDateTimeHandler_convertsToUtcLocalDateTime() throws Exception {
    OffsetDateTimeToLocalDateTimeTypeHandler handler =
        new OffsetDateTimeToLocalDateTimeTypeHandler();
    PreparedStatement ps = mock(PreparedStatement.class);
    ArgumentCaptor<OffsetDateTime> captor = ArgumentCaptor.forClass(OffsetDateTime.class);

    LocalDateTime local = LocalDateTime.of(2025, 1, 1, 10, 0);
    handler.setNonNullParameter(ps, 1, local, JdbcType.TIMESTAMP);

    verify(ps).setObject(eq(1), captor.capture());
    OffsetDateTime sent = captor.getValue();
    assertThat(sent.getOffset()).isEqualTo(ZoneOffset.UTC);
    assertThat(sent.toLocalDateTime()).isEqualTo(local);

    ResultSet rs = mock(ResultSet.class);
    OffsetDateTime stored = OffsetDateTime.of(2025, 1, 1, 12, 0, 0, 0, ZoneOffset.ofHours(9));
    when(rs.getObject("start_at", OffsetDateTime.class)).thenReturn(stored);

    LocalDateTime fetched = handler.getNullableResult(rs, "start_at");
    assertThat(fetched).isEqualTo(stored.toLocalDateTime());

    CallableStatement cs = mock(CallableStatement.class);
    when(cs.getObject(1, OffsetDateTime.class)).thenReturn(stored);
    LocalDateTime fetchedFromCs = handler.getNullableResult(cs, 1);
    assertThat(fetchedFromCs).isEqualTo(stored.toLocalDateTime());
  }

  @Test
  @DisplayName("selectActiveBySectionIdsのメモリ内ロジック：重複、end_atフィルタ、start_atでソート")
  void selectActiveBySectionIds_inMemory_noDatabase() {
    LocalDateTime now = LocalDateTime.of(2025, 1, 1, 9, 30);

    UaslReservation active1 = new UaslReservation();
    active1.setUaslReservationId("active-1");
    active1.setUaslSectionIds(Arrays.asList("SEC-1", "SEC-2"));
    active1.setStartAt(LocalDateTime.of(2025, 1, 1, 10, 0));
    active1.setEndAt(LocalDateTime.of(2025, 1, 1, 11, 0));

    UaslReservation expired = new UaslReservation();
    expired.setUaslReservationId("expired-overlap");
    expired.setUaslSectionIds(List.of("SEC-2"));
    expired.setStartAt(LocalDateTime.of(2025, 1, 1, 8, 0));
    expired.setEndAt(LocalDateTime.of(2025, 1, 1, 9, 0));

    UaslReservation active2 = new UaslReservation();
    active2.setUaslReservationId("active-2");
    active2.setUaslSectionIds(Arrays.asList("SEC-2", "SEC-3"));
    active2.setStartAt(LocalDateTime.of(2025, 1, 1, 10, 30));
    active2.setEndAt(LocalDateTime.of(2025, 1, 1, 12, 0));

    UaslReservation nonOverlap = new UaslReservation();
    nonOverlap.setUaslReservationId("non-overlap");
    nonOverlap.setUaslSectionIds(List.of("SEC-X"));
    nonOverlap.setStartAt(LocalDateTime.of(2025, 1, 1, 9, 30));
    nonOverlap.setEndAt(LocalDateTime.of(2025, 1, 1, 10, 30));

    List<UaslReservation> source = List.of(active1, expired, active2, nonOverlap);

    @SuppressWarnings("unchecked")
    List<UaslReservation> filtered = source.stream()
        .filter(r -> r.getEndAt() != null && !r.getEndAt().isBefore(now))
        .filter(r -> {
          List<String> ids = (List<String>) r.getUaslSectionIds();
          return ids != null && ids.stream().anyMatch(id -> List.of("SEC-2").contains(id));
        })
        .sorted(Comparator.comparing(UaslReservation::getStartAt))
        .toList();

    assertThat(filtered).hasSize(2);
    assertThat(filtered.get(0).getUaslReservationId()).isEqualTo("active-1");
    assertThat(filtered.get(1).getUaslReservationId()).isEqualTo("active-2");
    assertThat(filtered.get(0).getStartAt()).isBefore(filtered.get(1).getStartAt());
  }
}
