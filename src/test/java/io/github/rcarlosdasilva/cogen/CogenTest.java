package io.github.rcarlosdasilva.cogen;

import com.google.common.collect.Lists;
import io.github.rcarlosdasilva.cogen.config.Configuration;
import io.github.rcarlosdasilva.cogen.config.convention.Db;
import io.github.rcarlosdasilva.cogen.config.various.BasicPackage;
import io.github.rcarlosdasilva.cogen.config.various.Database;
import io.github.rcarlosdasilva.cogen.config.various.Table;
import io.github.rcarlosdasilva.cogen.core.Generator;
import org.junit.Test;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
public class CogenTest {

  @Test
  public void run() {
    String module = "core";
    String moduleFolder = "formicary-service-" + module;

    Configuration configuration = new Configuration();

    configuration.setOut("C:\\working\\java\\formicary");
    configuration.setTemplateDir("C:\\working\\java\\formicary\\assist\\cogen-template");
    // 按maven结构输出
    configuration.setMaven(true);
    configuration.setAuthor("<a href=\"mailto:rcarlosdasilva@qq.com\">Dean Zhao</a>");
    configuration.setShowTime(false);
//    configuration.setVersion("1.0.1");

    // 数据库配置
    Database database = new Database();
    database.setDb(Db.MYSQL);
    database.setDriverName("com.mysql.jdbc.Driver");
    database.setUrl("jdbc:mysql://localhost:3306/formicary-core?useUnicode=true&characterEncoding=utf-8" +
        "&allowMultiQueries=true");
    database.setUsername("root");
    database.setPassword("root");
    database.setIgnoreId(true);

    // 忽略公共字段
    database.ignoreField("time_create", "time_update", "who_create", "who_update");
    database.ignoreFieldsStartWith("flag_delete", "flag_disable");

    // 表配置
    Table table1 = new Table("core_");
    table1.setIncluds(Lists.newArrayList("core_menu"));
    table1.setHoldTablePrefix(false);
    table1.setHoldFieldPrefix(false);
    table1.fieldPrefix("flag_");
    table1.setIncluds(Lists.newArrayList("core_menu"));

    database.table(table1);
//    database.table(new Table("mid_"));
    configuration.setDatabase(database);

    // Entity配置
    BasicPackage entityPackage = new BasicPackage();
    entityPackage.setModule(module);
    entityPackage.setModulePath(moduleFolder);
    entityPackage.setName("com.yingxinhuitong.formicary.service." + module + ".storage.mysql.entity");
    entityPackage.setTemplate("entity.vm");
    configuration.setEntityPackage(entityPackage);

//    configuration.base(Menu.class, Feature.class, Component.class);

    // Mapper配置
    BasicPackage mapperPackage = new BasicPackage();
    mapperPackage.setModule(module);
    mapperPackage.setModulePath(moduleFolder);
    mapperPackage.setName("com.yingxinhuitong.formicary.service." + module + ".storage.mysql.mapper");
    mapperPackage.setTemplate("mapper.vm");
    mapperPackage.getBasicClass().setSuffix("Mapper");
    configuration.pck(mapperPackage);

    // Mapper XML配置
    BasicPackage mapperxmlPackage = new BasicPackage();
    mapperxmlPackage.setModule(module);
    mapperxmlPackage.setModulePath(moduleFolder);
    mapperxmlPackage.setName("../resources/storage.mapper");
    mapperxmlPackage.setTemplate("mapperxml.vm");
    mapperxmlPackage.getBasicClass().setSuffix("Mapper");
    mapperxmlPackage.getBasicClass().setExtension(".xml");
    configuration.pck(mapperxmlPackage);

    // Service配置
    BasicPackage servicePackage = new BasicPackage();
    servicePackage.setModule(module);
    servicePackage.setModulePath(moduleFolder);
    servicePackage.setName("com.yingxinhuitong.formicary.service." + module + ".business.service");
    servicePackage.setTemplate("service.vm");
    servicePackage.getBasicClass().setSuffix("Service");
    configuration.pck(servicePackage);

    // DTO配置
    BasicPackage dtoPackage = new BasicPackage();
    dtoPackage.setModule("common");
    dtoPackage.setModulePath("formicary-common");
    dtoPackage.setName("com.yingxinhuitong.formicary.common.exposed." + module + ".bean");
    dtoPackage.setTemplate("dto.vm");
    dtoPackage.getBasicClass().setSuffix("Dto");
    configuration.pck(dtoPackage);

    // Dubbo Service配置
    BasicPackage dubboServicePackage = new BasicPackage();
    dubboServicePackage.setModule("common");
    dubboServicePackage.setModulePath("formicary-common");
    dubboServicePackage.setName("com.yingxinhuitong.formicary.common.exposed." + module + ".service");
    dubboServicePackage.setTemplate("dubboservice.vm");
    dubboServicePackage.getBasicClass().setSuffix("DubboService");
    configuration.pck(dubboServicePackage);

    // Dubbo Service Impl配置
    BasicPackage dubboServiceImplPackage = new BasicPackage();
    dubboServiceImplPackage.setModule(module);
    dubboServiceImplPackage.setModulePath(moduleFolder);
    dubboServiceImplPackage.setName("com.yingxinhuitong.formicary.service." + module + ".exposed");
    dubboServiceImplPackage.setTemplate("dubboserviceimpl.vm");
    dubboServiceImplPackage.getBasicClass().setSuffix("DubboServiceImpl");
    configuration.pck(dubboServiceImplPackage);

    // Controller配置
    BasicPackage controllerPackage = new BasicPackage();
    controllerPackage.setModule("gateway");
    controllerPackage.setModulePath("formicary-gateway");
    controllerPackage.setName("com.yingxinhuitong.formicary.gateway.web");
    controllerPackage.setTemplate("controller.vm");
    controllerPackage.getBasicClass().setSuffix("Controller");
    configuration.pck(controllerPackage);

    // Service(Controller)配置
    BasicPackage serviceCtlPackage = new BasicPackage();
    serviceCtlPackage.setModule("gateway");
    serviceCtlPackage.setModulePath("formicary-gateway");
    serviceCtlPackage.setName("com.yingxinhuitong.formicary.gateway.business.service");
    serviceCtlPackage.setTemplate("servicectl.vm");
    serviceCtlPackage.getBasicClass().setSuffix("Service");
    configuration.pck(serviceCtlPackage);

    configuration.extra("dtoPck", "com.yingxinhuitong.formicary.common.exposed." + module + ".bean");
    configuration.extra("servicePck", "com.yingxinhuitong.formicary.service." + module + ".business.service");
    configuration.extra("mapperPck", "com.yingxinhuitong.formicary.service." + module + ".storage.mysql.mapper");
    configuration.extra("dubboServicePck", "com.yingxinhuitong.formicary.common.exposed." + module + ".service");
    Generator.run(configuration);
  }

}
