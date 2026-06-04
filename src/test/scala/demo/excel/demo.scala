package demo.excel

import scala.concurrent.ExecutionContext.Implicits.global

import javafx.application.Application
import javafx.stage.*
import javafx.scene.layout.*
import javafx.scene.control.*

import no.vedaadata.generator.Generator

import org.sphix.*
import org.sphix.control.*
import org.sphix.concurrent.FutureModal
import org.sphix.ui.*
import org.sphix.excel.*
import org.sphix.collection.*

case class Person(
  firstName: String,
  lastName: Option[String],
  age: Int,
  fortune: Option[BigDecimal],
  isEmployee: Boolean,
  birthDate: java.time.LocalDate,
  lastLogin: Option[java.time.LocalDateTime])

object Person:

  given firstName: Generator[String] = Generator("Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Heidi", "Ivan", "Judy", "Karl", "Leo", "Mallory", "Nina", "Oscar", "Peggy", "Quentin", "Rupert", "Sybil", "Trent", "Uma", "Victor", "Walter", "Xavier", "Yvonne", "Zara")
  given lastName: Generator[Option[String]] = Generator("Andersson", "Berg", "Carlsson", "Dahl", "Eriksson", "Fredriksson", "Gustafsson", "Hansson", "Isaksson", "Johansson", "Karlsson", "Larsson", "Nilsson", "Olofsson", "Persson", "Quist", "Rasmussen", "Svensson", "Thorsson", "Ulfsson", "Vikström", "Wikström", "Xenakis", "Ylitalo", "Zetterberg").andThen[Option]

  given age: Generator[Int] = Generator.between(0, 100)
  given fortune: Generator[Option[BigDecimal]] = Generator.between(0, 100000).map(BigDecimal(_)).andThen[Option]

  val persons = Generator[Person].generate(100)

end Person

class PersonTable 
  extends TableView[Person]
  with TableUtils[Person]:

  val firstName = new Column("First name", _.firstName.toVal):
    setDefaultCell()

  val lastName = new Column("Last name", _.lastName.toVal):
    setDefaultCell()

  val age = new Column("Age", _.age.toVal):
    setDefaultCell()

  val fortune = new Column("Fortune", _.fortune.toVal):
    setDefaultCell()

  val isEmployee = new Column("Is employee", _.isEmployee.toVal):
    setDefaultCell()

  val birthDate = new Column("Birth date", _.birthDate.toVal):
    setDefaultCell()

  val lastLogin = new Column("Last login", _.lastLogin.toVal):
    setDefaultCell()

  val personal = new HeaderColumn("Personal", age, birthDate, fortune)

  getColumns.addAll(firstName, lastName, personal, isEmployee, lastLogin)

end PersonTable

@main def main = Application.launch(classOf[TableExcelDemo])

class TableExcelDemo extends SimpleApp2("Table Excel demos"):

  def root(stage: Stage) =

    given Window = stage

    val pane = new BorderPane

    val defaultButton = new Button("Default...")
    val chooseRowsAndColumnsButton = new Button("Choose rows and columns...")
    val chooseIncludeColumnsButton = new Button("Choose include columns...")
    val chooseExcludeColumnsButton = new Button("Choose exclude columns...")

    val toolbar = new ToolBar(defaultButton, chooseRowsAndColumnsButton, chooseIncludeColumnsButton, chooseExcludeColumnsButton)

    val table = new PersonTable

    table.setItems(Person.persons.toObservableList)

    table.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)
    table.getSelectionModel.setCellSelectionEnabled(true)

    pane.setTop(toolbar)
    pane.setCenter(table)

    def exportWithOptions(options: TableExcel.Options): Unit =
      FileChoosing.withFileForSave("Excel", "xlsx"): file =>
        FutureModal("Exporting..."):
          TableExcel.writeFile(file, table, options)
        .onComplete: res =>
          Responding.respondWith(x => s"Exported to $x")(res)

    defaultButton.setOnAction: _ =>
      exportWithOptions(TableExcel.Options.Default)

    chooseRowsAndColumnsButton.setOnAction: _ =>
      TableExcel.optionsDialog(table).showAndWait().ifPresent: options =>
        exportWithOptions(options)

    chooseIncludeColumnsButton.setOnAction: _ =>
      TableExcel.optionsDialog(table, includeColumns = List(table.firstName, table.age)).showAndWait().ifPresent: options =>
        exportWithOptions(options)

    chooseExcludeColumnsButton.setOnAction: _ =>
      TableExcel.optionsDialog(table, excludeColumns = List(table.fortune, table.lastLogin)).showAndWait().ifPresent: options =>
        exportWithOptions(options)

    pane