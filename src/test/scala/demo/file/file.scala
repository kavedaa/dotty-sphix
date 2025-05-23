package demo.file

import javafx.application.Application
import javafx.scene.control.*
import javafx.scene.layout.*

import org.sphix.*
import org.sphix.ui.FileDropping
import org.sphix.ui.dialog.InfoDialog

@main def main = Application.launch(classOf[Demo])

class Demo extends SimpleApp:

  val pane = new BorderPane

  pane.setCenter(Label("Drop files here"))

  FileDropping.onDrop(pane): files =>
    InfoDialog(s"You dropped ${files.map(_.getName).mkString(", ")}").showAndWait()

  def root = pane

