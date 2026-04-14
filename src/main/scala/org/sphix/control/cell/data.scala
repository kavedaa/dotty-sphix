package org.sphix.control.cell

import java.time._
import org.sphix.ui.viewer.Data

//  useful for tooling like e.g. export so we can determine the type of the data without having a value
enum DataType:
  case boolean, string, byte, short, int, long, float, double, bigInt, bigDecimal, localDate, localTime, localDateTime

/**
  * 
  * @tparam T the type of the item in the cell.
  * @tparam D the type of the data that can be read from the cell using the [[dataValue]] method.
  */
trait DataCell[T, D](using dataTypeProvider: DataTypeProvider[D]) extends Cell[T]:
  def dataType: DataType = dataTypeProvider.dataType
  def dataValue(x: T): Option[D]

trait DataTypeProvider[T]:
  def dataType: DataType

object DataTypeProvider:

  given [A](using inner: DataTypeProvider[A]): DataTypeProvider[Option[A]] with
    def dataType = inner.dataType

  given DataTypeProvider[String] with
    def dataType = DataType.string

  given DataTypeProvider[Boolean] with
    def dataType = DataType.boolean

  given DataTypeProvider[Byte] with
    def dataType = DataType.byte

  given DataTypeProvider[Short] with
    def dataType = DataType.short

  given DataTypeProvider[Int] with
    def dataType = DataType.int

  given DataTypeProvider[Long] with
    def dataType = DataType.long

  given DataTypeProvider[Float] with
    def dataType = DataType.float

  given DataTypeProvider[Double] with
    def dataType = DataType.double

  given DataTypeProvider[BigInt] with
    def dataType = DataType.bigInt

  given DataTypeProvider[BigDecimal] with
    def dataType = DataType.bigDecimal

  given DataTypeProvider[LocalDate] with
    def dataType = DataType.localDate

  given DataTypeProvider[LocalTime] with
    def dataType = DataType.localTime

  given DataTypeProvider[LocalDateTime] with
    def dataType = DataType.localDateTime

// trait StringDataCell[T] extends DataCell[T, String]:
//   def dataType = DataType.string

// trait BooleanDataCell[T] extends DataCell[T, Boolean]:
//   def dataType = DataType.boolean

// trait ByteDataCell[T] extends DataCell[T, Byte]:
//   def dataType = DataType.byte

// trait ShortDataCell[T] extends DataCell[T, Short]:
//   def dataType = DataType.short

// trait IntDataCell[T] extends DataCell[T, Int]:
//   def dataType = DataType.int

// trait LongDataCell[T] extends DataCell[T, Long]:
//   def dataType = DataType.long

// trait FloatDataCell[T] extends DataCell[T, Float]:
//   def dataType = DataType.float

// trait DoubleDataCell[T] extends DataCell[T, Double]:
//   def dataType = DataType.double

// trait BigIntDataCell[T] extends DataCell[T, BigInt]:
//   def dataType = DataType.bigInt

// trait BigDecimalDataCell[T] extends DataCell[T, BigDecimal]:
//   def dataType = DataType.bigDecimal

// trait LocalDateDataCell[T] extends DataCell[T, LocalDate]:
//   def dataType = DataType.localDate

// trait LocalTimeDataCell[T] extends DataCell[T, LocalTime]:
//   def dataType = DataType.localTime

// trait LocalDateTimeDataCell[T] extends DataCell[T, LocalDateTime]:
//   def dataType = DataType.localDateTime
