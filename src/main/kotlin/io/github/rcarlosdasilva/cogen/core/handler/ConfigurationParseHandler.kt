package io.github.rcarlosdasilva.cogen.core.handler

import com.google.common.base.Strings
import io.github.rcarlosdasilva.cogen.core.Handler
import io.github.rcarlosdasilva.cogen.core.Storage
import io.github.rcarlosdasilva.cogen.util.LogIf
import mu.KotlinLogging

object ConfigurationParseHandler : Handler {

  private val logger = KotlinLogging.logger {}

  override fun handle() {
    logger.info("[config] - 翻牌子ing")

    with(Storage.configuration!!) {
      database?.run {
        LogIf.error(logger, Strings.isNullOrEmpty(driverName), "[config] - 未指定数据库驱动")
        LogIf.error(logger, Strings.isNullOrEmpty(url), "[config] - 未指定数据库地址")
        LogIf.error(logger, Strings.isNullOrEmpty(username), "[config] - 未指定数据库用户名")
        LogIf.error(logger, Strings.isNullOrEmpty(password), "[config] - 未指定数据库密码")
        LogIf.error(logger, tables.isEmpty(), "[config] - 未指定数据库的表配置")

        LogIf.error(logger, this@with.entityPackage == null, "[config] - 未配置Entity的包参数")
      } ?: run {
        logger.info { "[config] - 基于已存在Entity类生成代码" }
        LogIf.error(logger, entities.isEmpty(), "[config] - 没有配置数据库时，Entity必须配置")
      }
    }
  }

}