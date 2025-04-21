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

package com.intent_exchange.drone_highway.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * PropertyConfigクラスは、application.propertiesファイルからプロパティを読み込み、 ResourceBundleを初期化するための設定クラスです。
 */
@Configuration
public class PropertyConfig {

  /**
   * application.nameプロパティの値を保持するフィールド。
   */
  @Value("${application.name}")
  private String applicationName;

  /**
   * system.nameプロパティの値を保持するフィールド。
   */
  @Value("${system.name}")
  private String systemName;

  /**
   * applicationNameに基づいて初期化されるResourceBundle。
   */
  private static ResourceBundle bundle1;

  /**
   * systemNameに基づいて初期化されるResourceBundle。
   */
  private static ResourceBundle bundle2;

  /**
   * プロパティが注入された後にResourceBundleを初期化するメソッド。
   */
  @PostConstruct
  @SuppressWarnings("unchecked")
  public void init() {
    String configLocation = System.getenv("SPRING_CONFIG_LOCATION");
    if (configLocation != null) {
      Properties appProperties = new Properties();
      Properties sysProperties = new Properties();
      String[] locations = configLocation.split(",");
      for (String location : locations) {
        try (FileInputStream fis = new FileInputStream(location.trim())) {
          if (location.contains(applicationName + ".properties")) {
            appProperties.load(fis);
          } else if (location.contains(systemName + ".properties")) {
            sysProperties.load(fis);
          }
        } catch (IOException e) {
          throw new RuntimeException("Failed to load properties from " + location, e);
        }
      }
      bundle1 = new ResourceBundle() {
        @Override
        protected Object handleGetObject(String key) {
          return appProperties.getProperty(key);
        }

        @Override
        public Enumeration<String> getKeys() {
          return (Enumeration<String>) appProperties.propertyNames();
        }
      };
      bundle2 = new ResourceBundle() {
        @Override
        protected Object handleGetObject(String key) {
          return sysProperties.getProperty(key);
        }

        @Override
        public Enumeration<String> getKeys() {
          return (Enumeration<String>) sysProperties.propertyNames();
        }
      };
    } else {
      bundle1 = ResourceBundle.getBundle(applicationName);
      bundle2 = ResourceBundle.getBundle(systemName);
    }
  }

  /**
   * bundle1を取得するメソッド。
   *
   * @return 初期化されたResourceBundle
   */
  public static ResourceBundle getBundle1() {
    return bundle1;
  }

  /**
   * bundle2を取得するメソッド。
   *
   * @return 初期化されたResourceBundle
   */
  public static ResourceBundle getBundle2() {
    return bundle2;
  }

  /**
   * ResourceBundleを再読み込みするメソッド。
   */
  public void reload() {
    init();
  }
}

