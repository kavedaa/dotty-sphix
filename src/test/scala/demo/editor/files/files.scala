package demo.editor.files

import java.io.File

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.ui.dialog.*
import org.sphix.ui.editor.*

import demo.*

case class Submission(
  name: String,
  age: Int,
  files: List[File])

object FilesDemo extends BorderPane with DemoUtils:

  val icons = Map(
    "*"   -> getIcon("page_white.png"),
    "jpg" -> getIcon("image.png"),
    "png" -> getIcon("image.png"),
    "pdf" -> getIcon("page_white_acrobat.png"))

  val allFilesButton = new Button("All files")
  val onlyTxtButton = new Button("Only text files")
  val compositeButton = new Button("Composite")

  allFilesButton.setOnAction: _ =>
    new EditorFactory.Files("All files", Nil, icons).toDialog("Please select some files").showAndWait().ifPresent(println)

  onlyTxtButton.setOnAction: _ =>
    new EditorFactory.Files("Text files", List("txt"), icons).toDialog("Please select some files").showAndWait().ifPresent(println)

  compositeButton.setOnAction: _ =>
    given EditorFactory[List[File]] = EditorFactory.Files("All files", Nil, icons)
    EditorDialog[Submission]("Enter your details").showAndWait().ifPresent(println)

  val toolbar = new ToolBar(allFilesButton, onlyTxtButton, compositeButton)

  setTop(toolbar)