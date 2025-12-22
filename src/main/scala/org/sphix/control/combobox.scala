package org.sphix.control

import javafx.scene.control.*
import javafx.util.*

import org.sphix.control.cell.DataTypeProvider

trait ComboBoxUtils[T] extends ComboBox[T] with ListCells[T]:
  def setCell(listCell: => ListCell[T]) =
    setCellFactory(_ => listCell)

trait StringComboBoxUtils[T](render: T => String)(using DataTypeProvider[T]) extends ComboBoxUtils[T] with ListCells[T]:

  //  don't know why this is needed for empty comboboxes, it's a JavaFX thing
  private def nullSafeRender(x: T) = if x == null then "" else render(x)

  setCell(new StringCell(using nullSafeRender) {})
  setButtonCell(new StringCell(using nullSafeRender) {})
  setConverter {
    new StringConverter[T] {
      def fromString(x: String) = ???
      def toString(x: T) = nullSafeRender(x)
    }
  }


