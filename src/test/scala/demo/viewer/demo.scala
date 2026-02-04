package demo.viewer

import javafx.application.Application
import javafx.scene.control.*

import org.sphix.SimpleApp

@main def main = Application.launch(classOf[Demo])

class Demo extends SimpleApp:

  val grid = new Tab("Grid"):
    setContent(demo.viewer.grid.GridDemo)

  val table = new Tab("Table"):
    setContent(demo.viewer.table.TableDemo)

  val link = new Tab("Link"):
    setContent(demo.viewer.link.LinkDemo)

  val list = new Tab("List"):
    setContent(demo.viewer.list.ListDemo)

  val tabs = List(grid, table, link, list)

  tabs.foreach(_.setClosable(false))

  val tabPane = new TabPane(tabs*)

  def root = tabPane