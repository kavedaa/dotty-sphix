package demo.table.controls

import java.net.URI
import java.awt.Desktop

import javafx.application.Application
import javafx.scene.layout._
import javafx.scene.control._

import no.vedaadata.generator.*

import org.sphix.*
import org.sphix.util.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq
import org.sphix.util.ComboBoxFactory


case class Person(
  name: String,
  favoriteWebSite: Option[URI],
  hobby: String,
  pet: Option[String])

val hobbies = List("Fishing", "Hiking", "Reading")

val pets = List("Dog", "Cat", "Fish").map(x => Option(x)) :+ None

object Person:

  given Generator[Person] =
    (Generator("Joe", "Bob", "Tom"),
      Generator("http://google.com", "http://facebook.com", "http://reddit.com").map(x => URI(x)).andThen[Option],
      Generator.from(hobbies),
      Generator.from(pets)
    ).mapN(Person.apply)

class PersonTable 
  extends TableView[Person] 
  with TableUtils[Person]:

  val getIcon = ResourceImageResolver(getClass, "/icons/" + _)

  val arrow = getIcon("arrow_right.png")

  val name = new Column("Name", _.name.toVal):
    setDefaultCell()

  val favoriteWebLink1 = new Column("Text", _.favoriteWebSite.toVal):
    setCell:
      new StringCell(using _.map(_.toString))
      with HyperlinkCell(_.foreach(Desktop.getDesktop.browse))

  val favoriteWebLink2 = new Column("Graphic", _.favoriteWebSite.toVal):
    setCell:
      new ImageCell(using _.map(_ => arrow))
      with HyperlinkCell(_.foreach(Desktop.getDesktop.browse))

  val favoriteWebLink3 = new Column("Text and graphic", _.favoriteWebSite.toVal):
    setCell:
      new StringCell(using _.map(_.toString))
      with ImageCell(using _.map(_ => arrow))
      with HyperlinkCell(_.foreach(Desktop.getDesktop.browse))

  val favoriteWebLink4 = new Column("Conditional", _.favoriteWebSite.toVal):
    setCell:
      new StringCell(using _.map(_.toString).getOrElse("Not applicable"))
      with ImageCell(using _.map(_ => arrow))
      with HyperlinkCell(_.foreach(Desktop.getDesktop.browse), _.isDefined)

  val favoriteWebLink = HeaderColumn("Favorite web site", favoriteWebLink1, favoriteWebLink2, favoriteWebLink3, favoriteWebLink4)

  val hobby = new Column("Hobby", _.hobby.toVal):
    setCell:
      new StaticComboBoxCell(using new ComboBoxFactory.Editable)(_ => hobbies.to(ObservableSeq)) {}

  val pet = new Column("Pet", _.pet.toVal):
    setCell:
      new StaticComboBoxCell(using new ComboBoxFactory.Editable)(_ => pets.to(ObservableSeq)) {}

  getColumns.addAll(name, favoriteWebLink)


object ControlsDemo extends BorderPane:

  val persons = Generator[Person].generate(50).to(ObservableSeq)

  val table = new PersonTable:
    setItems(persons)

  setCenter(table)


