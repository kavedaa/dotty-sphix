package demo.table.derived

import scala.language.implicitConversions

import javafx.application.Application
import javafx.scene.control.*

import no.vedaadata.generator.Generator
import no.vedaadata.text.Format

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.*

@main def main() =
  Application.launch(classOf[DerivedTableDemo])

class DerivedTableDemo extends SimpleApp:

  val persons = Person.generator.generate(100).to(ObservableSeq)

  given org.sphix.control.cell.To[Pet, String] = 
    pet => pet.name

  given Format.BigDecimalFormat = java.text.DecimalFormat("0.00")

  val table = derivedTable[Person]
  table.setItems(persons)

  def root = table

