package org.sphix.control

import java.time._

import javafx.scene.*
import javafx.scene.control.{ ListView, ListCell }
import javafx.scene.image.Image

import no.vedaadata.text.Format

import cell.*

trait ListCells[T]:

  //  Text

  trait TextCell(text0: T => Option[String]) extends ListCell[T] with cell.TextCell[T]:
    def text(x: T) = text0(x)

  def TextCell(text: T => String) = new TextCell(x => Some(text(x))) {}

  trait BooleanTextCell[A](val trueText: Option[String], val falseText: Option[String])(using val toData: To[T, A])(using val asDataOption: AsOption[A, Boolean]) extends ListCell[T] with cell.BooleanTextCell[T, A]

  trait StripNewLines extends cell.StripNewLines[T]

  //  Primitive

  trait StringCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, String]) extends ListCell[T] with cell.StringCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait BooleanCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Boolean]) extends ListCell[T] with cell.BooleanCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait ByteCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Byte])(using val format: Format.ByteFormat) extends ListCell[T] with cell.ByteCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait ShortCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Short])(using val format: Format.ShortFormat) extends ListCell[T] with cell.ShortCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait IntCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Int])(using val format: Format.IntFormat) extends ListCell[T] with cell.IntCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait LongCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Long])(using val format: Format.LongFormat) extends ListCell[T] with cell.LongCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait FloatCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Float])(using val format: Format.FloatFormat) extends ListCell[T] with cell.FloatCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait DoubleCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Double])(using val format: Format.DoubleFormat) extends ListCell[T] with cell.DoubleCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait BigIntCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, BigInt])(using val format: Format.BigIntFormat) extends ListCell[T] with cell.BigIntCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait BigDecimalCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, BigDecimal])(using val format: Format.BigDecimalFormat) extends ListCell[T] with cell.BigDecimalCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait LocalDateCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, LocalDate])(using val formatter: Format.DateFormatter) extends ListCell[T] with cell.LocalDateCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait LocalTimeCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, LocalTime])(using val formatter: Format.TimeFormatter) extends ListCell[T] with cell.LocalTimeCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  trait LocalDateTimeCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, LocalDateTime])(using val formatter: Format.DateTimeFormatter) extends ListCell[T] with cell.LocalDateTimeCell[T, A]:
    override def toDataOption(x: T) = asOption(toData(x))

  //  Graphic

  trait GraphicCell(graphic0: T => Option[Node]) extends ListCell[T] with cell.GraphicCell[T]:
    def graphic(x: T) = graphic0(x)

  trait ImageCell[A](using val toImage: To[T, A])(using val asOption: AsOption[A, Image]) extends ListCell[T] with cell.ImageCell[T, A]

  trait StaticImageCell(val image: Image) extends ListCell[T] with cell.StaticImageCell[T]

  //  Misc

  trait HyperlinkCell(action0: T => Unit, isHyperLink0: (T => Boolean) = { _ => true }) extends ListCell[T] with cell.HyperlinkCell[T]:
    def action(x: T): Unit = action0(x)
    override def isHyperlink(x: T): Boolean = isHyperLink0(x)

  object HyperlinkCell:
    trait When(isHyperlink0: T => Boolean)(action0: T => Unit) extends ListCell[T] with cell.HyperlinkCell[T]:
      def action(x: T): Unit = action0(x)
      override def isHyperlink(x: T) = isHyperlink0(x)

  trait TooltipCell[A](using val toTooltipText: To[T, A])(using val asTooltipTextOption: AsOption[A, String]) extends ListCell[T] with cell.TooltipCell[T, A]

  trait ProgressBarCell[A](val min: Double, val max: Double)(using val toData: To[T, A])(using val asOption: AsOption[A, Double]) extends ListCell[T] with cell.ProgressBarCell[T, A]

  trait WebViewCell[A](using val toUrl: To[T, A])(using val asUrlOption: AsOption[A, String]) extends ListCell[T] with cell.WebViewCell[T, A]


trait ListUtils[T] extends ListCells[T]:
  this: ListView[T] =>

  def setCell(listCell: => ListCell[T]) =
    setCellFactory(_ => listCell)