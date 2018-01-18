package io.github.rcarlosdasilva.cogen.model;

import lombok.Data;

import java.util.List;

@Data
public class TableDetail {

  /**
   * 表名（原始）
   */
  private String name;
  private String comment;
  private List<FieldDetail> fields;
  /**
   * 基于类的名称
   */
  private String baseName;

}
