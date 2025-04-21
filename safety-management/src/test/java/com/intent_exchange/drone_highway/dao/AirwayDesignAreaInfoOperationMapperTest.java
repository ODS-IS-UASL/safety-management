package com.intent_exchange.drone_highway.dao;

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
import com.intent_exchange.drone_highway.dao.config.DatabaseTestConfig;
import com.intent_exchange.drone_highway.dao.config.XlsDataSetLoader;



@ExtendWith(SpringExtension.class)
@MybatisTest
@DbUnitConfiguration(dataSetLoader = XlsDataSetLoader.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
    DbUnitTestExecutionListener.class})
@ContextConfiguration(classes = {DatabaseTestConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
class AirwayDesignAreaInfoOperationMapperTest {

  @Autowired
  private AirwayDesignAreaInfoOperationMapper mapper;

  @Test
  @Transactional
  @DisplayName("接続テスト エリア指定なし")
  void testSelectAl11() {
    mapper.selectAll(600000, null);
  }

  @Test
  @Transactional
  @DisplayName("接続テスト エリア指定あり")
  void testSelectAl12() {
    mapper.selectAll(600000,
        "{\"type\": \"Polygon\",\"coordinates\": [[[100.0, 0.0],[101.0, 0.0],[101.0, 1.0],[100.0, 1.0],[100.0, 0.0]]]}");
  }
}
