package demo.dark

import scala.language.implicitConversions
import scala.concurrent.ExecutionContext.Implicits.global

import javafx.application.Application
import javafx.stage.{ Stage, Window }
import javafx.scene.Scene

import javafx.scene.control.*
import javafx.scene.layout.*

import no.vedaadata.generator.Generator

import org.sphix.* 
import org.sphix.control.Spring
import org.sphix.collection.ObservableSeq
import org.sphix.control.derivedTable
import org.sphix.concurrent.FutureModal
import org.sphix.ui.editor.EditorFactory
import org.sphix.util.DarkMode

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


@main def main = Application.launch(classOf[Demo])


class Demo extends Application:

  def start(stage: Stage) =

    given Window = stage

    val tabPane = new TabPane

    val tableTab = new Tab("Table", new TablePane)
    val popupTab = new Tab("Popup", new PopupPane)
    val tabs = List(tableTab, popupTab)
    tabs.foreach(_.setClosable(false))
    tabPane.getTabs.addAll(tabs*)

    val mainPane = new MainPane

    mainPane.setCenter(tabPane)

    stage.setScene(Scene(mainPane))
    stage.show()



class MainPane extends BorderPane:

  val darkMode = new ToggleButton("Light/dark")

  darkMode.selectedProperty.onValue: isDark =>
    if isDark then
      DarkMode.setDarkMode()
    else
      DarkMode.unsetDarkMode()

  val toolbar = new ToolBar(new Spring, darkMode)

  setTop(toolbar)

  darkMode.setSelected(true)


class TablePane extends BorderPane:

  val persons = Person.generator.generate(100).to(ObservableSeq)

  val table = derivedTable[Person]
  table.setItems(persons)

  setCenter(table)


class PopupPane(using Window) extends BorderPane:

  val showSpinner = new Button("Spinner")
  val showTextArea = new Button("Text area")

  val toolbar = new ToolBar(showSpinner, showTextArea)

  showSpinner.setOnAction: _ =>
    FutureModal("Please wait...")(Thread.sleep(2000)).onComplete(_ => ())

  showTextArea.setOnAction: _ =>
    EditorFactory.TextArea[String].toDialog.withInitialValue("Hello world!").showAndWait()

  setTop(toolbar)