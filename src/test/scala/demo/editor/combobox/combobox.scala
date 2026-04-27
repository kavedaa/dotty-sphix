package demo.editor.combobox

import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.geometry.Orientation

import org.sphix.ui.editor.EditorFactory
import org.sphix.util.ComboBoxFactory
import org.sphix.ui.RegionUtils

import demo.*

object ComboBoxDemo extends BorderPane with RegionUtils with Resources:

  val persons = List("Alice", "Bob", "Charlie", "David", "Eva", "Francis", "Grace", "Hank", "Ivy", "Jack", "Kathy", "Lance", "Mandy", "Nancy", "Oscar", "Patty", "Quincy", "Randy", "Sandy", "Tom", "Ursula", "Vince", "Wendy", "Xander", "Yvonne", "Zack")

  val personOptions = None +: persons.map(Option.apply)

  val plainButton = new Button("Plain - String")
  val searchableButton = new Button("Searchable - String")
  val editableButton = new Button("Editable - String")
  val clearableButton = new Button("Clearable - String")

  val plainOptionalButton = new Button("Plain - optional String")
  val searchableOptionalButton = new Button("Searchable - optional String")
  val editableOptionalButton = new Button("Editable - optional String")
  val clearableOptionalButton = new Button("Clearable - optional String")

  val plainOptionButton = new Button("Plain - Option[String]")
  val searchableOptionButton = new Button("Searchable - Option[String]")
  val editableOptionButton = new Button("Editable - Option[String]")
  val clearableOptionButton = new Button("Clearable - Option[String]")

  val plainOptionWithInitialValueButton = new Button("Plain - Option[String] with initial value")
  val searchableOptionWithInitialValueButton = new Button("Searchable - Option[String] with initial value")
  val editableOptionWithInitialValueButton = new Button("Editable - Option[String] with initial value")
  val clearableOptionWithInitialValueButton = new Button("Clearable - Option[String] with initial value")


  plainButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(persons).toDialog.showAndWait().ifPresent(println)

  searchableButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(persons)(using new ComboBoxFactory.Searchable).toDialog.showAndWait().ifPresent(println)

  editableButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(persons)(using new ComboBoxFactory.Editable).toDialog.showAndWait().ifPresent(println)

  clearableButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(persons).clearable.toDialog.showAndWait().ifPresent(println)


  plainOptionalButton.setOnAction: _ =>    
    EditorFactory.ComboBoxOption(persons).toDialog.showAndWait().ifPresent(println)

  searchableOptionalButton.setOnAction: _ =>    
    EditorFactory.ComboBoxOption(persons)(using new ComboBoxFactory.Searchable).toDialog.showAndWait().ifPresent(println)

  editableOptionalButton.setOnAction: _ =>    
    EditorFactory.ComboBoxOption(persons)(using new ComboBoxFactory.Editable).toDialog.showAndWait().ifPresent(println)

  clearableOptionalButton.setOnAction: _ =>    
    EditorFactory.ComboBoxOption(persons).clearable.toDialog.showAndWait().ifPresent(println)


  plainOptionButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(personOptions).toDialog.showAndWait().ifPresent(println)

  searchableOptionButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(personOptions)(using new ComboBoxFactory.Searchable).toDialog.showAndWait().ifPresent(println)

  editableOptionButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(personOptions)(using new ComboBoxFactory.Editable).toDialog.showAndWait().ifPresent(println)

  clearableOptionButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(personOptions).clearable.toDialog.showAndWait().ifPresent(println)


  plainOptionWithInitialValueButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(personOptions).toDialog.withInitialValue(None).showAndWait().ifPresent(println)

  searchableOptionWithInitialValueButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(personOptions)(using new ComboBoxFactory.Searchable).toDialog.withInitialValue(None).showAndWait().ifPresent(println)

  editableOptionWithInitialValueButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(personOptions)(using new ComboBoxFactory.Editable).toDialog.withInitialValue(None).showAndWait().ifPresent(println)

  clearableOptionWithInitialValueButton.setOnAction: _ =>    
    new EditorFactory.ComboBox(personOptions).clearable.toDialog.withInitialValue(None).showAndWait().ifPresent(println)

  setCenter:
    grid:
      List(
        List(plainButton, searchableButton, editableButton, clearableButton),
        List(plainOptionalButton, searchableOptionalButton, editableOptionalButton, clearableOptionalButton),
        List(plainOptionButton, searchableOptionButton, editableOptionButton, clearableOptionButton),
        List(plainOptionWithInitialValueButton, searchableOptionWithInitialValueButton, editableOptionWithInitialValueButton, clearableOptionWithInitialValueButton)
      )