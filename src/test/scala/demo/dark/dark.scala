package demo.dark

import scala.concurrent.ExecutionContext.Implicits.global

import com.sun.javafx.css.StyleManager

import javafx.application.Application
import javafx.stage.{ Stage, Window }
import javafx.scene.Scene

import javafx.scene.control.*
import javafx.scene.layout.*

import no.vedaadata.generator.Generator

import org.sphix.* 
import org.sphix.control.Spring
import org.sphix.collection.ObservableSeq
import org.sphix.control.given
import org.sphix.concurrent.FutureModal
import org.sphix.ui.editor.EditorFactory

case class Person(
  name: String,
  age: Int,
  occupation: Option[String])

object Person:
  val generator = 
    (Generator("Alex", "Bob", "Charlie", "David", "Edward", "Frank", "George", "Harry", "Ivan", "John"),
      Generator.between(20, 60),
      Generator("Engineer", "Doctor", "Lawyer", "Teacher", "Pilot", "Farmer", "Artist", "Musician", "Athlete", "Scientist").andThen[Option])
    .mapN(Person.apply)

class Demo extends Application:

  def start(stage: Stage) =

    given Window = stage

    val persons = Person.generator.generate(100).to(ObservableSeq)

    val showSpinner = new Button("Spinner")
    val showTextArea = new Button("Text area")

    val darkMode = new ToggleButton("Light/dark")

    darkMode.selectedProperty.onValue: isDark =>
      if isDark then
        StyleManager.getInstance.addUserAgentStylesheet("css/dark.css")
      else
        StyleManager.getInstance.removeUserAgentStylesheet("css/dark.css")

    val toolbar = new ToolBar(showSpinner, showTextArea, new Spring, darkMode)

    val table = summon[TableView[Person]]
    table.setItems(persons)

    val pane = new BorderPane    

    pane.setTop(toolbar)
    pane.setCenter(table)

    stage.setScene(Scene(pane))
    stage.show()

    showSpinner.setOnAction: _ =>
      FutureModal("Please wait...")(Thread.sleep(2000)).onComplete(_ => ())

    showTextArea.setOnAction: _ =>
      EditorFactory.TextArea[String].toDialog.withInitialValue("Hello world!").showAndWait()

    darkMode.setSelected(true)


@main def main = Application.launch(classOf[Demo])