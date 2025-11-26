package demo.editor.files

import java.io.File

import javafx.scene.Node

import org.sphix.*
import org.sphix.ui.editor.*
import org.sphix.ui.FormUtils


case class Submission(
  description: String,
  files: List[File])


class SubmissionEditorFactory extends EditorFactory[Submission]:
  def createEditor = new SubmissionEditor


class SubmissionEditor extends ProductEditor[Submission]:

  type C = SubmissionContainer

  val description = new TextAreaEditor[String]
  val files = new FilesEditor("All files", List("*"), Map())

  val elemEditors = List(
    description,
    files)
  .asInstanceOf[List[Editor[Any]]]

  def container(label: Option[String]) = SubmissionContainer(this)


class SubmissionContainer(editor: SubmissionEditor) extends Container with FormUtils:

  editor.files.pane.prefHeightProperty <== editor.description.textArea.heightProperty

  def layout(isTopLevel: Boolean, onLayoutChange: Option[() => Unit]) =
    hbox(
      editor.description.container(Some("Description")).layout(isTopLevel = false),
      editor.files.container(Some("Files")).layout(isTopLevel = false))

      


