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

package com.intent_exchange.uasl.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.intent_exchange.uasl.dto.response.ConformityAssessmentResultDto;

/**
 * データを処理するための共通インターフェース。
 * 
 * @param <T> 処理するデータの型
 */
public interface Processor<T> {

  /** ロガー */
  Logger logger = LoggerFactory.getLogger(Processor.class);

  /**
   * データを処理します。
   * 
   * @param dto 処理するデータ
   */
  default void process(T dto) {
    try {
      ConformityAssessmentResultDto result = checkProcess(dto);
      result.setInOperation(setInOperation());
      if (result.getChangenResults()) {
        webProcess(result);
        updateProcess(result);
      }
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
    }
  }

  /**
   * チェック処理をします。
   * 
   * @param dto チェック用DTO
   * @return チェック結果
   */
  ConformityAssessmentResultDto checkProcess(T dto);
  
  /**
   * 運航中フラグを設定します。
   * @return true:運航中 false:予約中
   */
  boolean setInOperation();

  /**
   * 外部通信を行います。
   * 
   * @param dto 外部通信用DTO
   */
  void webProcess(ConformityAssessmentResultDto dto);

  /**
   * データ更新を行います
   * 
   * @param dto 更新用DTO
   */
  void updateProcess(ConformityAssessmentResultDto dto);

}

