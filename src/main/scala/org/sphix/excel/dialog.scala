package org.sphix.excel

import scala.jdk.CollectionConverters.*

import javafx.scene.control.*

import org.controlsfx.control.CheckListView

import org.sphix.*
import org.sphix.ui.dialog.DialogUtils
import org.sphix.collection.*

/**
  * A dialog for selecting rows and columns to include when exporting a TableView to Excel.
  * If `includeColumns` is non-empty, only those columns will be available for selection. 
  * The columns in `excludeColumns`, if any, will not be available for selection. 
  * By default all columns are pre-selected.
  */
class TableExcelOptionsDialog(table: TableView[?], includeColumns: List[TableColumn[?, ?]] = Nil, excludeColumns: List[TableColumn[?, ?]] = Nil)
  extends Dialog[TableExcel.Options]
  with DialogUtils[TableExcel.Options]:

  val columnPaths = 
    TableExcel.getColumnPaths(table)
      .filter: path =>
        includeColumns.isEmpty || includeColumns.contains(path.leaf)
      .filterNot: path =>
        excludeColumns.contains(path.leaf)

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
      columns = TableExcel.Options.Columns.Only(includeColumnsList.getCheckModel.getCheckedItems.asScala.toList)
    yield TableExcel.Options(rows, columns)

  init()