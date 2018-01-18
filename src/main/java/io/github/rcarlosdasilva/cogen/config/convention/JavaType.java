package io.github.rcarlosdasilva.cogen.config.convention;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.stream.Stream;

public enum JavaType {

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

  private String packageName;
  private String name;

  JavaType(String name, String packageName) {
    this.name = name;
    this.packageName = packageName;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getName() {
    return name;
  }

  public boolean isPrimitiveType() {
    return !this.packageName.contains(".");
  }

  public static JavaType of(String name) {
    Optional<JavaType> type = Stream.of(JavaType.values()).filter(v -> v.getName().equalsIgnoreCase(name)).findFirst();
    return type.orElse(JavaType.OBJECT);
  }

}
