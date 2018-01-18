package io.github.rcarlosdasilva.cogen.model;

import io.github.rcarlosdasilva.cogen.config.convention.JavaType;
import lombok.Data;

@Data
public class FieldDetail {

  private boolean primaryKey;
  /**
   * 字段名（原始）
   */
  private String name;
  private String comment;
  /**
   * 数据库字段类型
   */
  private String type;
  /**
   * 转换为Java类型
   */
  private JavaType javaType;
  /**
   * 基于类的名称
   */
  private String baseName;

}
