package io.github.rcarlosdasilva.cogen.config.various;

import com.google.common.collect.Maps;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.Map;

@Data
public class BasicPackage {

  /**
   * 模板文件名（vm - Velocity）
   */
  private String template;
  /**
   * 所属子模块
   */
  private String module;
  /**
   * 字模块的目录，子模块相对于项目根目录（一般为输出目录）
   */
  private String modulePath;
  /**
   * 包名，相对于主包名
   */
  private String name;
  /**
   * 包下的类配置
   */
  private BasicClass basicClass = new BasicClass();

}
