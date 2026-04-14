package org.sphix.excel

import scala.jdk.CollectionConverters.*

import javafx.scene.control.*

import org.controlsfx.control.CheckListView

import org.sphix.*
import org.sphix.ui.dialog.DialogUtils
import org.sphix.collection.*

class TableExcelOptionsDialog(table: TableView[?])
  extends Dialog[TableExcel.Options]
  with DialogUtils[TableExcel.Options]:

  val columnPaths = TableExcel.getColumnPaths(table)

  def title = "Options"

  val rowsGroup = new ToggleGroup

  val allRowsButton = new RadioButton("All"):
    setToggleGroup(rowsGroup)
    setSelected(true)

  val selectedRowsButton = new RadioButton("Selected"):
    setToggleGroup(rowsGroup)

  val includeColumnsList = new CheckListView[TableExcel.ColumnPath]:
    setItems(columnPaths.toObservableList)
    getCheckModel.checkAll()

  def content = vbox(
    velem("Rows:", hbox(allRowsButton, selectedRowsButton)),
    velem("Columns:", includeColumnsList))

  def ok = "OK"

  def valid = Val(true)

  def result =
    for 
      rows <- 
        if allRowsButton.isSelected then Some(TableExcel.Options.Rows.All) 
        else if selectedRowsButton.isSelected then Some(TableExcel.Options.Rows.Selected) 
        else None
      columns = TableExcel.Options.Columns.Include(includeColumnsList.getCheckModel.getCheckedItems.asScala.toList)
    yield TableExcel.Options(rows, columns)

  init()