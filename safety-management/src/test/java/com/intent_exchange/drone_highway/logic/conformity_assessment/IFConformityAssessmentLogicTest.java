package com.intent_exchange.drone_highway.logic.conformity_assessment;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class IFConformityAssessmentLogicTest {

  private static class TestConformityAssessmentLogic
      implements IFConformityAssessmentLogic<Object> {
    @Override
    public Boolean check(Object dto) {
      return dto != null;
    }
  }

  @Test
  @DisplayName("checkメソッドのテスト - 正常系")
  void testCheck() {
    TestConformityAssessmentLogic logic = new TestConformityAssessmentLogic();
    assertTrue(logic.check(new Object()));
    assertFalse(logic.check(null));
  }

  @Test
  @DisplayName("getClassInfoメソッドのテスト")
  void testGetClassInfo() {
    TestConformityAssessmentLogic logic = new TestConformityAssessmentLogic();
    assertEquals("TestConformityAssessmentLogic", logic.getClassInfo());
  }

  @Test
  @DisplayName("getGenericTypeClassメソッドのテスト")
  void testGetGenericTypeClass() {
    TestConformityAssessmentLogic logic = new TestConformityAssessmentLogic();
    assertEquals(Object.class, logic.getGenericTypeClass());
  }
}
