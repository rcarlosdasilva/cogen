package io.github.rcarlosdasilva.cogen.core

import com.google.common.collect.Maps
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Renderer Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>01/16/2018</pre>
</Authors> */
class RendererTest {

  @Before
  fun before() {
  }

  @After
  fun after() {
  }

  @Test
  fun testRender() {
    val data = Maps.newHashMap<String, Any>()
    data["v"] = 123
    val content = Renderer.render("test.vm", data)
    println(content)
  }

} 
