package io.github.rcarlosdasilva.cogen.core;

import com.google.common.collect.Maps;
import io.github.rcarlosdasilva.kits.string.TextHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

/**
 * @author <a href="mailto:rcarlosdasilva@qq.com">Dean Zhao</a>
 */
public class Renderer {

  private static Map<String, Template> templates = Maps.newHashMap();

  public static void init(String templateDir) {
    Properties properties = new Properties();
    properties.setProperty("file.resource.loader.path", templateDir);
    Velocity.init(properties);
  }

  public static String render(String path, Map<String, Object> data) {
    VelocityContext context = new VelocityContext();
    data.forEach((k, v) -> context.put(k, v));
    context.put("TextHelper", TextHelper.class);

    Template template = template(path);
    StringWriter sw = new StringWriter();
    template.merge(context, sw);
    return sw.toString();
  }

  private static Template template(String path) {
    Template template = templates.get(path);
    if (template == null) {
      template = Velocity.getTemplate(path);
      templates.put(path, template);
    }
    return template;
  }

}
