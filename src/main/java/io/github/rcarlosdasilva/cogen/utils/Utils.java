package io.github.rcarlosdasilva.cogen.utils;

import io.github.rcarlosdasilva.kits.string.TextHelper;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
public class Utils {

  public static String name(String originalName, boolean upperFirst) {
    return upperFirst ? TextHelper.studlyCase(originalName) : TextHelper.camelCase(originalName);
  }

  public static String name(String originalName, String cut, boolean upperFirst) {
    String name = TextHelper.trim(originalName, cut, -1);
    return upperFirst ? TextHelper.studlyCase(name) : TextHelper.camelCase(name);
  }

  public static String name(String originalName, String prefix, String suffix, boolean upperFirst) {
    String name = TextHelper.join("_", prefix, originalName, suffix);
    return upperFirst ? TextHelper.studlyCase(name) : TextHelper.camelCase(name);
  }

}
