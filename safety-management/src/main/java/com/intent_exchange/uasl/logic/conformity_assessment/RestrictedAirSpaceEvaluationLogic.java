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

package com.intent_exchange.uasl.logic.conformity_assessment;

import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.dto.request.RestrictedAirSpaceDto;
import com.intent_exchange.uasl.dto.request.RestrictedAirSpaceRequestDto;
import com.intent_exchange.uasl.dto.response.RestrictedAirSpaceResponseDto;
import com.intent_exchange.uasl.logic.web.WebRestrictedAirSpaceLogic;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 規制/イベント情報の適合性評価ロジック
 */
@Component
public class RestrictedAirSpaceEvaluationLogic
    implements IFConformityAssessmentLogic<RestrictedAirSpaceDto> { 

  /**
   * 規制/イベント情報への通信用ロジック
   */
  @Autowired
  WebRestrictedAirSpaceLogic logic;

  /** ロガー */
  private static final Logger logger =
      LoggerFactory.getLogger(RestrictedAirSpaceEvaluationLogic.class);
  
  /**
   * 規制/イベント情報の適合性評価を行う
   * 
   * @param dto 規制/イベント情報のリクエスト情報
   * @return 規制/イベント情報の適合性評価結果
   */
  @SuppressWarnings({"unchecked"})
  @Override
  public Boolean check(RestrictedAirSpaceDto dto) {
    
    // StringのデータをList<List<Double>>に変換しrequestDtoに格納
    
    List<List<Double>> airSpace = new ArrayList<>();
    try {
        String areaInfo = dto.getAreaInfo();
        airSpace = ModelMapperUtil.listToString(areaInfo);
    } catch (IOException e) {
      logger.error(e.getMessage(), e);
    }
    List<String> timeScope = new ArrayList<>();
    timeScope.add(dto.getStartAt());
    timeScope.add(dto.getEndAt());
    RestrictedAirSpaceRequestDto requestDto = new RestrictedAirSpaceRequestDto(); 
    requestDto.setAirSpace(airSpace);
    requestDto.setTimeScope(timeScope);


    // 運航中の規制/イベント情報取得
    RestrictedAirSpaceResponseDto restrictedAirSpaceResponseDto = logic.getRestrictedAirSpace(requestDto);

    // 規制/イベント情報がNULLもしくは空の場合規制/イベントなし
    if ((restrictedAirSpaceResponseDto.getRestrictedAirSpace() == null)
        || (restrictedAirSpaceResponseDto.getRestrictedAirSpace().size() == 0)) {
      return true;
    }
    return false;
  }

}

