package io.github.rcarlosdasilva.cogen.core.handler

import io.github.rcarlosdasilva.cogen.core.Handler
import io.github.rcarlosdasilva.cogen.core.Storage
import io.github.rcarlosdasilva.cogen.util.LogIf
import mu.KotlinLogging
import java.io.IOException

object OpenExplorerHandler : Handler {

  private val logger = KotlinLogging.logger {}

  override fun handle() {
    with(Storage.configuration!!) {
      if (isOpenExplorer) {
        return
      }

      logger.info("[explorer] - 验货ing")
      try {
        val osName = System.getProperty("os.name")
        when {
          osName?.contains("Mac") == true -> Runtime.getRuntime().exec("open " + out)
          osName?.contains("Windows") == true -> Runtime.getRuntime().exec("cmd /c start " + out)
          else -> logger.info("文件输出目录:" + out)
        }
      } catch (ex: IOException) {
        LogIf.error(logger, "打开失败", ex)
      }
    }
  }

}