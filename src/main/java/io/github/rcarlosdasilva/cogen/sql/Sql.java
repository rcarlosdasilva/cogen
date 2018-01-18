package io.github.rcarlosdasilva.cogen.sql;

import io.github.rcarlosdasilva.cogen.config.convention.Db;

public interface Sql {

  String tables();

  String tableName();

  String tableComments();

  String tableComment();

  String fields();

  String fieldName();

  String fieldType();

  String fieldKey();

  String fieldComment();

  static Sql with(Db db) {
    switch (db) {
      case MYSQL:
        return MySql.instance;
      case MSSQL:
        return MsSql.instance;
      case ORACLE:
        return Oracle.instance;
      default:
        throw new IllegalStateException();
    }
  }

}
