package io.github.rcarlosdasilva.cogen.core.handler;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import io.github.rcarlosdasilva.cogen.config.various.Database;
import io.github.rcarlosdasilva.cogen.core.Handler;
import io.github.rcarlosdasilva.cogen.core.Storage;
import io.github.rcarlosdasilva.cogen.utils.LogIf;
import lombok.extern.slf4j.Slf4j;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
@Slf4j
public class ConfigurationParseHandler implements Handler {

  @Override
  public void handle() {
    log.info("[config] - 翻牌子ing");

    Database database = Storage.getConfiguration().getDatabase();
    boolean ok = false;

    if (database != null) {
      LogIf.error(log, Strings.isNullOrEmpty(database.getDriverName()), "未指定数据库驱动");
      LogIf.error(log, Strings.isNullOrEmpty(database.getUrl()), "未指定数据库地址");
      LogIf.error(log, Strings.isNullOrEmpty(database.getUsername()), "未指定数据库用户名");
      LogIf.error(log, Strings.isNullOrEmpty(database.getPassword()), "未指定数据库密码");
      LogIf.error(log, database.getTables().isEmpty(), "未指定数据库的表配置");

      LogIf.error(log, Storage.getConfiguration().getEntityPackage() == null, "未配置Entity的包参数");
    } else {
      LogIf.error(log, Storage.getConfiguration().getEntities() == null, "没有配置数据库时，Entity必须配置");
    }
  }

}
