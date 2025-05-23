package org.sphix.control.cell

import javafx.scene.control.TableCell
import javafx.event.EventHandler
import javafx.scene.control.*
import javafx.scene.input._
import javafx.util.StringConverter
import javafx.beans.{ InvalidationListener, Observable }

import org.sphix.*
import org.sphix.util.*
import org.sphix.control.*
import org.sphix.collection.mutable.ObservableBuffer
import org.sphix.collection.ObservableSeq
import org.sphix.util.ComboBoxFactory

trait ComboBoxTableCell[S, T] extends TableCell[S, T] { cell =>

  def items(s: S): ObservableSeq[T]
  
  def f(t: T): String
  
  this.getStyleClass().add("combo-box-table-cell");

  private lazy val comboBox = new ComboBox[T] with ComboBoxUtils[T] {

    setCell(TextCell(f))
    setButtonCell(TextCell(f))

    setMaxWidth(java.lang.Double.MAX_VALUE);

    getSelectionModel.selectedItemProperty onValue { v =>
      Option(v) foreach { item =>
        cell.commitEdit(item)
      }
    }

    setOnKeyPressed(new EventHandler[KeyEvent] {
      def handle(t: KeyEvent) = {
        if (new KeyCodeCombination(KeyCode.ESCAPE) `match` t) {
          cell.cancelEdit()
        }
      }
    })

  }

  override def startEdit() = {
    if (isEditable && getTableView.isEditable && getTableColumn.isEditable) {
    
      comboBox setItems items(getTableView.getItems get getIndex)
      comboBox.getSelectionModel select getItem //	important that this comes before super.startEdit()

      super.startEdit()

      setText(null)
      setGraphic(comboBox)

      //      comboBox show ()	//	this is wanted for good UX but triggers weird bug 
      comboBox.requestFocus()
    }
  }

  override def cancelEdit() = {
    super.cancelEdit()
    setText(f(getItem))
    setGraphic(null)
  }

  override def updateItem(item: T, empty: Boolean) = {
    super.updateItem(item, empty)
    if (!empty) {
      if (isEditing) {
        comboBox.getSelectionModel select getItem
        setText(null)
        setGraphic(comboBox)
      }
      else {
        setText(f(getItem))
        setGraphic(null)
      }
    }
    else {
      setText(null)
      setGraphic(null)
    }
  }
}


trait StaticComboBoxTableCell[S, T] extends TableCell[S, T]:
  cell =>

  def factory: ComboBoxFactory[T]

  def items: S => ObservableSeq[T]
  
  this.getStyleClass.add("combo-box-table-cell")

  private lazy val comboBox = 
    val cb = factory.create()
    cb.setConverter(factory.stringConverter)
    cb.setCellFactory(_ => createCell())
    cb.setButtonCell(createCell())
    cb.setMaxWidth(java.lang.Double.MAX_VALUE)
    cb

  private def createCell() = new ListCell[T] with TextCell[T]:
    def text(x: T) = Option(factory.stringConverter.toString(x))

  val listener = new InvalidationListener:
    def invalidated(o: Observable) =
      val item = comboBox.getSelectionModel.getSelectedItem
      if (item != null) && (item != getItem) then
          cell.startEdit()
          cell.commitEdit(item)

  override def updateItem(item: T, empty: Boolean) =
    super.updateItem(item, empty)
    if !empty then
      comboBox.getSelectionModel.selectedItemProperty.removeListener(listener)
      comboBox.setItems(items(cell.getTableRow.getItem))
      comboBox.getSelectionModel.select(item)
      comboBox.getSelectionModel.selectedItemProperty.addListener(listener)
      setText(null)
      setGraphic(comboBox)
    else 
      setText(null)
      setGraphic(null)
