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

package com.intent_exchange.uasl.handler.myBatis;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

/**
 * Array型のカラムを処理するためのカスタムTypeHandlerクラス。
 */
@MappedTypes(String[].class)
@MappedJdbcTypes(JdbcType.ARRAY)
public class ArrayTypeHandler implements TypeHandler<List<String>> {

  @Override
  public void setParameter(PreparedStatement ps, int i, List<String> parameter, JdbcType jdbcType)
      throws SQLException {
    // 配列に変換してセット
    ps.setArray(i, ps.getConnection().createArrayOf("VARCHAR", parameter.toArray()));
  }

  @Override
  public List<String> getResult(ResultSet rs, String columnName) throws SQLException {
    // 配列をリストに変換して取得
    Array array = rs.getArray(columnName);
    return Arrays.asList((String[]) array.getArray());
  }

  @Override
  public List<String> getResult(ResultSet rs, int columnIndex) throws SQLException {
    // 配列をリストに変換して取得
    Array array = rs.getArray(columnIndex);
    return Arrays.asList((String[]) array.getArray());
  }

  @Override
  public List<String> getResult(CallableStatement cs, int columnIndex) throws SQLException {
    // 配列をリストに変換して取得
    Array array = cs.getArray(columnIndex);
    return Arrays.asList((String[]) array.getArray());
  }

}

