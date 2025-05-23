package org.sphix.control

import java.time.*

import javafx.util.Callback
import javafx.scene.control.{ TableColumn, TableCell, TableView, ContentDisplay }
import javafx.geometry.Pos

import no.vedaadata.text.Format

import cell.*
  
trait TableCellFactory[S, T] extends Callback[TableColumn[S, T], TableCell[S, T]]

object TableCellFactory:

  given defaultString[S, T, A](using to: To[T, A])(using asOption: AsOption[A, String]): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.StringCell[T, A]:
        def toDataOption(x: T) = asOption(to(x))

  given defaultBoolean[S, T, A](using to: To[T, A])(using asOption: AsOption[A, Boolean]): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.BooleanCell[T, A]:
        def toDataOption(x: T) = asOption(to(x))

  given defaultByte[S, T, A](using to: To[T, A])(using asOption: AsOption[A, Byte])(using format0: Format.ByteFormat): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.ByteCell[T, A] with cell.AlignedCell[T]:
        def toDataOption(x: T) = asOption(to(x))
        def format = format0
        def pos = Pos.TOP_RIGHT

  given defaultShort[S, T, A](using to: To[T, A])(using asOption: AsOption[A, Short])(using format0: Format.ShortFormat): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.ShortCell[T, A] with cell.AlignedCell[T]:
        def toDataOption(x: T) = asOption(to(x))
        def format = format0
        def pos = Pos.TOP_RIGHT

  given defaultInt[S, T, A](using to: To[T, A])(using asOption: AsOption[A, Int])(using format0: Format.IntFormat): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.IntCell[T, A] with cell.AlignedCell[T]:
        def toDataOption(x: T) = asOption(to(x))
        def format = format0
        def pos = Pos.TOP_RIGHT

  given defaultLong[S, T, A](using to: To[T, A])(using asOption: AsOption[A, Long])(using format0: Format.LongFormat): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.LongCell[T, A] with cell.AlignedCell[T]:
        def toDataOption(x: T) = asOption(to(x))
        def format = format0
        def pos = Pos.TOP_RIGHT

  given defaultFloat[S, T, A](using to: To[T, A])(using asOption: AsOption[A, Float])(using format0: Format.FloatFormat): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.FloatCell[T, A] with cell.AlignedCell[T]:
        def toDataOption(x: T) = asOption(to(x))
        def format = format0
        def pos = Pos.TOP_RIGHT

  given defaultDouble[S, T, A](using to: To[T, A])(using asOption: AsOption[A, Double])(using format0: Format.DoubleFormat): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.DoubleCell[T, A] with cell.AlignedCell[T]:
        def toDataOption(x: T) = asOption(to(x))
        def format = format0
        def pos = Pos.TOP_RIGHT

  given defaultBigInt[S, T, A](using to: To[T, A])(using asOption: AsOption[A, BigInt])(using format0: Format.BigIntFormat): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.BigIntCell[T, A] with cell.AlignedCell[T]:
        def toDataOption(x: T) = asOption(to(x))
        def format = format0
        def pos = Pos.TOP_RIGHT

  given defaultBigDecimal[S, T, A](using to: To[T, A])(using asOption: AsOption[A, BigDecimal])(using format0: Format.BigDecimalFormat): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.BigDecimalCell[T, A] with cell.AlignedCell[T]:
        def toDataOption(x: T) = asOption(to(x))
        def format = format0
        def pos = Pos.TOP_RIGHT

  given defaultLocalDate[S, T, A](using to: To[T, A])(using asOption: AsOption[A, LocalDate])(using formatter0: Format.DateFormatter): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.LocalDateCell[T, A]:
        def toDataOption(x: T) = asOption(to(x))
        def formatter = formatter0

  given defaultLocalTime[S, T, A](using to: To[T, A])(using asOption: AsOption[A, LocalTime])(using formatter0: Format.TimeFormatter): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.LocalTimeCell[T, A]:
        def toDataOption(x: T) = asOption(to(x))
        def formatter = formatter0

  given defaultLocalDateTime[S, T, A](using to: To[T, A])(using asOption: AsOption[A, LocalDateTime])(using formatter0: Format.DateTimeFormatter): TableCellFactory[S, T] = 
    _ => 
      new TableCell[S, T] with cell.LocalDateTimeCell[T, A]:
        def toDataOption(x: T) = asOption(to(x))
        def formatter = formatter0
