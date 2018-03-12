package io.github.rcarlosdasilva.cogen.core.handler

import com.baomidou.mybatisplus.annotations.TableField
import com.baomidou.mybatisplus.annotations.TableName
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import io.github.rcarlosdasilva.cogen.config.BasicPackage
import io.github.rcarlosdasilva.cogen.config.JavaType
import io.github.rcarlosdasilva.cogen.core.Handler
import io.github.rcarlosdasilva.cogen.core.Renderer
import io.github.rcarlosdasilva.cogen.core.Storage
import io.github.rcarlosdasilva.cogen.model.ClassModel
import io.github.rcarlosdasilva.cogen.model.FieldModel
import io.github.rcarlosdasilva.cogen.model.TableModel
import io.github.rcarlosdasilva.cogen.util.Files
import io.github.rcarlosdasilva.cogen.util.LogIf
import io.github.rcarlosdasilva.cogen.util.name
import mu.KotlinLogging
import java.util.*

object CodeGenerateHandler : Handler {

  private val logger = KotlinLogging.logger {}

  private lateinit var entities: List<ClassModel>

  override fun handle() {
    logger.info("[code] - 繁殖ing")

    if (Storage.configuration!!.database == null) {
      scanEntities()
    } else {
      genEntities()
    }
    genPackages()
  }

  private fun scanEntities() {
    val models = mutableListOf<ClassModel>()
    Storage.configuration!!.entities.forEach { etc ->
      if (!etc.isAnnotationPresent(TableName::class.java)) {
        return@forEach
      }

      val clsModel = ClassModel(etc.simpleName)
      clsModel.pck = etc.`package`.name

      val tn = etc.getDeclaredAnnotation(TableName::class.java) as TableName
      val tabModel = TableModel(tn.value)

      val fields = Lists.newArrayList<FieldModel>()
      etc.declaredFields.forEach { etf ->
        val fldModel = if (etf.isAnnotationPresent(TableField::class.java)) {
          val tf = etf.getDeclaredAnnotation(TableField::class.java)
          FieldModel(tf.value)
        } else {
          FieldModel(etf.name)
        }
        fldModel.javaName = etf.name
        fldModel.javaType = JavaType.of(etf.type.simpleName)
        fields.add(fldModel)
      }
      tabModel.fields = fields
      clsModel.table = tabModel

      models.add(clsModel)
    }
    entities = models
  }

  private fun genEntities() {
    LogIf.error(logger, Storage.filteredTables == null, "[code] - 没有可以处理的表")

    val models = mutableListOf<ClassModel>()
    val pck = Storage.configuration!!.entityPackage!!
    Storage.filteredTables?.forEach { tabModel ->
      val className = name(tabModel.javaName!!, pck.basicClass.prefix, pck.basicClass.suffix, true)
      val imports = mutableListOf<String>()

      val clsModel = ClassModel(className)
      clsModel.module = pck.module
      clsModel.pck = pck.name
      clsModel.table = tabModel
      pck.basicClass.supers.run {
        val fieldNames = tabModel.fields!!.map { it.name }.toList()
        var sc: String? = null
        var defaultSc: String? = null
        for ((k, v) in this) {
          if (fieldNames.contains(k)) {
            sc = v
            break
          }
          if (k.isBlank()) {
            defaultSc = v
          }
        }
        clsModel.superClass = if (sc == null) {
          defaultSc
        } else {
          sc
        }
        if (clsModel.superClass != null) {
          imports.add(clsModel.superClass!!)
          clsModel.superClass = clsModel.superClass!!.split(".").last()
        }
      }

      tabModel.fields = tabModel.fields!!.filter { !it.isIgnore }
      imports.addAll(tabModel.fields?.filter {
        it.javaType != null && it.javaType?.fullName!!.contains(".")
      }?.map { it.javaType?.fullName!! }?.distinct()?.toMutableList() ?: mutableListOf())
      clsModel.imports = imports

      val data = basicData()
      data["cls"] = clsModel

      output(pck, className, data)

      models.add(clsModel)
    }
    entities = models
  }

  private fun genPackages() {
    Storage.configuration!!.packages.forEach { this.genOnePackage(it) }
  }

  private fun genOnePackage(pck: BasicPackage) {
    entities.forEach { entity ->
      val className = name(entity.name, pck.basicClass.prefix, pck.basicClass.suffix, true)

      val clsModel = ClassModel(className)
      clsModel.module = pck.module
      clsModel.pck = pck.name

      val data = basicData()
      data["entity"] = entity
      data["cls"] = clsModel

      output(pck, className, data)
    }
  }

  private fun output(pck: BasicPackage, className: String, data: Map<String, Any>) {
    val content = Renderer.render(pck.template, data)

    val path = Files.path(
      Storage.configuration!!.out!!,
      pck.module!!,
      Storage.configuration!!.mavenPath,
      pck.name,
      className
    ) + (pck.basicClass.extension ?: Storage.configuration!!.lang.extension)
    Files.write(content, path, Storage.configuration!!.isCoverage)
  }

  private fun basicData(): MutableMap<String, Any> {
    val data = Maps.newHashMap<String, Any>()
    with(Storage.configuration!!) {
      data["authorName"] = authorName
      data["authorEmail"] = authorEmail
      if (isShowTime) {
        data["time"] = Date()
      }
      if (version?.isNotBlank() == true) {
        data["version"] = version
      }
      data.putAll(extras)
    }
    return data
  }
}