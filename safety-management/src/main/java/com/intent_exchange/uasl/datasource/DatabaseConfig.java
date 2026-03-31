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

package com.intent_exchange.uasl.datasource;

import java.util.Properties;
import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import com.intent_exchange.uasl.datasource.properties.MyBatisProperties;
import com.intent_exchange.uasl.datasource.properties.TransactionProperties;
import com.zaxxer.hikari.HikariDataSource;

/**
 * DatabaseConfigクラスは、データベース接続とトランザクション管理の設定を提供します。
 * このクラスはSpringの@Configurationアノテーションが付けられており、Spring IoCコンテナによって管理されます。
 */
@Configuration
@PropertySource("classpath:database.properties")
public class DatabaseConfig {

  /** トランザクションに関する設定を保持するオブジェクト */
  private final TransactionProperties transactionProperties;

  /** MyBatisに関する設定を保持するオブジェクト */
  private final MyBatisProperties myBatisProperties;

  /**
   * DatabaseConfigクラスのコンストラクタです。
   *
   * @param transactionProperties トランザクションプロパティ
   * @param myBatisProperties MyBatisプロパティ
   */
  @Autowired
  public DatabaseConfig(TransactionProperties transactionProperties,
      MyBatisProperties myBatisProperties) {
    this.transactionProperties = transactionProperties;
    this.myBatisProperties = myBatisProperties;
  }

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

  @Value("${datasource.hikari.connection-init-sql}")
  private String connectionInitSql;

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
    dataSource.setConnectionInitSql(connectionInitSql);

    // postgresqlの場合は、スキーマをsearch_pathに設定する
    // dataSource.setSchema(schema);
    Properties dsProperties = new Properties();
    dsProperties.setProperty("search_path", schema);
    dataSource.setDataSourceProperties(dsProperties);

    return dataSource;
  }

  /**
   * トランザクションマネージャを提供するBeanを生成します。
   *
   * @param dataSource データソース
   * @return トランザクションマネージャ
   */
  @Bean
  public PlatformTransactionManager transactionManager(DataSource dataSource) {
    DataSourceTransactionManager transactionManager = new DataSourceTransactionManager(dataSource);
    transactionManager.setDefaultTimeout(transactionProperties.getDefaultTimeout());
    return transactionManager;
  }

  /**
   * SqlSessionFactoryを提供するBeanを生成します。
   *
   * @return SqlSessionFactory
   * @throws Exception 例外
   */
  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    sessionFactory.setDataSource(dataSource());
    sessionFactory.setVfs(SpringBootVFS.class);
    sessionFactory.setTypeAliasesPackage("com.intent_exchange.uasl.model");
    sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
        .getResources("classpath:com/intent_exchange/uasl/dao/*.xml"));

    org.apache.ibatis.session.Configuration configuration =
        new org.apache.ibatis.session.Configuration();
    configuration.setMapUnderscoreToCamelCase(myBatisProperties.isMapUnderscoreToCamelCase());
    configuration.setLazyLoadingEnabled(myBatisProperties.isLazyLoadingEnabled());

    sessionFactory.setConfiguration(configuration);

    return sessionFactory.getObject();
  }

  /**
   * エンティティマネージャファクトリを提供するBeanを生成します。
   *
   * @return エンティティマネージャファクトリ
   */
  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dataSource());
    em.setPackagesToScan("com.intent_exchange.uasl.entity");
    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

    Properties properties = new Properties();
    properties.setProperty("hibernate.dialect", hibernateDialect);
    em.setJpaProperties(properties);

    return em;
  }
}

