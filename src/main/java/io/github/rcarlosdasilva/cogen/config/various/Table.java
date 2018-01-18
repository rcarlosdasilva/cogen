package io.github.rcarlosdasilva.cogen.config.various;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@Data
public class Table {

  /**
   * 表前缀
   * <p>
   * 例如：表sys_user，prefix = sys
   */
  private String prefix;
  /**
   * 在指定的表前缀范围内，只包括哪些表（与excluds排斥，优先采用）.
   * <p>
   * includs只需要表前缀后的名称，例如：表sys_user，includs只需要加入"user"字符串<br>
   * 如果有多个单词，例如：表sys_user_and_admin，includs加入"user_and_admin"字符串
   */
  private List<String> includs;
  /**
   * 在指定的表前缀范围内，排除掉哪些表（与includs排斥）.
   * <p>
   * 参考includs
   */
  private List<String> excluds;
  /**
   * 生成的entity类名，是否保留表前缀（默认false）.
   */
  private boolean holdTablePrefix = false;
  /**
   * 表字段的前缀，只有holdFieldPrefix为false时有效
   */
  @Setter(AccessLevel.NONE)
  private List<String> fieldPrefixs = Lists.newArrayList();
  /**
   * 生成的entity类名，是否保留表字段前缀（默认false）.
   */
  private boolean holdFieldPrefix = false;
  /**
   * 表中需要忽略掉的字段
   */
  @Setter(AccessLevel.NONE)
  private List<String> ignoreFields = Lists.newArrayList();

  public Table(String prefix) {
    this.prefix = prefix;
  }

  public void fieldPrefix(String... prefixs) {
    this.fieldPrefixs.addAll(Lists.newArrayList(prefixs));
  }

  public void ignoreField(String... ignoreFields) {
    this.ignoreFields.addAll(Lists.newArrayList(ignoreFields));
  }

  public static Table includs(String prefix, List<String> includs) {
    Table table = new Table(prefix);
    table.setIncluds(includs);
    return table;
  }

  public static Table excluds(String prefix, List<String> excluds) {
    Table table = new Table(prefix);
    table.setExcluds(excluds);
    return table;
  }

}
