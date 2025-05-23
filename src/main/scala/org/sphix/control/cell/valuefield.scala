package org.sphix.control.cell

import javafx.scene.control.*
import javafx.scene.input.*

import org.sphix.*
import org.sphix.control.*

trait ValueFieldTableCell[S, T](using converter: ValueConverter[T]) extends TableCell[S, T]:
  cell =>

  lazy val valueField = new ValueField[T]:
    setOnKeyPressed({
      case keyEvent if new KeyCodeCombination(KeyCode.ENTER) `match` keyEvent =>
        value() match
          case Value.Valid(x) => 
            cell.commitEdit(x)
          case _ => //  TODO add some tooltip?        
      case keyEvent if new KeyCodeCombination(KeyCode.ESCAPE) `match` keyEvent => 
        cell.cancelEdit()
      case _ =>
    })

  override def startEdit() =
    if isEditable && getTableView.isEditable then
      super.startEdit()
      valueField.setValue(getItem)
      setText(null)
      setGraphic(valueField)
      valueField.requestFocus()

  override def commitEdit(value: T) =
    super.commitEdit(value)
    setGraphic(null)
    getTableView.requestFocus()

  override def cancelEdit() =
    super.cancelEdit()
    setText(converter.convert(getItem))
    setGraphic(null)

  override def updateItem(item: T, empty: Boolean) = 
    super.updateItem(item, empty)
    if isEmpty() then
      setText(null)
      setGraphic(null)
    else 
      if isEditing() then
        if valueField != null then valueField.setValue(getItem)        
        setText(null)
        setGraphic(valueField)
      else
        setText(converter.convert(getItem))
        setGraphic(null)

