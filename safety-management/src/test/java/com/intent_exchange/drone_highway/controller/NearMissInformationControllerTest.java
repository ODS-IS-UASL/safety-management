package com.intent_exchange.drone_highway.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.intent_exchange.drone_highway.entity.NearMissInformationRequest;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponse;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributes;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesRouteDeviationInfoInner;
import com.intent_exchange.drone_highway.entity.NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner;
import com.intent_exchange.drone_highway.service.NearMissInformationService;

/**
 * ヒヤリハット情報取得のコントローラのテストクラス。
 */

@ExtendWith(MockitoExtension.class)
@WebMvcTest(NearMissInformationController.class)
class NearMissInformationControllerTest {


  @MockBean
  private NearMissInformationService service;

  @InjectMocks
  private NearMissInformationController controller;

  @Test
  @DisplayName("ヒヤリハット情報取得")

  void nearMissInformationControllerTest1() throws JsonProcessingException, ParseException {
    // リクエストEntity
    NearMissInformationRequest nearMissInformationRequest = new NearMissInformationRequest();

    // レスポンスEntity
    NearMissInformationResponse nearMissInformationResponse = new NearMissInformationResponse();
    NearMissInformationResponseAttributes nearMissInformationResponseAttributes =
        new NearMissInformationResponseAttributes();
    NearMissInformationResponseAttributesRouteDeviationInfoInner routeDevioationInfo =
        new NearMissInformationResponseAttributesRouteDeviationInfoInner();
    List<NearMissInformationResponseAttributesRouteDeviationInfoInner> routeDeviationInfoList =
        new ArrayList<>();
    routeDeviationInfoList.add(routeDevioationInfo);

    List<NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner> thirdPartyEntryMonitoringList =
        new ArrayList<>();
    NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner thirdPartyEntryMonitoring =
        new NearMissInformationResponseAttributesThirdPartyEntryMonitoringInner();
    thirdPartyEntryMonitoringList.add(thirdPartyEntryMonitoring);

    nearMissInformationResponseAttributes.setRouteDeviationInfo(routeDeviationInfoList);
    nearMissInformationResponseAttributes
        .setThirdPartyEntryMonitoring(thirdPartyEntryMonitoringList);
    nearMissInformationResponse.setAttributes(nearMissInformationResponseAttributes);

    // モックの設定
    when(service.nearMissInformationEntry(nearMissInformationRequest))
        .thenReturn(nearMissInformationResponse);
    // テスト対象のメソッド実行
    ResponseEntity<NearMissInformationResponse> response =
        controller.getNearMissInformation(nearMissInformationRequest);
    // 結果確認
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(nearMissInformationResponse, response.getBody());
  }

}
