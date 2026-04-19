package demo.editor.dialog

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.ui.editor.*

import demo.*

object DialogDemo extends BorderPane with DemoUtils:

  //  Showing how to use EditorDialog with different constructors and options

  val defaultButton = new Button("Default")
  val withTitleButton = new Button("With title")
  val withTitleAndSubmitButton = new Button("With title and submit button")
  val withTitleAndSubmitAndLabelButton = new Button("With title, submit button and label")
  val withSubmitButton = new Button("With submit button")

  defaultButton.setOnAction: _ =>
    new EditorDialog[String].showAndWait().ifPresent(println)

  withTitleButton.setOnAction: _ =>
    new EditorDialog[String]("Information").showAndWait().ifPresent(println)

  withTitleAndSubmitButton.setOnAction: _ =>
    new EditorDialog[String]("Information", "Submit").showAndWait().ifPresent(println)

  withTitleAndSubmitAndLabelButton.setOnAction: _ =>
    new EditorDialog[String]("Information", "Submit", "Please enter your information").showAndWait().ifPresent(println)

  withSubmitButton.setOnAction: _ =>
    new EditorDialog[String](ok = Some("Submit")).showAndWait().ifPresent(println)

  //  It's also possible to use EditorFactory#toDialog (with all variations, only one shown here)

  val withEditorFactoryButton = new Button("With editor factory")

  withEditorFactoryButton.setOnAction: _ =>
    EditorFactory[String].toDialog("Information", "Submit", "Please enter your information").showAndWait().ifPresent(println)

  //  With initial value

  val withInitialValueButton = new Button("With initial value")
  val withInitialValueOptionSomeButton = new Button("With initial value (some)")
  val withInitialValueOptionNoneButton = new Button("With initial value (none)")

  withInitialValueButton.setOnAction: _ =>
    new EditorDialog[String]().withInitialValue("Hello World").showAndWait().ifPresent(println)

  withInitialValueOptionSomeButton.setOnAction: _ =>
    new EditorDialog[String]().withInitialValueOption(Some("Hello World")).showAndWait().ifPresent(println)

  withInitialValueOptionNoneButton.setOnAction: _ =>
    new EditorDialog[String]().withInitialValueOption(None).showAndWait().ifPresent(println)

  val toolbar = new ToolBar(
    defaultButton, 
    withTitleButton, 
    withTitleAndSubmitButton, 
    withTitleAndSubmitAndLabelButton, 
    withSubmitButton, 
    withEditorFactoryButton,
    withInitialValueButton,
    withInitialValueOptionSomeButton,
    withInitialValueOptionNoneButton)

  setTop(toolbar)