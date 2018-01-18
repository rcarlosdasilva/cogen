package io.github.rcarlosdasilva.cogen.config.various;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Data
public class BasicType {

  @NonNull
  private String name;
  private List<String> genericTypes;

}
