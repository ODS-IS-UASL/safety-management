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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.intent_exchange.uasl.dao.MonitoringInformationMapper;
import com.intent_exchange.uasl.dto.request.MonitoringInformationDto;
import com.intent_exchange.uasl.dto.request.ThirdPartyMonitoringInformationRequestDto;
import com.intent_exchange.uasl.dto.response.ThirdPartyMonitoringInformationResponseDto;
import com.intent_exchange.uasl.model.MonitoringInformation;
import com.intent_exchange.uasl.util.ModelMapperUtil;

/**
 * 第三者立入監視情報の登録するに関するロジックを提供します。
 */
@Component
public class MonitoringInformationLogic {

  @Autowired
  private MonitoringInformationMapper mapper;

  /**
   * 第三者立入監視情報を登録します。
   * 
   * @param monitoringInfoDto 第三者立入監視情報
   * @param dto 第三者立入監視のID情報
   */
  @Transactional
  public void insert(ThirdPartyMonitoringInformationResponseDto monitoringInfoDto,
      ThirdPartyMonitoringInformationRequestDto dto) {

    MonitoringInformationDto MonitoringInformationDto = new MonitoringInformationDto();
    MonitoringInformationDto.setUaslReservationId(dto.getUaslReservationId());
    MonitoringInformationDto.setMonitoringInformation(
        ModelMapperUtil.convertListObjectToJson(monitoringInfoDto.getFeatures()));
    MonitoringInformationDto.setUaslAdministratorId(dto.getUaslAdministratorId());
    MonitoringInformationDto.setOperatorId(dto.getOperatorId());
    MonitoringInformationDto.setUaslId(dto.getUaslId());

    MonitoringInformation map =
        ModelMapperUtil.map(MonitoringInformationDto, MonitoringInformation.class);
    mapper.insertSelective(map);
  }
}

