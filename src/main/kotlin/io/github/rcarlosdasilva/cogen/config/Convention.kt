package io.github.rcarlosdasilva.cogen.config

enum class Db { MYSQL, MSSQL, ORACLE }

enum class NameStyle {
  /** 小写驼峰  */
  LOWER_CAMEL,
  /** 大写驼峰  */
  UPPER_CAMEL,
  /** 下划线  */
  SNAKE_CASE,
  /** 中划线  */
  STRIKE_CASE
}

enum class JavaType private constructor(val simpleName: String, val fullName: String) {

  BASE_INT("int", "int"),
  BASE_SHORT("short", "short"),
  BASE_LONG("long", "long"),
  BASE_BOOLEAN("boolean", "boolean"),
  BASE_FLOAT("float", "float"),
  BASE_DOUBLE("double", "double"),
  BASE_CHAR("char", "char"),
  BASE_BYTE("byte", "byte"),
  BASE_BYTE_ARRAY("byte[]", "byte[]"),

  STRING("String", "String"),
  INTEGER("Integer", "Integer"),
  SHORT("Short", "Short"),
  LONG("Long", "Long"),
  BOOLEAN("Boolean", "Boolean"),
  FLOAT("Float", "Float"),
  DOUBLE("Double", "Double"),
  CHARACTER("Character", "Character"),
  BYTE("Byte", "Byte"),
  OBJECT("Object", "Object"),

  DATE("Date", "java.util.Date"),
  TIME("Time", "java.sql.Time"),
  BLOB("Blob", "java.sql.Blob"),
  CLOB("Clob", "java.sql.Clob"),
  TIMESTAMP("Timestamp", "java.sql.Timestamp"),
  BIG_INTEGER("BigInteger", "java.math.BigInteger"),
  BIG_DECIMAL("BigDecimal", "java.math.BigDecimal"),
  LOCAL_DATE("LocalDate", "java.time.LocalDate"),
  LOCAL_TIME("LocalTime", "java.time.LocalTime"),
  LOCAL_DATE_TIME("LocalDateTime", "java.time.LocalDateTime");

  val isPrimitiveType: Boolean
    get() = !this.fullName.contains(".")

  companion object {
    fun of(name: String): JavaType {
      return JavaType.values().firstOrNull { it.simpleName.equals(name, true) } ?: JavaType.OBJECT
    }
  }

}
