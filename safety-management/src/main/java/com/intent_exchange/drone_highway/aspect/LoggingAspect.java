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

package com.intent_exchange.drone_highway.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * LoggingAspectは、com.intent_exchange.drone_highwayパッケージおよびそのサブパッケージ内の
 * すべてのパブリックメソッドのメソッド呼び出しと戻り値をログに記録するアスペクトです。
 *
 * <p>このアスペクトは、AspectJアノテーションを使用してポイントカットとアドバイスを定義し、ログ記録を行います。
 * @Beforeアドバイスはメソッド実行前にメソッドシグネチャと引数をログに記録し、
 * @AfterReturningアドバイスはメソッド実行後にメソッドシグネチャと戻り値をログに記録します。
 *
 * <p>使用例:
 *
 * <pre>
 * &#64;Aspect
 * &#64;Component
 * public class LoggingAspect {
 *     // アドバイスメソッド
 * }
 * </pre>
 *
 * @see org.aspectj.lang.annotation.Aspect
 * @see org.aspectj.lang.annotation.Before
 * @see org.aspectj.lang.annotation.AfterReturning
 */
@Aspect
@Component
public class LoggingAspect {
  private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

  /**
   * メソッド呼び出しのシグネチャと引数をログに記録します。
   *
   * @param joinPoint メソッド呼び出しを表すジョインポイント
   */
  @Before(
      "execution(public * com.intent_exchange.drone_highway..*(..)) && !execution(public * com.intent_exchange.drone_highway.controller..*(..))")
  public void logMethodCall(JoinPoint joinPoint) {
    logger.debug("IN :{}({})", joinPoint.getSignature(), joinPoint.getArgs());
  }

  /**
   * メソッドの戻り値のシグネチャと戻り値をログに記録します。
   *
   * @param joinPoint メソッド呼び出しを表すジョインポイント
   * @param result メソッドの戻り値
   */
  @AfterReturning(
      pointcut =
          "execution(public * com.intent_exchange.drone_highway..*(..)) && !execution(public * com.intent_exchange.drone_highway.controller..*(..))",
      returning = "result")
  public void logMethodReturn(JoinPoint joinPoint, Object result) {
    logger.debug("OUT:{} RETURN: {}", joinPoint.getSignature(), result);
  }
}

