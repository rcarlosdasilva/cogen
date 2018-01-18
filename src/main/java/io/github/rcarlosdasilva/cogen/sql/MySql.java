package io.github.rcarlosdasilva.cogen.sql;

class MySql implements Sql {

  static final MySql instance = new MySql();

  private MySql() {
  }

  @Override
  public String tables() {
    return "show tables";
  }

  @Override
  public String tableName() {
    return "NAME";
  }

  @Override
  public String tableComments() {
    return "show table status";
  }

  @Override
  public String tableComment() {
    return "COMMENT";
  }

  @Override
  public String fields() {
    return "show full fields from %s";
  }

  @Override
  public String fieldName() {
    return "FIELD";
  }

  @Override
  public String fieldType() {
    return "TYPE";
  }

  @Override
  public String fieldKey() {
    return "KEY";
  }

  @Override
  public String fieldComment() {
    return "COMMENT";
  }

}
