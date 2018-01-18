package io.github.rcarlosdasilva.cogen.core.handler;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.rcarlosdasilva.cogen.config.various.Database;
import io.github.rcarlosdasilva.cogen.core.Handler;
import io.github.rcarlosdasilva.cogen.core.Storage;
import io.github.rcarlosdasilva.cogen.model.FieldDetail;
import io.github.rcarlosdasilva.cogen.model.TableDetail;
import io.github.rcarlosdasilva.cogen.sql.Sql;
import io.github.rcarlosdasilva.cogen.utils.LogIf;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.List;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Slf4j
public class DatabaseReadHandler implements Handler {

  private Connection connection;
  private Database database;
  private Sql sql;

  @Override
  public void handle() {
    log.info("[model] - 勾搭ing");

    this.database = Storage.getConfiguration().getDatabase();
    if (database == null) {
      log.info("[model] - 无视数据库!");
      return;
    }

    sql = Sql.with(database.getDb());

    try {
      connection = connect();
      LogIf.error(log, connection == null, "连接数据库失败");
      Storage.setAllTables(readTables());
    } finally {
      try {
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException ex) {
        LogIf.error(log, "断开连接异常", ex);
      }
    }
  }

  private List<TableDetail> readTables() {
    String tableCommentsSql = sql.tableComments();

    List<TableDetail> tables = Lists.newArrayList();
    try (PreparedStatement preparedStatement = connection.prepareStatement(tableCommentsSql);
         ResultSet results = preparedStatement.executeQuery()) {

      while (results.next()) {
        String tableName = results.getString(sql.tableName());
        LogIf.error(log, Strings.isNullOrEmpty(tableName), "获取不到表信息，或数据库为空");

        String tableComment = results.getString(sql.tableComment());
        TableDetail table = new TableDetail();
        table.setName(tableName);
        table.setComment(tableComment);
        table.setFields(readFields(tableName));
        tables.add(table);
      }
    } catch (Exception ex) {
      LogIf.error(log, "查询表信息异常", ex);
    }
    return tables;
  }

  private List<FieldDetail> readFields(String tableName) {
    String fieldsSql = String.format(sql.fields(), tableName);

    final String idName = database.getIdName();
    final boolean idSpecified = !Strings.isNullOrEmpty(idName);
    final boolean ignoreId = database.isIgnoreId();
    boolean foundId = false;
    List<FieldDetail> fields = Lists.newArrayList();

    try (PreparedStatement preparedStatement = connection.prepareStatement(fieldsSql);
         ResultSet results = preparedStatement.executeQuery()) {

      while (results.next()) {
        FieldDetail field = new FieldDetail();

        final String fieldName = results.getString(sql.fieldName());
        if (!foundId) {
          boolean isId = false;
          if (idSpecified) {
            isId = idName.equalsIgnoreCase(fieldName);
          } else {
            String key = results.getString(sql.fieldKey());
            // 避免多重主键设置，目前只取第一个找到ID
            isId = !Strings.isNullOrEmpty(key) && "PRI".equalsIgnoreCase(key);
          }

          // 处理ID
          if (isId) {
            if (ignoreId) {
              continue;
            }

            field.setPrimaryKey(true);
            foundId = true;
          }
        }
        field.setName(fieldName);
        field.setComment(results.getString(sql.fieldComment()));
        field.setType(results.getString(sql.fieldType()));

        fields.add(field);
      }
    } catch (SQLException ex) {
      LogIf.error(log, "查询表字段信息异常", ex);
    }
    return fields;
  }

  private Connection connect() {
    try {
      Class.forName(database.getDriverName());
      return DriverManager.getConnection(database.getUrl(), database.getUsername(),
          database.getPassword());
    } catch (ClassNotFoundException | SQLException ex) {
      LogIf.error(log, "连接数据库异常", ex);
    }
    return null;
  }

}
