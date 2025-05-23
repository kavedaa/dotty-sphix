package demo.table

import javafx.scene.layout.*
import javafx.scene.control.*

import no.vedaadata.generator.Generator

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.*

case class HeaderDemoData(name: String, isTall: Boolean, isSmart: Boolean, isAlive: Boolean)

object HeaderDemo extends BorderPane:

  given Generator[String] = Generator("Joe", "Bob", "Tom")

  val data = Generator[HeaderDemoData].generate(10)

  val table = new TableView[HeaderDemoData] with TableUtils[HeaderDemoData]:
    
    val name = new Column("Name", _.name.toVal):
      setDefaultCell()

    val isTall = new Column("Is tall", _.isTall.toVal):
      setDefaultCell()

    val isSmart = new Column("Yeah this guy is really smart", _.isSmart.toVal):
      setDefaultCell()

    val isAlive = new Column("Is alive", _.isAlive.toVal):
      setDefaultCell()

    getColumns.addAll(name, isTall, isSmart, isAlive)

    setItems(data.toObservableList)

  setCenter(table)
