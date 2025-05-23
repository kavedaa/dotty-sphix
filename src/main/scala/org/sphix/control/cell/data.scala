package org.sphix.control.cell

import java.time._

//  useful for tooling like e.g. export so we can determine the type of the data without having a value
enum DataType:
  case boolean, string, byte, short, int, long, float, double, bigInt, bigDecimal, localDate, localTime, localDateTime

trait DataCell[T, A, D] extends Cell[T]:
  def dataType: DataType
  def toDataOption(x: T): Option[D]
  def dataValue(x: T) = toDataOption(x)

trait StringDataCell[T, A] extends DataCell[T, A, String]:
  def dataType = DataType.string

trait BooleanDataCell[T, A] extends DataCell[T, A, Boolean]:
  def dataType = DataType.boolean

trait ByteDataCell[T, A] extends DataCell[T, A, Byte]:
  def dataType = DataType.byte

trait ShortDataCell[T, A] extends DataCell[T, A, Short]:
  def dataType = DataType.short

trait IntDataCell[T, A] extends DataCell[T, A, Int]:
  def dataType = DataType.int

trait LongDataCell[T, A] extends DataCell[T, A, Long]:
  def dataType = DataType.long

trait FloatDataCell[T, A] extends DataCell[T, A, Float]:
  def dataType = DataType.float

trait DoubleDataCell[T, A] extends DataCell[T, A, Double]:
  def dataType = DataType.double

trait BigIntDataCell[T, A] extends DataCell[T, A, BigInt]:
  def dataType = DataType.bigInt

trait BigDecimalDataCell[T, A] extends DataCell[T, A, BigDecimal]:
  def dataType = DataType.bigDecimal

trait LocalDateDataCell[T, A] extends DataCell[T, A, LocalDate]:
  def dataType = DataType.localDate

trait LocalTimeDataCell[T, A] extends DataCell[T, A, LocalTime]:
  def dataType = DataType.localTime

trait LocalDateTimeDataCell[T, A] extends DataCell[T, A, LocalDateTime]:
  def dataType = DataType.localDateTime
