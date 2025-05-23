package org.sphix.ui.editor

import scala.jdk.CollectionConverters.*

import javafx.scene.control.*
import javafx.util.*

import org.controlsfx.control.SearchableComboBox

import org.sphix.*
import org.sphix.util.*
import org.sphix.control.*
import org.sphix.collection._
import org.sphix.collection.ObservableSeq._
import org.sphix.collection.mutable.ObservableBuffer

// TODO make all the renders implicit

class ListViewListEditorFactory[A](items: => Iterable[A])(render: A => String)(using Layouter[Container.Primitive]) extends EditorFactory[List[A]]:
  def createEditor = new Editor[List[A]]:
    type C = Container.Primitive
    val listView = new ListView[A] with ListUtils[A]:
      setItems(ObservableSeq.from(items))
      setCell(new StringCell(using x => render(x)) {})
      getSelectionModel.setSelectionMode(SelectionMode.MULTIPLE)
    def get = listView.getSelectionModel.getSelectedItems.asScala.toList
    val value = listView.getSelectionModel.getSelectedItems.asVal.map(Value.Valid.apply)
    val status = Val(Status.Valid)
    def set(xs: List[A]) = xs foreach listView.getSelectionModel.select
    def clear() = listView.getSelectionModel.clearSelection()
    def container(label: Option[String]) = Container.Primitive(this, label, listView)

class CheckBoxListEditorFactory[A](items: => Iterable[A])(render: A => String)(using Layouter[Container.MultiPrimitive]) extends EditorFactory[List[A]]:
  def createEditor = new Editor[List[A]]:    
    type C = Container.MultiPrimitive
    val itemCheckboxes = items.map(item => new CheckBox(render(item)) -> item).toList
    def get = itemCheckboxes.collect { case (cb, item) if cb.isSelected => item }
    val value = 
      itemCheckboxes
        .map { (cb, item) => cb.selectedProperty.map( _ -> item) } 
        .mapSeq { xs => Value.Valid(xs.toList.collect { case (selected, item) if selected => item } ) }
    val status = Val(Status.Valid)
    def set(xs: List[A]) = xs.foreach { x => itemCheckboxes.find((cb, item) => item == x) foreach { (cb, item) => cb.setSelected(true) } }
    def clear() = itemCheckboxes.foreach { (cb, item) => cb.setSelected(false) }
    def container(label: Option[String]) = Container.MultiPrimitive(this, label, itemCheckboxes.map(_._1))

class ListViewItemEditorFactory[A](items: => Iterable[A])(render: A => String)(using Layouter[Container.Primitive]) extends EditorFactory[A]:
  def createEditor = new Editor[A]:
    type C = Container.Primitive
    val listView = new ListView[A] with ListUtils[A]:
      setItems(ObservableSeq.from(items))
      setCell(new StringCell(using x => render(x)) {})
      getSelectionModel.setSelectionMode(SelectionMode.SINGLE)
    def get = listView.getSelectionModel.getSelectedItem
    val value = listView.getSelectionModel.selectedItemProperty.mapOption.map(Value.fromOption)
    val status = listView.getSelectionModel.selectedItemProperty.isNotNull.map(Status.fromBoolean)
    def set(x: A) = listView.getSelectionModel.select(x)
    def clear() = listView.getSelectionModel.clearSelection()
    def container(label: Option[String]) = Container.Primitive(this, label, listView)

class RadioItemEditorFactory[A](items: => Iterable[A])(render: A => String)(using Layouter[Container.MultiPrimitive]) extends EditorFactory[A]:
  def createEditor = new Editor[A]:
    type C = Container.MultiPrimitive
    val group = new ToggleGroup
    val itemRadios = items.map(item => new RadioButton(render(item)) { setToggleGroup(group) } -> item).toList
    def get = itemRadios.collectFirst { case (r, item) if r.isSelected => item } .get
    val value = itemRadios
      .map { (r, item) => r.selectedProperty map { _ -> item } }
      .mapSeq { xs => Value.fromOption(xs.toList collectFirst { case (selected, item) if selected => item} ) }
    val status = group.selectedToggleProperty.isNotNull.map(Status.fromBoolean)
    def set(x: A) = itemRadios find { (r, item) => item == x } foreach { (r, item) => r.setSelected(true) }
    def clear() = itemRadios foreach { (r, item) => r.setSelected(false) }    
    def container(label: Option[String]) = Container.MultiPrimitive(this, label, itemRadios.map(_._1))

// class ComboBoxEditorFactoryBase[A](comboBoxFactory: => ComboBox[A])(items: => Iterable[A])(render: A => String)(using Layouter[Container.Primitive[A], A]) extends EditorFactory[A]:
//   def createEditor = new Editor[A]:
//     type C = Container.Primitive[A]
//     val comboBox = comboBoxFactory
//     comboBox.setMaxWidth(Int.MaxValue)
//     comboBox.setItems(ObservableSeq.from(items))
//     comboBox.setConverter {
//       new StringConverter[A] {
//         def fromString(x: String) = ???
//         def toString(x: A) = if x == null then "" else render(x)
//       }
//     }
//     val value = comboBox.getSelectionModel.selectedItemProperty.mapOption.map(Value.fromOption)
//     val status = comboBox.getSelectionModel.selectedItemProperty.isNotNull.map(Status.fromBoolean)
//     def set(x: A) = comboBox.getSelectionModel.select(x)
//     def clear() = comboBox.getSelectionModel.clearSelection()
//     def container(label: Option[String]) = Container.Primitive(this, label, comboBox)

class ComboBoxEditorFactory[A](items: => Iterable[A])(using comboBoxFactory: ComboBoxFactory[A])(using Layouter[Container.Primitive]) extends EditorFactory[A]:
  def createEditor = new Editor[A]:
    type C = Container.Primitive
    val comboBox = comboBoxFactory.create()
    comboBox.setMaxWidth(Int.MaxValue)
    comboBox.setItems(ObservableSeq.from(items))
    comboBox.setConverter(comboBoxFactory.stringConverter)
    def get = comboBox.getSelectionModel.getSelectedItem
    val value = comboBox.getSelectionModel.selectedItemProperty.mapOption.map(Value.fromOption)
    val status = comboBox.getSelectionModel.selectedItemProperty.isNotNull.map(Status.fromBoolean)
    def set(x: A) = comboBox.getSelectionModel.select(x)
    def clear() = comboBox.getSelectionModel.clearSelection()
    def container(label: Option[String]) = Container.Primitive(this, label, comboBox)

