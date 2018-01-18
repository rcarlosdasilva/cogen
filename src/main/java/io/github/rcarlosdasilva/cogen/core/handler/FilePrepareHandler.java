package io.github.rcarlosdasilva.cogen.core.handler;

import io.github.rcarlosdasilva.cogen.config.Configuration;
import io.github.rcarlosdasilva.cogen.config.various.BasicPackage;
import io.github.rcarlosdasilva.cogen.core.Handler;
import io.github.rcarlosdasilva.cogen.core.Renderer;
import io.github.rcarlosdasilva.cogen.core.Storage;
import io.github.rcarlosdasilva.cogen.utils.Files;
import io.github.rcarlosdasilva.cogen.utils.LogIf;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Slf4j
public class FilePrepareHandler implements Handler {

  private Configuration configuration;

  @Override
  public void handle() {
    log.info("[file] - 铺床ing");

    configuration = Storage.getConfiguration();

    Renderer.init(configuration.getTemplateDir());

    if (configuration.getEntityPackage() != null) {
      createFolder(configuration.getEntityPackage());
    }
    List<BasicPackage> packages = configuration.getPackages();
    packages.forEach(this::createFolder);
  }

  private void createFolder(BasicPackage packageConfig) {
    String fullPath = Files.path(configuration.getOut(), packageConfig.getModulePath(), configuration.mavenPath(),
        packageConfig.getName());
    File folder = new File(fullPath);
    if (!folder.exists()) {
      LogIf.error(log, !folder.mkdirs(), "无法创建目录: " + fullPath);
    } else if (folder.isFile()) {
      LogIf.error(log, true, "指定目录实际上是一个文件: " + fullPath);
    }
  }

}
