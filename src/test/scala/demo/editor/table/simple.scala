package demo.editor.table.simple

import scala.jdk.CollectionConverters.*

import javafx.scene.control.*
import javafx.scene.layout.*

import no.vedaadata.generator.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.ui.editor.*
import org.sphix.collection.*
import org.sphix.collection.mutable.ObservableBuffer

case class Person(name: String, age: Int, hobby: String)

object Person:
  given Generator[Person] = 
    (Generator("Alex", "Bob", "Charlie"), Generator.between(20, 50), Generator("Reading", "Swimming", "Cycling")).mapN(Person.apply)    

class PersonTable extends TableView[Person] with TableUtils[Person]:

  val name = new Column("Name", _.name.toVal):
    setDefaultCell()

  val age = new Column("Age", _.age.toVal):
    setDefaultCell()

  val hobby = new Column("Hobby", _.hobby.toVal):
    setCell(new TextFieldCell {})
  
  getColumns.addAll(name, age, hobby)

  setEditable(true)

class PersonsEditorFactory extends EditorFactory[List[Person]]:
  def createEditor = new Editor:
    type C = Container.Primitive
    val table = new PersonTable
    def get = table.getItems.toList
    val status = Val(Status.Valid)
    def value = Val(Value.Valid(table.getItems.toList))
    def set(x: List[Person]) = table.getItems.setAll(x.asJava)
    def clear() = {}
    def container(label: Option[String]) = Container.Primitive(this, label, table)
