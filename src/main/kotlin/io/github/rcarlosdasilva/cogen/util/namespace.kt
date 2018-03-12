package io.github.rcarlosdasilva.cogen.util

import io.github.rcarlosdasilva.kits.string.TextHelper
import mu.KLogger
import mu.KotlinLogging
import java.io.File
import java.io.FileWriter
import java.io.IOException

object LogIf {

  fun trace(logger: KLogger, expression: Boolean, message: String) {
    if (expression) logger.trace(message)
  }

  fun debug(logger: KLogger, expression: Boolean, message: String) {
    if (expression) logger.debug(message)
  }

  fun info(logger: KLogger, expression: Boolean, message: String) {
    if (expression) logger.info(message)
  }

  fun warn(logger: KLogger, expression: Boolean, message: String) {
    if (expression) logger.warn(message)
  }

  fun error(logger: KLogger, expression: Boolean, message: String) {
    if (expression) {
      logger.error(message)
      throw IllegalStateException()
    }
  }

  fun error(logger: KLogger, message: String, t: Throwable) {
    logger.error(message, t)
    throw IllegalStateException(t)
  }

}

object Files {

  private val logger = KotlinLogging.logger {}

  fun write(content: String, path: String, coverage: Boolean): Boolean {
    val file = File(path)
    if (file.isDirectory) {
      return false
    }

    if (file.exists() && !coverage) {
      return false
    }
    try {
      FileWriter(file).use { writer ->
        writer.write(content)
        return true
      }
    } catch (ex: IOException) {
      LogIf.error(logger, "写文件异常", ex)
    }

    return false
  }

  fun path(vararg parts: String): String =
    parts.filter { it.isNotBlank() }.joinToString("/") {
      val t = it.replace("..", "#")
        .replace('.', '/')
        .replace("#", "..")
      TextHelper.trim(t, "/")
    }

}

fun name(originalName: String, upperFirst: Boolean): String? {
  return if (upperFirst) TextHelper.studlyCase(originalName) else TextHelper.camelCase(originalName)
}

fun name(originalName: String, cut: String, upperFirst: Boolean): String? {
  val name = TextHelper.trim(originalName, cut, -1)
  return if (upperFirst) TextHelper.studlyCase(name) else TextHelper.camelCase(name)
}

fun name(originalName: String, prefix: String, suffix: String, upperFirst: Boolean): String {
  val name = TextHelper.join("_", prefix, originalName, suffix)
  return if (upperFirst) TextHelper.studlyCase(name) else TextHelper.camelCase(name)
}