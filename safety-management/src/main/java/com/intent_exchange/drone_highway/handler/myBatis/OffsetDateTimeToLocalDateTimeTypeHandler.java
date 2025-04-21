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

package com.intent_exchange.drone_highway.handler.myBatis;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

/**
 * OffsetDateTimeをLocalDateTimeに変換するためのTypeHandlerクラス。
 */
public class OffsetDateTimeToLocalDateTimeTypeHandler extends BaseTypeHandler<LocalDateTime> {

  /**
   * PreparedStatementにLocalDateTimeパラメータを設定します。
   *
   * @param ps PreparedStatement
   * @param i パラメータのインデックス
   * @param parameter 設定するLocalDateTimeパラメータ
   * @param jdbcType JDBCタイプ
   * @throws SQLException SQL例外が発生した場合
   */
  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter,
      JdbcType jdbcType) throws SQLException {
    ps.setObject(i, parameter.atOffset(ZoneOffset.UTC));
  }

  /**
   * ResultSetから指定されたカラム名のOffsetDateTimeを取得し、LocalDateTimeに変換して返します。
   *
   * @param rs ResultSet
   * @param columnName カラム名
   * @return 取得したLocalDateTime、またはnull
   * @throws SQLException SQL例外が発生した場合
   */
  @Override
  public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
    OffsetDateTime offsetDateTime = rs.getObject(columnName, OffsetDateTime.class);
    return offsetDateTime != null ? offsetDateTime.toLocalDateTime() : null;
  }

  /**
   * ResultSetから指定されたカラムインデックスのOffsetDateTimeを取得し、LocalDateTimeに変換して返します。
   *
   * @param rs ResultSet
   * @param columnIndex カラムインデックス
   * @return 取得したLocalDateTime、またはnull
   * @throws SQLException SQL例外が発生した場合
   */
  @Override
  public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    OffsetDateTime offsetDateTime = rs.getObject(columnIndex, OffsetDateTime.class);
    return offsetDateTime != null ? offsetDateTime.toLocalDateTime() : null;
  }

  /**
   * CallableStatementから指定されたカラムインデックスのOffsetDateTimeを取得し、LocalDateTimeに変換して返します。
   *
   * @param cs CallableStatement
   * @param columnIndex カラムインデックス
   * @return 取得したLocalDateTime、またはnull
   * @throws SQLException SQL例外が発生した場合
   */
  @Override
  public LocalDateTime getNullableResult(CallableStatement cs, int columnIndex)
      throws SQLException {
    OffsetDateTime offsetDateTime = cs.getObject(columnIndex, OffsetDateTime.class);
    return offsetDateTime != null ? offsetDateTime.toLocalDateTime() : null;
  }
}

