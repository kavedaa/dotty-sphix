package demo.viewer.link

import java.time.LocalDate

import javafx.scene.layout.*

import no.vedaadata.text.Render

import org.sphix.ui.viewer.Viewer
import org.sphix.ui.RegionUtils

case class Person(
  name: String,
  birthDate: LocalDate,
  city: Option[String],
  email: Option[String],
  fortune: Option[BigDecimal])

object Person:
  
  val items = List(
    Person("Alice", LocalDate.of(1990, 5, 15), Some("Oslo"), Some("alice@example.com"), Some(BigDecimal("1000.50"))),
    Person("Bob", LocalDate.of(1995, 8, 22), None, Some("bob@example.com"), None),
    Person("Charlie", LocalDate.of(1985, 12, 3), Some("London"), None, Some(BigDecimal("2000.75"))),
    Person("Diana", LocalDate.of(1992, 3, 10), Some("Berlin"), None, None))

  given (Person => String) = _.name

object LinkDemo extends BorderPane with RegionUtils:

  val personViewer = Viewer.tableProduct[Person]

  val viewer = Viewer.Hyperlink[Person](person => personViewer.toDialog(person).showAndWait())

  val links = Person.items.map(viewer) 

  setCenter:
    vbox(
      hbox(links*)
    )