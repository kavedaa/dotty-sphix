package demo.table.multi

import scala.language.implicitConversions
import scala.util.*

import javafx.stage.Window
import javafx.geometry.*
import javafx.scene.layout.BorderPane
import javafx.scene.control.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.util.*
import org.sphix.collection.ObservableSeq

import no.vedaadata.generator.*

import demo.table.*

case class Person(
  name: String,
  age: Int,
  hobbies: List[String])

object Person:

  val generator = 
    (Generator("Ann", "Bob", "Christina", "Eric", "Frida", "George"),
    Generator.between(18, 65),
    Generator.pick(3)(List("Skiing", "Fishing", "Hiking", "Reading", "Swimming", "Cycling", "Golf"))).mapN(Person.apply)

class PersonTable extends TableView[Person] with TableUtils[Person]:

  val name = new Column("Name", _.name.toVal):
    setDefaultCell()

  val age = new Column("Age", _.age.toVal):
    setDefaultCell()

  val hobbiesV = new Column("Hobbies", _.hobbies.toVal):
    setCell:
      new VBoxCell(hobby => new Hyperlink(hobby)) {}

  val hobbiesH = new Column("Hobbies", _.hobbies.toVal):
    setCell:
      new HBoxCell(5, Some("-"))(hobby => new Hyperlink(hobby)) {}

  getColumns.addAll(name, age, hobbiesV, hobbiesH)


class MultiDemo(using Window) extends TableDemoBase(new PersonTable):
  val persons = Person.generator.generate(1000).to(ObservableSeq)
  table.setItems(persons)

