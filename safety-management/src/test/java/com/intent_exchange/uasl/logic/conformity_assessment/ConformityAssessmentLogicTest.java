package com.intent_exchange.uasl.logic.conformity_assessment;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.util.ModelMapperUtil;
import com.intent_exchange.uasl.util.PropertyUtil;
import com.intent_exchange.uasl.dao.UaslReservationMapper;

@ExtendWith(MockitoExtension.class)
class ConformityAssessmentLogicTest {

  @Mock
  private WeatherWindSpeedLogic weatherWindLogic;

  @Mock
  private RestrictedAirSpaceEvaluationLogic restrictedAirSpaceLogic;

  @Mock
  private ThirdPartyMonitoringInformationEvaluationLogic thirdPartyLogic;

  @Mock
  private RailwayOperationInformationEvaluationLogic railwayLogic;

  @Mock
  private UaslReservationMapper uaslReservationMapper;

  @InjectMocks
  private ConformityAssessmentLogic conformityAssessmentLogic;

  private ConformityAssessmentExecutionDto executionDto;

  @BeforeEach
  void setUp() {
    executionDto = new ConformityAssessmentExecutionDto();
    executionDto.setToken("dummyToken");
    executionDto.setApiKey("dummyApiKey");
    executionDto.setEvaluationResults(true);
    executionDto.setRailwayOperationEvaluationResults(true);
    executionDto.setThirdPartyEvaluationResults(true);

    try (MockedStatic<PropertyUtil> mockedPropertyUtil = mockStatic(PropertyUtil.class)) {
      mockedPropertyUtil.when(() -> PropertyUtil.getProperty("token")).thenReturn("dummyToken");
      mockedPropertyUtil.when(() -> PropertyUtil.getProperty("apiKey")).thenReturn("dummyApiKey");
      mockedPropertyUtil.when(() -> PropertyUtil.getPropertyDecimal("dummy.wind.speed.range"))
          .thenReturn(new BigDecimal("10.0"));
      mockedPropertyUtil.when(() -> PropertyUtil.getProperty(anyString())).thenReturn(null);
    }

    // 判定ロジックのリストを初期化
    conformityAssessmentLogic.init();
  }

