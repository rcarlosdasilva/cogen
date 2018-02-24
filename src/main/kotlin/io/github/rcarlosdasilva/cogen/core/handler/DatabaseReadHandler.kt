package io.github.rcarlosdasilva.cogen.core.handler

import com.google.common.base.Strings
import io.github.rcarlosdasilva.cogen.core.Handler
import io.github.rcarlosdasilva.cogen.core.Storage
import io.github.rcarlosdasilva.cogen.db.Sql
import io.github.rcarlosdasilva.cogen.model.FieldModel
import io.github.rcarlosdasilva.cogen.model.TableModel
import io.github.rcarlosdasilva.cogen.util.LogIf
import mu.KotlinLogging
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DatabaseReadHandler : Handler {

  private val logger = KotlinLogging.logger {}

  private lateinit var connection: Connection
  private lateinit var sql: Sql

  override fun handle() {
    logger.info("[db] - 勾搭ing")

    Storage.configuration!!.database?.run {
      sql = Sql.with(db)
      connection = connect()

      readTables()
      try {
        connection.close()
      } catch (ex: SQLException) {
        LogIf.error(logger, "[db] - 断开连接异常", ex)
      }
    } ?: run {
      logger.info("[db] - 无视数据库!")
      return
    }
  }

  private fun readTables() {
    val tableCommentsSql = sql.tableComments()

    val tables = mutableListOf<TableModel>()
    try {
      connection.prepareStatement(tableCommentsSql).use { ps ->
        ps.executeQuery().use { results ->

          while (results.next()) {
            val tableName = results.getString(sql.tableName())
            LogIf.error(logger, Strings.isNullOrEmpty(tableName), "[db] - 获取不到表信息，或数据库为空")

            val tableComment = results.getString(sql.tableComment())
            val table = TableModel(tableName)
            table.comment = tableComment
            table.fields = readFields(tableName)
            tables.add(table)
          }
        }
      }
    } catch (ex: Exception) {
      LogIf.error(logger, "[db] - 查询表信息异常", ex)
    }

    Storage.tables = tables
  }

  private fun readFields(tableName: String): List<FieldModel> {
    val fieldsSql = String.format(sql.fields(), tableName)
    val fields = mutableListOf<FieldModel>()

    with(Storage.configuration!!.database!!) {
      var foundId = false

      try {
        connection.prepareStatement(fieldsSql).use { preparedStatement ->
          preparedStatement.executeQuery().use { results ->

            while (results.next()) {
              val fieldName = results.getString(sql.fieldName())
              val field = FieldModel(fieldName)

              if (!foundId) {
                val isId = if (idName.isNotBlank()) {
                  idName.equals(fieldName, ignoreCase = true)
                } else {
                  val key = results.getString(sql.fieldKey())
                  // 避免多重主键设置，目前只取第一个找到ID
                  !Strings.isNullOrEmpty(key) && "PRI".equals(key, ignoreCase = true)
                }

                // 处理ID
                if (isId) {
                  if (isIgnoreId) {
                    continue
                  }

                  field.isPrimaryKey = true
                  foundId = true
                }
              }

              field.comment = results.getString(sql.fieldComment())
              field.type = results.getString(sql.fieldType())
              fields.add(field)
            }
          }
        }
      } catch (ex: SQLException) {
        LogIf.error(logger, "[db] - 查询表字段信息异常", ex)
      }
    }

    return fields
  }

  private fun connect(): Connection =
    try {
      with(Storage.configuration!!.database!!) {
        Class.forName(driverName)
        DriverManager.getConnection(url, username, password)
      }
    } catch (ex: Exception) {
      LogIf.error(logger, "[db] - 连接数据库异常", ex)
      throw ex
    }

}