package org.sphix.ui.editor

import java.time.LocalDate

import javafx.scene.control.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.control.ValueConverter

class ValueEditorFactory[A](using converter: ValueConverter[A])(using Layouter[Container.Primitive]) extends EditorFactory[A]:
  def createEditor = new Editor[A]:
    type C = Container.Primitive
    val valueField = new ValueField[A]
    def get = valueField.getValue.get
    val value = valueField.value
    val status = valueField.value.map(_.status)
    def set(x: A) = valueField.setValue(x)
    def clear() = valueField.clear()
    def container(label: Option[String]) = Container.Primitive(this, label, valueField)

class CheckBoxEditorFactory(using Layouter[Container.Primitive]) extends EditorFactory[Boolean]:
  def createEditor = new Editor[Boolean]:
    type C = Container.Primitive
    val checkBox = new CheckBox
    def get = checkBox.isSelected
    val value = checkBox.selectedProperty.map(Value.Valid.apply)
    val status = Val(Status.Valid)
    def set(x: Boolean) = checkBox.setSelected(x)
    def clear() = checkBox.setSelected(false)
    def container(label: Option[String]) = Container.Primitive(this, label, checkBox)

//  TODO 
// class BooleanRadiosEditorFactory(using Layouter[Container.Primitive]) extends EditorFactory[Boolean]:
//   def createEditor = new Editor[Boolean]:
//     type C = Container.Primitive
//     val checkBox = new CheckBox
//     val value = checkBox.selectedProperty.map(Value.Valid.apply)
//     val status = Val(Status.Valid)
//     def set(x: Boolean) = checkBox.setSelected(x)
//     def clear() = checkBox.setSelected(false)
//     def container(label: Option[String]) = Container.Primitive(this, label, checkBox)

//  TODO could we do something like TextInputControlFactory?

class TextFieldEditorFactory[A](using converter: ValueConverter[A])(using Layouter[Container.Primitive]) extends EditorFactory[A]:
  def createEditor = new Editor[A]:
    type C = Container.Primitive
    val textField = new TextField
    def get = converter.deconvert(textField.getText).get
    val value = textField.textProperty.map(converter.deconvert)
    val status = value.map(_.status)
    def set(x: A) = textField.setText(converter.convert(x))
    def clear() = textField.clear()
    def container(label: Option[String]) = Container.Primitive(this, label, textField)

class TextAreaEditorFactory[A](using converter: ValueConverter[A])(using Layouter[Container.Primitive]) extends EditorFactory[A]:
  def createEditor = new Editor[A]:
    type C = Container.Primitive
    val textArea = new TextArea
    def get = converter.deconvert(textArea.getText).get
    val value = textArea.textProperty.map(converter.deconvert)
    val status = value.map(_.status)
    def set(x: A) = textArea.setText(converter.convert(x))
    def clear() = textArea.clear()
    def container(label: Option[String]) = Container.Primitive(this, label, textArea)

class DatePickerEditorFactory(using Layouter[Container.Primitive]) extends EditorFactory[LocalDate]:
  def createEditor = new Editor[LocalDate]:
    type C = Container.Primitive
    val datePicker = new DatePicker
    def get = datePicker.getValue
    val value = datePicker.valueProperty.mapOption.map(Value.fromOption)
    val status = datePicker.valueProperty.isNotNull.map(Status.fromBoolean)
    def set(x: LocalDate) = datePicker.setValue(x)
    def clear() = datePicker.setValue(null)
    def container(label: Option[String]) = Container.Primitive(this, label, datePicker)

//  TODO generalize?
class DatePickerOptionEditorFactory(using Layouter[Container.Primitive]) extends EditorFactory[Option[LocalDate]]:
  def createEditor = new Editor[Option[LocalDate]]:
    type C = Container.Primitive
    val datePicker = new DatePicker
    def get = Option(datePicker.getValue)
    val value = datePicker.valueProperty.mapOption.map(x => Value.fromOption(x)).map(_.liftOption)
    val status = Val(Status.Valid)
    def set(x: Option[LocalDate]) = datePicker.setValue(x.orNull)
    def clear() = datePicker.setValue(null)
    def container(label: Option[String]) = Container.Primitive(this, label, datePicker)
