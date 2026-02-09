package demo.table.proxy

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

  val string = new Column("String", _.toVal):
    setCell(new StringCell(using _.string) {})

  val boolean = new Column("Boolean", _.toVal):
    setCell(new BooleanCell(using _.boolean) {})

  val byte = new Column("Byte", _.toVal):
    setCell(new ByteCell(using _.byte) {})

  val short = new Column("Short", _.toVal):
    setCell(new ShortCell(using _.short) {})

  val int = new Column("Int", _.toVal):
    setCell(new IntCell(using _.int) {})

  val long = new Column("Long", _.toVal):
    setCell(new LongCell(using _.long) {})

  val float = new Column("Float", _.toVal):
    setCell(new FloatCell(using _.float) {})

  val double = new Column("Double", _.toVal):
    setCell(new DoubleCell(using _.double) {})

  val bigInt = new Column("BigInt", _.toVal):
    setCell(new BigIntCell(using _.bigInt) {})

  val bigDecimal = new Column("BigDecimal", _.toVal):
    setCell(new BigDecimalCell(using _.bigDecimal) {})

  val localDate = new Column("LocalDate", _.toVal):
    setCell(new LocalDateCell(using _.localDate) {})

  val localTime = new Column("LocalTime", _.toVal):
    setCell(new LocalTimeCell(using _.localTime) {})

  val localDateTime = new Column("LocalDateTime", _.toVal):
    setCell(new LocalDateTimeCell(using _.localDateTime) {})

  getColumns.addAll(string, boolean, byte, short, int, long, float, double, bigInt, bigDecimal, localDate, localTime, localDateTime)

