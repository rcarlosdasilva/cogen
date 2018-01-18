package io.github.rcarlosdasilva.cogen.core.handler;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.github.rcarlosdasilva.cogen.config.Configuration;
import io.github.rcarlosdasilva.cogen.config.convention.JavaType;
import io.github.rcarlosdasilva.cogen.config.various.BasicPackage;
import io.github.rcarlosdasilva.cogen.config.various.Database;
import io.github.rcarlosdasilva.cogen.core.Handler;
import io.github.rcarlosdasilva.cogen.core.Renderer;
import io.github.rcarlosdasilva.cogen.core.Storage;
import io.github.rcarlosdasilva.cogen.model.ClassDetail;
import io.github.rcarlosdasilva.cogen.model.FieldDetail;
import io.github.rcarlosdasilva.cogen.model.TableDetail;
import io.github.rcarlosdasilva.cogen.utils.Files;
import io.github.rcarlosdasilva.cogen.utils.LogIf;
import io.github.rcarlosdasilva.cogen.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.VelocityEngine;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Slf4j
public class CodeGenerateHandler implements Handler {

  private VelocityEngine engine;
  private Configuration configuration;
  private Database database;
  List<ClassDetail> entities = Lists.newArrayList();

  @Override
  public void handle() {
    log.info("[code] - 繁殖ing");

    configuration = Storage.getConfiguration();
    database = configuration.getDatabase();

    genEntities();

    genPackages();
  }

  private void genEntities() {
    if (database == null) {
      configuration.getEntities().forEach(cls -> {
        ClassDetail cd = new ClassDetail();
        cd.setName(cls.getSimpleName());
        cd.setPck(cls.getPackage().getName());

        if (cls.isAnnotationPresent(TableName.class)) {
          TableName tn = (TableName) cls.getDeclaredAnnotation(TableName.class);
          TableDetail td = new TableDetail();
          td.setName(tn.value());

          List<FieldDetail> fields = Lists.newArrayList();
          Stream.of(cls.getDeclaredFields()).forEach(clsf -> {
            FieldDetail fd = new FieldDetail();
            if (clsf.isAnnotationPresent(TableField.class)) {
              TableField tf = clsf.getDeclaredAnnotation(TableField.class);
              fd.setName(tf.value());
            } else {
              fd.setName(clsf.getName());
            }
            fd.setBaseName(clsf.getName());
            fd.setJavaType(JavaType.of(clsf.getType().getSimpleName()));
            fields.add(fd);
          });
          td.setFields(fields);
          cd.setTable(td);
        }

        entities.add(cd);
      });
      return;
    }

    List<TableDetail> tables = Storage.getFilteredTables();
    LogIf.error(log, tables == null, "没有可以处理的表");
    tables.forEach(table -> {
      BasicPackage pck = configuration.getEntityPackage();
      String className = Utils.name(table.getBaseName(), pck.getBasicClass().getPrefix(),
          pck.getBasicClass().getSuffix(), true);

      List<String> imports = table.getFields().stream()
          .filter(filter -> filter.getJavaType() != null && filter.getJavaType().getPackageName().contains("."))
          .map(filter -> filter.getJavaType().getPackageName())
          .distinct().collect(Collectors.toList());

      ClassDetail classDetail = new ClassDetail();
      classDetail.setModule(pck.getModule());
      classDetail.setName(className);
      classDetail.setPck(pck.getName());
      classDetail.setImports(imports);
      classDetail.setTable(table);

      Map<String, Object> data = basicData();
      data.put("cls", classDetail);

      output(pck, className, data);

      entities.add(classDetail);
    });
  }

  private void genPackages() {
    configuration.getPackages().forEach(this::genOnePackage);
  }

  private void genOnePackage(BasicPackage pck) {
    entities.forEach(entity -> {
      String className = Utils.name(entity.getName(), pck.getBasicClass().getPrefix(),
          pck.getBasicClass().getSuffix(), true);

      ClassDetail classDetail = new ClassDetail();
      classDetail.setModule(pck.getModule());
      classDetail.setName(className);
      classDetail.setPck(pck.getName());

      Map<String, Object> data = basicData();
      data.put("entity", entity);
      data.put("cls", classDetail);

      output(pck, className, data);
    });
  }

  private void output(BasicPackage pck, String className, Map<String, Object> data) {
    String content = Renderer.render(pck.getTemplate(), data);

    String path = Files.path(configuration.getOut(), pck.getModulePath(), configuration.mavenPath(), pck.getName(),
        className) + pck.getBasicClass().getExtension();
    Files.write(content, path);
  }

  private Map<String, Object> basicData() {
    Map<String, Object> data = Maps.newHashMap();
    data.put("author", configuration.getAuthor());
    if (configuration.isShowTime()) {
      data.put("time", new Date());
    }
    data.put("version", configuration.getVersion());
    data.putAll(configuration.getExtra());
    return data;
  }

}
