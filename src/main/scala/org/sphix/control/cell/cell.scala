package org.sphix.control.cell

import javafx.scene.Node

//  base trait for all cells

trait Cell[T] extends javafx.scene.control.Cell[T]:

  // override this and call super in each cell implementation
  def onUpdate(item: T): Unit = {}

  override def updateItem(item: T, empty: Boolean) =
    super.updateItem(item, empty)
    setText(null)
    setGraphic(null)
    if !empty then onUpdate(item)


//  base trait for all cells that display text

trait TextCell[T] extends Cell[T]:
  def text(x: T): Option[String]
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    setText(text(x).orNull)


//  base trait for some cells that display graphics

trait GraphicCell[T] extends Cell[T]:
  def graphic(x: T): Option[Node]
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    setGraphic(graphic(x).orNull)

