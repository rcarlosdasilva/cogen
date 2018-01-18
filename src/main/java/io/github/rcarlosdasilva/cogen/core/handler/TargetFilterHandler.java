package io.github.rcarlosdasilva.cogen.core.handler;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.rcarlosdasilva.cogen.config.convention.JavaType;
import io.github.rcarlosdasilva.cogen.config.various.Database;
import io.github.rcarlosdasilva.cogen.config.various.Table;
import io.github.rcarlosdasilva.cogen.converter.DbTypeConverter;
import io.github.rcarlosdasilva.cogen.core.Handler;
import io.github.rcarlosdasilva.cogen.core.Storage;
import io.github.rcarlosdasilva.cogen.model.FieldDetail;
import io.github.rcarlosdasilva.cogen.model.TableDetail;
import io.github.rcarlosdasilva.cogen.utils.LogIf;
import io.github.rcarlosdasilva.cogen.utils.Utils;
import io.github.rcarlosdasilva.kits.string.TextHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Slf4j
public class TargetFilterHandler implements Handler {

  private Database database;

  @Override
  public void handle() {
    log.info("[filter] - 暖床ing");

    database = Storage.getConfiguration().getDatabase();

    // 从数据库取回来的
    List<TableDetail> tableDetails = Storage.getAllTables();

    if (database != null) {
      LogIf.error(log, tableDetails == null, "没有获取任何表信息");

      // 配置的表
      Map<String, Table> tableConfigs = database.getTables();
      List<TableDetail> results = Lists.newArrayList();

      tableDetails.forEach(detail -> {
        tableConfigs.forEach((prefix, tableConfig) -> {
          TableDetail td = tableHits(detail, prefix, tableConfig);
          if (td != null) {
            results.add(td);
          }
        });
      });

      Storage.setFilteredTables(results);
    }
  }

  private TableDetail tableHits(TableDetail tableDetail, String prefix, Table table) {
    String name = tableDetail.getName();
    if (Strings.isNullOrEmpty(name) || !name.startsWith(prefix)) {
      return null;
    }

    if (table.getIncluds() != null) {
      if (!table.getIncluds().contains(name)) {
        return null;
      }
    } else if (table.getExcluds() != null) {
      if (table.getExcluds().contains(name)) {
        return null;
      }
    }

    final String cut = table.isHoldTablePrefix() ? "" : table.getPrefix();
    tableDetail.setBaseName(Utils.name(tableDetail.getName(), cut, true));

    List<FieldDetail> fieldDetails = tableDetail.getFields();
    LogIf.error(log, fieldDetails == null, "读取不到表字段信息");
    List<FieldDetail> results = Lists.newArrayList();
    fieldDetails.forEach(detail -> {
      FieldDetail fd = fieldHits(detail, table);
      if (fd != null) {
        results.add(fd);
      }
    });
    tableDetail.setFields(results);

    return tableDetail;
  }

  private FieldDetail fieldHits(FieldDetail fieldDetail, Table table) {
    if (fieldDetail.isPrimaryKey() && database.isIgnoreId()) {
      return null;
    }
    if (database.getIgnoreFields() != null && database.getIgnoreFields().contains(fieldDetail.getName())) {
      return null;
    }
    if (table.getIgnoreFields() != null && table.getIgnoreFields().contains(fieldDetail.getName())) {
      return null;
    }

    if (database.getIgnoreFieldsByPrefix() != null) {
      Optional<String> result = database.getIgnoreFieldsByPrefix().stream().filter(prefix ->
          fieldDetail.getName().startsWith(prefix)).findFirst();
      if (result.isPresent()) {
        return null;
      }
    }

    String baseName = null;
    if (!table.isHoldFieldPrefix()) {
      Optional<String> result = table.getFieldPrefixs().stream().filter(prefix -> fieldDetail.getName().startsWith
          (prefix)).findFirst();
      if (result.isPresent()) {
        baseName = Utils.name(fieldDetail.getName(), result.get(), false);
      }
    }
    if (baseName == null) {
      baseName = Utils.name(fieldDetail.getName(), false);
    }

    fieldDetail.setBaseName(baseName);
    fieldDetail.setJavaType(database.getDbTypeConverter().convert(fieldDetail.getType()));
    return fieldDetail;
  }

}
