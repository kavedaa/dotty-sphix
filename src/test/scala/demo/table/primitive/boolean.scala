package demo.table.primitive.boolean

import javafx.stage.Window
import javafx.scene.control.TableView
import javafx.geometry.Pos

import no.vedaadata.generator.Generator

import org.sphix.*
import org.sphix.util.*
import org.sphix.control.*
import org.sphix.collection.*

import demo.table.TableDemoBase

case class Person(
  name: String,
  isAdult: Boolean,
  isRich: Option[Boolean])

object Person:
  given Generator[Person] =
    (Generator("Ann", "Bob", "Christina", "Eric", "Frida", "George"),
    Generator[Boolean],
    Generator[Option[Boolean]]).mapN(Person.apply)
  
case class PersonModel(person0: Person):
  val name = Val(person0.name)
  val isAdult = Var(person0.isAdult)
  val isRich = Var(person0.isRich)

  isAdult.onValue(x => println(s"***** isAdult: $x"))

class PersonTable extends TableView[PersonModel] with TableUtils[PersonModel]:

  //  todo add trait for this
  val getIcon = ResourceImageResolver(getClass, "/icons/" + _)

  val userIcon = getIcon("user.png")
  val acceptIcon = getIcon("accept.png")
  val cancelIcon = getIcon("cancel.png")

  val name = new Column("Name", _.name):
    setCell:
      new StringCell {}

  val isAdult1 = new Column("Is adult", _.isAdult):
    setCell:
      new BooleanCell {}

  val isAdult2 = new Column("Is adult", _.isAdult):
    setCell:
      BooleanTextCell("Yes", "No")

  val isAdult3 = new Column("Is adult", _.isAdult):
    setCell:
      new BooleanImageCell(Some(acceptIcon), Some(cancelIcon))
      with AlignedCell(Pos.CENTER)

  val isAdult4 = new Column("Is adult", _.isAdult):
    setCell:
      new BooleanImageCell(Some(acceptIcon), Some(cancelIcon))
      with BooleanTextCell(Some("Yes"), Some("No"))

  val isAdult5 = new Column("Is adult", _.isAdult):
    setCell:
      new CheckBoxCellA
      with AlignedCell(Pos.CENTER)

  val isRich1 = new Column("Is rich", _.isRich):
    setCell:
      new BooleanCell {}

  val isRich2 = new Column("Is rich", _.isRich):
    setCell:
      BooleanTextCell("Yes", "No")

  val isRich3 = new Column("Is rich", _.isRich):
    setCell:
      new BooleanImageCell(Some(acceptIcon), Some(cancelIcon))
      with AlignedCell(Pos.CENTER)

  getColumns.addAll(name, isAdult1, isAdult2, isAdult3, isAdult4, isAdult5, isRich1, isRich2, isRich3)

  setEditable(true)


class BooleanDemo(using Window) extends TableDemoBase(new PersonTable):

  val data = Generator[Person].generate(10).map(PersonModel.apply)

  table.setItems(data.toObservableList)