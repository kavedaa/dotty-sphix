package demo.editor.files

import java.io.File

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.ui.dialog.*
import org.sphix.ui.editor.*

import demo.*

object FilesDemo extends BorderPane with Resources:

  val icons = Map(
    "*"   -> getIcon("page_white.png"),
    "jpg" -> getIcon("image.png"),
    "png" -> getIcon("image.png"),
    "pdf" -> getIcon("page_white_acrobat.png"))

  val allFilesButton = new Button("All files")
  val onlyTxtButton = new Button("Only text files")
  val compositeButton = new Button("Composite")
  val compositeMultipleButton = new Button("Composite multiple")

  allFilesButton.setOnAction: _ =>
    new EditorFactory.Files("All files", Nil, icons).toDialog("Please select some files").showAndWait().ifPresent(println)

  onlyTxtButton.setOnAction: _ =>
    new EditorFactory.Files("Text files", List("txt"), icons).toDialog("Please select some files").showAndWait().ifPresent(println)

  compositeButton.setOnAction: _ =>
    (new SubmissionEditorFactory).toDialog("Enter your details").showAndWait().ifPresent(println)

  compositeMultipleButton.setOnAction: _ =>
    given EditorFactory[Submission] = new SubmissionEditorFactory
    val factory = new DynamicEditorFactory[Submission](1)
    factory.toDialog("Submissions").showAndWait().ifPresent(println)
    

  val toolbar = new ToolBar(allFilesButton, onlyTxtButton, compositeButton, compositeMultipleButton)

  setTop(toolbar)