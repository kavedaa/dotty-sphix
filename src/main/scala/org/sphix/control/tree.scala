package org.sphix.control

import javafx.scene.control.{ TreeCell, TreeItem, TreeView }

import org.sphix.control.cell.*

trait TreeCells[T]:

  //  Primitive

  trait StringCell[A](using val toData: To[T, A])(using asOption: AsOption[A, String]) extends TreeCell[T] with cell.StringCell[T]:
    def dataValue(x: T) = asOption(toData(x))



trait TreeUtils[T] extends TreeCells[T]:
  this: TreeView[T] =>

  def setCell(treeCell: => TreeCell[T]) = 
    setCellFactory(_ => treeCell)