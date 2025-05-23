package demo.table.derived

import javafx.application.Application
import javafx.scene.control.*

import no.vedaadata.generator.Generator
import no.vedaadata.text.Format

import org.sphix.*
import org.sphix.control.*
import org.sphix.control.given
import org.sphix.collection.*

@main def main() =
  Application.launch(classOf[DerivedTableDemo])

class DerivedTableDemo extends SimpleApp:

  val persons = Generator.generate[Person](100).to(ObservableSeq)

  given org.sphix.control.cell.To[Pet, String] = 
    pet => pet.name

  given Format.BigDecimalFormat = java.text.DecimalFormat("0.00")

  val table = summon[TableView[Person]]
  table.setItems(persons)

  def root = table

