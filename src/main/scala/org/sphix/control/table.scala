package org.sphix.control

import scala.compiletime.*

import java.time._

import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.control.{ TableColumn, TableCell, TableView, ContentDisplay, Label }
import javafx.scene.*
import javafx.scene.image.*
import javafx.util.Callback
import javafx.beans.property.Property

import no.vedaadata.text.LabelTransformer
import no.vedaadata.text.Format

import org.sphix.*
import org.sphix.util.*
import org.sphix.given
import org.sphix.collection.ObservableSeq
import org.sphix.util.ComboBoxFactory

import cell.*
import java.util.Comparator

trait TableColumnCells[S, T]:

  //   Data

  trait StringDataCell[A]

  //  Primitive

  trait StringCell[A](using val toData: To[T, A])(using asOption: AsOption[A, String]) extends TableCell[S, T] with cell.StringCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait BooleanCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Boolean]) extends TableCell[S, T] with cell.BooleanCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait ByteCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Byte])(using val format: Format.ByteFormat) extends TableCell[S, T] with cell.ByteCell[T]:
    override def dataValue(x: T) = asOption(toData(x))
  
  trait ShortCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Short])(using val format: Format.ShortFormat) extends TableCell[S, T] with cell.ShortCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait IntCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Int])(using val format: Format.IntFormat) extends TableCell[S, T] with cell.IntCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait LongCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Long])(using val format: Format.LongFormat) extends TableCell[S, T] with cell.LongCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait FloatCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Float])(using val format: Format.FloatFormat) extends TableCell[S, T] with cell.FloatCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait DoubleCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, Double])(using val format: Format.DoubleFormat) extends TableCell[S, T] with cell.DoubleCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait BigIntCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, BigInt])(using val format: Format.BigIntFormat) extends TableCell[S, T] with cell.BigIntCell[T]:
    override def dataValue(x: T) = asOption(toData(x))
  
  trait BigDecimalCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, BigDecimal])(using val format: Format.BigDecimalFormat) extends TableCell[S, T] with cell.BigDecimalCell[T]:
    override def dataValue(x: T) = asOption(toData(x))
  
  trait LocalDateCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, LocalDate])(using val formatter: Format.DateFormatter) extends TableCell[S, T] with cell.LocalDateCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait LocalTimeCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, LocalTime])(using val formatter: Format.TimeFormatter) extends TableCell[S, T] with cell.LocalTimeCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait LocalDateTimeCell[A](using val toData: To[T, A])(using val asOption: AsOption[A, LocalDateTime])(using val formatter: Format.DateTimeFormatter) extends TableCell[S, T] with cell.LocalDateTimeCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  //  convenience for backwards comp. consider if to keep
  def LocalDateCell(using formatter: Format.DateFormatter)(using AsOption[T, LocalDate]) = new LocalDateCell {}
  def LocalDateTimeCell(using formatter: Format.DateTimeFormatter)(using AsOption[T, LocalDateTime]) = new LocalDateTimeCell {}
  def LocalDateOptionCell(using formatter: Format.DateFormatter)(using AsOption[T, LocalDate]) = new LocalDateCell {}
  def LocalDateTimeOptionCell(using formatter: Format.DateTimeFormatter)(using AsOption[T, LocalDateTime]) = new LocalDateTimeCell {}

  def BigDecimalCell(using format: Format.BigDecimalFormat)(using AsOption[T, BigDecimal]) = new BigDecimalCell {}
  def BigDecimalOptionCell(using format: Format.BigDecimalFormat)(using AsOption[T, BigDecimal]) = new BigDecimalCell {}

  //  Text

  trait TextCell(text0: T => Option[String]) extends TableCell[S, T] with cell.TextCell[T]:
    def text(x: T) = text0(x)

  def TextCell(text: T => String) = new TextCell(x => Some(text(x))) {}

  trait BooleanTextCell[A](val trueText: Option[String], val falseText: Option[String])(using toData: To[T, A])(using asOption: AsOption[A, Boolean]) extends TableCell[S, T] with cell.BooleanTextCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  def BooleanTextCell[A](trueText: Option[String], falseText: Option[String])(using To[T, A])(using AsOption[A, Boolean]) = new BooleanTextCell[A](trueText, falseText) {}
  def BooleanTextCell[A](trueText: String, falseText: String)(using To[T, A])(using AsOption[A, Boolean]) = new BooleanTextCell[A](Some(trueText), Some(falseText)) {}

  //  Graphic

  trait GraphicCell(graphic0: T => Option[Node]) extends TableCell[S, T] with cell.GraphicCell[T]:
    def graphic(x: T) = graphic0(x)

  //  convenience for backwards comp. consider if to keep
  object GraphicCell:
    def apply(graphic: T => Option[Node]) = new GraphicCell(graphic) {}

  trait ImageCell[A](using val toImage: To[T, A])(using val asOption: AsOption[A, Image]) extends TableCell[S, T] with cell.ImageCell[T, A]

  trait StaticImageCell(val image: Image) extends TableCell[S, T] with cell.StaticImageCell[T]

  trait BooleanGraphicCell[A](val booleanGraphic: Node)(using toData: To[T, A])(using asOption: AsOption[A, Boolean]) extends TableCell[S, T] with cell.BooleanGraphicCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  trait BooleanImageCell[A](val trueImage: Option[Image] = None, val falseImage: Option[Image] = None)(using toData: To[T, A])(using asOption: AsOption[A, Boolean]) extends TableCell[S, T] with cell.BooleanImageCell[T]:
    override def dataValue(x: T) = asOption(toData(x))

  def BooleanImageCell[A](trueImage: Option[Image], falseImage: Option[Image] = None)(using To[T, A])(using AsOption[A, Boolean]) = new BooleanImageCell(trueImage, falseImage) {}
  def BooleanImageCell[A](trueImage: Image, falseImage: Image)(using To[T, A])(using AsOption[A, Boolean]) = new BooleanImageCell(Some(trueImage), Some(falseImage)) {}

  //  Control

  trait HyperlinkCell(action0: T => Unit, isHyperLink0: (T => Boolean) = { _ => true }) extends TableCell[S, T] with cell.HyperlinkCell[T]:
    def action(x: T): Unit = action0(x)
    override def isHyperlink(x: T): Boolean = isHyperLink0(x)

  object HyperlinkCell:
    trait When(isHyperlink0: T => Boolean)(action0: T => Unit) extends TableCell[S, T] with cell.HyperlinkCell[T]:
      def action(x: T): Unit = action0(x)
      override def isHyperlink(x: T) = isHyperlink0(x)

  trait TooltipCell[A](using val toTooltipText: To[T, A])(using val asTooltipTextOption: AsOption[A, String]) extends TableCell[S, T] with cell.TooltipCell[T, A]

  trait ProgressBarCell[A](val min: Double, val max: Double)(using to: To[T, A])(using asOption: AsOption[A, Double]) extends TableCell[S, T] with cell.ProgressBarCell[T]:
    def dataValue(x: T) = asOption(to(x))


  trait WebViewCell[A](using val toUrl: To[T, A])(using val asUrlOption: AsOption[A, String]) extends TableCell[S, T] with cell.WebViewCell[T, A]


  //  Util

  trait StripNewLines extends cell.StripNewLines[T]

  trait AlignmentCell(pos0: T => Option[Pos]) extends TableCell[S, T] with cell.AlignmentCell[T]:
    def pos(x: T) = pos0(x)

  trait AlignedCell(val pos: Pos) extends TableCell[S, T] with cell.AlignedCell[T]

  trait ContentDisplayCell(val contentDisplay: ContentDisplay) extends TableCell[S, T] with cell.ContentDisplayCell[T]

  trait StyleCell(val style: T => Option[String]) extends TableCell[S, T] with cell.StyleCell[T]
  trait StyledCell(val styled: String) extends TableCell[S, T] with cell.StyledCell[T]

  trait StyleClassCell(val styleClass: Map[String, T => Boolean]) extends TableCell[S, T] with cell.StyleClassCell[T]
  trait StyleClassedCell(val styleClassed: Iterable[String]) extends TableCell[S, T] with cell.StyleClassedCell[T]

  trait PseudoClassCell(val pseudoClass: Map[String, T => Boolean]) extends TableCell[S, T] with cell.PseudoClassCell[T]

  trait EnableDisableCell(val enabled: T => Boolean) extends TableCell[S, T] with cell.EnableDisableCell[T]

  //  Multi

  trait VBoxCell[A](f0: A => Node) extends TableCell[S, List[A]] with cell.VBoxCell[A]:
    def f(x: A) = f0(x)

  trait HBoxCell[A](val gap: Int, val divider: Option[String] = None)(f0: A => Node) extends TableCell[S, List[A]] with cell.HBoxCell[A]:
    def f(x: A) = f0(x)

  //  Editables

  trait TextFieldCell[D](using val converter: Converter[T, String])(using asDataOption: AsOption[T, D]) extends cell.TextFieldTableCell[S, T, D]:
    def dataValue(x: T) = asDataOption(x)

  //  for backwards comp.
  object TextFieldCell:
    def apply[D](using Converter[T, String], AsOption[T, D], DataTypeProvider[D]) = 
      new TextFieldCell[D] {}
  
  trait CheckBoxCellA extends TableCell[S, Boolean] with cell.CheckBoxCell
  
  trait CheckBoxCell(f0: S => Property[Boolean]) extends cell.CheckBoxTableCell[S]:
    def f(s: S) = f0(s)

  object CheckBoxCell:
    def apply(f0: S => Property[Boolean]) = new CheckBoxCell(f0) {}

  trait TriStateCheckBoxCell(checked0: S => Property[Boolean], indeterminate0: S => Property[Boolean]) 
    extends cell.TriStateCheckBoxTableCell[S, (Boolean, Boolean)]:
    def checked(s: S) = checked0(s)
    def indeterminate(s: S) = indeterminate0(s)

  object TriStateCheckBoxCell:
    def apply(checked0: S => Property[Boolean], indeterminate0: S => Property[Boolean]) = 
      new TriStateCheckBoxCell(checked0, indeterminate0) {}

  trait ComboBoxCell(items0: S => ObservableSeq[T])(f0: T => String) extends cell.ComboBoxTableCell[S, T]:
    def items(s: S) = items0(s)
    def f(t: T) = f0(t)

  // legacy
  object ComboBoxCell:
    def apply(items0: S => ObservableSeq[T], f0: T => String) = new ComboBoxCell(items0)(f0) {}

  trait StaticComboBoxCell(using factory0: ComboBoxFactory[T])(items0: S => ObservableSeq[T]) extends cell.StaticComboBoxTableCell[S, T]:
    def factory = factory0
    def items = items0


  // trait ComboBox2Cell[A] extends cell.ComboBox2TableCell[S, A]

  // object ComboBox2Cell {
  //   def apply[A](render0: A => String) = new ComboBox2Cell[A] {
  //     val render = render0
  //   }
  // }

  trait DatePickerCell extends cell.DatePickerTableCell[S]

  object DatePickerCell {
    def apply(formatter0: java.time.format.DateTimeFormatter) = new DatePickerCell {
      def formatter = formatter0
    }
  }

