package org.sphix.control.cell

import java.time._

import no.vedaadata.text.Format

import org.sphix.control._

//  Cells for Scala/Java types that we know how to render as text
//  These also include the corresponding data cell

trait StringCell[T, A] extends StringDataCell[T, A] with TextCell[T]:
  def text(x: T) = dataValue(x)

trait BooleanCell[T, A] extends BooleanDataCell[T, A] with TextCell[T]:
  def text(x: T) = dataValue(x).flatMap(b => Option.when(b)("\u2713"))

trait ByteCell[T, A] extends ByteDataCell[T, A] with TextCell[T]:
  def format: Format.ByteFormat
  def text(x: T) = dataValue(x).map(format.formatByte)

trait ShortCell[T, A] extends ShortDataCell[T, A] with TextCell[T]:
  def format: Format.ShortFormat
  def text(x: T) = dataValue(x).map(format.formatShort)

trait IntCell[T, A] extends IntDataCell[T, A] with TextCell[T]:
  def format: Format.IntFormat
  def text(x: T) = dataValue(x).map(format.formatInt)

trait LongCell[T, A] extends LongDataCell[T, A] with TextCell[T]:
  def format: Format.LongFormat
  def text(x: T) = dataValue(x).map(format.formatLong)

trait FloatCell[T, A] extends FloatDataCell[T, A] with TextCell[T]:
  def format: Format.FloatFormat
  def text(x: T) = dataValue(x).map(format.formatFloat)

trait DoubleCell[T, A] extends DoubleDataCell[T, A] with TextCell[T]:
  def format: Format.DoubleFormat
  def text(x: T) = dataValue(x).map(format.formatDouble)

trait BigIntCell[T, A] extends BigIntDataCell[T, A] with TextCell[T]:
  def format: Format.BigIntFormat
  def text(x: T) = dataValue(x).map(format.formatBigInt)

trait BigDecimalCell[T, A] extends BigDecimalDataCell[T, A] with TextCell[T]:
  def format: Format.BigDecimalFormat
  def text(x: T) = dataValue(x).map(format.formatBigDecimal)

trait LocalDateCell[T, A] extends LocalDateDataCell[T, A] with TextCell[T]:
  def formatter: Format.DateFormatter
  def text(x: T) = dataValue(x).map(formatter.formatDate)

trait LocalTimeCell[T, A] extends LocalTimeDataCell[T, A] with TextCell[T]:
  def formatter: Format.TimeFormatter
  def text(x: T) = dataValue(x).map(formatter.formatTime)

trait LocalDateTimeCell[T, A] extends LocalDateTimeDataCell[T, A] with TextCell[T]:
  def formatter: Format.DateTimeFormatter
  def text(x: T) = dataValue(x).map(formatter.formatDateTime)

