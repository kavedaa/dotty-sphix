package demo.table

import scala.language.implicitConversions

import java.time.*

import javafx.stage.Window
import javafx.application.Application
import javafx.scene.layout._
import javafx.scene.control._

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq
import org.sphix.ui.FileChoosing
import org.sphix.excel.TableExcel

class DataTable extends TableView[Data] with TableUtils[Data]:

  val string = new Column("String", _.string.toVal):
    setDefaultCell()

  val boolean = new Column("Boolean", _.boolean.toVal):
    setDefaultCell()

  val byte = new Column("Byte", _.byte.toVal):
    setDefaultCell()

  val short = new Column("Short", _.short.toVal):
    setDefaultCell()

  val int = new Column("Int", _.int.toVal):
    setDefaultCell()

  val long = new Column("Long", _.long.toVal):
    setDefaultCell()

  val float = new Column("Float", _.float.toVal):
    setDefaultCell()

  val double = new Column("Double", _.double.toVal):
    setDefaultCell()

  val bigInt = new Column("BigInt", _.bigInt.toVal):
    setDefaultCell()

  val bigDecimal = new Column("BigDecimal", _.bigDecimal.toVal):
    setDefaultCell()

  val localDate = new Column("LocalDate", _.localDate.toVal):
    setDefaultCell()

  val localTime = new Column("LocalTime", _.localTime.toVal):
    setDefaultCell()

  val localDateTime = new Column("LocalDateTime", _.localDateTime.toVal):
    setDefaultCell()

  getColumns.addAll(string, boolean, byte, short, int, long, float, double, bigInt, bigDecimal, localDate, localTime, localDateTime)


class DefaultsDemo(using Window) extends TableDemoBase(new DataTable):
  val data = Data.generator.generate(100).to(ObservableSeq)
  table.setItems(data)

