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

import java.time.Clock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.intent_exchange.drone_highway.converter.BooleanToStringConverter;
import com.intent_exchange.drone_highway.converter.DateToLocalDateTimeConverter;
import com.intent_exchange.drone_highway.converter.LocalDateTimeToStringConverter;
import com.intent_exchange.drone_highway.converter.StringToBooleanConverter;
import com.intent_exchange.drone_highway.converter.StringToLocalDateTimeConverter;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * ModelMapperの設定を行うための構成クラス。 このクラスでは、DTOとエンティティ間のカスタムマッピングを設定します。
 */
@Configuration
public class ModelMapperConfig {

  @Autowired
  private Clock clock;

  /**
   * ModelMapperのインスタンスを作成し、カスタムマッピングを追加します。
   *
   * @return カスタムマッピングが設定されたModelMapperのインスタンス
   */
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    modelMapper.getConfiguration()
        .setFieldMatchingEnabled(true)
        .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
    modelMapper.addConverter(new StringToLocalDateTimeConverter(clock));
    modelMapper.addConverter(new DateToLocalDateTimeConverter(clock));
    modelMapper.addConverter(new LocalDateTimeToStringConverter(clock));
    modelMapper.addConverter(new BooleanToStringConverter());
    modelMapper.addConverter(new StringToBooleanConverter());

    ModelMapperUtil.setModelMapper(modelMapper);
    return modelMapper;
  }
}

