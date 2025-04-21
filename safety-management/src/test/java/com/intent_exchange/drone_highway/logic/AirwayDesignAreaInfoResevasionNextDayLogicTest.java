package com.intent_exchange.drone_highway.logic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import com.intent_exchange.drone_highway.dao.AirwayDesignAreaInfoReservationMapper;
import com.intent_exchange.drone_highway.dto.request.AreaInfoConditionDto;
import com.intent_exchange.drone_highway.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.drone_highway.model.AirwayDesignAreaInfoReservation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/** AirwayDesignAreaInfoResevasionNextDayLogicのテストクラス。*/

// TestConfigとModelMapperConfigのModelMapperインスタンス作成のbeenが重複してエラーのため、Bean 定義の上書きを許可します。
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
class AirwayDesignAreaInfoResevasionNextDayLogicTest {

  @Mock
  private AirwayDesignAreaInfoReservationMapper mapper;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private AirwayDesignAreaInfoResevasionNextDayLogic logic;

  private List<AirwayDesignAreaInfoReservation> reservations;

  @BeforeEach
  void setUp() {
    // モックのセットアップ
    AirwayDesignAreaInfoReservation reservation = new AirwayDesignAreaInfoReservation();
    reservations = Arrays.asList(reservation);

    when(mapper.selectNextDay()).thenReturn(reservations);
  }

  @Test
  @DisplayName("航路情報取得条件を取得する")
  void testGet1() {
    // 航路情報取得条件の取得の実行
    List<AirwayDesignAreaInfoReservation> result = mapper.selectNextDay();

    // 結果がnullでないことを確認
    assertNotNull(result);

    // 結果のリストのサイズが1であることを確認
    assertEquals(1, result.size());

    // 結果のリストが期待される予約リストと一致することを確認
    assertEquals(reservations, result);

    // mapperのselectNextDayメソッドが呼び出されたことを確認
    verify(mapper).selectNextDay();
  }

  @Test
  @DisplayName("モデルクラスを適合性評価に使用するdtoクラスに変換しリターンする")
  void testGet2() {

    ConformityAssessmentExecutionDto executionDto = new ConformityAssessmentExecutionDto();

    // modelMapperのモックの実行動作
    when(modelMapper.map(any(AirwayDesignAreaInfoReservation.class),
        eq(ConformityAssessmentExecutionDto.class))).thenReturn(executionDto);

    // ModelMapperUtil.mapListメソッドのモックの実行動作
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.mapList(any(List.class),
          eq(ConformityAssessmentExecutionDto.class))).thenAnswer(invocation -> {
            List<AirwayDesignAreaInfoReservation> list = invocation.getArgument(0);
            return list.stream()
                .map(reservation -> modelMapper.map(reservation,
                    ConformityAssessmentExecutionDto.class))
                .collect(Collectors.toList());
          });

      AreaInfoConditionDto dto = new AreaInfoConditionDto();
      // 航路情報取得条件の取得の実行
      List<ConformityAssessmentExecutionDto> result = logic.get(dto);

      // 結果がnullでないことを確認
      assertNotNull(result);

      // 結果のリストのサイズが1であることを確認
      assertEquals(1, result.size());

      // mapperのselectNextDayメソッドが呼び出されたことを確認
      verify(mapper).selectNextDay();

      // modelMapperのmapメソッドが呼び出されたことを確認
      verify(modelMapper).map(any(AirwayDesignAreaInfoReservation.class),
          eq(ConformityAssessmentExecutionDto.class));
    }
  }
}
