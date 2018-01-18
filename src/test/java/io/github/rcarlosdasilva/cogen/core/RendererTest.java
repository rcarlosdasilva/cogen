package io.github.rcarlosdasilva.cogen.core;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

import java.util.Map;

/**
 * Renderer Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>01/16/2018</pre>
 */
public class RendererTest {

  @Before
  public void before() {
  }

  @After
  public void after() {
  }

  @Test
  public void testRender() {
    Map<String, Object> data = Maps.newHashMap();
    data.put("v", 123);
    String content = Renderer.render("test.vm", data);
    System.out.println(content);
  }

} 
