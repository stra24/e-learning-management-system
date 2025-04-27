package com.everrefine.elms.infrastructure.typehandler;

import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public abstract class StringValueObjectTypeHandler<T> extends BaseTypeHandler<T> {

  private final Class<T> type;

  public StringValueObjectTypeHandler(Class<T> type) {
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) {
    try {
      var valueMethod = type.getMethod("getValue");
      String value = (String) valueMethod.invoke(parameter);
      ps.setString(i, value);
    } catch (Exception e) {
      throw new RuntimeException("Failed to extract value from value object", e);
    }
  }

  @Override
  public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return create(rs.getString(columnName));
  }

  @Override
  public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return create(rs.getString(columnIndex));
  }

  @Override
  public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return create(cs.getString(columnIndex));
  }

  private T create(String value) {
    if (value == null || value.isEmpty()) {
      return null;
    }
    try {
      Constructor<T> constructor = type.getConstructor(String.class);
      return constructor.newInstance(value);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create value object", e);
    }
  }
}
