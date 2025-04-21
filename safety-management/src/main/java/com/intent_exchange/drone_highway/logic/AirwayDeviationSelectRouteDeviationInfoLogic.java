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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.drone_highway.dao.AirwayDesignAreaInfoDeviationMapper;
import com.intent_exchange.drone_highway.dto.request.AirwayDesignAreaInfoDeviationDto;
import com.intent_exchange.drone_highway.model.AirwayDesignAreaInfoDeviation;
import com.intent_exchange.drone_highway.util.ModelMapperUtil;
import jakarta.transaction.Transactional;

@Component
public class AirwayDeviationSelectRouteDeviationInfoLogic {

  @Autowired
  private AirwayDesignAreaInfoDeviationMapper mapper;

  /**
   * 航路逸脱情報テーブルに登録するための情報を航路逸脱情報ビューより取得します。
   */
  @Transactional
  public List<AirwayDesignAreaInfoDeviationDto> get() {

    List<AirwayDesignAreaInfoDeviation> list = mapper.selectRouteDeviationInfo();

    return ModelMapperUtil.mapList(list, AirwayDesignAreaInfoDeviationDto.class);
  }
}

