package demo.crud

import scala.language.implicitConversions

import javafx.application.*
import javafx.scene.control.*

import no.vedaadata.generator.*

import org.sphix.*
import org.sphix.collection.toObservableList
import org.sphix.control.derivedTable
import org.sphix.ui.crud.*
import org.sphix.collection.mutable.ObservableBuffer
import org.sphix.control.derivedTable

import demo.*

@main def main = Application.launch(classOf[Demo])

case class Person(name: String, age: Int)

object Person:
  given Generator[Person] = (Generator("Joe", "Tom", "Bob"), Generator.between(20, 80)).mapN(Person.apply)

class Demo extends SimpleApp with Resources:

  val persons = Generator[Person].generate(10).to(ObservableBuffer)

  def add() = 
    Generator[Person].generate(1).foreach: person =>
      persons += person

  def edit() = 
    pane.selectedItem().foreach: person =>
      println(s"$person edited")

  def delete() =     
    pane.selectedItem().foreach: person =>
      persons.removeRef(person)

  def clear() = 
    persons.clear()

  def refresh() = 
    persons() = Generator[Person].generate(10)

  val pane: PersonPane = new PersonPane(add, edit, delete, clear, refresh)

  pane.table.setItems(persons)

  val root = pane


class PersonPane(add: Crud.Op, edit: Crud.Op, delete: Crud.Op, clear: Crud.Op, refresh: Crud.Op)(using CrudTexts, CrudIcons)
  extends TableCrudPane[Person]
  with CrudPane.Add(add)
  with CrudPane.Edit(edit)
  with CrudPane.Delete(delete)
  with CrudPane.Clear(clear)
  with CrudPane.Refresh(refresh):

  val table = derivedTable[Person]
  table.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)

  def content = table

  init()
