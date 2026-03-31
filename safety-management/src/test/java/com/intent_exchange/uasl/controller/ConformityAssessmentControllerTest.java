package com.intent_exchange.uasl.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intent_exchange.uasl.entity.ConformityAssessmentExecutionEntity;
import com.intent_exchange.uasl.entity.ConformityAssessmentExecutionEntityAircraftInfo;
import com.intent_exchange.uasl.entity.ConformityAssessmentResponseEntity;
import com.intent_exchange.uasl.exception.ConformityAssessmentException;
import com.intent_exchange.uasl.service.ConformityAssessmentService;

@WebMvcTest(ConformityAssessmentController.class)
class ConformityAssessmentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ConformityAssessmentService service;

  @Autowired
  private ObjectMapper objectMapper;

  private ConformityAssessmentExecutionEntity entity;

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
  }

  @Test
  @DisplayName("適合性評価 OK")
  void testExecutionConformityAssessment1() throws Exception {
    ConformityAssessmentResponseEntity et = new ConformityAssessmentResponseEntity();
    et.setEvaluationResults("evaluationResults");
    et.setReasons("reasons");
    et.setType(ConformityAssessmentResponseEntity.TypeEnum.EVENT);

    // モックサービスの設定
    when(service.executionReservationConformityAssessment(any())).thenReturn(et);

    // HTTPリクエストのシミュレーション
    mockMvc.perform(post("/conformity-assessment").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entity))).andExpect(status().isOk());
  }

  @Test
  @DisplayName("適合性評価 NG Bad Request")
  void testExecutionConformityAssessment2() throws Exception {
    // モックサービスの設定
    doThrow(new ConformityAssessmentException("Bad Request")).when(service)
        .executionReservationConformityAssessment(any());

    // HTTPリクエストのシミュレーション
    mockMvc.perform(post("/conformity-assessment").contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entity))).andExpect(status().isBadRequest());
  }

  @Test
  @DisplayName("適合性評価 NG Internal Server Error")
  void testExecutionConformityAssessment3() throws Exception {
    // モックサービスの設定
    doThrow(new RuntimeException("Internal Server Error")).when(service)
        .executionReservationConformityAssessment(any());

    // HTTPリクエストのシミュレーション
    mockMvc
        .perform(post("/conformity-assessment").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(entity)))
        .andExpect(status().isInternalServerError());
  }
}
