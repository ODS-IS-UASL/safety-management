package com.intent_exchange.drone_highway.logic.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.client.RestTemplate;
import com.intent_exchange.drone_highway.dto.request.RestrictedAirSpaceRequestDto;
import com.intent_exchange.drone_highway.dto.response.RestrictedAirSpaceResponseDto;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * 空域デジタルツイン 規制/イベント情報 (A-1-1-1-6)への通信用ロジックのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class WebRestrictedAirSpaceLogicTest {

  @Mock
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  @InjectMocks
  private WebRestrictedAirSpaceLogic logic;


  @Test
  @DisplayName("指定された情報に基づいて規制/イベント情報を取得する 正常終了")
  void testGetRestrictedAirSpace1() {
    // テストデータ
    RestrictedAirSpaceRequestDto requestDto = new RestrictedAirSpaceRequestDto();
    // URL生成
    String url = PropertyUtil.getProperty("post.restricted.air.space.url");

    // 規制/イベント情報のレスポンス
    RestrictedAirSpaceResponseDto restrictedAirSpaceResponceDto =
        new RestrictedAirSpaceResponseDto();
    List<List<Double>> airSpace = new ArrayList<>();
    List<Double> airSpace1 = new ArrayList<>();
    airSpace1.add(35.0);
    airSpace1.add(139.0);
    airSpace.add(airSpace1);
    List<String> timeScope = new ArrayList<>();
    timeScope.add("2024-12-05T01:00:00Z");
    timeScope.add("2024-12-05T02:00:00Z");
    List<Object> restrictedAirSpace = new ArrayList<>();
    restrictedAirSpace.add(airSpace);
    restrictedAirSpace.add(timeScope);
    restrictedAirSpaceResponceDto.setRestrictedAirSpace(restrictedAirSpace);

    when(restTemplate.postForObject(url, requestDto, RestrictedAirSpaceResponseDto.class))
        .thenReturn(restrictedAirSpaceResponceDto);

    // テスト実行
    RestrictedAirSpaceResponseDto result = logic.getRestrictedAirSpace(requestDto);

    // 結果確認 レスポンスのrestrictedAirSpaceResponceDtoに"feature1"が含まれていることを確認
    assertNotNull(result);
    assertEquals(airSpace, result.getRestrictedAirSpace().get(0));
    assertEquals(timeScope, result.getRestrictedAirSpace().get(1));
  }
}
