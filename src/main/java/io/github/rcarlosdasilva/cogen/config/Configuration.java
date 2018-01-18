package io.github.rcarlosdasilva.cogen.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.rcarlosdasilva.cogen.config.various.BasicPackage;
import io.github.rcarlosdasilva.cogen.config.various.Database;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 总配置
 *
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Data
public final class Configuration {

  /**
   * 数据库配置
   */
  private Database database;
  /**
   * 如果没有数据库配置，必须制定entity的位置
   */
  @Setter(AccessLevel.NONE)
  private List<Class> entities;
  /**
   * 生成Entity的配置
   */
  private BasicPackage entityPackage;
  /**
   * 其他包配置
   */
  @Setter(AccessLevel.NONE)
  private List<BasicPackage> packages = Lists.newArrayList();

  /**
   * 代码生成输出目录
   */
  private String out;
  /**
   * 模板根目录
   */
  private String templateDir;
  /**
   * 生成类注释中作者
   */
  private String author;
  /**
   * 类生成时间
   */
  private boolean showTime;
  /**
   * 版本信息
   */
  private String version;
  /**
   * 是否按照Maven项目结构输出（会在模块后加入src/main/java路径）
   */
  private boolean maven = false;
  // todo 覆盖已有类
  /**
   * 附加自定义参数，应用到mv模板中
   */
  @Setter(AccessLevel.NONE)
  private Map<String, Object> extra = Maps.newHashMap();
  /**
   * 执行完打开文件夹
   */
  private boolean openExplorer = false;

  public String mavenPath() {
    return maven ? "src/main/java" : "";
  }

  public void base(Class... entityClass) {
    this.entities = Stream.of(entityClass).distinct().collect(Collectors.toList());
  }

  public void pck(BasicPackage pck) {
    this.packages.add(pck);
  }

  public void extra(String k, Object v) {
    extra.put(k, v);
  }

}
