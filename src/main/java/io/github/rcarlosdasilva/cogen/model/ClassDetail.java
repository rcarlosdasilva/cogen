package io.github.rcarlosdasilva.cogen.model;

import lombok.Data;

import java.util.List;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Data
public class ClassDetail {

  private String module;
  private String name;
  private String pck;
  private List<String> imports;
  private TableDetail table;

  public static ClassDetail with(String classPath) {
    ClassDetail detail = new ClassDetail();
    int lastDot = classPath.lastIndexOf(".");
    detail.name = classPath.substring(lastDot + 1);
    detail.pck = classPath.substring(0, lastDot);
    return detail;
  }

}
