package demo.table.graphic

import scala.util._

import javafx.stage.Window
import javafx.geometry._
import javafx.scene.layout.BorderPane
import javafx.scene.control.TableView
import javafx.scene.image.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.util.*
import org.sphix.collection.ObservableSeq

import no.vedaadata.generator.*

import demo.table.*

case class Country(
  name: String,
  isMonarchy: Boolean)

case class Pet(
  name: String,
  isMammal: Boolean)

case class Person(
  name: String,
  isAdult: Boolean,
  country: Country,
  hasLargeFortune: Option[Boolean],
  pet: Option[Pet])

object Person:

  val generator = 
    (Generator("Ann", "Bob", "Christina", "Eric", "Frida", "George"),
    Generator[Boolean],
    Generator(Country("Norway", true), Country("Sweden", true), Country("Germany", false)),
    Generator[Option[Boolean]],
    Generator(Pet("Fido", true), Pet("Felix", true), Pet("Lizzy", false)).andThen[Option]).mapN(Person.apply)

class PersonTable extends TableView[Person] with TableUtils[Person] {

  val getIcon = ResourceImageResolver(getClass, "/icons/" + _)

  val userIcon = getIcon("user.png")
  val acceptIcon = getIcon("accept.png")
  val cancelIcon = getIcon("cancel.png")

  val name = new Column("Name", _.name.toVal):
    setCell:
      new StaticImageCell(userIcon)
      with StringCell

  val isAdult = new Column("Is adult", _.isAdult.toVal):
    setCell:
      new BooleanImageCell(Some(acceptIcon))
      with AlignedCell(Pos.CENTER)

  val hasLargeFortune = new Column("Has large fortune", _.hasLargeFortune.toVal):
    setCell:
      new BooleanImageCell(Some(acceptIcon), Some(cancelIcon)) {}

  val country = new Column("Country", _.country.toVal):
    setCell:
      new TextCell(x => Some(x.name))
      with BooleanImageCell(Some(acceptIcon))(using _.isMonarchy)

  getColumns.addAll(name, isAdult, hasLargeFortune, country)
}

class GraphicDemo(using Window) extends TableDemoBase(new PersonTable):
  val persons = Person.generator.generate(25).to(ObservableSeq)
  table.setItems(persons)

