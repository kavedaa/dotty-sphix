package demo.table.primitive.bigdecimal

import scala.util.*

import java.text.DecimalFormat

import javafx.stage.Window
import javafx.scene.control.TableView
import javafx.geometry.Pos

import no.vedaadata.generator.Generator
import no.vedaadata.text.Format

import org.sphix.*
import org.sphix.util.*
import org.sphix.control.*
import org.sphix.collection.*

import demo.table.TableDemoBase

case class InvoiceLine(
  item: String,
  priceEach: BigDecimal,
  quantity: Option[BigDecimal]):
  def total: Try[BigDecimal] = quantity match
    case Some(q) => Success(priceEach * q)
    case None => Failure(Exception("Quantity is not defined"))

object InvoiceLine:
  given Generator[InvoiceLine] =
    (Generator("Apple", "Banana", "Cherry", "Date", "Elderberry", "Fig"),
    Generator.between(0.0, 1000.0).map(BigDecimal.apply),
    Generator.between(1.0, 10.0).map(BigDecimal.apply).andThen[Option]).mapN(InvoiceLine.apply)
  
case class InvoiceLineModel(invoiceLine0: InvoiceLine):
  val item = Val(invoiceLine0.item)
  val priceEach = Var(invoiceLine0.priceEach)
  val quantity = Var(invoiceLine0.quantity)
  val total = Val(invoiceLine0.total)
  
class InvoiceLineTable extends TableView[InvoiceLineModel] with TableUtils[InvoiceLineModel]:

  given Format.BigDecimalFormat = DecimalFormat("0.00")

  //  todo add trait for this
  val getIcon = ResourceImageResolver(getClass, "/icons/" + _)

  val userIcon = getIcon("user.png")
  val acceptIcon = getIcon("accept.png")
  val cancelIcon = getIcon("cancel.png")

  val item = new Column("Name", _.item):
    setDefaultCell()

  val priceEach = new Column("Price each", _.priceEach):
    setCell:
      new BigDecimalCell
      with AlignedCell(Pos.CENTER_RIGHT)

  val quantity = new Column("Quantity", _.quantity):
    setCell:
      new BigDecimalCell
      with AlignedCell(Pos.CENTER_RIGHT)

  val total = new Column("Total", _.total): 
    setCell:
      new BigDecimalCell
      with AlignedCell(Pos.CENTER_RIGHT)

  getColumns.addAll(item, priceEach, quantity, total)

class BigDecimalDemo(using Window) extends TableDemoBase(new InvoiceLineTable):

  val data = Generator[InvoiceLine].generate(10).map(InvoiceLineModel.apply)

  table.setItems(data.toObservableList)