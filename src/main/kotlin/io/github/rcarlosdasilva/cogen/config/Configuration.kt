package io.github.rcarlosdasilva.cogen.config

import io.github.rcarlosdasilva.cogen.db.MysqlTypeConverter

/**
 * 总配置
 *
 * @author [Dean Zhao](mailto:rcarlosdasilva@qq.com)
 */
class Configuration {
  /**
   * 数据库配置，依据数据库表生成代码
   */
  var database: Database? = null
  /**
   * 如果没有数据库配置，必须指定Entity类，依据已存在的类生成代码
   */
  val entities: MutableSet<Class<*>> by lazy { mutableSetOf<Class<*>>() }
  /**
   * 生成Entity的配置
   */
  var entityPackage: BasicPackage? = null
  /**
   * 其他包配置
   */
  val packages: MutableList<BasicPackage> by lazy { mutableListOf<BasicPackage>() }

  /**
   * 代码生成输出目录
   */
  var out: String? = null
  /**
   * 模板根目录
   */
  var templateDir: String? = null
  /**
   * 生成类注释中作者
   */
  var authorName: String? = null
  /**
   * 生成类注释中作者邮件地址
   */
  var authorEmail: String? = null
  /**
   * 类生成时间
   */
  var isShowTime: Boolean = false
  /**
   * 版本信息
   */
  var version: String? = null
  /**
   * java代码中类型是否使用原始类型
   */
  var isUsePrimitive = true
  /**
   * 采用的语言
   */
  var lang = ClassType.JAVA
  /**
   * 是否按照Maven项目结构输出（会在模块后加入src/main/java路径）
   */
  val mavenPath: String by lazy {
    when (lang) {
      ClassType.JAVA -> "src/main/java"
      ClassType.KOTLIN -> "src/main/kotlin"
    }
  }
  /**
   * 是否覆盖已存在文件
   */
  var isCoverage = false
  /**
   * 附加自定义参数，应用到mv模板中
   */
  val extra: MutableMap<String, Any> by lazy { mutableMapOf<String, Any>() }
  /**
   * 执行完打开文件夹
   */
  var isOpenExplorer = false
}

class Database(
  val driverName: String,
  val url: String,
  val username: String,
  val password: String
) {
  var db = Db.MYSQL
  var dbTypeConverter = MysqlTypeConverter()
  /**
   * 表中id的字段名
   */
  var idName = "id"
  /**
   * 忽略ID，不生成id字段
   */
  var isIgnoreId = true
  /**
   * 表配置
   */
  val tables: MutableMap<String, Table> by lazy { mutableMapOf<String, Table>() }
  /**
   * 全局表中需要忽略掉的字段
   */
  var ignoreFields: List<String>? = null
  /**
   * 全局，需要忽略的字段前缀
   */
  var ignoreFieldsByPrefix: List<String>? = null
}

/**
 * @param prefix 表前缀。例如：表sys_user，prefix = sys
 */
class Table(val prefix: String) {
  /**
   * 在指定的表前缀范围内，只包括哪些表（与excluds排斥，优先采用）.
   *
   * includs只需要表前缀后的名称，例如：表sys_user，includs只需要加入"user"字符串<br></br>
   * 如果有多个单词，例如：表sys_user_and_admin，includs加入"user_and_admin"字符串
   */
  var includs: List<String>? = null
  /**
   * 在指定的表前缀范围内，排除掉哪些表（与includs排斥）.
   *
   * 参考includs
   */
  var excluds: List<String>? = null
  /**
   * 生成的entity类名，是否保留表前缀（默认false）.
   */
  var isHoldTablePrefix = false
  /**
   * 表字段的前缀，只有isHoldFieldPrefix为false时有效
   */
  var fieldPrefixs: List<String>? = null
  /**
   * 生成的entity类名，是否保留表字段前缀（默认false）.
   */
  var isHoldFieldPrefix = false
  /**
   * 表中需要忽略掉的字段
   */
  var ignoreFields: List<String>? = null
}

/**
 * @param name 包名
 * @param template 模板文件名
 */
class BasicPackage(
  val name: String,
  val template: String
) {
  /**
   * 模块名，不设置则输出目录相对项目根目录
   */
  var module: String? = null
  /**
   * 包下的类配置
   */
  var basicClass = BasicClass()
}

class BasicClass {
  /**
   * 生成文件前缀
   */
  var prefix: String = ""
  /**
   * 生成文件后缀
   */
  var suffix: String = ""
  /**
   * 文件扩展名
   */
  var extension: String? = null
}

enum class ClassType(val extension: String) { JAVA(".java"), KOTLIN(".kt") }