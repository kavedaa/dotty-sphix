package org.sphix.control

import javafx.scene.control.{ TreeTableView, TreeTableColumn, TreeTableCell }
import javafx.beans.value.ObservableValue

import org.sphix.control.*
import org.sphix.control.cell.*

trait TreeTableColumnCells[S, T]:

  //  Primitive

  trait StringCell[A](using val toData: To[T, A])(using asOption: AsOption[A, String]) extends TreeTableCell[S, T] with cell.StringCell[T]:
    override def dataValue(x: T) = asOption(toData(x))


trait TreeTableUtils[S]:
  this: TreeTableView[S] =>

  class Column[T](prefWidth: Option[Double])(text: String, f: S => ObservableValue[T])
    extends TreeTableColumn[S, T](text):

    def this(prefWidth: Double)(text: String, f: S => ObservableValue[T]) =
      this(Some(prefWidth))(text, f)

    def this(text: String, f: S => ObservableValue[T]) =
      this(None)(text, f)

    prefWidth.foreach(setPrefWidth)

    setCellValueFactory(x => f(x.getValue.getValue))

    def setCell(tableCell: => TreeTableCell[S, T]) = 
      setCellFactory(_ => tableCell)    

  class HeaderColumn(prefWidth: Option[Double])(text: String, subColumns: TreeTableColumn[S, ?]*)
    extends TreeTableColumn[S, Nothing](text):
    
    def this(text: String, subColumns: TreeTableColumn[S, ?]*) =
      this(None)(text, subColumns*)
    
    prefWidth.foreach(setPrefWidth)
    getColumns.addAll(subColumns*)
