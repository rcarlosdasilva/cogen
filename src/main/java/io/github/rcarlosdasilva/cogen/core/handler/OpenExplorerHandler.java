package io.github.rcarlosdasilva.cogen.core.handler;

import io.github.rcarlosdasilva.cogen.core.Handler;
import io.github.rcarlosdasilva.cogen.core.Storage;
import io.github.rcarlosdasilva.cogen.utils.LogIf;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Slf4j
public class OpenExplorerHandler implements Handler {

  @Override
  public void handle() {
    if (!Storage.getConfiguration().isOpenExplorer()) {
      return;
    }
    log.info("[explorer] - 开箱ing");

    String outDir = Storage.getConfiguration().getOut();
    try {
      String osName = System.getProperty("os.name");
      if (osName != null) {
        if (osName.contains("Mac")) {
          Runtime.getRuntime().exec("open " + outDir);
        } else if (osName.contains("Windows")) {
          Runtime.getRuntime().exec("cmd /c start " + outDir);
        } else {
          log.info("文件输出目录:" + outDir);
        }
      }
    } catch (IOException ex) {
      LogIf.error(log, "打开失败", ex);
    }
  }

}
