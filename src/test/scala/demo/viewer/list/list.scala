package demo.viewer.list

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

object ListDemo extends BorderPane with RegionUtils:

  val textListViewer = Viewer.TextList[Person]

  val textList = textListViewer(Person.items)

  val personViewer = Viewer.gridProduct[Person]
  val linkViewer = Viewer.Hyperlink[Person](person => personViewer.toDialog("Person", person).showAndWait())
  val linkListViewer = Viewer.List(linkViewer)
  val linkList = linkListViewer(Person.items)

  val gridListViewer = Viewer.List(Viewer.gridProduct[Person].map(x => titled("Person", x)))
  val gridList = gridListViewer(Person.items)

  setCenter:
    vbox(
      hbox(textList, linkList, gridList)
    )