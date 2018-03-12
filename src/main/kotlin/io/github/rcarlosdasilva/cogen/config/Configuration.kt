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
  val extras: MutableMap<String, String> by lazy { mutableMapOf<String, String>() }
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
  /**
   * 数据库与java类型转换，参数为true，使用原始类型
   */
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
   * 全局，表中需要忽略掉的字段
   */
  var ignoreFields: List<String>? = null
  /**
   * 全局，需要忽略的字段前缀，慎用
   */
  var ignoreFieldsByPrefixes: List<String>? = null
  /**
   * 全局，需要剪切掉的表字段的前缀，例如：字段flag_abc，生成的类中变量名为abc，则设置为flag_
   */
  var cutFieldPrefixes: List<String>? = null
}

/**
 * @param prefix 表前缀。例如：表sys_user，prefix = sys
 */
class Table(val prefix: String) {
  /**
   * 在指定的表前缀范围内，只包括哪些表（与excludes排斥，优先采用）.
   *
   * includes只需要表前缀后的名称，例如：表sys_user，includes只需要加入"user"字符串<br></br>
   * 如果有多个单词，例如：表sys_user_and_admin，includes加入"user_and_admin"字符串
   */
  var includes: List<String>? = null
  /**
   * 在指定的表前缀范围内，排除掉哪些表（与includes排斥）.
   *
   * 参考includes
   */
  var excludes: List<String>? = null
  /**
   * 生成的entity类名，是否保留表前缀（默认false）.
   */
  var isHoldTablePrefix = false
  /**
   * 同Database中的cutFieldPrefixes规则，范围为表配置
   */
  var cutFieldPrefixes: List<String>? = null
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
  /**
   * 超类，entity包，key为字段，value为超类名，即包含key字段的表生成的类，超类为对应的value。其他包也可使用类似的规则，如果无需区分，则不需要设置，在模板中不取${cls.superClass}即可
   */
  val supers: MutableMap<String, String> by lazy { linkedMapOf<String, String>() }
}

enum class ClassType(val extension: String) { JAVA(".java"), KOTLIN(".kt") }