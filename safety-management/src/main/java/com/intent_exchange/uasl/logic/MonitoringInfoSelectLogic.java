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

package com.intent_exchange.uasl.logic;

import java.util.List;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.intent_exchange.uasl.dao.MonitoringInformationMapper;
import com.intent_exchange.uasl.dto.request.NearMissInformationRequestSelectDto;
import com.intent_exchange.uasl.dto.response.MonitoringInfoDetailDto;
import com.intent_exchange.uasl.model.MonitoringInformation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 安全管理支援(SafetyManagement)のDBに関連する処理をします。
 */
@Component
public class MonitoringInfoSelectLogic {
  /**
   * MonitoringInformationMapperのインスタンス。 航路設計エリア情報の操作に使用されます。
   */
  @Autowired
  private MonitoringInformationMapper monitoringInformationMapper;



  /**
   * 第三者立入監視情報リストを取得します。
   * 
   * @param dto ヒヤリハット情報要求
   * @return 第三者立入監視情報テーブルからのリスト
   */
  @Transactional
  public List<MonitoringInfoDetailDto> moniterSelect(NearMissInformationRequestSelectDto dto) {
    /** 第三者立入監視情報リストを取得します。 */

    MonitoringInformation map = ModelMapperUtil.map(dto, MonitoringInformation.class);
    List<MonitoringInformation> list = monitoringInformationMapper.moniterSelect(map);
    return ModelMapperUtil.mapList(list, MonitoringInfoDetailDto.class);
  }
}

