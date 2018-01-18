package io.github.rcarlosdasilva.cogen.converter;

import io.github.rcarlosdasilva.cogen.config.convention.JavaType;

public class MysqlTypeConverter implements DbTypeConverter {

  private boolean useBaseType;

  public MysqlTypeConverter() {
    this(true);
  }

  public MysqlTypeConverter(boolean useBaseType) {
    this.useBaseType = useBaseType;
  }

  @Override
  public JavaType convert(String type) {
    type = type.toLowerCase();
    if (type.contains("char") || type.contains("text")) {
      return JavaType.STRING;
    } else if (type.contains("bigint")) {
      return useBaseType ? JavaType.BASE_LONG : JavaType.LONG;
    } else if (type.contains("tinyint(1)")) {
      return useBaseType ? JavaType.BASE_BOOLEAN : JavaType.BOOLEAN;
    } else if (type.contains("int")) {
      return useBaseType ? JavaType.BASE_INT : JavaType.INTEGER;
    } else if (type.contains("date") || type.contains("time") || type.contains("year")) {
      return JavaType.DATE;
    } else if (type.contains("text")) {
      return JavaType.STRING;
    } else if (type.contains("bit")) {
      return useBaseType ? JavaType.BASE_BOOLEAN : JavaType.BOOLEAN;
    } else if (type.contains("decimal")) {
      return JavaType.BIG_DECIMAL;
    } else if (type.contains("clob")) {
      return JavaType.CLOB;
    } else if (type.contains("blob")) {
      return JavaType.BLOB;
    } else if (type.contains("binary")) {
      return JavaType.BASE_BYTE_ARRAY;
    } else if (type.contains("float")) {
      return useBaseType ? JavaType.BASE_FLOAT : JavaType.FLOAT;
    } else if (type.contains("double")) {
      return useBaseType ? JavaType.BASE_DOUBLE : JavaType.DOUBLE;
    } else if (type.contains("json") || type.contains("enum")) {
      return JavaType.STRING;
    }
    return JavaType.STRING;
  }

}