  @Test
  @DisplayName("適合性評価の実行_成功")
  void testExecutionConformityAssessment1() {
	when(uaslReservationMapper.selectMaxWindSpeed(any(), any(), any())).thenReturn(15.0);
    when(weatherWindLogic.check(any())).thenReturn(true);
    when(restrictedAirSpaceLogic.check(any())).thenReturn(true);
    when(thirdPartyLogic.check(any())).thenReturn(true);
    when(railwayLogic.check(any())).thenReturn(true);

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertTrue(result.getEvaluationResults());
    }
  }

  @Test
  @DisplayName("適合性評価の実行_第三者評価NG")
  void testExecutionConformityAssessment2() {
    when(weatherWindLogic.check(any())).thenReturn(true);
    when(restrictedAirSpaceLogic.check(any())).thenReturn(true);
    when(railwayLogic.check(any())).thenReturn(true);
    when(thirdPartyLogic.check(any())).thenReturn(false);
    when(thirdPartyLogic.getClassInfo())
        .thenReturn("ThirdPartyMonitoringInformationEvaluationLogic");

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertFalse(result.getEvaluationResults());
      assertEquals("第三者立入監視情報判定エラー", result.getMessage());
      assertEquals("intrusion", result.getType());
    }
  }

  @Test
  @DisplayName("適合性評価の実行_風速評価NG")
  void testExecutionConformityAssessment3() {
    when(weatherWindLogic.check(any())).thenReturn(false);
    when(weatherWindLogic.getClassInfo()).thenReturn("WeatherWindSpeedLogic");

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertFalse(result.getEvaluationResults());
      assertEquals("風速条件範囲内判定エラー", result.getMessage());
      assertEquals("weather", result.getType());
    }
  }

  @Test
  @DisplayName("適合性評価の実行_規制/イベント評価NG")
  void testExecutionConformityAssessment4() {
    when(weatherWindLogic.check(any())).thenReturn(true);
    when(restrictedAirSpaceLogic.check(any())).thenReturn(false);
    when(restrictedAirSpaceLogic.getClassInfo()).thenReturn("RestrictedAirSpaceEvaluationLogic");


    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertFalse(result.getEvaluationResults());
      assertEquals("規制/イベント情報判定エラー", result.getMessage());
      assertEquals("event", result.getType());
    }
  }

  @Test
  @DisplayName("適合性評価の実行_鉄道運行評価NG")
  void testExecutionConformityAssessment5() {
    when(weatherWindLogic.check(any())).thenReturn(true);
    when(restrictedAirSpaceLogic.check(any())).thenReturn(true);
    when(railwayLogic.check(any())).thenReturn(false);
    when(railwayLogic.getClassInfo()).thenReturn("RailwayOperationInformationEvaluationLogic");

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertFalse(result.getEvaluationResults());
      assertEquals("鉄道運行情報判定エラー", result.getMessage());
      assertEquals("railway", result.getType());
    }
  }

  @Test
  @DisplayName("鉄道運航の適合性評価のみ実施：結果OK")
  void testExecutionConformityAssessment6() {
    executionDto.setLogicClazz(RailwayOperationInformationEvaluationLogic.class);
    when(railwayLogic.check(any())).thenReturn(true);

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertTrue(result.getEvaluationResults());
    }
  }

  @Test
  @DisplayName("鉄道運航の適合性評価のみ実施：結果NG")
  void testExecutionConformityAssessment7() {
    executionDto.setLogicClazz(RailwayOperationInformationEvaluationLogic.class);
    when(railwayLogic.check(any())).thenReturn(false);
    when(railwayLogic.getClassInfo()).thenReturn("RailwayOperationInformationEvaluationLogic");

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertFalse(result.getEvaluationResults());
      assertEquals("鉄道運行情報判定エラー", result.getMessage());
      assertEquals("railway", result.getType());
    }
  }

  @Test
  @DisplayName("鉄道運航の適合性評価のみ実施OK：過去の第三者立入がNG")
  void testExecutionConformityAssessment8() {
    executionDto.setLogicClazz(RailwayOperationInformationEvaluationLogic.class);
    executionDto.setThirdPartyEvaluationResults(false);
    when(railwayLogic.check(any())).thenReturn(true);

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertFalse(result.getChangenResults());
    }
  }

  @Test
  @DisplayName("第三者立入の適合性評価のみ実施OK：過去の鉄道運航がNG")
  void testExecutionConformityAssessment9() {
    executionDto.setLogicClazz(ThirdPartyMonitoringInformationEvaluationLogic.class);
    executionDto.setRailwayOperationEvaluationResults(false);
    when(thirdPartyLogic.check(any())).thenReturn(true);

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertFalse(result.getChangenResults());
    }
  }

  @Test
  @DisplayName("鉄道運航・第三者立入の適合性評価の実行結果がnull")
  void testExecutionConformityAssessment10() {
    when(weatherWindLogic.check(any())).thenReturn(true);
    when(restrictedAirSpaceLogic.check(any())).thenReturn(true);
    when(railwayLogic.check(any())).thenReturn(null);
    when(thirdPartyLogic.check(any())).thenReturn(null);

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertTrue(result.getEvaluationResults());
    }
  }


  @Test
  @DisplayName("適合性評価の実行_エラー時の読み飛ばし")
  void testExecutionConformityAssessment11() {
    when(weatherWindLogic.check(any())).thenThrow(new RuntimeException("Error"));
    when(restrictedAirSpaceLogic.check(any())).thenReturn(true);
    when(thirdPartyLogic.check(any())).thenReturn(true);
    when(railwayLogic.check(any())).thenReturn(true);

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(invocation -> new ConformityAssessmentResultDto());

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertTrue(result.getEvaluationResults());
    }
  }

  @Test
  @DisplayName("適合性評価の実行_最大風速データなし")
  void testExecutionConformityAssessment12() {
    when(uaslReservationMapper.selectMaxWindSpeed(any(), any(), any())).thenReturn(null);
    when(weatherWindLogic.check(any())).thenReturn(true);
    when(restrictedAirSpaceLogic.check(any())).thenReturn(true);
    when(thirdPartyLogic.check(any())).thenReturn(true);
    when(railwayLogic.check(any())).thenReturn(true);
    
    Double expectedWindSpeed = PropertyUtil.getPropertyDecimal("dummy.wind.speed.range").doubleValue();

    try (MockedStatic<ModelMapperUtil> mockedModelMapperUtil = mockStatic(ModelMapperUtil.class)) {
      mockedModelMapperUtil
          .when(() -> ModelMapperUtil.map(any(), eq(ConformityAssessmentResultDto.class)))
          .thenAnswer(
              invocation -> {
                ConformityAssessmentExecutionDto source = invocation.getArgument(0);
                assertEquals(expectedWindSpeed, source.getWindSpeedRange());
                return new ConformityAssessmentResultDto();
              });

      ConformityAssessmentResultDto result =
          conformityAssessmentLogic.executionConformityAssessment(executionDto);

      assertNotNull(result);
      assertTrue(result.getEvaluationResults());
    }
  }
}
