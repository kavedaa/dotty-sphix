package demo.editor.option

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.ui.dialog.EditorDialog
import org.sphix.ui.editor.EditorFactory

enum Hobby:
  case Chess, Golf, Tennis

object Hobby:
  given (Hobby => String) = _.toString

case class Person(
  firstName: String,
  lastName: Option[String],
  age: Option[Int],
  isSmart: Option[Boolean],
  hobby: Option[Hobby])

object OptionDemo extends BorderPane:

  val primitiveButton = new Button("Primitive")
  val comboButton = new Button("Combo")

  val compositeButton = new Button("Composite")

  given EditorFactory[Hobby] = EditorFactory.ComboBox[Hobby](Hobby.values)

  primitiveButton.setOnAction: _ =>
    new EditorDialog[Option[String]]().showAndWait().ifPresent(println)

  comboButton.setOnAction: _ =>
    new EditorDialog[Option[Hobby]]().showAndWait().ifPresent(println)

  compositeButton.setOnAction: _ =>
    new EditorDialog[Person]().showAndWait().ifPresent(println)

  // initialButton.setOnAction: _ =>
  //   val person = Person("John", "Smith", 34, true)
  //   new EditorDialog[Person].withInitialValue(person).showAndWait().ifPresent(println)

  // optionsButton.setOnAction: _ =>
  //   new EditorDialog[Person]().showAndWait().ifPresent(println)

  val toolbar = ToolBar(primitiveButton, comboButton, compositeButton)

  setTop(toolbar)