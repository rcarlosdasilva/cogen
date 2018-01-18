package io.github.rcarlosdasilva.cogen.utils;

import com.google.common.base.Strings;
import io.github.rcarlosdasilva.kits.string.TextHelper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Slf4j
public class Files {

  public static boolean write(String content, String path) {
    File file = new File(path);
    if (file.isDirectory()) {
      return false;
    }

    try (Writer writer = new FileWriter(file)) {
      writer.write(content);
      return true;
    } catch (IOException ex) {
      LogIf.error(log, "写文件异常", ex);
    }
    return false;
  }

  public static String path(String... parts) {
    return Stream.of(parts).filter(p -> !Strings.isNullOrEmpty(p))
        .map(p -> {
          p = p.replace("..", "#");
          p = p.replace('.', '/');
          p = p.replace("#", "..");
          return TextHelper.trim(p, "/");
        }).collect(Collectors.joining("/"));
  }

}
