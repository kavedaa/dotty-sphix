package org.sphix.excel

import scala.util.*
import scala.jdk.CollectionConverters.*

import java.io.*
import java.time.*

import javafx.stage.Window
import javafx.util.Callback
import javafx.beans.value.ObservableValue
import javafx.scene.control.*

import org.apache.poi.ss.usermodel.{ Row, CellStyle, Workbook }
import org.apache.poi.xssf.usermodel.XSSFWorkbook

import no.vedaadata.excel.*
import no.vedaadata.text.LabelTransformer

import org.sphix.control.cell.DataCell
import org.sphix.control.cell.DataType

object TableExcel:

  case class Options(
    rows: Options.Rows,
    columns: Options.Columns)

  object Options:

    enum Rows:
      case All, Selected

    enum Columns:
      case All
      case Include(columnPaths: List[ColumnPath])

    val Default = Options(Rows.All, Columns.All)
  

  opaque type WidthFactor <: Double = Double

  object WidthFactor:

    given default: WidthFactor = 50.0

  def optionsDialog(table: TableView[?])(using Window): TableExcelOptionsDialog =
    new TableExcelOptionsDialog(table)

  def getColumnPaths(table: TableView[?]): List[ColumnPath] =
    table.getColumns.asScala.toList.flatMap(ColumnPath.leafs)

  def writeFile(file: File, table: TableView[?], options: Options = Options.Default)(using WidthFactor): Try[File] =

    given wb: Workbook = new XSSFWorkbook

    val sheet = wb.createSheet

    given baseCellStyle: CellStyle = wb.createCellStyle()

    val allColumnPaths = getColumnPaths(table)

    val columnPaths = options.columns match
      case Options.Columns.All => allColumnPaths
      case Options.Columns.Include(columnPaths) => columnPaths

    val columnDatas = ColumnData.fromColumnPaths(columnPaths)(baseCellStyle)

    val headerRow = sheet.createRow(0)

    columnDatas.foreach: columnData =>
      sheet.setColumnWidth(columnData.index, columnData.width.toInt)
      createCell(columnData.index, columnData.title)(using headerRow)

    val items = options.rows match
      case Options.Rows.All => table.getItems
      case Options.Rows.Selected => table.getSelectionModel.getSelectedItems

    items.asScala.toList.zipWithIndex.foreach: (item, index) =>
      given Row = sheet.createRow(index + 1)  
      columnDatas.foreach: columnData =>
        val cellValue = columnData.tableCellValueFactory.call(new TableColumn.CellDataFeatures[Any, Any](table.asInstanceOf[TableView[Any]], columnData.column, item))  
          given CellStyle = columnData.cellStyle
          columnData.dataCell match
            case Success(dataCell) =>
              dataCell.dataValue(cellValue.getValue).foreach: value =>
                value match
                  case x: String => createCell(columnData.index, x)
                  case x: Boolean => createCell(columnData.index, x)
                  case x: Byte => createCell(columnData.index, x)
                  case x: Short => createCell(columnData.index, x)
                  case x: Int => createCell(columnData.index, x)
                  case x: Long => createCell(columnData.index, x)
                  case x: Float => createCell(columnData.index, x)
                  case x: Double => createCell(columnData.index, x)
                  case x: BigInt => createCell(columnData.index, x)
                  case x: BigDecimal => createCell(columnData.index, x)
                  case x: LocalDate => createCell(columnData.index, x)
                  case x: LocalTime => createCell(columnData.index, x)
                  case x: LocalDateTime => createCell(columnData.index, x)
                  case null => createCell(columnData.index, "null")
                  case _ => createCell(columnData.index, "unsupported data type")                
            case Failure(ex) => createCell(columnData.index, ex.getMessage)                        

    val fos = new FileOutputStream(file) 

    try
      wb.write(fos)
      Success(file)
    catch
      case ex => Failure(ex)
    finally
      wb.close()
      fos.close()

  end writeFile

  private case class ColumnData(
    column: TableColumn[Any, Any],
    index: Int,
    title: String,
    tableCellValueFactory: Callback[TableColumn.CellDataFeatures[Any, Any], ObservableValue[Any]],
    dataCell: Try[DataCell[Any, Any]],
    cellStyle: CellStyle,
    width: Double)

  private object ColumnData:
    def fromColumnPaths(columnPaths: List[ColumnPath])(using wf: WidthFactor)(baseCellStyle: CellStyle)(using Workbook) = 
      columnPaths.zipWithIndex.flatMap: (columnPath, index) =>
        val column = columnPath.leaf.asInstanceOf[TableColumn[Any, Any]]
        val title = columnPath.fullTitle
        for 
          tableCellValueFactory <- Option(column.getCellValueFactory)
          tableCellFactory <- Option(column.getCellFactory)
          dataCell = tableCellFactory.call(column) match
            case dataCell: DataCell[?, ?] => Success(dataCell.asInstanceOf[DataCell[Any, Any]])
            case _ => Failure(new Exception("not a data cell"))        
          cellStyle = dataCell.map(_.dataType).map(getCellStyleProvider).getOrElse(CellStyleProvider.default).provide(baseCellStyle)
        yield ColumnData(
          column,
          index,
          title,
          tableCellValueFactory,
          dataCell,
          cellStyle,
          column.getWidth * wf)            

  private def getCellStyleProvider(dataType: DataType) =
    dataType match
      case DataType.localDate => CellStyleProvider.localDate
      case DataType.localTime => CellStyleProvider.localTime
      case DataType.localDateTime => CellStyleProvider.localDateTime
      case _ => CellStyleProvider.default
    

  case class ColumnPath(leaf: TableColumn[?, ?], path: List[TableColumn[?, ?]]):
    private def children: List[ColumnPath] = leaf match
      case c if c.getColumns.isEmpty => List(this)
      case c => c.getColumns.asScala.toList.flatMap(column => ColumnPath(column, fullPath).children)
    private val fullPath = leaf +: path
    def titles = fullPath.reverse.map(_.getText)
    def fullTitle = titles.mkString(", ")
    override def toString = fullTitle

  private object ColumnPath:
    def leafs(column: TableColumn[?, ?]) = ColumnPath(column, Nil).children
