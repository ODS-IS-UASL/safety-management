package com.intent_exchange.uasl.logic;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
import com.intent_exchange.uasl.config.TestConfig;

/**
 * UaslDeviationCalculatorのテストクラス。
 */
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class UaslDeviationCalculatorTest {

  /** テスト対象クラス */
  @InjectMocks
  private UaslDeviationCalculator uaslDeviationCalculator;
  
  @Test
  @DisplayName("95パーセンタイル値を算出(逸脱量リストがnull)")
  void testCalculatePercentileStr1() {
    // 95パーセンタイル値を算出する逸脱量リスト(null)
    List<Double> deviationList = null;
    // テスト対象メソッドを実行
    String result = uaslDeviationCalculator.calcPercentileStr(deviationList);
    // 結果の検証
    // 対象のデータ無しのため0.0を返却
    assertEquals("0.0", result);
  }
  
  @Test
  @DisplayName("95パーセンタイル値を算出(逸脱量リストが空)")
  void testCalculatePercentileStr2() {
    // 95パーセンタイル値を算出する逸脱量リスト(空)
    List<Double> deviationList = new ArrayList<>();
    // テスト対象メソッドを実行
    String result = uaslDeviationCalculator.calcPercentileStr(deviationList);
    // 結果の検証
    // 対象のデータ無しのため0.0になる
    assertEquals("0.0", result);
  }
  
  @Test
  @DisplayName("95パーセンタイル値を算出(逸脱量リストがデータ1件)")
  void testCalculatePercentileStr3() {
    // 95パーセンタイル値を算出する逸脱量リスト
    List<Double> deviationList = Arrays.asList(10.0);
    // テスト対象メソッドを実行
    String result = uaslDeviationCalculator.calcPercentileStr(deviationList);
    // 結果の検証
    // 分割位置=0.95*(1+1)=1.9 → 両サイド(1番目と2番目)の平均値は取れないため1番目の値になる
    assertEquals("10.0", result);
  }
  
  @Test
  @DisplayName("95パーセンタイル値を算出(逸脱量リストがデータ2件)")
  void testCalculatePercentileStr4() {
    // 95パーセンタイル値を算出する逸脱量リスト
    List<Double> deviationList = Arrays.asList(10.0, 20.0);
    // テスト対象メソッドを実行
    String result = uaslDeviationCalculator.calcPercentileStr(deviationList);
    // 結果の検証
    // 分割位置=0.95*(2+1)=2.85 → 両サイド(2番目と3番目)の平均値を取れないため2番目の値になる
    assertEquals("20.0", result);
  }

  @Test
  @DisplayName("95パーセンタイル値を算出(逸脱量リストがデータ3件)")
  void testCalculatePercentileStr5() {
    // 95パーセンタイル値を算出する逸脱量リスト
    List<Double> deviationList = Arrays.asList(10.0, 20.0, 30.0);
    // テスト対象メソッドを実行
    String result = uaslDeviationCalculator.calcPercentileStr(deviationList);
    // 結果の検証
    // 分割位置=0.95*(3+1)=3.8 → 両サイド(3番目と4番目)の平均値を取れないため3番目の値になる
    assertEquals("30.0", result);
  }
  
  @Test
  @DisplayName("95パーセンタイル値算出(逸脱量リストがデータ10件)")
  void testCalculatePercentileStr6() {
    // 95パーセンタイル値を算出する逸脱量リスト
    List<Double> deviationList = Arrays.asList(
        10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0);
    // テスト対象メソッドを実行
    String result = uaslDeviationCalculator.calcPercentileStr(deviationList);
    // 結果の検証
    // 分割位置=0.95*(10+1)=10.45 → 両サイド(10番目と11番目)の平均値を取れないため10番目の値になる
    assertEquals("100.0", result);
  }
  
  @Test
  @DisplayName("95パーセンタイル値算出(逸脱量リストがデータ10件 ※順不同データ。昇順ソート確認用。)")
  void すい() {
    // 95パーセンタイル値を算出する逸脱量リスト
    List<Double> deviationList = Arrays.asList(
        100.0, 20.0, 20.0, 80.0, 50.0, 60.0, 77.0, 12.0, 30.0, 33.0);
    // テスト対象メソッドを実行
    String result = uaslDeviationCalculator.calcPercentileStr(deviationList);
    // 結果の検証
    // 分割位置=0.95*(10+1)=10.45 → 両サイド(10番目と11番目)の平均値を取れないため昇順ソートした10番目の値になる
    assertEquals("100.0", result);
  }
  
  @Test
  @DisplayName("95パーセンタイル値算出(逸脱量リストがデータ20件)")
  void testCalculatePercentileStr8() {
    // 95パーセンタイル値を算出する逸脱量リスト
    List<Double> deviationList = Arrays.asList(
        10.0, 15.0, 20.0, 20.0, 20.0, 30.0, 40.0, 50.0, 55.0, 60.0,
        63.0, 70.0, 72.0, 72.0, 80.0, 89.0, 90.0, 90.0, 95.0, 98.0);
    // テスト対象メソッドを実行
    String result = uaslDeviationCalculator.calcPercentileStr(deviationList);
    // 結果の検証
    // 分割位置=0.95*(20+1)=19.05 → 両サイド(19番目と20番目)の平均値=(95.0+98.0)÷2=96.50
    assertEquals("96.50", result);
  }
  
  @Test
  @DisplayName("95パーセンタイル値算出(逸脱量リストがデータ100件 ※分割位置が整数値となるケース)")
  void testCalculatePercentileStr9() {
    // 95パーセンタイル値を算出する逸脱量リスト
    List<Double> deviationList = Arrays.asList(
        101.0, 102.0, 103.0, 104.0, 105.0, 106.0, 107.0, 108.0, 109.0, 110.0,
        111.0, 112.0, 113.0, 114.0, 115.0, 116.0, 117.0, 118.0, 119.0, 120.0,
        121.0, 122.0, 123.0, 124.0, 125.0, 126.0, 127.0, 128.0, 129.0, 130.0,
        131.0, 132.0, 133.0, 134.0, 135.0, 136.0, 137.0, 138.0, 139.0, 140.0,
        141.0, 142.0, 143.0, 144.0, 145.0, 146.0, 147.0, 148.0, 149.0, 150.0,
        151.0, 152.0, 153.0, 154.0, 155.0, 156.0, 157.0, 158.0, 159.0, 160.0,
        161.0, 162.0, 163.0, 164.0, 165.0, 166.0, 167.0, 168.0, 169.0, 170.0,
        171.0, 172.0, 173.0, 174.0, 175.0, 176.0, 177.0, 178.0, 179.0, 180.0,
        181.0, 182.0, 183.0, 184.0, 185.0, 186.0, 187.0, 188.0, 189.0, 190.0,
        191.0, 192.0, 193.0, 194.0, 195.0, 196.0, 197.0, 198.0, 199.0);
    // テスト対象メソッドを実行
    String result = uaslDeviationCalculator.calcPercentileStr(deviationList);
    // 結果の検証
    // 分割位置=0.95*(99+1)=95 → 95番目の値=195.0
    assertEquals("195.0", result);
  }

}
