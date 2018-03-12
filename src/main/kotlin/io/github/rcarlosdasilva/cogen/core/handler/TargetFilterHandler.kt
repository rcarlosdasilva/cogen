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

    tableConfig.includes?.run {
      if (!this.contains(name)) {
        return null
      }
    }
    tableConfig.excludes?.run {
      if (this.contains(name)) {
        return null
      }
    }

    val cut = if (tableConfig.isHoldTablePrefix) "" else tableConfig.prefix
    tableModel.javaName = name(name, cut, true)

    LogIf.error(logger, tableModel.fields == null, "读取不到表字段信息")
    val results = Lists.newArrayList<FieldModel>()

    tableModel.fields?.forEach { model ->
      fieldHits(model, tableConfig).also {
        results.add(it)
      }
    }
    tableModel.fields = results

    return tableModel
  }
}

private fun fieldHits(fieldModel: FieldModel, table: Table): FieldModel {
  with(Storage.configuration!!.database!!) {
    // 字段是否需要忽略，主键忽略规则1个，全局忽略规则2个，表内规则1个
    val ignore = when {
      fieldModel.isPrimaryKey && isIgnoreId -> true
      ignoreFields != null && ignoreFields!!.contains(fieldModel.name) -> true
      ignoreFieldsByPrefixes?.firstOrNull { fieldModel.name.startsWith(it) } != null -> true
      table.ignoreFields != null && table.ignoreFields!!.contains(fieldModel.name) -> true
      else -> false
    }

    if (ignore) {
      fieldModel.isIgnore = ignore
      return fieldModel
    }

    val cut = cutFieldPrefixes?.firstOrNull {
      fieldModel.name.startsWith(it)
    } ?: table.cutFieldPrefixes?.firstOrNull {
      fieldModel.name.startsWith(it)
    }
    fieldModel.javaName = cut?.let {
      name(fieldModel.name, it, false)
    } ?: name(fieldModel.name, false)

    fieldModel.javaType = dbTypeConverter.convert(fieldModel.type!!)
  }
  return fieldModel
}