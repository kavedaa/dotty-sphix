package demo.dialog

import scala.language.implicitConversions

import javafx.application.Application
import javafx.scene.control.*

import org.sphix.*
import org.sphix.util.*
import org.sphix.util.given
import org.sphix.ui.dialog.*
import org.sphix.control.derivedTable

case class Person(name: String, age: Int, hobbyAsWeCanSeeIsAReallyLongLabelAndSomeMoreYesThisIsGood: Option[String])

@main def main = Application.launch(classOf[Demo])

class Demo extends SimpleApp:

  val getIcon = ResourceImageResolver(getClass, "/icons/" + _)

  val Information = getIcon("information.png")

  val persons = List(
    Person("Joe", 23, Some("Golf")),
    Person("Bob", 34, None),
    Person("Tom", 45, None))

  val tableDialogButton = new Button("Table dialog")
  val tableMonologButton = new Button("Table monolog")

  given TableView[Person] = derivedTable[Person]

  tableDialogButton.setOnAction: _ =>
    val dialog = TableDialog(persons)("Persons", "These are the persons", Information, "Go for it!")
    dialog.showAndWait().ifPresent: _ =>
      println("The user agreed")

  tableMonologButton.setOnAction: _ =>
    TableMonolog(persons)("Persons", "These are the persons", Information).showAndWait()

  def root = ToolBar(tableDialogButton, tableMonologButton)

