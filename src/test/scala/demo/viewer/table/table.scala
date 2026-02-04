package demo.viewer.table

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
    Person("Diana", LocalDate.of(1992, 3, 10), Some("Berlin"), None, None)
  )

object TableDemo extends BorderPane with RegionUtils:

  val tableProductViewer = Viewer.tableProduct[Person]
  val productTables = Person.items.map(tableProductViewer)

  val tableViewer = Viewer.table[Person]
  val table = tableViewer(Person.items)

  setCenter:
    vbox(
      hbox(productTables*),
      hbox(table)
    )