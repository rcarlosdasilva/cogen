package io.github.rcarlosdasilva.cogen.core

import io.github.rcarlosdasilva.cogen.config.Configuration
import io.github.rcarlosdasilva.cogen.model.TableModel

object Storage {

  var configuration: Configuration? = null
  var tables: List<TableModel>? = null
  var filteredTables: List<TableModel>? = null

}