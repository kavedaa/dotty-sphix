package demo.editor.dynamic

import javafx.scene.Node
import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.beans.value.ObservableValue

import org.sphix.*
import org.sphix.ui.FormUtils
import org.sphix.ui.editor.*
import org.sphix.ui.dialog.EditorDialog

case class SomeData(
  foo: String,
  bar: Int)

case class SomeOtherData(
  foo: String,
  bar: Int)

case class ListsOfData(
  someDatas: List[SomeData],
  someOtherDatas: List[SomeOtherData])
    
case class Person(name: String, age: Int)


object DynamicDemo extends BorderPane:

  val dynamicButton = new Button("Dynamic")
  val dynamicCompositeButton = new Button("Dynamic composite")
  val dynamic2Button = new Button("Dynamic 2")
  val dynamic3Button = new Button("Dynamic 3")

  dynamicButton.setOnAction: _ =>
    given EditorFactory[List[String]] = new DynamicEditorFactory
    new EditorDialog[List[String]](Some("Please input any number of strings"), Some(List("Foo", "Bar"))).showAndWait().ifPresent(println)

  dynamicCompositeButton.setOnAction: _ =>
    given Layouter.Strategy = Layouter.Strategy.Horizontal
    given Layouter.Frame = Layouter.Frame.Border
    given someDataEditor: EditorFactory[List[SomeData]] = new DynamicEditorFactory
    given someOtherDataEditor: EditorFactory[List[SomeOtherData]] = new DynamicEditorFactory
    val data = ListsOfData(List(SomeData("foo", 1), SomeData("bar", 2)), List(SomeOtherData("foo", 2)))
    new EditorDialog[ListsOfData](Some("Please enter some data"), Some(data)).showAndWait().ifPresent(println)

  dynamic2Button.setOnAction: _ =>
    given EditorFactory[List[Person]] = new DynamicEditorFactory
    new EditorDialog[List[Person]]().showAndWait().ifPresent(println)


  val toolbar = ToolBar(dynamicButton, dynamicCompositeButton, dynamic2Button)

  setTop(toolbar)
