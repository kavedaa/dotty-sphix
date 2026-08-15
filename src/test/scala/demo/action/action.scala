package demo.action

import scala.language.implicitConversions

import javafx.application.*
import javafx.scene.control.*

import no.vedaadata.generator.*

import org.sphix.*
import org.sphix.collection.toObservableList
import org.sphix.control.derivedTable
import org.sphix.ui.action.*
import org.sphix.collection.mutable.ObservableBuffer
import org.sphix.control.derivedTable

import demo.*

@main def main = Application.launch(classOf[Demo])

case class Person(name: String, age: Int)

object Person:
  given Generator[Person] = (Generator("Joe", "Tom", "Bob"), Generator.between(20, 80)).mapN(Person.apply)

class Demo extends SimpleApp with Resources:

  val persons = Generator[Person].generate(10).to(ObservableBuffer)

  val add = ActionHandler[ActionType.Add]:
    Generator[Person].generate(1).foreach: person =>
      persons += person

  val edit = ActionHandler[ActionType.Edit]:
    pane.actionItem().foreach: person =>
      println(s"$person edited")

  val delete = ActionHandler[ActionType.Delete]:
    pane.actionItem().foreach: person =>
      persons.removeRef(person)

  val clear = ActionHandler[ActionType.Clear]:
    persons.clear()

  val refresh = ActionHandler[ActionType.Refresh]:
    persons() = Generator[Person].generate(10)

  val pane: PersonPane = new PersonPane(add, edit, delete, clear, refresh)

  pane.table.setItems(persons)

  val root = pane


class PersonPane(
  add: ActionHandler[ActionType.Add], 
  edit: ActionHandler[ActionType.Edit], 
  delete: ActionHandler[ActionType.Delete], 
  clear: ActionHandler[ActionType.Clear], 
  refresh: ActionHandler[ActionType.Refresh])
  (using ActionTexts, ActionIcons)
  extends TableActionPane[Person]
  with ActionPane.Add(add)
  with ActionPane.Edit(edit)
  with ActionPane.Delete(delete)
  with ActionPane.Clear(clear)
  with ActionPane.Refresh(refresh):

  val table = derivedTable[Person]
  table.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)

  def content = table

  init()
