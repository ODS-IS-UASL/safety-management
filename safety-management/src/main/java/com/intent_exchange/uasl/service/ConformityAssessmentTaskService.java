/*
 * Copyright 2025 Intent Exchange, Inc.
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.intent_exchange.uasl.service;

import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.intent_exchange.uasl.async.AsyncProcessor;
import com.intent_exchange.uasl.async.Processor;
import com.intent_exchange.uasl.dto.request.UaslDesignRailwayCrossingInfoDto;
import com.intent_exchange.uasl.dto.request.AreaInfoConditionDto;
import com.intent_exchange.uasl.dto.request.ConformityAssessmentExecutionDto;
import com.intent_exchange.uasl.logic.IFUaslDesignAreaInfoLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 運行中の適合性評価に関する処理をします。
 */
@Component
@Service
public class ConformityAssessmentTaskService {

  /** ロガー */
  private static final Logger logger =
      LoggerFactory.getLogger(ConformityAssessmentTaskService.class);

  @Autowired
  private ApplicationContext context;

  @Autowired
  private AsyncProcessor asyncProcessor;

  /**
   * 運行中の適合性評価に関する処理をします。
   *
   * @param AreaInfoConditionDto dto
   * @param dataClazz IFUaslDesignAreaInfoLogic
   * @param prcClazz Processor
   * 
   */
  public void conformityAssessment(AreaInfoConditionDto dto,
      Class<? extends IFUaslDesignAreaInfoLogic> dataClazz,
      Class<? extends Processor<ConformityAssessmentExecutionDto>> prcClazz) {
    try {
      IFUaslDesignAreaInfoLogic logic = context.getBean(dataClazz);
      // 対象データ取得
      List<ConformityAssessmentExecutionDto> dataList = logic.get(dto);

      // 鉄道運航情報変更通知の場合のみ
      if (dto.getStation1() != null && dto.getStation2() != null) {
        Iterator<ConformityAssessmentExecutionDto> iterator = dataList.iterator();
        while (iterator.hasNext()) {
          ConformityAssessmentExecutionDto data = iterator.next();

          // json型の文字列をDTOに変換
          UaslDesignRailwayCrossingInfoDto uaslDesignRailwayCrossingInfoDto = ModelMapperUtil
              .convertListMapToListDto(ModelMapperUtil.jsonToListMap(data.getRailwayCrossingInfo()),
                  UaslDesignRailwayCrossingInfoDto.class)
              .get(0);

          // 交点情報を持っていない場合、リストから削除し次の予約にスキップ
          if (uaslDesignRailwayCrossingInfoDto == null) {
            iterator.remove();
            continue;
          }
          // 変更通知を受けた駅と取得した運航中の予約の駅が一致しない場合リストから削除
          if (!(uaslDesignRailwayCrossingInfoDto.getStation1().equals(dto.getStation1())
              && uaslDesignRailwayCrossingInfoDto.getStation2().equals(dto.getStation2()))) {
            iterator.remove();
          }
        }

      }
      if (dto.getLogicClazz() != null) {
        dataList.forEach(d -> {
          d.setLogicClazz(dto.getLogicClazz());
          d.setRestricted(true);
        });
      }
      // // 疎通テスト用
      // List<ConformityAssessmentExecutionDto> dataList = new ArrayList<>();
      // ConformityAssessmentExecutionDto data = new ConformityAssessmentExecutionDto();
      // data.setAreaInfo(
      // "{\"type\": \"Polygon\",\"coordinates\": [[[100.0, 0.0],[101.0, 0.0],[101.0, 1.0],[100.0,
      // 1.0],[100.0, 0.0]]]}");
      // data.setRestricted(true);
      // dataList.add(data);
      // メソッド内で非同期処理を実施
      asyncProcessor.processDataList(dataList, prcClazz);
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }
}

