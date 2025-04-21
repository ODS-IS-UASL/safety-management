package com.intent_exchange.drone_highway.dao.config;

import java.io.InputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.excel.XlsDataSet;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;

@Component
public class XlsDataSetLoader extends AbstractDataSetLoader {

  private ResourceLoader resourceLoader;

  // デフォルトコンストラクタ
  public XlsDataSetLoader() {
    super();
  }

  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  @Override
  protected IDataSet createDataSet(Resource resource) throws Exception {
    // TODO 自動生成されたメソッド・スタブ
    try (InputStream inputStream = resource.getInputStream()) {
      return new XlsDataSet(inputStream);
    }
  }
}
