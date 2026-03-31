package com.intent_exchange.uasl.logic.conformity_assessment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.RestrictedAirSpaceDto;
import com.intent_exchange.uasl.dto.request.RestrictedAirSpaceRequestDto;
import com.intent_exchange.uasl.dto.response.RestrictedAirSpaceResponseDto;
import com.intent_exchange.uasl.logic.web.WebRestrictedAirSpaceLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
public class RestrictedAirSpaceEvaluationLogicTest {

  @Mock
  private Clock clock; // Clockクラスのモック

  @Mock
  private WebRestrictedAirSpaceLogic webRestrictedAirSpaceLogic;

  @InjectMocks
  private RestrictedAirSpaceEvaluationLogic logic;

  @Test
  @DisplayName("規制/イベント情報の適合性評価 規制/イベント情報がNULL")
  void testCheck1() {

    RestrictedAirSpaceDto dto = new RestrictedAirSpaceDto();
    dto.setAreaInfo("[[35.0,139.0]]");
    dto.setStartAt("2024-12-05T01:00:00Z");
    dto.setEndAt("2024-12-05T02:00:00Z");

    List<List<Double>> airSpace = new ArrayList<>();
    List<Double> airSpace1 = new ArrayList<>();
    airSpace1.add(35.0);
    airSpace1.add(139.0);
    airSpace.add(airSpace1);
    List<String> timeScope = new ArrayList<>();
    timeScope.add("2024-12-05T01:00:00Z");
    timeScope.add("2024-12-05T02:00:00Z");
    RestrictedAirSpaceRequestDto requestDto = new RestrictedAirSpaceRequestDto();
    requestDto.setAirSpace(airSpace);
    requestDto.setTimeScope(timeScope);


    RestrictedAirSpaceResponseDto restrictedAirSpaceResponseDto =
        new RestrictedAirSpaceResponseDto();
    restrictedAirSpaceResponseDto.setRestrictedAirSpace(null);

    when(webRestrictedAirSpaceLogic.getRestrictedAirSpace(requestDto))
        .thenReturn(restrictedAirSpaceResponseDto);


    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.listToString(dto.getAreaInfo())).thenReturn(airSpace);
      assertTrue(logic.check(dto));
    }
  }

  @Test
  @DisplayName("規制/イベント情報の適合性評価 規制/イベント情報が空")
  void testCheck2() {
    RestrictedAirSpaceDto dto = new RestrictedAirSpaceDto();
    dto.setAreaInfo("[[35.0,139.0]]");
    dto.setStartAt("2024-12-05T01:00:00Z");
    dto.setEndAt("2024-12-05T02:00:00Z");

    List<List<Double>> airSpace = new ArrayList<>();
    List<Double> airSpace1 = new ArrayList<>();
    airSpace1.add(35.0);
    airSpace1.add(139.0);
    airSpace.add(airSpace1);
    List<String> timeScope = new ArrayList<>();
    timeScope.add("2024-12-05T01:00:00Z");
    timeScope.add("2024-12-05T02:00:00Z");
    RestrictedAirSpaceRequestDto requestDto = new RestrictedAirSpaceRequestDto();
    requestDto.setAirSpace(airSpace);
    requestDto.setTimeScope(timeScope);


    RestrictedAirSpaceResponseDto restrictedAirSpaceResponseDto =
        new RestrictedAirSpaceResponseDto();
    List<Object> restrictedAirSpace = new ArrayList<>();
    restrictedAirSpaceResponseDto.setRestrictedAirSpace(restrictedAirSpace);

    when(webRestrictedAirSpaceLogic.getRestrictedAirSpace(requestDto))
        .thenReturn(restrictedAirSpaceResponseDto);


    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.listToString(dto.getAreaInfo())).thenReturn(airSpace);
      assertTrue(logic.check(dto));
    }

  }

  @Test
  @DisplayName("第三者立入監視情報の適合性評価 第三者立入監視なし trafficsが空")
  void testCheck3() {
    RestrictedAirSpaceDto dto = new RestrictedAirSpaceDto();
    dto.setAreaInfo("[[35.0,139.0]]");
    dto.setStartAt("2024-12-05T01:00:00Z");
    dto.setEndAt("2024-12-05T02:00:00Z");

    List<List<Double>> airSpace = new ArrayList<>();
    List<Double> airSpace1 = new ArrayList<>();
    airSpace1.add(35.0);
    airSpace1.add(139.0);
    airSpace.add(airSpace1);
    List<String> timeScope = new ArrayList<>();
    timeScope.add("2024-12-05T01:00:00Z");
    timeScope.add("2024-12-05T02:00:00Z");
    RestrictedAirSpaceRequestDto requestDto = new RestrictedAirSpaceRequestDto();
    requestDto.setAirSpace(airSpace);
    requestDto.setTimeScope(timeScope);


    RestrictedAirSpaceResponseDto restrictedAirSpaceResponseDto =
        new RestrictedAirSpaceResponseDto();
    List<Object> restrictedAirSpace = new ArrayList<>();
    restrictedAirSpace.add(airSpace);
    restrictedAirSpaceResponseDto.setRestrictedAirSpace(restrictedAirSpace);

    when(webRestrictedAirSpaceLogic.getRestrictedAirSpace(requestDto))
        .thenReturn(restrictedAirSpaceResponseDto);


    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.listToString(dto.getAreaInfo())).thenReturn(airSpace);
      assertFalse(logic.check(dto));
    }
  }

  @Test
  @DisplayName("規制/イベント情報の適合性評価 ModelMapperUtil.listToStringでエラー")
  void testCheck4() {

    RestrictedAirSpaceDto dto = new RestrictedAirSpaceDto();
    dto.setAreaInfo("[[35.0,139.0]]");
    dto.setStartAt("2024-12-05T01:00:00Z");
    dto.setEndAt("2024-12-05T02:00:00Z");

    List<List<Double>> airSpace = new ArrayList<>();
    List<String> timeScope = new ArrayList<>();
    timeScope.add("2024-12-05T01:00:00Z");
    timeScope.add("2024-12-05T02:00:00Z");
    RestrictedAirSpaceRequestDto requestDto = new RestrictedAirSpaceRequestDto();
    requestDto.setAirSpace(airSpace);
    requestDto.setTimeScope(timeScope);


    RestrictedAirSpaceResponseDto restrictedAirSpaceResponseDto =
        new RestrictedAirSpaceResponseDto();

    when(webRestrictedAirSpaceLogic.getRestrictedAirSpace(requestDto))
        .thenReturn(restrictedAirSpaceResponseDto);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.listToString(dto.getAreaInfo()))
          .thenThrow(new IOException());

      assertTrue(logic.check(dto));
    }
  }

}
