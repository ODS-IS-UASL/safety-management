package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mockStatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.intent_exchange.uasl.dao.MonitoringInformationMapper;
import com.intent_exchange.uasl.dto.geojson.SubscriptionsGeoJsonFeatures;
import com.intent_exchange.uasl.dto.request.ThirdPartyMonitoringInformationRequestDto;
import com.intent_exchange.uasl.dto.response.ThirdPartyMonitoringInformationResponseDto;
import com.intent_exchange.uasl.model.MonitoringInformation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class MonitoringInformationLogicTest {
  
  @Mock
  private MonitoringInformationMapper mapper;

  @InjectMocks
  private MonitoringInformationLogic logic;
  
  @Test
  @DisplayName("新しい第三者立入監視情報を登録する")
  void testInsert1() {

    ThirdPartyMonitoringInformationResponseDto responceDto = new ThirdPartyMonitoringInformationResponseDto();
    
    List<Object> features = new ArrayList<>();
    SubscriptionsGeoJsonFeatures feature = new SubscriptionsGeoJsonFeatures();
    Map<String, Object> properties = new HashMap<>();
    int intrusionStatus = 1;
    properties.put("intrusionStatus", intrusionStatus);
    feature.setProperties(properties);
    features.add(feature);
    responceDto.setFeatures(features);

    ThirdPartyMonitoringInformationRequestDto requestDto = new ThirdPartyMonitoringInformationRequestDto();
    requestDto.setUaslReservationId("1");
    requestDto.setUaslAdministratorId("1");
    requestDto.setOperatorId("1");
    requestDto.setUaslId("1");
    
    
    MonitoringInformation model = new MonitoringInformation();
    model.setUaslReservationId(requestDto.getUaslReservationId());
    model.setMonitoringInformation(
        ModelMapperUtil.convertListObjectToJson(responceDto.getFeatures()));
    model.setUaslAdministratorId(requestDto.getUaslAdministratorId());
    model.setOperatorId(requestDto.getOperatorId());
    model.setUaslId(requestDto.getUaslId());

    
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(model, MonitoringInformation.class)).thenReturn(model);
      assertDoesNotThrow(() -> logic.insert(responceDto, requestDto));
    }
  }
}
