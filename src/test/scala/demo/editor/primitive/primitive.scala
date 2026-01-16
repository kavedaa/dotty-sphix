package demo.editor.primitive

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.ui.dialog.*
import org.sphix.ui.editor.*

import demo.*

object PrimitiveDemo extends BorderPane with DemoUtils:

  val stringButton = new Button("String")

  val clearableButton = new Button("Clearable String")

  stringButton.setOnAction: _ =>
    new EditorDialog[String]("Please input a string").showAndWait().ifPresent(println)

  clearableButton.setOnAction: _ =>
    EditorFactory[String].clearable.toDialog("Please input a string").showAndWait().ifPresent(println)

  val toolbar = new ToolBar(stringButton, clearableButton)

  setTop(toolbar)