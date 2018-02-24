package io.github.rcarlosdasilva.cogen.db

import io.github.rcarlosdasilva.cogen.config.JavaType

interface DbTypeConverter {

  fun convert(type: String): JavaType

}

class MysqlTypeConverter @JvmOverloads constructor(private val useBaseType: Boolean = true) : DbTypeConverter {

  override fun convert(type: String): JavaType {
    val t = type.toLowerCase()

    return when {
      t.contains("char") || t.contains("text") -> JavaType.STRING
      t.contains("bigint") -> if (useBaseType) JavaType.BASE_LONG else JavaType.LONG
      t.contains("tinyint(1)") -> if (useBaseType) JavaType.BASE_BOOLEAN else JavaType.BOOLEAN
      t.contains("int") -> if (useBaseType) JavaType.BASE_INT else JavaType.INTEGER
      t.contains("date") || type.contains("time") || type.contains("year") -> JavaType.DATE
      t.contains("text") -> JavaType.STRING
      t.contains("bit") -> if (useBaseType) JavaType.BASE_BOOLEAN else JavaType.BOOLEAN
      t.contains("decimal") -> JavaType.BIG_DECIMAL
      t.contains("clob") -> JavaType.CLOB
      t.contains("blob") -> JavaType.BLOB
      t.contains("binary") -> JavaType.BASE_BYTE_ARRAY
      t.contains("float") -> if (useBaseType) JavaType.BASE_FLOAT else JavaType.FLOAT
      t.contains("double") -> if (useBaseType) JavaType.BASE_DOUBLE else JavaType.DOUBLE
      t.contains("json") || type.contains("enum") -> JavaType.STRING
      else -> JavaType.STRING
    }
  }

}