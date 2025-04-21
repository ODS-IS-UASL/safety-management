package com.intent_exchange.drone_highway.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import com.intent_exchange.drone_highway.async.AsyncProcessor;
import com.intent_exchange.drone_highway.async.Processor;
import com.intent_exchange.drone_highway.dto.request.AreaInfoConditionDto;
import com.intent_exchange.drone_highway.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.drone_highway.logic.AirwayDesignAreaInfoOperationProcessorLogic;
import com.intent_exchange.drone_highway.logic.IFAirwayDesignAreaInfoLogic;
import com.intent_exchange.drone_highway.logic.conformity_assessment.RailwayOperationInformationEvaluationLogic;

/**
 * ConformityAssessmentTaskServiceのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class ConformityAssessmentTaskServiceTest {

  @Mock
  private ApplicationContext context;

  @Mock
  private AsyncProcessor asyncProcessor;

  @Mock
  private IFAirwayDesignAreaInfoLogic logic;

  @Mock
  private Processor<ConformityAssessmentExecutionDto> processor;

  @InjectMocks
  private ConformityAssessmentTaskService service;

  /**
   * conformityAssessmentOperationメソッドのテストを行います。
   */
  @Test
  @DisplayName("運行中の適合性評価に関する処理を実行")
  void testConformityAssessmentOperation() {
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    List<ConformityAssessmentExecutionDto> dataList = new ArrayList<>();
    when(context.getBean(IFAirwayDesignAreaInfoLogic.class)).thenReturn(logic);
    when(logic.get(dto)).thenReturn(dataList);
    Class<? extends Processor<ConformityAssessmentExecutionDto>> prcClazz =
        AirwayDesignAreaInfoOperationProcessorLogic.class;
    assertDoesNotThrow(
        () -> service.conformityAssessment(dto, IFAirwayDesignAreaInfoLogic.class, prcClazz));
    verify(asyncProcessor).processDataList(dataList, prcClazz);
  }

  @Test
  @DisplayName("鉄道運行情報変更通知時、5件の運航中データのうち2件が適合性評価対象")
  void testConformityAssessmentWithRailwayNotification() {
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    dto.setStation1("西部秩父駅");
    dto.setStation2("横瀬駅");
    dto.setLogicClazz(RailwayOperationInformationEvaluationLogic.class);

    ConformityAssessmentExecutionDto data1 = new ConformityAssessmentExecutionDto();
    data1.setRailwayCrossingInfo("[{\"station1\":\"西部秩父駅\",\"station2\":\"横瀬駅\"}]");

    ConformityAssessmentExecutionDto data2 = new ConformityAssessmentExecutionDto();
    data2.setRailwayCrossingInfo("[{\"station1\":\"西部秩父駅\",\"station2\":\"他の駅\"}]");

    ConformityAssessmentExecutionDto data3 = new ConformityAssessmentExecutionDto();
    data3.setRailwayCrossingInfo("[{\"station1\":\"他の駅\",\"station2\":\"横瀬駅\"}]");

    ConformityAssessmentExecutionDto data4 = new ConformityAssessmentExecutionDto();
    data4.setRailwayCrossingInfo("[null]");

    ConformityAssessmentExecutionDto data5 = new ConformityAssessmentExecutionDto();
    data5.setRailwayCrossingInfo("[{\"station1\":\"西部秩父駅\",\"station2\":\"横瀬駅\"}]");

    List<ConformityAssessmentExecutionDto> dataList =
        new ArrayList<>(Arrays.asList(data1, data2, data3, data4, data5));

    List<ConformityAssessmentExecutionDto> processDataList =
        new ArrayList<>(Arrays.asList(data1, data5));

    when(context.getBean(IFAirwayDesignAreaInfoLogic.class)).thenReturn(logic);
    when(logic.get(dto)).thenReturn(dataList);

    Class<? extends Processor<ConformityAssessmentExecutionDto>> prcClazz =
        AirwayDesignAreaInfoOperationProcessorLogic.class;

    assertDoesNotThrow(
        () -> service.conformityAssessment(dto, IFAirwayDesignAreaInfoLogic.class, prcClazz));
    verify(asyncProcessor).processDataList(processDataList, prcClazz);
  }

  @Test
  @DisplayName("鉄道運行情報変更通知時,station2がnull")
  void testConformityAssessmentWithRailwayNotification2() {
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    dto.setStation1("西部秩父駅");
    List<ConformityAssessmentExecutionDto> dataList = new ArrayList<>();
    when(context.getBean(IFAirwayDesignAreaInfoLogic.class)).thenReturn(logic);
    when(logic.get(dto)).thenReturn(dataList);
    Class<? extends Processor<ConformityAssessmentExecutionDto>> prcClazz =
        AirwayDesignAreaInfoOperationProcessorLogic.class;
    assertDoesNotThrow(
        () -> service.conformityAssessment(dto, IFAirwayDesignAreaInfoLogic.class, prcClazz));
    verify(asyncProcessor).processDataList(dataList, prcClazz);
  }

  @Test
  @DisplayName("鉄道運行情報変更通知時、JsonToListMapでエラー")
  void testConformityAssessmentWithRailwayNotification3() {
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    dto.setStation1("西部秩父駅");
    dto.setStation2("横瀬駅");
    dto.setLogicClazz(RailwayOperationInformationEvaluationLogic.class);

    ConformityAssessmentExecutionDto data1 = new ConformityAssessmentExecutionDto();
    data1.setRailwayCrossingInfo("info");

    ConformityAssessmentExecutionDto data2 = new ConformityAssessmentExecutionDto();
    data2.setRailwayCrossingInfo("[{\"station1\":\"西部秩父駅\",\"station2\":\"他の駅\"}]");

    ConformityAssessmentExecutionDto data3 = new ConformityAssessmentExecutionDto();
    data3.setRailwayCrossingInfo("[{\"station1\":\"他の駅\",\"station2\":\"横瀬駅\"}]");

    ConformityAssessmentExecutionDto data4 = new ConformityAssessmentExecutionDto();
    data4.setRailwayCrossingInfo("[null]");

    ConformityAssessmentExecutionDto data5 = new ConformityAssessmentExecutionDto();
    data5.setRailwayCrossingInfo("[{\"station1\":\"西部秩父駅\",\"station2\":\"横瀬駅\"}]");

    List<ConformityAssessmentExecutionDto> dataList =
        new ArrayList<>(Arrays.asList(data1, data2, data3, data4, data5));

    List<ConformityAssessmentExecutionDto> processDataList =
        new ArrayList<>(Arrays.asList(data1, data5));

    when(context.getBean(IFAirwayDesignAreaInfoLogic.class)).thenReturn(logic);
    when(logic.get(dto)).thenReturn(dataList);

    Class<? extends Processor<ConformityAssessmentExecutionDto>> prcClazz =
        AirwayDesignAreaInfoOperationProcessorLogic.class;

    service.conformityAssessment(dto, IFAirwayDesignAreaInfoLogic.class, prcClazz);
    verify(asyncProcessor, never()).processDataList(processDataList, prcClazz);
  }
}
