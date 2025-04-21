package com.intent_exchange.drone_highway.logic.web;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.util.UriComponentsBuilder;
import com.intent_exchange.drone_highway.dto.response.RailwayOperationInformationResponseDto;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * 空域デジタルツイン 鉄道運行情報 (A-1-1-1-5)への通信用ロジックのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class WebRailwayOperationLogicTest {

  @Mock
  @Qualifier("customRestTemplate")
  private RestTemplate restTemplate;

  @InjectMocks
  private WebRailwayOperationLogic logic;

  @Test
  @DisplayName("運行ダイヤの取得 正常終了")
  void testGetRailwayOperationInformation1() {
    // モックされた現在の時刻を設定
    ZonedDateTime mockZonedNow = ZonedDateTime.of(2025, 2, 11, 13, 30, 0, 0, ZoneId.of("UTC"));

    // テストデータの準備
    String station1 = "西部秩父駅";
    String station2 = "横瀬";
    ZonedDateTime startDateTime =
        ZonedDateTime.of(LocalDateTime.of(2025, 1, 9, 10, 0), ZoneId.of("UTC"));
    ZonedDateTime endDateTime =
        ZonedDateTime.of(LocalDateTime.of(2025, 1, 9, 12, 0), ZoneId.of("UTC"));

    String url = PropertyUtil.getProperty("get.railway.operation.information.url");
    String urlTemplate = UriComponentsBuilder.fromHttpUrl(url)
        .queryParam("station1", station1)
        .queryParam("station2", station2)
        .queryParam("datetimeFrom",
            startDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
        .queryParam("datetimeTo",
            endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")))
        .toUriString();

    // レスポンス
    RailwayOperationInformationResponseDto responseDto =
        new RailwayOperationInformationResponseDto();
    List<List<String>> regularDiagram = new ArrayList<>();
    regularDiagram.add(List.of("11:08", "11:11"));
    regularDiagram.add(List.of("11:24", "11:27"));
    responseDto.setRegularDiagram(regularDiagram);

    // モックのRestTemplateの動作を定義
    when(restTemplate.getForObject(urlTemplate, RailwayOperationInformationResponseDto.class))
        .thenReturn(responseDto);

    // メソッドの呼び出し
    RailwayOperationInformationResponseDto response =
        logic.getRailwayOperationInformation(station1, station2, startDateTime, endDateTime);

    // 結果の検証
    assertNotNull(response);
    assertEquals(List.of("11:08", "11:11"), response.getRegularDiagram().get(0));

  }
}
