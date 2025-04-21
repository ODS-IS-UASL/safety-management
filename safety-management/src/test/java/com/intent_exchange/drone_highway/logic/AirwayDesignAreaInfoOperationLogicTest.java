package com.intent_exchange.drone_highway.logic;

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
import com.intent_exchange.drone_highway.dao.AirwayDesignAreaInfoOperationMapper;
import com.intent_exchange.drone_highway.dto.request.AreaInfoConditionDto;
import com.intent_exchange.drone_highway.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.drone_highway.model.AirwayDesignAreaInfoOperation;
import com.intent_exchange.drone_highway.util.PropertyUtil;

/**
 * AirwayDesignAreaInfoOperationLogicのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
class AirwayDesignAreaInfoOperationLogicTest {

  @Mock
  private AirwayDesignAreaInfoOperationMapper mapper;

  @InjectMocks
  private AirwayDesignAreaInfoOperationLogic logic;

  /**
   * operationメソッドのテストを行います。
   */
  @Test
  @DisplayName("対象予約、航路情報取得条件を取得します。")
  void testGet() {

    int interval = (int) (PropertyUtil.getPropertyInt("operation.task.schedule.rate") * 1.05);
    AreaInfoConditionDto dto = new AreaInfoConditionDto();
    List<AirwayDesignAreaInfoOperation> list = new ArrayList<>();
    when(mapper.selectAll(interval, dto.getRestrictedArea())).thenReturn(list);

    // Act
    List<ConformityAssessmentExecutionDto> dataList = logic.get(dto);

    // Assert
    assertEquals(list, dataList);
    verify(mapper).selectAll(interval, dto.getRestrictedArea());
  }
}
