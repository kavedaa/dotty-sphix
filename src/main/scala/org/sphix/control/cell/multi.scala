package org.sphix.control.cell

import scala.jdk.CollectionConverters.*

import javafx.scene.Node
import javafx.scene.layout.Pane
import javafx.scene.control.Label

trait MultiCell[A] extends GraphicCell[List[A]]:
  def f(x: A): Node
  val container: Pane
  def divider: Option[String]
  def graphic(x: List[A]): Option[Node] = 
    container.getChildren.clear()
    val items = x.map(f)
    items.headOption.foreach(container.getChildren.add)
    items.tail.foreach: item =>
      divider.foreach(x => container.getChildren.add(new Label(x)))
        container.getChildren.add(item)
    Some(container)

trait VBoxCell[A] extends MultiCell[A]:  
  def divider: Option[String] = None
  val container: Pane = new javafx.scene.layout.VBox

trait HBoxCell[A] extends MultiCell[A]:  
  def gap: Int
  val container: Pane = 
    new javafx.scene.layout.HBox(gap):
      setAlignment(javafx.geometry.Pos.CENTER_LEFT)
