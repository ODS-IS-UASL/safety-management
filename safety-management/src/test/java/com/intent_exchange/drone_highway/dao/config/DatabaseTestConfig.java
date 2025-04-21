package com.intent_exchange.drone_highway.dao.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
@MapperScan("com.intent_exchange.drone_highway.dao")
@PropertySource("classpath:database-test.properties")
public class DatabaseTestConfig {

  /** データベースのURL */
  @Value("${datasource.url}")
  private String url;

  /** データベースのユーザー名 */
  @Value("${datasource.username}")
  private String username;

  /** データベースのパスワード */
  @Value("${datasource.password}")
  private String password;

  /** データベースのスキーマ */
  @Value("${datasource.schema}")
  private String schema;

  /** データベースのドライバークラス名 */
  @Value("${datasource.driver-class-name}")
  private String driverClassName;

  /** HikariCPの最大プールサイズ */
  @Value("${datasource.hikari.maximum-pool-size}")
  private int maximumPoolSize;

  /** HikariCPの最小アイドル数 */
  @Value("${datasource.hikari.minimum-idle}")
  private int minimumIdle;

  /** HikariCPのアイドルタイムアウト */
  @Value("${datasource.hikari.idle-timeout}")
  private long idleTimeout;

  /** HikariCPの最大ライフタイム */
  @Value("${datasource.hikari.max-lifetime}")
  private long maxLifetime;

  /** HikariCPのコネクションタイムアウト */
  @Value("${datasource.hikari.connection-timeout}")
  private long connectionTimeout;

  @Value("${jpa.properties.hibernate.dialect}")
  private String hibernateDialect;

  /**
   * データソースを提供するBeanを生成します。
   *
   * @return データソース
   */
  @Bean
  public DataSource dataSource() {
    HikariDataSource dataSource = new HikariDataSource();
    dataSource.setDriverClassName(driverClassName);
    dataSource.setJdbcUrl(url);
    dataSource.setUsername(username);
    dataSource.setPassword(password);
    dataSource.setMaximumPoolSize(maximumPoolSize);
    dataSource.setMinimumIdle(minimumIdle);
    dataSource.setIdleTimeout(idleTimeout);
    dataSource.setMaxLifetime(maxLifetime);
    dataSource.setConnectionTimeout(connectionTimeout);

    // postgresqlの場合は、スキーマをsearch_pathに設定する
    //    dataSource.setSchema(schema);
    Properties dsProperties = new Properties();
    dsProperties.setProperty("search_path", schema);
    dataSource.setDataSourceProperties(dsProperties);

    return dataSource;
  }
}
