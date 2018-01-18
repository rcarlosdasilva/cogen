package io.github.rcarlosdasilva.cogen.core;


import io.github.rcarlosdasilva.cogen.config.Configuration;
import io.github.rcarlosdasilva.cogen.config.various.Table;
import io.github.rcarlosdasilva.cogen.model.TableDetail;

import java.util.List;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
public class Storage {

  private static Configuration configuration;
  private static List<TableDetail> allTables;
  private static List<TableDetail> filteredTables;
  private static Table processingTableConfig;
  private static TableDetail processingTableDetail;

  private Storage() {
    throw new IllegalStateException("Storage class");
  }

  public static Configuration getConfiguration() {
    return configuration;
  }

  public static void setConfiguration(Configuration configuration) {
    Storage.configuration = configuration;
  }

  public static void cleanup() {
    allTables = null;
  }

  public static List<TableDetail> getAllTables() {
    return allTables;
  }

  public static void setAllTables(List<TableDetail> allTables) {
    Storage.allTables = allTables;
  }

  public static List<TableDetail> getFilteredTables() {
    return filteredTables;
  }

  public static void setFilteredTables(List<TableDetail> filteredTables) {
    Storage.filteredTables = filteredTables;
  }

  public static Table getProcessingTableConfig() {
    return processingTableConfig;
  }

  public static void setProcessingTableConfig(Table processingTableConfig) {
    Storage.processingTableConfig = processingTableConfig;
  }

  public static TableDetail getProcessingTableDetail() {
    return processingTableDetail;
  }

  public static void setProcessingTableDetail(TableDetail processingTableDetail) {
    Storage.processingTableDetail = processingTableDetail;
  }

}
