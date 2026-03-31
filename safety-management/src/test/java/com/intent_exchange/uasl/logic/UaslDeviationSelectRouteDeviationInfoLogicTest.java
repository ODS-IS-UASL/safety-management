package com.intent_exchange.uasl.logic;

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
import com.intent_exchange.uasl.dao.UaslDesignAreaInfoDeviationMapper;
import com.intent_exchange.uasl.dto.request.UaslDesignAreaInfoDeviationDto;
import com.intent_exchange.uasl.model.UaslDesignAreaInfoDeviation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/** UaslDeviationSelectRouteDeviationInfoLogicのテストクラス。*/

// TestConfigとModelMapperConfigのModelMapperインスタンス作成のbeenが重複してエラーのため、Bean 定義の上書きを許可します。
@ExtendWith(MockitoExtension.class)
class UaslDeviationSelectRouteDeviationInfoLogicTest {

  @Mock
  private UaslDesignAreaInfoDeviationMapper mapper;

  @Mock
  private ModelMapper modelMapper;

  @InjectMocks
  private UaslDeviationSelectRouteDeviationInfoLogic logic;

  private List<UaslDesignAreaInfoDeviation> deviations;

  @BeforeEach
  void setUp() {
    // モックのセットアップ
    UaslDesignAreaInfoDeviation deviation = new UaslDesignAreaInfoDeviation();
    deviations = Arrays.asList(deviation);

    when(mapper.selectRouteDeviationInfo()).thenReturn(deviations);
  }

  @Test
  @DisplayName("航路逸脱情報を取得する")
  void testGet1() {
    // 航路逸脱情報の取得の実行
    List<UaslDesignAreaInfoDeviation> result = mapper.selectRouteDeviationInfo();

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

    UaslDesignAreaInfoDeviationDto deviationDto = new UaslDesignAreaInfoDeviationDto();

    // modelMapperのモックの実行動作
    when(modelMapper.map(any(UaslDesignAreaInfoDeviation.class),
        eq(UaslDesignAreaInfoDeviationDto.class))).thenReturn(deviationDto);

    // ModelMapperUtil.mapListメソッドのモックの実行動作
    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.mapList(any(List.class),
          eq(UaslDesignAreaInfoDeviationDto.class))).thenAnswer(invocation -> {
            List<UaslDesignAreaInfoDeviation> list = invocation.getArgument(0);
            return list.stream()
                .map(deviation -> modelMapper.map(deviation,
                    UaslDesignAreaInfoDeviationDto.class))
                .collect(Collectors.toList());
          });

      // 航路逸脱情報の取得の実行
      List<UaslDesignAreaInfoDeviationDto> result = logic.get();

      // 結果がnullでないことを確認
      assertNotNull(result);

      // 結果のリストのサイズが1であることを確認
      assertEquals(1, result.size());

      // mapperのselectRouteDeviationInfoメソッドが呼び出されたことを確認
      verify(mapper).selectRouteDeviationInfo();

      // modelMapperのmapメソッドが呼び出されたことを確認
      verify(modelMapper).map(any(UaslDesignAreaInfoDeviation.class),
          eq(UaslDesignAreaInfoDeviationDto.class));
    }
  }
}
