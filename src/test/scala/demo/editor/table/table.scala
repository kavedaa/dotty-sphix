package demo.editor.table

import javafx.scene.control.*
import javafx.scene.layout.*

import no.vedaadata.generator.*

object TableDemo extends BorderPane:

  val simpleButton = new Button("Simple")
  val editableButton = new Button("Editable")

  simpleButton.setOnAction: _ =>
    val dialog = new simple.PersonsEditorFactory().toDialog
    val persons = Generator[simple.Person].generate(10)
    dialog.withInitialValue(persons).showAndWait().ifPresent(println)

  editableButton.setOnAction: _ =>
    val dialog = new editable.PersonsEditorFactory(List("Reading", "Fishing", "Chess", "Golf")).toDialog
    val persons = List("Ann", "Bob", "Charlie").map(editable.PersonModel.apply)
    dialog.withInitialValue(persons).showAndWait().ifPresent(x => println(x.map(_.render)))

  val toolbar = new ToolBar(simpleButton, editableButton)

  setTop(toolbar)    