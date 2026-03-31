package com.intent_exchange.uasl.dao;

import java.util.List;
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
import com.intent_exchange.uasl.model.UaslDesignAreaInfoReservation;

@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
class UaslDesignAreaInfoReservationMapperTest {

  @Autowired
  private UaslDesignAreaInfoReservationMapper mapper;

  @Test
  @Transactional
  @DisplayName("接続テスト selectNowDay")
  void testSelectNowDay1() {
    List<UaslDesignAreaInfoReservation> list = mapper.selectNowDay();
    System.out.println(list);
  }

  @Test
  @Transactional
  @DisplayName("接続テスト selectNextDay")
  void testSelectNextDay1() {
    List<UaslDesignAreaInfoReservation> list = mapper.selectNextDay();
    System.out.println(list);
  }
}
