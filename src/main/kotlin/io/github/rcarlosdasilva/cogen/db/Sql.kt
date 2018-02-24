package io.github.rcarlosdasilva.cogen.db

import io.github.rcarlosdasilva.cogen.config.Db

interface Sql {

  fun tables(): String

  fun tableName(): String

  fun tableComments(): String

  fun tableComment(): String

  fun fields(): String

  fun fieldName(): String

  fun fieldType(): String

  fun fieldKey(): String

  fun fieldComment(): String

  companion object {
    fun with(db: Db): Sql =
      when (db) {
        Db.MYSQL -> MySql
        Db.MSSQL -> MsSql
        Db.ORACLE -> Oracle
      }
  }

}

internal object MySql : Sql {
  override fun tables(): String = "show tables"

  override fun tableName(): String = "NAME"

  override fun tableComments(): String = "show table status"

  override fun tableComment(): String = "COMMENT"

  override fun fields(): String = "show full fields from %s"

  override fun fieldName(): String = "FIELD"

  override fun fieldType(): String = "TYPE"

  override fun fieldKey(): String = "KEY"

  override fun fieldComment(): String = "COMMENT"
}

internal object MsSql : Sql {
  override fun tables(): String =
    "select cast(name as varchar(500)) as TABLE_NAME from sysObjects where xtype='U' order by name"

  override fun tableName(): String = "TABLE_NAME"

  override fun tableComments(): String =
    """select name as TABLE_NAME,(select cast(value as varchar(500))
       from sys.extended_properties where major_id=id and minor_id = 0) as COMMENTS
       from sysobjects where xtype='U'"""

  override fun tableComment(): String = "COMMENTS"

  override fun fields(): String =
    """SELECT  cast(a.NAME AS VARCHAR(500)) AS TABLE_NAME,cast(b.NAME AS VARCHAR(500)) AS COLUMN_NAME,
       cast(c.VALUE AS VARCHAR(500)) AS COMMENTS,cast(sys.types.NAME AS VARCHAR (500)) AS DATA_TYPE,
       ( SELECT CASE count(1) WHEN 1 then 'PRI' ELSE '' END
       FROM syscolumns,sysobjects,sysindexes,sysindexkeys,systypes
       WHERE syscolumns.xusertype = systypes.xusertype AND syscolumns.id = object_id (A.NAME) AND sysobjects.xtype = 'PK'
       AND sysobjects.parent_obj = syscolumns.id AND sysindexes.id = syscolumns.id
       AND sysobjects.NAME = sysindexes.NAME AND sysindexkeys.id = syscolumns.id
       AND sysindexkeys.indid = sysindexes.indid
       AND syscolumns.colid = sysindexkeys.colid AND syscolumns.NAME = B.NAME) as 'KEY',
       b.is_identity isIdentity FROM sys.TABLES a
       INNER JOIN sys.COLUMNS b ON b.object_id = a.object_id
       LEFT JOIN sys.types ON b.user_type_id = sys.types.user_type_id
       LEFT JOIN sys.extended_properties c ON c.major_id = b.object_id AND c.minor_id = b.column_id
       WHERE a.NAME = '%s' and sys.types.NAME !='sysname' """

  override fun fieldName(): String = "name"

  override fun fieldType(): String = "type"

  override fun fieldKey(): String = "key"

  override fun fieldComment(): String = "comment"
}

internal object Oracle : Sql {
  override fun tables(): String = "SELECT * FROM USER_TABLES"

  override fun tableName(): String = "TABLE_NAME"

  override fun tableComments(): String = "SELECT * FROM USER_TAB_COMMENTS"

  override fun tableComment(): String = "COMMENTS"

  override fun fields(): String =
    """SELECT A.COLUMN_NAME, CASE WHEN A.DATA_TYPE='NUMBER' THEN
       (CASE WHEN A.DATA_PRECISION IS NULL THEN A.DATA_TYPE
       WHEN NVL(A.DATA_SCALE, 0) > 0 THEN A.DATA_TYPE||'('||A.DATA_PRECISION||','||A.DATA_SCALE||')'
       ELSE A.DATA_TYPE||'('||A.DATA_PRECISION||')' END)
       ELSE A.DATA_TYPE END DATA_TYPE, B.COMMENTS,DECODE(C.POSITION, '1', 'PRI') KEY
       FROM USER_TAB_COLUMNS A INNER JOIN USER_COL_COMMENTS B ON A.TABLE_NAME = B.TABLE_NAME
       AND A.COLUMN_NAME = B.COLUMN_NAME LEFT JOIN USER_CONSTRAINTS D
       ON D.TABLE_NAME = A.TABLE_NAME AND D.CONSTRAINT_TYPE = 'P'
       LEFT JOIN USER_CONS_COLUMNS C ON C.CONSTRAINT_NAME = D.CONSTRAINT_NAME
       AND C.COLUMN_NAME=A.COLUMN_NAME WHERE A.TABLE_NAME = '%s' ORDER BY A.COLUMN_ID """

  override fun fieldName(): String = "COLUMN_NAME"

  override fun fieldType(): String = "DATA_TYPE"

  override fun fieldKey(): String = "KEY"

  override fun fieldComment(): String = "COMMENTS"
}