package io.github.rcarlosdasilva.cogen.sql;

class Oracle implements Sql {

  static final Oracle instance = new Oracle();

  private Oracle() {
  }

  @Override
  public String tables() {
    return "SELECT * FROM USER_TABLES";
  }

  @Override
  public String tableName() {
    return "TABLE_NAME";
  }

  @Override
  public String tableComments() {
    return "SELECT * FROM USER_TAB_COMMENTS";
  }

  @Override
  public String tableComment() {
    return "COMMENTS";
  }

  @Override
  public String fields() {
    return "SELECT A.COLUMN_NAME, CASE WHEN A.DATA_TYPE='NUMBER' THEN "
        + "(CASE WHEN A.DATA_PRECISION IS NULL THEN A.DATA_TYPE "
        + "WHEN NVL(A.DATA_SCALE, 0) > 0 THEN A.DATA_TYPE||'('||A.DATA_PRECISION||','||A.DATA_SCALE||')' "
        + "ELSE A.DATA_TYPE||'('||A.DATA_PRECISION||')' END) "
        + "ELSE A.DATA_TYPE END DATA_TYPE, B.COMMENTS,DECODE(C.POSITION, '1', 'PRI') KEY "
        + "FROM USER_TAB_COLUMNS A INNER JOIN USER_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME"
        + " AND A.COLUMN_NAME = B.COLUMN_NAME LEFT JOIN USER_CONSTRAINTS D "
        + "ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P' "
        + "LEFT JOIN USER_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME "
        + "AND C.COLUMN_NAME=A.COLUMN_NAME WHERE A.TABLE_NAME = '%s' ORDER BY A.COLUMN_ID ";
  }

  @Override
  public String fieldName() {
    return "COLUMN_NAME";
  }

  @Override
  public String fieldType() {
    return "DATA_TYPE";
  }

  @Override
  public String fieldKey() {
    return "KEY";
  }

  @Override
  public String fieldComment() {
    return "COMMENTS";
  }

}
