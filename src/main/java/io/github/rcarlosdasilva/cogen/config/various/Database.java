package io.github.rcarlosdasilva.cogen.config.various;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import io.github.rcarlosdasilva.cogen.config.convention.Db;
import io.github.rcarlosdasilva.cogen.converter.DbTypeConverter;
import io.github.rcarlosdasilva.cogen.converter.MysqlTypeConverter;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

/**
 * 数据源配置
 *
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Data
public class Database {

  private Db db = Db.MYSQL;
  private String url;
  private String driverName;
  private String username;
  private String password;
  private DbTypeConverter dbTypeConverter = new MysqlTypeConverter();
  /**
   * 表中id的字段名
   */
  private String idName = "id";
  /**
   * 忽略ID，不生成id字段
   */
  private boolean ignoreId = false;
  /**
   * 表配置
   */
  @Setter(AccessLevel.NONE)
  private Map<String, Table> tables = Maps.newHashMap();
  /**
   * 全局表中需要忽略掉的字段
   */
  @Setter(AccessLevel.NONE)
  private List<String> ignoreFields = Lists.newArrayList();
  /**
   * 全局，需要忽略的字段前缀
   */
  @Setter(AccessLevel.NONE)
  private List<String> ignoreFieldsByPrefix = Lists.newArrayList();

  /**
   * 添加表配置
   *
   * @param table {@link Table}
   */
  public void table(Table table) {
    tables.put(table.getPrefix(), table);
  }

  public void ignoreField(String... ignoreFields) {
    this.ignoreFields.addAll(Lists.newArrayList(ignoreFields));
  }

  public void ignoreFieldsStartWith(String... prefixs) {
    this.ignoreFieldsByPrefix.addAll(Lists.newArrayList(prefixs));
  }

}
