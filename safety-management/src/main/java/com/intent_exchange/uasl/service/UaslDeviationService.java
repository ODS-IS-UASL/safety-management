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

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.intent_exchange.uasl.dto.request.UaslDesignAreaInfoDeviationDto;
import com.intent_exchange.uasl.logic.UaslDeviationLogic;
import com.intent_exchange.uasl.logic.UaslDeviationSelectRouteDeviationInfoLogic;

/**
 * 航路逸脱情報に関連する処理をします。
 */
@Service
public class UaslDeviationService {

  /**
   * 航路逸脱情報取得に関するロジック
   */
  @Autowired
  private UaslDeviationSelectRouteDeviationInfoLogic logic;

  /**
   * 航路逸脱情報登録に関するロジック
   */
  @Autowired
  UaslDeviationLogic uaslDeviationLogic;

  /**
   * 航路逸脱情報を登録します。
   */
  public void registerRouteDeviationInfo() {

    // 航路逸脱情報ビューより航路逸脱を取得
    List<UaslDesignAreaInfoDeviationDto> list = logic.get();

    if (list == null || list.isEmpty()) {
      return;
    }
    // 航路逸脱情報を登録
    uaslDeviationLogic.registerRouteDeviationInfo(list);
  }

}

