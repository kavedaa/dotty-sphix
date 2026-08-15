package demo.collection

import javafx.application.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.collection.*
import org.sphix.collection.mutable.ObservableBuffer
import org.sphix.control.*
import org.sphix.control.given
import org.sphix.ui.action.*

import no.vedaadata.generator.*

import demo.*

@main def main = Application.launch(classOf[Demo])

case class Person(name: String, age: Int)

object Person:
  given Generator[Person] = (Generator("Joe", "Tom", "Bob"), Generator.between(20, 80)).mapN(Person.apply)

class Demo extends SimpleApp with Resources:

  val generator = Generator[Person]

  val persons = ObservableBuffer.from(generator.generate(10))

  val add = ActionHandler[ActionType.Add]:
    generator.next().foreach(persons.addOne)

  val delete = ActionHandler[ActionType.Delete]:
    pane.actionItem().foreach: person =>
      val index = persons.indexWhere(_ eq person)
      if index != -1 then persons.remove(index)

  val refresh = ActionHandler[ActionType.Refresh]:
    persons() = generator.generate(10)

  val pane: PersonPane = new PersonPane(add, delete, refresh)

  pane.table.setItems(persons.toObservableList)

  val root = pane

  val numPersons = persons(_.size)

  pane.info.textProperty <== numPersons.map(n => s"There are $n persons")


class PersonPane(add: ActionHandler[ActionType.Add], delete: ActionHandler[ActionType.Delete], refresh: ActionHandler[ActionType.Refresh])
  (using ActionTexts, ActionIcons)
  extends TableActionPane[Person]
  with ActionPane.Add(add)
  with ActionPane.Delete(delete)
  with ActionPane.Refresh(refresh):

  val table = derivedTable[Person]
  table.getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)

  def content = table

  val info = new Label

  val statusBar = new ToolBar(info)

  init()

  setBottom(statusBar)
