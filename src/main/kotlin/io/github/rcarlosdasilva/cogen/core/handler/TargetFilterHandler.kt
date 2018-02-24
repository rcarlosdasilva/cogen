package io.github.rcarlosdasilva.cogen.core.handler

import com.google.common.collect.Lists
import io.github.rcarlosdasilva.cogen.config.Table
import io.github.rcarlosdasilva.cogen.core.Handler
import io.github.rcarlosdasilva.cogen.core.Storage
import io.github.rcarlosdasilva.cogen.model.FieldModel
import io.github.rcarlosdasilva.cogen.model.TableModel
import io.github.rcarlosdasilva.cogen.util.LogIf
import io.github.rcarlosdasilva.cogen.util.name
import mu.KotlinLogging

object TargetFilterHandler : Handler {

  private val logger = KotlinLogging.logger {}

  override fun handle() {
    logger.info("[filter] - 卸妆ing")

    Storage.configuration!!.database?.run {
      LogIf.error(logger, Storage.tables == null, "没有获取任何表信息")
      val results = mutableListOf<TableModel>()

      // 从数据库取回来的
      Storage.tables?.forEach { model ->
        // 配置的表
        tables.forEach { (prefix, tableConfig) ->
          tableHits(model, prefix, tableConfig)?.also {
            results.add(it)
          }
        }
      }

      Storage.filteredTables = results
    }
  }

  private fun tableHits(tableModel: TableModel, prefix: String, tableConfig: Table): TableModel? {
    val name = tableModel.name
    if (name.isBlank() || !name.startsWith(prefix)) {
      return null
    }
    if (tableConfig.includs?.contains(name) != true) {
      return null
    }
    if (tableConfig.excluds?.contains(name) != true) {
      return null
    }

    val cut = if (tableConfig.isHoldTablePrefix) "" else tableConfig.prefix
    tableModel.javaName = name(name, cut, true)

    LogIf.error(logger, tableModel.fields == null, "读取不到表字段信息")
    val results = Lists.newArrayList<FieldModel>()

    tableModel.fields?.forEach { model ->
      fieldHits(model, tableConfig)?.also {
        results.add(it)
      }
    }
    tableModel.fields = results

    return tableModel
  }

  private fun fieldHits(fieldModel: FieldModel, table: Table): FieldModel? {
    with(Storage.configuration!!.database!!) {
      if (fieldModel.isPrimaryKey && isIgnoreId) {
        return null
      }
      if (ignoreFields?.contains(fieldModel.name) == true) {
        return null
      }
      if (table.ignoreFields?.contains(fieldModel.name) == true) {
        return null
      }
      ignoreFieldsByPrefix?.firstOrNull {
        fieldModel.name.startsWith(it)
      }?.run { return@with }

      fieldModel.javaName = if (table.isHoldFieldPrefix || table.fieldPrefixs?.isEmpty() == true) {
        name(fieldModel.name, false)
      } else {
        table.fieldPrefixs?.firstOrNull {
          fieldModel.name.startsWith(it)
        }?.let {
            name(fieldModel.name, it, false)
          } ?: name(fieldModel.name, false)
      }
      fieldModel.javaType = dbTypeConverter.convert(fieldModel.type!!)
    }
    return fieldModel
  }
}