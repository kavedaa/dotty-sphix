package demo.table.editing.general

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
import org.sphix.collection.*
import org.sphix.control.cell.*

import demo.table.TableDemoBase

enum Hobby:
  case Golf, Chess, Fishing, Hunting, Skiing, Tennis, Hiking

object Hobby:
  given (Hobby => String) = _.toString

enum City:
  case Bergen, Oslo, London, Paris

object City:
  given (City => String) = _.toString

case class Person(
  firstName: String,
  lastName: Option[String],
  fortune: BigDecimal,
  debt: Option[BigDecimal],
  isEmployee: Boolean,
  isPetOwner: Option[Boolean],
  city: City,
  favoriteHobby: Option[Hobby],
  dateOfBirth: LocalDate,
  dateOfDeath: Option[LocalDate])

object Person:

  given Generator[Person] =
    (Generator("Alex", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy", "Karl", "Leo", "Mallory", "Nina", "Oscar", "Peggy", "Quentin", "Rupert", "Sybil", "Trent", "Uma", "Victor", "Walter", "Xavier", "Yvonne", "Zara"),
    Generator("Andersson", "Berg", "Carlsson", "Dahl", "Eriksson", "Fredriksson", "Gustafsson", "Hansson", "Ivarsson", "Johansson", "Karlsson", "Larsson", "Mårtensson", "Nilsson", "Olsson", "Persson", "Quist", "Rasmussen", "Svensson", "Thorsson", "Ulfsson", "Vikström", "Wikström", "Xenonsson", "Yngvesson", "Zetterberg").andThen[Option],
    Generator.between(BigDecimal(0), BigDecimal(1000000)),
    Generator.between(BigDecimal(0), BigDecimal(1000000)).andThen[Option],
    Generator[Boolean],
    Generator[Option[Boolean]],
    Generator.from(City.values),
    Generator.from(Hobby.values).andThen[Option],
    Generator(LocalDate.of(1900, 1, 1), LocalDate.of(2000, 12, 31)),
    Generator(LocalDate.of(1950, 1, 1), LocalDate.of(2025, 12, 31)).andThen[Option]).mapN(Person.apply)

end Person

case class PersonModel(person0: Person):

  val firstName = Var(person0.firstName)
  val lastName = Var(person0.lastName)
  val fortune = Var(person0.fortune)
  val debt = Var(person0.debt)
  val isEmployee = Var(person0.isEmployee)  
  val isPetOwner = TriStateModel.fromOption(person0.isPetOwner)  
  val city = Var(person0.city)
  val favoriteHobby = Var(person0.favoriteHobby)
  val dateOfBirth = Var(person0.dateOfBirth)
  val dateOfDeath = Var(person0.dateOfDeath)

  val toPerson = (firstName, lastName, fortune, debt, isEmployee, isPetOwner.asOption, city, favoriteHobby, dateOfBirth, dateOfDeath).mapN(Person.apply)

  toPerson.onValue(println)

end PersonModel

class PersonTable extends TableView[PersonModel] with TableUtils[PersonModel]:

  val firstName = new Column(150)("First name", _.firstName):
    setCell:
      new TextFieldCell {}

  val lastName = new Column(150)("Last name", _.lastName):
    setCell:
      new TextFieldCell {}

  val fortune = new Column("Fortune", _.fortune):
    setCell:
      new TextFieldCell {}

  val debt = new Column("Debt", _.debt):
    setCell:
      new TextFieldCell {}

  val isEmployee = new Column("Is employee", _.isEmployee):
    setCell:
      new CheckBoxCellA {}

  // val isPetOwner = new Column("Is pet owner", _.isPetOwner):
  //   setCell:
  //     CheckBoxTableCell(_.isPetOwner.asVal)

  val city = new Column("City", _.city):
    setCell:
      new ComboBoxCellA(_ => City.values.to(ObservableSeq)) {}

  val favoriteHobby = new Column("Favorite hobby", _.favoriteHobby):
    setCell:
      new ComboBoxCellA(_ => Hobby.values.map(Some.apply).to(ObservableSeq)) {}

  // val dateOfBirth = new Column("Date of birth", _.dateOfBirth):
  //   setCell:
  //     new DatePickerCell {}

  getColumns.addAll(firstName, lastName, fortune, debt, isEmployee, city, favoriteHobby)

  setEditable(true)

end PersonTable

class GeneralDemo(using Window) extends TableDemoBase(new PersonTable):
  val persons = Generator[Person].generate(10).to(ObservableSeq).map(PersonModel.apply)
  table.setItems(persons.toObservableList)

