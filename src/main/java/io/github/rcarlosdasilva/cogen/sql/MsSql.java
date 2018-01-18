package io.github.rcarlosdasilva.cogen.sql;

class MsSql implements Sql {

  static final MsSql instance = new MsSql();

  private MsSql() {
  }

  @Override
  public String tables() {
    return "select cast(name as varchar(500)) as TABLE_NAME from sysObjects where xtype='U' order by name";
  }

  @Override
  public String tableName() {
    return "TABLE_NAME";
  }

  @Override
  public String tableComments() {
    return "select name as TABLE_NAME,(select cast(value as varchar(500))"
        + " from sys.extended_properties where major_id=id and minor_id = 0) as COMMENTS"
        + " from sysobjects where xtype='U'";
  }

  @Override
  public String tableComment() {
    return "COMMENTS";
  }

  @Override
  public String fields() {
    return "SELECT  cast(a.NAME AS VARCHAR(500)) AS TABLE_NAME,cast(b.NAME AS VARCHAR(500)) AS COLUMN_NAME,"
        + " cast(c.VALUE AS VARCHAR(500)) AS COMMENTS,cast(sys.types.NAME AS VARCHAR (500)) AS DATA_TYPE,"
        + " ( SELECT CASE count(1) WHEN 1 then 'PRI' ELSE '' END"
        + " FROM syscolumns,sysobjects,sysindexes,sysindexkeys,systypes "
        + " WHERE syscolumns.xusertype = systypes.xusertype AND syscolumns.id = object_id (A.NAME) AND sysobjects.xtype = 'PK'"
        + " AND sysobjects.parent_obj = syscolumns.id AND sysindexes.id = syscolumns.id "
        + " AND sysobjects.NAME = sysindexes.NAME AND sysindexkeys.id = syscolumns.id "
        + " AND sysindexkeys.indid = sysindexes.indid "
        + " AND syscolumns.colid = sysindexkeys.colid AND syscolumns.NAME = B.NAME) as 'KEY',"
        + " b.is_identity isIdentity FROM sys.TABLES a "
        + " INNER JOIN sys.COLUMNS b ON b.object_id = a.object_id "
        + " LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id   "
        + " LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id "
        + " WHERE a.NAME = '%s' and sys.types.NAME !='sysname' ";
  }

  @Override
  public String fieldName() {
    return "name";
  }

  @Override
  public String fieldType() {
    return "type";
  }

  @Override
  public String fieldKey() {
    return "key";
  }

  @Override
  public String fieldComment() {
    return "comment";
  }

}
