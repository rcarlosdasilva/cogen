package io.github.rcarlosdasilva.cogen.core.handler

import io.github.rcarlosdasilva.cogen.config.BasicPackage
import io.github.rcarlosdasilva.cogen.core.Handler
import io.github.rcarlosdasilva.cogen.core.Renderer
import io.github.rcarlosdasilva.cogen.core.Storage
import io.github.rcarlosdasilva.cogen.util.Files
import io.github.rcarlosdasilva.cogen.util.LogIf
import mu.KotlinLogging
import java.io.File

object FilePrepareHandler : Handler {

  private val logger = KotlinLogging.logger {}

  override fun handle() {
    logger.info("[file] - 铺床ing")

    with(Storage.configuration!!) {
      Renderer.init(templateDir!!)
      entityPackage?.also { createFolder(entityPackage!!) }
      packages.forEach { createFolder(it) }
    }
  }

  private fun createFolder(pck: BasicPackage) {
    val fullPath = with(Storage.configuration!!) {
      Files.path(out!!, pck.module!!, mavenPath, pck.name)
    }
    val folder = File(fullPath)
    if (!folder.exists()) {
      LogIf.error(logger, !folder.mkdirs(), "[file] - 无法创建目录: " + fullPath)
    } else if (folder.isFile) {
      LogIf.error(logger, true, "[file] - 指定目录是一个文件: " + fullPath)
    }
  }
}