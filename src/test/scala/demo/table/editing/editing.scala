package demo.table.editing

import scala.util.Random

import java.time.*

import javafx.stage.Window
import javafx.application.Application
import javafx.scene.layout._
import javafx.scene.control._
import javafx.util.StringConverter
import javafx.geometry.Pos

import no.vedaadata.generator.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq
import org.sphix.control.cell.*

enum Hobby:
  case Golf, Chess, Fishing, Hunting, Skiing, Tennis, Hiking

object Hobby:
  given (Hobby => String) = _.toString

enum City:
  case Bergen, Oslo, London, Paris

case class Person(
  firstName: String,
  lastName: String,
  age: Int,
  city: City,
  isAdult: Boolean,
  isSenior: Option[Boolean],
  favoriteHobby: Option[Hobby],
  hobbies: List[Hobby])

object Person:

  given Generator[Person] =
    (Generator("Ole", "Per", "Jens"),
    Generator("Hansen", "Nilsen", "Andersen"),
    IntGenerator(20, 50),
    Generator.from(City.values),
    Generator[Boolean],
    Generator[Option[Boolean]],
    Generator.from(Hobby.values).andThen[Option],
    Generator.pick(3)(Hobby.values)).mapN(Person.apply)

case class PersonModel(person0: Person):

  val firstName = Var(person0.firstName)
  val lastName = Var(person0.lastName)
  val age = Var(person0.age)
  val city = Var(person0.city)
  val isAdult = Var(person0.isAdult) 
  val isSenior = TriStateModel.fromOption(person0.isSenior)  
  val favoriteHobby = Var(person0.favoriteHobby)

  val toPerson = (firstName, lastName, age, city, isAdult, isSenior.asOption, favoriteHobby, Val(person0.hobbies)).mapN(Person.apply)

  toPerson.onValue(println)

class PersonTable extends TableView[PersonModel] with TableUtils[PersonModel]:

  val firstName = new Column(150)("First name", _.firstName):
    setCell(new TextFieldCell {})

  val lastName = new Column(150)("Last name", _.lastName):
    setCell(new javafx.scene.control.cell.TextFieldTableCell(new javafx.util.converter.DefaultStringConverter))

  val age = new Column("Age", _.age)

  val isAdult1 = new Column("Adult 1", _.isAdult):
    setCell:
      new CheckBoxCell(_.isAdult)
      with AlignedCell(Pos.CENTER)

  val isAdult2 = new Column("Adult 2", _.isAdult):
    setCell:
      new CheckBoxCell(_.isAdult)
      with AlignedCell(Pos.CENTER)

  val isSenior = new Column("Senior", _.isSenior.asVal):
    setCell(TriStateCheckBoxCell(_.isSenior.checked, _.isSenior.indeterminate))

  val city1 = new Column("City 1", _.city):
    setCell(ComboBoxCell(_ => City.values.to(ObservableSeq), _.toString))

  given (City => String) = _.toString

  val city2 = new Column("City 2", _.city):
    setCell(new StaticComboBoxCell(_ => City.values.to(ObservableSeq)) {})

  val favoriteHobby = new Column("Favorite hobby", _.favoriteHobby):
    setCell(new StaticComboBoxCell(_ => Hobby.values.map(Some.apply).to(ObservableSeq)) {})

  getColumns.addAll(firstName, lastName, age, city1, city2, isAdult1, isAdult2, isSenior, favoriteHobby)

  setEditable(true)

class EditingDemo(using Window) extends TabPane:

  val comboBoxDemoTab = new Tab("ComboBox"):
    val comboBoxDemo = new demo.table.editing.combobox.ComboBoxDemo
    setContent(comboBoxDemo.pane)
    setClosable(false)

  getTabs.addAll(comboBoxDemoTab)