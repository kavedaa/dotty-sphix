package demo.table.editing.textfield

import javafx.stage.Window
import javafx.scene.control.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.*

import no.vedaadata.generator.Generator

import demo.table.*
import org.sphix.util.Converter


case class Person(
  firstName: String,
  lastName: Option[String],
  age: Int,
  fortune: Option[Long])

object Person:

  val firstNames = List("Bob", "John", "Tom")
  val lastNames = List("Andersson", "Johnson", "Nilsson")

  given Generator[Person] = 
    (Generator.from(firstNames), 
    Generator.from(lastNames).andThen[Option], 
    Generator.between(20, 60), 
    Generator.between[Long](10000, 100000).andThen[Option])
    .mapN(Person.apply)

case class PersonModel(person0: Person):

  val firstName = Var(person0.firstName)
  val lastName = Var(person0.lastName)
  val age = Var(person0.age)
  val fortune = Var(person0.fortune)

  val toPerson = (firstName, lastName, age, fortune).mapN(Person.apply)

  toPerson.onValue(println)
  

class PersonTable extends TableView[PersonModel] with TableUtils[PersonModel]:

  val firstName = new Column("First name", _.firstName):
    setCell(new TextFieldCell {})

  val lastName = new Column("Last name", _.lastName):
    setCell(new TextFieldCell {})

  val age = new Column("Age", _.age):
    setCell(new TextFieldCell {}) 

  val fortune = new Column("Fortune", _.fortune):
    setCell(new TextFieldCell {})

  getColumns.addAll(firstName, lastName, age, fortune)

  setEditable(true)
  getSelectionModel.setCellSelectionEnabled(true)


class TextFieldDemo(using Window) extends TableDemoBase(new PersonTable):
  val persons = Generator[Person].generate(10).to(ObservableSeq).map(PersonModel.apply)
  table.setItems(persons.toObservableList)
