package io.github.rcarlosdasilva.cogen

import io.github.rcarlosdasilva.cogen.config.*
import io.github.rcarlosdasilva.cogen.core.Generator
import org.junit.Test

/**
 * @author [Dean Zhao](mailto:rcarlosdasilva@qq.com)
 */
class CogenTest {

  @Test
  fun run() {
    val module = "core"
    val moduleFolder = "formicary-service-" + module

    val configuration = Configuration()

    configuration.out = "C:\\working\\java\\formicary"
    configuration.templateDir = "C:\\working\\java\\formicary\\assist\\cogen-template"
    // 按maven结构输出
    configuration.authorName = "Dean Zhao"
    configuration.authorEmail = "rcarlosdasilva@qq.com"
    configuration.isShowTime = false
//    configuration.version = "v1.0"

    // 数据库配置
    val database = Database(
      "com.mysql.jdbc.Driver",
      "jdbc:mysql://localhost:3306/formicary-core?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true",
      "root",
      "root"
    )
    database.db = Db.MYSQL
    database.isIgnoreId = true

    // 忽略公共字段
    database.ignoreFields = listOf("time_create", "time_update", "who_create", "who_update")
    database.ignoreFieldsByPrefixes = listOf("flag_delete", "flag_disable")

    // 表配置
    val table1 = Table("core_")
    table1.includes = listOf("core_menu")
    table1.isHoldTablePrefix = false
    table1.cutFieldPrefixes = listOf("flag_")

    database.tables[table1.prefix] = table1
    //    database.table(new Table("mid_"));
    configuration.database = database

    // Entity配置
    val entityPackage = BasicPackage("com.yingxinhuitong.formicary.service.$module.storage.mysql.entity", "entity.vm")
    entityPackage.module = moduleFolder
    configuration.entityPackage = entityPackage

    //    configuration.base(Menu.class, Feature.class, Component.class);

    // Mapper配置
    val mapperPackage = BasicPackage("com.yingxinhuitong.formicary.service.$module.storage.mysql.mapper", "mapper.vm")
    mapperPackage.module = moduleFolder
    mapperPackage.basicClass.suffix = "Mapper"
    configuration.packages.add(mapperPackage)

    // Mapper XML配置
    val mapperxmlPackage = BasicPackage("../resources/storage.mapper", "mapperxml.vm")
    mapperxmlPackage.module = moduleFolder
    mapperxmlPackage.basicClass.suffix = "Mapper"
    mapperxmlPackage.basicClass.extension = ".xml"
    configuration.packages.add(mapperxmlPackage)

    // Service配置
    val servicePackage = BasicPackage("com.yingxinhuitong.formicary.service.$module.business.service", "service.vm")
    servicePackage.module = moduleFolder
    servicePackage.basicClass.suffix = "Service"
    configuration.packages.add(servicePackage)

    // DTO配置
    val dtoPackage = BasicPackage("com.yingxinhuitong.formicary.common.exposed.$module.bean", "dto.vm")
    dtoPackage.module = "formicary-common"
    dtoPackage.basicClass.suffix = "Dto"
    configuration.packages.add(dtoPackage)

    // Dubbo Service配置
    val dubboServicePackage =
      BasicPackage("com.yingxinhuitong.formicary.common.exposed.$module.service", "dubboservice.vm")
    dubboServicePackage.module = "formicary-common"
    dubboServicePackage.basicClass.suffix = "DubboService"
    configuration.packages.add(dubboServicePackage)

    // Dubbo Service Impl配置
    val dubboServiceImplPackage =
      BasicPackage("com.yingxinhuitong.formicary.service.$module.exposed", "dubboserviceimpl.vm")
    dubboServiceImplPackage.module = moduleFolder
    dubboServiceImplPackage.basicClass.suffix = "DubboServiceImpl"
    configuration.packages.add(dubboServiceImplPackage)

    // Controller配置
    val controllerPackage = BasicPackage("com.yingxinhuitong.formicary.gateway.web", "controller.vm")
    controllerPackage.module = "formicary-gateway"
    controllerPackage.basicClass.suffix = "Controller"
    configuration.packages.add(controllerPackage)

    // Service(Controller)配置
    val serviceCtlPackage = BasicPackage("com.yingxinhuitong.formicary.gateway.business.service", "servicectl.vm")
    serviceCtlPackage.module = "formicary-gateway"
    serviceCtlPackage.basicClass.suffix = "Service"
    configuration.packages.add(serviceCtlPackage)

    configuration.extras["dtoPck"] = "com.yingxinhuitong.formicary.common.exposed.$module.bean"
    configuration.extras["servicePck"] = "com.yingxinhuitong.formicary.service.$module.business.service"
    configuration.extras["mapperPck"] = "com.yingxinhuitong.formicary.service.$module.storage.mysql.mapper"
    configuration.extras["dubboServicePck"] = "com.yingxinhuitong.formicary.common.exposed.$module.service"
    Generator.run(configuration)
  }

}
