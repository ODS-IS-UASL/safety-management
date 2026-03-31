package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.intent_exchange.uasl.dao.UaslDesignAreaInfoSectionMapper;
import com.intent_exchange.uasl.dto.request.UaslDesignAreaInfoSectionDto;
import com.intent_exchange.uasl.model.UaslDesignAreaInfoSection;
import com.intent_exchange.uasl.util.ModelMapperUtil;

@ExtendWith(MockitoExtension.class)
class UaslDesignAreaInfoSectionLogicTest {

  @Mock
  private UaslDesignAreaInfoSectionMapper mapper;

  @InjectMocks
  private UaslDesignAreaInfoSectionLogic logic;

  private UaslDesignAreaInfoSection model;
  private UaslDesignAreaInfoSectionDto dto;

  @BeforeEach
  void setUp() {
    model = new UaslDesignAreaInfoSection();
    model.setUaslId("1-1-1");
    model.setUaslSectionId("1");
    model.setAltitude("Test Altitude");
    model.setLatStart("Example");
    model.setLonStart("Example");
    model.setLatEnd("Example");
    model.setLonEnd("Example");
    model.setGeometry(new Object());
    model.setAreaInfo("Example");

    dto = new UaslDesignAreaInfoSectionDto();
    dto.setUaslId("1-1-1");
    dto.setUaslSectionId("1");
    dto.setAltitude("Test Altitude");
    dto.setLatStart("Example");
    dto.setLonStart("Example");
    dto.setLatEnd("Example");
    dto.setLonEnd("Example");
    dto.setGeometry(new Object());
    dto.setAreaInfo("Example");
  }

  @Test
  @DisplayName("航路情報の取得_成功")
  void testGet1() {
    when(mapper.selectByPrimaryKey("1")).thenReturn(model);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(model, UaslDesignAreaInfoSectionDto.class))
          .thenReturn(dto);

      // メソッドの呼び出し
      UaslDesignAreaInfoSectionDto result = logic.get("1");

      // 結果の検証
      assertNotNull(result);
      assertEquals("1-1-1", result.getUaslId());
      assertEquals("1", result.getUaslSectionId());
      assertEquals("Test Altitude", result.getAltitude());
      assertEquals("Example", result.getLatStart());
      assertEquals("Example", result.getLonStart());
      assertEquals("Example", result.getLatEnd());
      assertEquals("Example", result.getLonEnd());
      assertNotNull(result.getGeometry());
      assertEquals("Example", result.getAreaInfo());

      // モックの検証
      verify(mapper, times(1)).selectByPrimaryKey("1");
    }
  }

  @Test
  @DisplayName("航路情報の取得_取得できず失敗")
  void testGet2() {
    when(mapper.selectByPrimaryKey("1")).thenReturn(null);

    try (MockedStatic<ModelMapperUtil> mockedStatic = mockStatic(ModelMapperUtil.class)) {
      mockedStatic.when(() -> ModelMapperUtil.map(null, UaslDesignAreaInfoSectionDto.class))
          .thenReturn(null);

      // メソッドの呼び出し
      UaslDesignAreaInfoSectionDto result = logic.get("1");

      // 結果の検証
      assertNull(result);

      // モックの検証
      verify(mapper, times(1)).selectByPrimaryKey("1");
    }
  }

  @Test
  @DisplayName("航路情報の取得_例外発生")
  void testGet3() {
    // モックの設定
    when(mapper.selectByPrimaryKey("1")).thenThrow(new RuntimeException("DB error"));

    // 例外がスローされることを確認
    RuntimeException exception = assertThrows(RuntimeException.class, () -> logic.get("1"));
    assertEquals("DB error", exception.getMessage());

    // モックの検証
    verify(mapper, times(1)).selectByPrimaryKey("1");
  }
}
