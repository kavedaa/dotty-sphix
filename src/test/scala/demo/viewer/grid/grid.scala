package demo.viewer.grid

import javafx.scene.layout.*

import no.vedaadata.text.Render

import org.sphix.ui.viewer.Viewer
import org.sphix.ui.RegionUtils

case class Person(
  name: String,
  age: Int,
  city: Option[String],
  email: Option[String],
  fortune: Option[BigDecimal])

object Person:
  
  val items = List(
    Person("Alice", 30, Some("Oslo"), Some("alice@example.com"), Some(BigDecimal("1000.50"))),
    Person("Bob", 25, None, Some("bob@example.com"), None),
    Person("Charlie", 35, Some("London"), None, Some(BigDecimal("2000.75"))),
    Person("Diana", 28, Some("Berlin"), None, None)
  )

object GridDemo extends BorderPane with RegionUtils:

  val viewer = Viewer.gridProduct[Person].map(border)

  val panes = Person.items.map(viewer)

  val alignedViewer = Viewer.alignedGridProduct[Person].map(border)

  val alignedPanes = Person.items.map(alignedViewer)

  val copyableTextViewer = 
    given [A : Render]: Viewer[A] = Viewer.readOnlyTextFieldViewer
    Viewer.gridProduct[Person].map(border)
      
  val copyableTextPanes = Person.items.map(copyableTextViewer)

  setCenter:
    vbox(
      titled("Plain", hbox(panes*)),
      titled("Aligned", hbox(alignedPanes*)),
      titled("Copyable text", hbox(copyableTextPanes*))
    )