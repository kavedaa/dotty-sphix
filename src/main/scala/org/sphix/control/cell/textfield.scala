package org.sphix.control.cell

import javafx.scene.control.*
import javafx.event.EventHandler
import javafx.scene.input.*

import org.sphix.util.*

trait TextFieldTableCell[S, T, D] extends TableCell[S, T] with DataCell[T, D]:
  
  cell =>

  def converter: Converter[T, String]

  lazy val textField = new TextField:
    setOnKeyPressed {
      new EventHandler[KeyEvent]:
        def handle(event: KeyEvent) =
          event match 
            case t if new KeyCodeCombination(KeyCode.ENTER) `match` t =>
              Option(getText).flatMap(converter.deconvert).foreach(commitEdit)
              t.consume()
            case t if new KeyCodeCombination(KeyCode.ESCAPE) `match` t => 
              cell.cancelEdit()
            case _ =>
    }
    
  override def startEdit() =
    if isEditable && getTableView.isEditable then
      super.startEdit()
      textField.setText(converter.convert(getItem).orNull)
      setText(null)
      setGraphic(textField)
      textField.requestFocus()
      textField.selectAll()

  override def commitEdit(value: T) =
    super.commitEdit(value)
    setGraphic(null)
    getTableView.requestFocus()

  override def cancelEdit() =
    super.cancelEdit()
    setText(converter.convert(getItem).orNull)
    setGraphic(null)

  override def updateItem(item: T, empty: Boolean) =
    super.updateItem(item, empty)
    if (isEmpty()) {
      setText(null)
      setGraphic(null)
    }
    else {
      if (isEditing()) {
        if (textField != null) {
          textField setText converter.convert(getItem).orNull
        }
        setText(null)
        setGraphic(textField)
      }
      else {
        setText(converter.convert(getItem).orNull)
        setGraphic(null)
      }
    }



trait TextFieldListCell[T] extends ListCell[T] { cell =>

  def converter: RightConverter[T, String]

  lazy val textField = new TextField {

    setOnKeyPressed(new EventHandler[KeyEvent] {
      def handle(t: KeyEvent) = {
        t match {
          case t if new KeyCodeCombination(KeyCode.ENTER) `match` t =>
            converter deconvert getText map commitEdit
          case t if new KeyCodeCombination(KeyCode.ESCAPE) `match` t => cell.cancelEdit()
          case _ =>
        }
      }
    })
  }

  override def startEdit() = {
    if (isEditable && getListView.isEditable) {
      super.startEdit()
      textField setText (converter convert getItem)
      setText(null)
      setGraphic(textField)
      textField.requestFocus()
    }
  }

  override def commitEdit(value: T) = {
    super.commitEdit(value)
    setGraphic(null)
    getListView.requestFocus()
  }

  override def cancelEdit() = {
    super.cancelEdit()
    setText(converter convert getItem)
    setGraphic(null)
  }

  override def updateItem(item: T, empty: Boolean) = {
    super.updateItem(item, empty)
    if (isEmpty()) {
      setText(null)
      setGraphic(null)
    }
    else {
      if (isEditing()) {
        if (textField != null) {
          textField setText (converter convert getItem)
        }
        setText(null)
        setGraphic(textField)
      }
      else {
        setText(converter convert getItem)
        setGraphic(null)
      }
    }
  }
}



//class TextFieldPropertyListCell extends ListCell[Property[String]] with TextFieldPropertyCell
//
//object TextFieldPropertyListCell extends Callback[ListView[Property[String]], ListCell[Property[String]]] {
//  def call(v: ListView[Property[String]]) = new TextFieldPropertyListCell
//}
//
