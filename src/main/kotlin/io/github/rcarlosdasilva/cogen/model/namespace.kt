package io.github.rcarlosdasilva.cogen.model

import io.github.rcarlosdasilva.cogen.config.JavaType

class TableModel(val name: String) {
  var comment: String? = null
  var fields: List<FieldModel>? = null
  var javaName: String? = null
}

class FieldModel(val name: String) {
  var isIgnore: Boolean = false
  var isPrimaryKey: Boolean = false
  var comment: String? = null
  /**
   * 数据库字段类型
   */
  var type: String? = null
  /**
   * 转换为Java类型
   */
  var javaType: JavaType? = null
  var javaName: String? = null
}

class ClassModel(val name: String) {
  var module: String? = null
  var pck: String? = null
  var imports: List<String>? = null
  var superClass: String? = null
  var table: TableModel? = null
}