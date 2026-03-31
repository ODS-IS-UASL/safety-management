package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.intent_exchange.uasl.dao.UaslDesignAreaInfoReservationMapper;
import com.intent_exchange.uasl.dto.request.AreaInfoConditionDto;
import com.intent_exchange.uasl.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.uasl.model.UaslDesignAreaInfoReservation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/** UaslDesignAreaInfoResevasionNowDayLogicのテストクラス。 */

// TestConfigとModelMapperConfigのModelMapperインスタンス作成のbeenが重複してエラーのため、Bean 定義の上書きを許可します。
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
class UaslDesignAreaInfoResevasionNowDayLogicTest {

  @Mock
  private UaslDesignAreaInfoReservationMapper mapper;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private UaslDesignAreaInfoResevasionNowDayLogic logic;

  private List<UaslDesignAreaInfoReservation> reservations;

  @BeforeEach
  void setUp() {
    // モックのセットアップ
    UaslDesignAreaInfoReservation reservation = new UaslDesignAreaInfoReservation();
    reservations = Arrays.asList(reservation);

    when(mapper.selectNowDay()).thenReturn(reservations);
  }

  @Test
  @DisplayName("航路情報取得条件を取得する")
  void testGet1() {
    // 航路情報取得条件の取得の実行
    List<UaslDesignAreaInfoReservation> result = mapper.selectNowDay();

    // 結果がnullでないことを確認
    assertNotNull(result);

    // 結果のリストのサイズが1であることを確認
    assertEquals(1, result.size());

    // 結果のリストが期待される予約リストと一致することを確認
    assertEquals(reservations, result);

    // mapperのselectNowDayメソッドが呼び出されたことを確認
    verify(mapper).selectNowDay();
  }

  @Test
  @DisplayName("モデルクラスを適合性評価に使用するdtoクラスに変換しリターンする")
  void testGet2() {

    ConformityAssessmentExecutionDto executionDto = new ConformityAssessmentExecutionDto();

    // modelMapperのモックの実行動作
    when(modelMapper.map(any(UaslDesignAreaInfoReservation.class),
        eq(ConformityAssessmentExecutionDto.class))).thenReturn(executionDto);

    // ModelMapperUtil.mapListメソッドのモックの実行動作
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.mapList(any(List.class),
          eq(ConformityAssessmentExecutionDto.class))).thenAnswer(invocation -> {
            List<UaslDesignAreaInfoReservation> list = invocation.getArgument(0);
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

      // mapperのselectNowDayメソッドが呼び出されたことを確認
      verify(mapper).selectNowDay();

      // modelMapperのmapメソッドが呼び出されたことを確認
      verify(modelMapper).map(any(UaslDesignAreaInfoReservation.class),
          eq(ConformityAssessmentExecutionDto.class));
    }
  }
}
