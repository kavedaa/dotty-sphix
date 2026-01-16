package demo.editor.clearable

import java.time.*

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.ui.dialog.*
import org.sphix.ui.editor.*

import demo.*

case class Data(
  name: String,
  city: Option[String],
  from: LocalDate,
  to: Option[LocalDate],
  distance: Double)

object ClearableDemo extends BorderPane with DemoUtils:

  given stringFactory: EditorFactory[Option[String]] = (new EditorFactory.TextField[Option[String]]).clearable
  given localDateFactory: EditorFactory[Option[LocalDate]] = (new EditorFactory.DatePickerOption).clearable

  val primitivesButton = new Button("Primitives")

  primitivesButton.setOnAction: _ =>
    new EditorDialog[Data].showAndWait().ifPresent(println)

  val toolbar = new ToolBar(primitivesButton)

  setTop(toolbar)