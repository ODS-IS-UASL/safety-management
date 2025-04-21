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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import com.intent_exchange.drone_highway.dao.AirwayDesignAreaInfoDeviationMapper;
import com.intent_exchange.drone_highway.dto.request.AirwayDesignAreaInfoDeviationDto;
import com.intent_exchange.drone_highway.model.AirwayDesignAreaInfoDeviation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/** AirwayDeviationSelectRouteDeviationInfoLogicのテストクラス。*/

// TestConfigとModelMapperConfigのModelMapperインスタンス作成のbeenが重複してエラーのため、Bean 定義の上書きを許可します。
@ExtendWith(MockitoExtension.class)
class AirwayDeviationSelectRouteDeviationInfoLogicTest {

  @Mock
  private AirwayDesignAreaInfoDeviationMapper mapper;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private AirwayDeviationSelectRouteDeviationInfoLogic logic;

  private List<AirwayDesignAreaInfoDeviation> deviations;

  @BeforeEach
  void setUp() {
    // モックのセットアップ
    AirwayDesignAreaInfoDeviation deviation = new AirwayDesignAreaInfoDeviation();
    deviations = Arrays.asList(deviation);

    when(mapper.selectRouteDeviationInfo()).thenReturn(deviations);
  }

  @Test
  @DisplayName("航路逸脱情報を取得する")
  void testGet1() {
    // 航路逸脱情報の取得の実行
    List<AirwayDesignAreaInfoDeviation> result = mapper.selectRouteDeviationInfo();

    // 結果がnullでないことを確認
    assertNotNull(result);

    // 結果のリストのサイズが1であることを確認
    assertEquals(1, result.size());

    // 結果のリストが期待される逸脱情報リストと一致することを確認
    assertEquals(deviations, result);

    // mapperのselectRouteDeviationInfoメソッドが呼び出されたことを確認
    verify(mapper).selectRouteDeviationInfo();
  }

  @Test
  @DisplayName("モデルクラスをDTOクラスに変換しリターンする")
  void testGet2() {

    AirwayDesignAreaInfoDeviationDto deviationDto = new AirwayDesignAreaInfoDeviationDto();

    // modelMapperのモックの実行動作
    when(modelMapper.map(any(AirwayDesignAreaInfoDeviation.class),
        eq(AirwayDesignAreaInfoDeviationDto.class))).thenReturn(deviationDto);

    // ModelMapperUtil.mapListメソッドのモックの実行動作
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.mapList(any(List.class),
          eq(AirwayDesignAreaInfoDeviationDto.class))).thenAnswer(invocation -> {
            List<AirwayDesignAreaInfoDeviation> list = invocation.getArgument(0);
            return list.stream()
                .map(deviation -> modelMapper.map(deviation,
                    AirwayDesignAreaInfoDeviationDto.class))
                .collect(Collectors.toList());
          });

      // 航路逸脱情報の取得の実行
      List<AirwayDesignAreaInfoDeviationDto> result = logic.get();

      // 結果がnullでないことを確認
      assertNotNull(result);

      // 結果のリストのサイズが1であることを確認
      assertEquals(1, result.size());

      // mapperのselectRouteDeviationInfoメソッドが呼び出されたことを確認
      verify(mapper).selectRouteDeviationInfo();

      // modelMapperのmapメソッドが呼び出されたことを確認
      verify(modelMapper).map(any(AirwayDesignAreaInfoDeviation.class),
          eq(AirwayDesignAreaInfoDeviationDto.class));
    }
  }
}