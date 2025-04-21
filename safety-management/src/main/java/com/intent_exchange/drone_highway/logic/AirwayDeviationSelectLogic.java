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

package com.intent_exchange.drone_highway.logic;

import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dao.AirwayDeviationMapper;
import com.intent_exchange.drone_highway.dto.request.NearMissInformationRequestSelectDto;
import com.intent_exchange.drone_highway.dto.response.AirwayDeviationDetailDto;
import com.intent_exchange.drone_highway.model.AirwayDeviation;
import com.intent_exchange.drone_highway.model.AirwayDeviationSelect;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;

/**
 * 安全管理支援(A-1-4)のDBに関連する処理をします。
 */
@Component
public class AirwayDeviationSelectLogic {

  /**
   * AirwayDeviationMapperのインスタンス。 航路設計エリア情報の操作に使用されます。
   */
  @Autowired
  private AirwayDeviationMapper airwayDeviationMapper;

  /**
   * 航路逸脱情報リストを取得します
   * 
   * @param dto ヒヤリハット情報要求パラメータ
   * @return 逸脱情報テーブルからのリスト
   */
  @Transactional
  public List<AirwayDeviationDetailDto> nermissSelect(NearMissInformationRequestSelectDto dto) {
    AirwayDeviationSelect map = ModelMapperUtil.map(dto, AirwayDeviationSelect.class);
    List<AirwayDeviation> list = airwayDeviationMapper.nermissSelect(map);
    return ModelMapperUtil.mapList(list, AirwayDeviationDetailDto.class);
  }
}

