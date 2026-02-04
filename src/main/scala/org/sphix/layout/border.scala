package org.sphix.layout

import javafx.scene.Node
import javafx.geometry.Insets
import javafx.scene.layout.*
import javafx.scene.paint.Color

trait BorderFactory:
  def create: Border

object BorderFactory:

  given default: BorderFactory = new BorderFactory:
    def create = new Border(
      new BorderStroke(
        Color.GRAY,
        BorderStrokeStyle.SOLID,
        CornerRadii(5),
        BorderWidths.DEFAULT))

end BorderFactory

trait BorderUtils:

  def padding: Int

  /**
    * Creates a border around a node and returns a region.
    * If the node is already a region, the border is set directly on it.
    * The border can be customized by providing a different [[BorderFactory]].
    */
  def border(node: Node)(using factory: BorderFactory): Region =
    val border = factory.create
    node match
      case region: Region =>
        region.setBorder(border)
        region.setPadding(Insets(padding))
        region
      case x =>
        val stack = new StackPane(node)
        stack.setBorder(border)
        stack.setPadding(Insets(padding))
        stack

end BorderUtils