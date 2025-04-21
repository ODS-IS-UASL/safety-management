package com.intent_exchange.drone_highway.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

@Configuration
public class TestConfig {
  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    ModelMapperUtil.setModelMapper(modelMapper);
    return modelMapper;
  }

  @Bean
  public MqttClient mqttClient() {
    return Mockito.mock(MqttClient.class);
  }
}
