package com.intent_exchange.uasl.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.intent_exchange.uasl.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;
import com.intent_exchange.uasl.entity.ConformityAssessmentExecutionEntity;
import com.intent_exchange.uasl.entity.ConformityAssessmentExecutionEntityAircraftInfo;
import com.intent_exchange.uasl.entity.ConformityAssessmentResponseEntity;
import com.intent_exchange.uasl.logic.ConformityAssessmentReservationLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class ConformityAssessmentServiceTest {

  @Mock
  private ConformityAssessmentReservationLogic logic;

  @InjectMocks
  private ConformityAssessmentService service;

  private ConformityAssessmentExecutionEntity entity;

  private ConformityAssessmentExecutionDto dto;

  private ConformityAssessmentExecutionEntityAircraftInfo info;

  @BeforeEach
  void setUp() {
    entity = new ConformityAssessmentExecutionEntity();
    entity.setUaslSectionId("1");
    entity.setStartAt(new Date());
    entity.setEndAt(new Date());

    info = new ConformityAssessmentExecutionEntityAircraftInfo();
    info.setMaker("example_maker");
    info.setModelNumber("example_model");

    entity.setAircraftInfo(info);

    dto = new ConformityAssessmentExecutionDto();
    dto.setUaslId("1-1-1");
    dto.setStartAt(LocalDateTime.now());
    dto.setEndAt(LocalDateTime.now().plusHours(1));
  }

  @Test
  @DisplayName("適合性評価の実施 - 正常系")
  void testConformityAssessmentService1() {
    // ModelMapperUtilの静的メソッドをモック
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(entity, ConformityAssessmentExecutionDto.class))
          .thenReturn(dto);
      ConformityAssessmentResultDto dt = new ConformityAssessmentResultDto();
      dt.setMessage("message");
      dt.setType("event");
      ConformityAssessmentResponseEntity et = new ConformityAssessmentResponseEntity();
      et.setReasons("message");
      when(logic.checkReservationConformityAssessment(dto)).thenReturn(dt);
      mockedStatic.when(() -> ModelMapperUtil.map(dt, ConformityAssessmentResponseEntity.class))
          .thenReturn(et);
      ConformityAssessmentResponseEntity actual =
          service.executionReservationConformityAssessment(entity);
      assertEquals("message", actual.getReasons());

      // メソッドが1回呼び出されたことを確認
      verify(logic, times(1)).checkReservationConformityAssessment(dto);
    }
  }

  @Test
  @DisplayName("適合性評価の実施 - 準正常系")
  void testConformityAssessmentService2() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(entity, ConformityAssessmentExecutionDto.class))
          .thenReturn(dto);

      // logicのメソッドが例外をスローするように設定
      doThrow(new IllegalArgumentException("入力データが不正です")).when(logic)
          .checkReservationConformityAssessment(dto);

      // 例外をスローすることを確認
      IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
          () -> service.executionReservationConformityAssessment(entity));
      assertEquals("入力データが不正です", exception.getMessage());
    }
  }

  @Test
  @DisplayName("適合性評価の実施 - 異常系")
  void testConformityAssessmentService3() {
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(entity, ConformityAssessmentExecutionDto.class))
          .thenReturn(dto);

      // logicのメソッドが例外をスローするように設定
      doThrow(new RuntimeException("予期しない例外")).when(logic)
          .checkReservationConformityAssessment(dto);

      // 例外をスローすることを確認
      RuntimeException exception = assertThrows(RuntimeException.class,
          () -> service.executionReservationConformityAssessment(entity));
      assertEquals("予期しない例外", exception.getMessage());
    }
  }

  @Test
  @DisplayName("適合性評価の実施 - 正常系 typeがnull")
  void testConformityAssessmentService4() {
    // ModelMapperUtilの静的メソッドをモック
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(entity, ConformityAssessmentExecutionDto.class))
          .thenReturn(dto);
      ConformityAssessmentResultDto dt = new ConformityAssessmentResultDto();
      dt.setMessage("message");
      dt.setType(null);
      ConformityAssessmentResponseEntity et = new ConformityAssessmentResponseEntity();
      et.setReasons("message");
      when(logic.checkReservationConformityAssessment(dto)).thenReturn(dt);
      mockedStatic.when(() -> ModelMapperUtil.map(dt, ConformityAssessmentResponseEntity.class))
          .thenReturn(et);
      ConformityAssessmentResponseEntity actual =
          service.executionReservationConformityAssessment(entity);
      assertEquals("message", actual.getReasons());

      // メソッドが1回呼び出されたことを確認
      verify(logic, times(1)).checkReservationConformityAssessment(dto);
    }
  }

  @Test
  @DisplayName("適合性評価の実施 - 正常系 typeが空文字")
  void testConformityAssessmentService5() {
    // ModelMapperUtilの静的メソッドをモック
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(entity, ConformityAssessmentExecutionDto.class))
          .thenReturn(dto);
      ConformityAssessmentResultDto dt = new ConformityAssessmentResultDto();
      dt.setMessage("message");
      dt.setType(" ");
      ConformityAssessmentResponseEntity et = new ConformityAssessmentResponseEntity();
      et.setReasons("message");
      when(logic.checkReservationConformityAssessment(dto)).thenReturn(dt);
      mockedStatic.when(() -> ModelMapperUtil.map(dt, ConformityAssessmentResponseEntity.class))
          .thenReturn(et);
      ConformityAssessmentResponseEntity actual =
          service.executionReservationConformityAssessment(entity);
      assertEquals("message", actual.getReasons());

      // メソッドが1回呼び出されたことを確認
      verify(logic, times(1)).checkReservationConformityAssessment(dto);
    }
  }
}
