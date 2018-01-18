package io.github.rcarlosdasilva.cogen.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import io.github.rcarlosdasilva.cogen.config.Configuration;
import io.github.rcarlosdasilva.cogen.core.handler.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 代码生成器
 *
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Slf4j
public final class Generator {

  private static final Generator INSTANCE = new Generator();

  private static List<Handler> handlers = Lists.newArrayList();

  static {
    handlers.add(new ConfigurationParseHandler());
    handlers.add(new DatabaseReadHandler());
    handlers.add(new FilePrepareHandler());
    handlers.add(new TargetFilterHandler());
    handlers.add(new CodeGenerateHandler());
    handlers.add(new OpenExplorerHandler());
  }

  /**
   * 生成代码
   *
   * @param configuration config
   */
  public static void run(Configuration configuration) {
    Preconditions.checkNotNull(configuration);

    Storage.setConfiguration(configuration);

    log.info("============== Begin ==============");
    handlers.forEach(Handler::handle);
    log.info("============== Done ==============");
  }

}