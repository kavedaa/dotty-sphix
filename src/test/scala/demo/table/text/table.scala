package demo.table.text

import scala.language.implicitConversions

import java.time.*

import javafx.application.Application
import javafx.scene.control._

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq

import demo.table.Data

@main def main() =
  Application.launch(classOf[TableDemo])

class TableDemo extends SimpleApp:

  val data = Data.generator.generate(100).to(ObservableSeq)

  val table = new DataTable:
    setItems(data)

  def root = table

class DataTable extends TableView[Data] with TableUtils[Data]:

  val string = new Column("String", _.string.toVal):
    setCell(new StringCell {})

  val boolean = new Column("Boolean", _.boolean.toVal):
    setCell(new BooleanCell {})

  val byte = new Column("Byte", _.byte.toVal):
    setCell(new ByteCell {})

  // val short = new Column("Short", _.short.toVal):
  //   setCell(new ShortCell() {})

  // val int = new Column("Int", _.int.toVal):
  //   setCell(new IntCell() {})

  // val long = new Column("Long", _.long.toVal):
  //   setCell(new LongCell() {})

  // val float = new Column("Float", _.float.toVal):
  //   setCell(new FloatCell() {})

  // val double = new Column("Double", _.double.toVal):
  //   setCell(new DoubleCell() {})

  // val bigInt = new Column("BigInt", _.bigInt.toVal):
  //   setCell(new BigIntCell() {})

  // val bigDecimal = new Column("BigDecimal", _.bigDecimal.toVal):
  //   setCell(new BigDecimalCell() {})

  // val localDate = new Column("LocalDate", _.localDate.toVal):
  //   setCell(new LocalDateCell() {})

  // val localTime = new Column("LocalTime", _.localTime.toVal):
  //   setCell(new LocalTimeCell() {})

  // val localDateTime = new Column("LocalDateTime", _.localDateTime.toVal):
  //   setCell(new LocalDateTimeCell() {})

  // getColumns.addAll(string, boolean, byte, short, int, long, float, double, bigInt, bigDecimal, localDate, localTime, localDateTime)

