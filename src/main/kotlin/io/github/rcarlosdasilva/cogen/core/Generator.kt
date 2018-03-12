package io.github.rcarlosdasilva.cogen.core

import io.github.rcarlosdasilva.cogen.config.Configuration
import io.github.rcarlosdasilva.cogen.core.handler.*
import mu.KotlinLogging

/**
 * 代码生成器
 *
 * @author [Dean Zhao](mailto:rcarlosdasilva@qq.com)
 */
object Generator {

  private val logger = KotlinLogging.logger {}

  private val handlers: List<Handler> by lazy {
    listOf(
      ConfigurationParseHandler,
      DatabaseReadHandler,
      FilePrepareHandler,
      TargetFilterHandler,
      CodeGenerateHandler,
      OpenExplorerHandler
    )
  }

  /**
   * 生成代码
   *
   * @param configuration config
   */
  fun run(configuration: Configuration) {
    Storage.configuration = configuration

    logger.info("============== Cogen Begin ==============")
    handlers.forEach(Handler::handle)
    logger.info("============== Cogen Done ==============")
  }

}

// TODO 生成枚举类