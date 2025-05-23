package demo.editor.composite

import javafx.scene.Node
import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.beans.value.ObservableValue

import org.sphix.*
import org.sphix.ui.FormUtils
import org.sphix.ui.editor.*
import org.sphix.ui.dialog.EditorDialog

case class Person(
  firstName: String,
  lastName: Option[String],
  age: Int,
  isSmart: Boolean)

object CompositeDemo extends BorderPane:

  val compositeButton = new Button("Composite")

  val blankButton = new Button("Blank")
  val initialButton = new Button("Initial")
  val optionsButton = new Button("Options")

  compositeButton.setOnAction: _ =>
    EditorFactory(new PersonEditor).toDialog.showAndWait().ifPresent(println)

  blankButton.setOnAction: _ =>
    new EditorDialog[Person]().showAndWait().ifPresent(println)

  initialButton.setOnAction: _ =>
    val person = Person("John", Some("Smith"), 34, true)
    new EditorDialog[Person].withInitialValue(person).showAndWait().ifPresent(println)

  // optionsButton.setOnAction: _ =>
  //   new EditorDialog[Person]().showAndWait().ifPresent(println)

  val toolbar = ToolBar(compositeButton, blankButton, initialButton)

  setTop(toolbar)


class PersonEditor extends CompositeEditor[Person] with FormUtils:

  val firstName = Editor[String]
  val lastName = Editor[Option[String]]
  val age = Editor[Int]
  val isSmart = Editor[Boolean]

  val editors = List(firstName, lastName, age, isSmart)

  def get = Person(
    firstName.get,
    lastName.get,
    age.get,
    isSmart.get)

  def set(x: Person) = 
    firstName.set(x.firstName)
    lastName.set(x.lastName)
    age.set(x.age)
    isSmart.set(x.isSmart)

  def container(label: Option[String]) = new Container:
    def layout(isTopLevel: Boolean, onLayoutChange: Option[() => Unit]) = 
      hbox(
        vbox(
          firstName.container(Some("First Name")).layout(false, onLayoutChange), 
          lastName.container(Some("Last Name")).layout(false, onLayoutChange)),
        vbox(
          age.container(Some("Age")).layout(false, onLayoutChange), 
          isSmart.container(Some("Is Smart")).layout(false, onLayoutChange)))
    

