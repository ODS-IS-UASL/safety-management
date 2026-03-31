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

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 非同期処理を行うサービスクラス。
 */
@Service
public class AsyncProcessor {

  @Autowired
  private ApplicationContext applicationContext;

  /**
   * 非同期で処理を実行します。
   * 
   * @param <T> 処理するデータの型
   * @param dto 処理するデータ
   * @param processor データを処理するプロセッサ
   * @return CompletableFuture<Void> 非同期処理の結果
   */
  @Async("taskExecutor")
  public <T> CompletableFuture<Void> process(T dto, Processor<T> processor) {
    processor.process(dto);
    return CompletableFuture.completedFuture(null);
  }

  /**
   * データリストを非同期で処理します。
   * 
   * @param <T> 処理するデータの型
   * @param dataList 処理するデータのリスト
   * @param processorClass プロセッサのクラス
   */
  public <T> void processDataList(List<T> dataList, Class<? extends Processor<T>> processorClass) {
    Processor<T> processor =
        (Processor<T>) applicationContext.getBean(processorClass.getSimpleName());
    dataList.forEach(data -> process(data, processor));
  }
}

