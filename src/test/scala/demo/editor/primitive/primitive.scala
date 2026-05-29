package demo.editor.primitive

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.ui.dialog.*
import org.sphix.ui.editor.*
import org.sphix.util.TextFieldFactory

import demo.*

object PrimitiveDemo extends BorderPane with Resources:

  val stringButton = new Button("String")
  val passwordButton = new Button("Password")
  val clearableButton = new Button("Clearable String")
  val clearableFieldButton = new Button("Clearable String Field")

  stringButton.setOnAction: _ =>
    new EditorDialog[String]("Please input a string").showAndWait().ifPresent(println)

  passwordButton.setOnAction: _ =>
    EditorFactory.TextField[String](using TextFieldFactory.Password).toDialog("Please input a password").showAndWait().ifPresent(println)

  clearableButton.setOnAction: _ =>
    EditorFactory[String].clearable.toDialog("Please input a string").showAndWait().ifPresent(println)

  clearableFieldButton.setOnAction: _ =>
    EditorFactory.TextField[String](using TextFieldFactory.Clearable).toDialog("Please input a string").showAndWait().ifPresent(println)

  val toolbar = new ToolBar(stringButton, passwordButton, clearableButton, clearableFieldButton)

  setTop(toolbar)