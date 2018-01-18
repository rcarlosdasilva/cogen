package io.github.rcarlosdasilva.cogen.utils;

import org.slf4j.Logger;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
public class LogIf {

  public static void trace(Logger logger, boolean expression, String message) {
    if (expression && logger.isTraceEnabled()) {
      logger.trace(message);
    }
  }

  public static void debug(Logger logger, boolean expression, String message) {
    if (expression && logger.isDebugEnabled()) {
      logger.debug(message);
    }
  }

  public static void info(Logger logger, boolean expression, String message) {
    if (expression && logger.isInfoEnabled()) {
      logger.info(message);
    }
  }

  public static void warn(Logger logger, boolean expression, String message) {
    if (expression && logger.isWarnEnabled()) {
      logger.warn(message);
    }
  }

  public static void error(Logger logger, boolean expression, String message) {
    if (expression) {
      logger.error(message);
      throw new IllegalStateException();
    }
  }

  public static void error(Logger logger, String message, Throwable t) {
    logger.error(message, t);
    throw new IllegalStateException(t);
  }

}
