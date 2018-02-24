package io.github.rcarlosdasilva.cogen.core

import com.google.common.collect.Maps
import io.github.rcarlosdasilva.kits.string.TextHelper
import org.apache.velocity.Template
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import java.io.StringWriter
import java.util.*

object Renderer {

  private val templates = Maps.newHashMap<String, Template>()

  fun init(templateDir: String) {
    val properties = Properties()
    properties.setProperty("file.resource.loader.path", templateDir)
    Velocity.init(properties)
  }

  fun render(path: String, data: Map<String, Any>): String {
    val context = VelocityContext()
    data.forEach { k, v -> context.put(k, v) }
    context.put("TextHelper", TextHelper::class.java)

    val template = template(path)
    val sw = StringWriter()
    template.merge(context, sw)
    return sw.toString()
  }

  private fun template(path: String): Template =
    templates[path] ?: run {
      templates[path] = Velocity.getTemplate(path)
      templates[path]!!
    }

}