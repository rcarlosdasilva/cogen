package io.github.rcarlosdasilva.cogen.config.various;

import java.util.List;

import com.google.common.collect.Lists;
import lombok.Data;

@Data
public class BasicClass {

  /**
   * 生成文件前缀
   */
  private String prefix;
  /**
   * 生成文件后缀
   */
  private String suffix;
  /**
   * 生成文件扩展名
   */
  private String extension = ".java";

}
