package org.sphix.control.cell

import java.time._

import no.vedaadata.text.Format

import org.sphix.control._

//  Cells for Scala/Java types that we know how to render as text
//  These also include the corresponding data cell

trait StringCell[T] extends DataCell[T, String] with TextCell[T]:
  def text(x: T) = dataValue(x)

trait BooleanCell[T] extends DataCell[T, Boolean] with TextCell[T]:
  def text(x: T) = dataValue(x).flatMap(b => Option.when(b)("\u2713"))

trait ByteCell[T] extends DataCell[T, Byte] with TextCell[T]:
  def format: Format.ByteFormat
  def text(x: T) = dataValue(x).map(format.formatByte)

trait ShortCell[T] extends DataCell[T, Short] with TextCell[T]:
  def format: Format.ShortFormat
  def text(x: T) = dataValue(x).map(format.formatShort)

trait IntCell[T] extends DataCell[T, Int] with TextCell[T]:
  def format: Format.IntFormat
  def text(x: T) = dataValue(x).map(format.formatInt)

trait LongCell[T] extends DataCell[T, Long] with TextCell[T]:
  def format: Format.LongFormat
  def text(x: T) = dataValue(x).map(format.formatLong)

trait FloatCell[T] extends DataCell[T, Float] with TextCell[T]:
  def format: Format.FloatFormat
  def text(x: T) = dataValue(x).map(format.formatFloat)

trait DoubleCell[T] extends DataCell[T, Double] with TextCell[T]:
  def format: Format.DoubleFormat
  def text(x: T) = dataValue(x).map(format.formatDouble)

trait BigIntCell[T] extends DataCell[T, BigInt] with TextCell[T]:
  def format: Format.BigIntFormat
  def text(x: T) = dataValue(x).map(format.formatBigInt)

trait BigDecimalCell[T] extends DataCell[T, BigDecimal] with TextCell[T]:
  def format: Format.BigDecimalFormat
  def text(x: T) = dataValue(x).map(format.formatBigDecimal)

trait LocalDateCell[T] extends DataCell[T, LocalDate] with TextCell[T]:
  def formatter: Format.DateFormatter
  def text(x: T) = dataValue(x).map(formatter.formatDate)

trait LocalTimeCell[T] extends DataCell[T, LocalTime] with TextCell[T]:
  def formatter: Format.TimeFormatter
  def text(x: T) = dataValue(x).map(formatter.formatTime)

trait LocalDateTimeCell[T] extends DataCell[T, LocalDateTime] with TextCell[T]:
  def formatter: Format.DateTimeFormatter
  def text(x: T) = dataValue(x).map(formatter.formatDateTime)