//  trait ValueFieldCell(using ValueConverter[T]) extends cell.TextFieldTableCell[S, T]


trait TableUtils[S]:
  this: TableView[S] =>

  class Column[T](prefWidth: Option[Double])(text: String, f: S => ObservableValue[T])
    extends TableColumn[S, T](text) 
    with TableColumnCells[S, T]:

    def this(prefWidth: Double)(text: String, f: S => ObservableValue[T]) =
      this(Some(prefWidth))(text, f)

    def this(text: String, f: S => ObservableValue[T]) =
      this(None)(text, f)

    prefWidth.foreach(setPrefWidth)

    setCellValueFactory(x => f(x.getValue))

    def setCell(tableCell: => TableCell[S, T]) =
      setCellFactory(_ => tableCell)    

    def setDefaultCell()(using cellFactory: TableCellFactory[S, T]) = setCellFactory(cellFactory)

    def setDefaultComparator()(using comparator: Comparator[T]) = setComparator(comparator)

  class HeaderColumn(prefWidth: Option[Double])(text: String, subColumns: TableColumn[S, ?]*)
    extends TableColumn[S, Nothing](text):
    
    def this(text: String, subColumns: TableColumn[S, ?]*) =
      this(None)(text, subColumns*)
    
    prefWidth.foreach(setPrefWidth)
    getColumns.addAll(subColumns*)

  //  don't really think this works that well as the text rendering is not very good
  // trait VerticalHeaderText:
  //   this: TableColumn[S, ?] =>
  //   val label = Label(getText)
  //   val padding = label.getPadding
  //   label.setPadding(Insets(padding.getTop, 10, padding.getBottom, 10))
  //   label.setRotate(-90)
  //   setGraphic(Group(label))
  //   setText(null)
