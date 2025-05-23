package org.sphix.pane

import javafx.stage.*
import javafx.scene.*
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.geometry.*

class DetachableSplitPane(first: Node, second: Node):

  val inner = new SplitPane

  val group = new ToggleGroup

  val detachButton = new ToggleButton("D"):
    setToggleGroup(group)

  val toolbar = new ToolBar(detachButton)
  toolbar.setOrientation(Orientation.VERTICAL)

  private val wrapper = new BorderPane

  wrapper.setCenter(second)
  wrapper.setRight(toolbar)

  inner.getItems.addAll(first, wrapper)

  inner.setOrientation(Orientation.VERTICAL)

  detachButton setOnAction { _ =>
    println(inner.getItems)
    inner.getItems.remove(wrapper)
    println(inner.getItems)
    // Exception in thread "JavaFX Application Thread" java.lang.IllegalArgumentException: BorderPane@63d52a7is already inside a scene-graph and cannot be set as root
    // ??
    val scene = new Scene(wrapper)
    val stage = new Stage
    stage.setScene(scene)
    stage.show()
  }
