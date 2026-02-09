package demo.collection

import javafx.application.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.collection.*
import org.sphix.collection.mutable.ObservableBuffer
import org.sphix.control.*
import org.sphix.control.given
import org.sphix.ui.crud.*

import no.vedaadata.generator.*

@main def main = Application.launch(classOf[Demo])

case class Person(name: String, age: Int)

object Person:
  given Generator[Person] = (Generator("Joe", "Tom", "Bob"), Generator.between(20, 80)).mapN(Person.apply)

class Demo extends SimpleApp:

  val generator = Generator[Person]

  val persons = ObservableBuffer.from(generator.generate(10))

  def add() = 
    generator.next().foreach(persons.addOne)

  def delete() =     
    pane.selectedItem().foreach: person =>
      val index = persons.indexWhere(_ eq person)
      if index != -1 then persons.remove(index)

  def refresh() = 
    persons() = generator.generate(10)

  given CrudTexts = CrudTexts.Default
  given CrudIcons = CrudIcons.Default

  val pane: PersonPane = new PersonPane(add, delete, refresh)

  pane.table.setItems(persons.toObservableList)

  val root = pane

  val numPersons = persons(_.size)

  pane.info.textProperty <== numPersons.map(n => s"There are $n persons")


class PersonPane(add: Crud.Op, delete: Crud.Op, refresh: Crud.Op)(using CrudTexts, CrudIcons)
  extends TableCrudPane[Person]
  with CrudPane.Add(add)
  with CrudPane.Delete(delete)
  with CrudPane.Refresh(refresh):

  val table = summon[TableView[Person]]
  table.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)

  def content = table

  val info = new Label

  val statusBar = new ToolBar(info)

  init()

  setBottom(statusBar)
