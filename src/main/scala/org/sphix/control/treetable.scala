package org.sphix.control

import javafx.scene.control._
import javafx.beans.value.ObservableValue

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

  class HeaderColumn(prefWidth: Option[Double])(text: String, subColumns: TreeTableColumn[S, _]*)
    extends TreeTableColumn[S, Nothing](text):
    
    def this(text: String, subColumns: TreeTableColumn[S, _]*) =
      this(None)(text, subColumns: _*)
    
    prefWidth.foreach(setPrefWidth)
    getColumns.addAll(subColumns: _*)
