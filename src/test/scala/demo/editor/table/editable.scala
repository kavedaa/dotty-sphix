package demo.editor.table.editable

import scala.language.implicitConversions

import scala.jdk.CollectionConverters.*

import javafx.scene.control.*
import javafx.scene.layout.*

import no.vedaadata.generator.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.ui.editor.*
import org.sphix.collection.*
import org.sphix.collection.mutable.ObservableBuffer

class PersonModel(val name: String):

  val hobby = Var[Option[String]](None)

  val status = hobby.map: x =>
    if x.isEmpty then Status.Empty
    else Status.Valid

  def render = s"Person: $name, ${hobby()}"

class PersonTable(hobbies: List[String]) extends TableView[PersonModel] with TableUtils[PersonModel]:

  val name = new Column("Name", _.name.toVal):
    setDefaultCell()

  val hobby = new Column("Hobby", _.hobby):
    setCell(new StaticComboBoxCell(_ => None +: hobbies.map(Some.apply)) {})
  
  getColumns.addAll(name, hobby)

  setEditable(true)

  setSelectionModel(new NoTableViewSelectionModel(this))

class PersonsEditorFactory(hobbies: List[String]) extends EditorFactory[List[PersonModel]]:
  def createEditor = new Editor:
    type C = Container.Primitive
    val table = new PersonTable(hobbies)
    val items = ObservableBuffer.observeElements[PersonModel]()(_.status)
    table.setItems(items)
    def get = items.toList
    val status = items(x => Status.sequence(x.map(_.status())))
    def value = Val(Value.Valid(items.toList))
    def set(x: List[PersonModel]) = items.setAll(x.asJava)
    def clear() = {}
    def container(label: Option[String]) = Container.Primitive(this, label, table)


