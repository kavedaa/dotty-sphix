package demo.table.editing.combobox

import scala.util.Random

import java.time.*

import javafx.stage.Window
import javafx.scene.layout._
import javafx.scene.control._

import no.vedaadata.generator.*

import org.sphix.*
import org.sphix.util.*
import org.sphix.control.*
import org.sphix.collection.*

import demo.table.*
import org.sphix.util.ComboBoxFactory

case class Country(
  code: String,
  name: String)

object Country:
  given (Country => String) = _.name

case class Person(
  name: String,
  hobby: String,
  pet: Option[String],
  country: Country,
  city: Option[String])

val hobbies = List("Fishing", "Hiking", "Reading")

val pets = None +: List("Dog", "Cat", "Fish", "Bird", "Hamster", "Turtle").map(Option.apply) 

val countries = List(Country("NO", "Norway"), Country("SE", "Sweden"), Country("DK", "Denmark"))

val countryCities = Map(
  "NO" -> List("Oslo", "Bergen", "Trondheim"),
  "SE" -> List("Stockholm", "Gothenburg", "Malmö"),
  "DK" -> List("Copenhagen", "Aarhus", "Odense")
)

object Person:

  given Generator[Person] =
    (Generator("Joe", "Bob", "Tom"),
      Generator.from(hobbies),
      Generator.from(pets),
      Generator.from(countries),
      Generator(None)
    ).mapN(Person.apply)


case class PersonModel(person0: Person):

  val name = Var(person0.name)
  val hobby = Var(person0.hobby)
  val pet = Var(person0.pet)
  val country = Var(person0.country)
  val city = Var(person0.city)

  val availableCitiesVal = country.map(c => None +: countryCities.getOrElse(c.code, Nil).map(Option.apply))

  val availableCities = ObservableSeq.fromVal(availableCitiesVal)

  val toPerson = (name, hobby, pet, country, city).mapN(Person.apply)

  toPerson.onValue(x => println(s"----- The person is: $x"))

class PersonTable extends TableView[PersonModel] with TableUtils[PersonModel]:

  val name = new Column(150)("Name", _.name):
    setDefaultCell()

  val hobby1 = new Column("Hobby", _.hobby):
    setCell:
      new StaticComboBoxCell(_ => hobbies.to(ObservableSeq)) {}

  val hobby2 = new Column("Hobby", _.hobby):
    setCell:
      new StaticComboBoxCell(using new ComboBoxFactory.Editable)(_ => hobbies.to(ObservableSeq)) {}

  val pet = new Column("Pet", _.pet):
    setCell:
      new StaticComboBoxCell(using new ComboBoxFactory.Searchable)(_ => pets.to(ObservableSeq)) {}

  val country = new Column("Country", _.country):
    setCell:
      new StaticComboBoxCell(_ => countries.to(ObservableSeq)) {}

  val city = new Column("City", _.city):
    setCell:
      new StaticComboBoxCell(using new ComboBoxFactory.Editable)(_.availableCities) {}

  setEditable(true)

  getColumns.addAll(name, hobby1, hobby2, pet, country, city)   


class ComboBoxDemo(using Window) extends TableDemoBase(new PersonTable):
  val persons = Generator[Person].generate(100).to(ObservableSeq).map(PersonModel.apply)
  table.setItems(persons.toObservableList)

