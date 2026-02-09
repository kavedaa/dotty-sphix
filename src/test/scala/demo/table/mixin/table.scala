package demo.table.mixin

import scala.language.implicitConversions
import scala.util.*

import java.time.*
import java.time.format.*
import java.text.DecimalFormat

import javafx.application.Application
import javafx.geometry.*
import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.scene.image.*
import javafx.scene.shape.*
import javafx.scene.paint.*

import no.vedaadata.generator.Generator
import no.vedaadata.text.Format

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq
import org.sphix.ui.FileChoosing
import org.sphix.excel.TableExcel

import demo.table.*

class PersonTable extends TableView[Person] with TableUtils[Person]:

  given Format.DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss")

  given Format.DoubleFormat = DecimalFormat("0.0 %")

  val name = new Column("Name", _.name.toVal):
    getStyleClass.add("name")
    setCell:
      new StringCell
      with StyledCell("-fx-font-style: italic")
      with AlignedCell(Pos.CENTER_LEFT) 

  val age = new Column("Age", _.age.toVal):
    getStyleClass.add("age")
    setDefaultCell()

  val birthDate = new Column("Birth date", _.birthDate.toVal):
    setDefaultCell()

  val isAdult = new Column("Adult", _.isAdult.toVal):
    setCell:
      new BooleanGraphicCell(new Circle(8, Color.GREEN)) 
      with AlignedCell(Pos.CENTER)

  val country = new Column("Country", _.country.toVal):
    setCell:
      new StringCell(using _.map(_.name)) 
      with TooltipCell(using _.map(_.description))

  val height = new Column("Height", _.height.toVal):
    setDefaultCell()

  val weight = new Column("Weight", _.weight.toVal):
    setDefaultCell()

  val physical = HeaderColumn("Physical", height, weight)

  val rating = new Column("Rating", _.rating.toVal):
    setCell:
      new ProgressBarCell(0d, 1d)
      with ContentDisplayCell(ContentDisplay.RIGHT)
      with AlignedCell(Pos.TOP_RIGHT)

  val fortune = new Column("Fortune", _.fortune.toVal):
    setCell:
      new BigDecimalCell
      with AlignedCell(Pos.CENTER_RIGHT) 
      with TooltipCell(using _.map:
        case x if x > 10000 => "Good"
        case x if x > 0 => "Not so much"
        case _ => "Ouch")
      with StyleClassedCell(Seq("fortune"))
      with StyleClassCell(
        Map(
          "positive" -> { _.toOption.exists(_ > 0) },
          "negative" -> { _.toOption.exists(_ < 0) } ))

  val lastLogin = new Column("Last login", _.lastLogin.toVal):
    setDefaultCell()

  getColumns.addAll(name, age, birthDate, isAdult, country, physical, rating, fortune, lastLogin)


object MixinDemo extends BorderPane:

  val persons = Person.generator.generate(100).to(ObservableSeq)

  val table = new PersonTable:
    setItems(persons)

  setCenter(table)


