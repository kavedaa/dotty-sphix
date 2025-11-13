package org.sphix.ui.editor

import scala.jdk.CollectionConverters.*

import java.io.File
import java.awt.Desktop

import javafx.geometry.*
import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.scene.image.* 

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.*
import org.sphix.ui.FileChoosing
import org.sphix.ui.FileDropping


class FilesEditorFactory
  (fileTypesDescription: String, fileExtensions: List[String], iconMap: Map[String, Image] = Map())  
  extends EditorFactory[List[File]]:
  def createEditor = new FilesEditor(fileTypesDescription, fileExtensions, iconMap)


class FilesEditor
  (fileTypesDescription: String, fileExtensions: List[String], iconMap: Map[String, Image]) 
  extends Editor[List[File]]:

  type C = Container.Primitive

  val table = new FileTable(iconMap)

  def get = table.getItems.toList

  def status = Val(Status.Valid)

  def value = table.itemsProperty.map(x => Value.Valid(x.toList))

  def set(x: List[File]) = table.setItems(x.toObservableList)

  def clear() = table.getItems.clear()

  val addButton = new Button("+")
  val removeButton = new Button("-")
  val clearButton = new Button("x")

  val toolbar = new ToolBar(addButton, removeButton, clearButton)

  val pane = new BorderPane with FileDropping:
    onDrop(table, fileExtensions): files =>
      table.getItems.addAll(files.asJava)

  pane.setTop(toolbar)
  pane.setCenter(table)

  def container(label: Option[String]) = new Container.Primitive(this, label, pane)

  addButton.setOnAction: _ =>
    FileChoosing.withMultipleFilesForOpen(fileTypesDescription, fileExtensions*): files =>
      table.getItems.addAll(files.asJava)

  removeButton.setOnAction: _ =>
    Option(table.getSelectionModel.getSelectedItem).foreach: selectedItem =>
      table.getItems.remove(selectedItem)

  clearButton.setOnAction: _ =>
    clear()

end FilesEditor


class FileTable(iconMap: Map[String, Image])
  extends TableView[File]
  with TableUtils[File]:

  def extension(f: File) = 
    val filename = f.getName
    val lastDotIndex = filename.lastIndexOf('.')
    if lastDotIndex > 0 && (lastDotIndex < filename.length - 1)  then
      Some(filename.substring(lastDotIndex + 1).toLowerCase)
    else None
  
  def resolveIcon(extension: String) =
    iconMap.get(extension).orElse(iconMap.get("*"))

  val icon = new Column(30)("", x => extension(x).flatMap(resolveIcon).toVal):
    setCell:
      new ImageCell
      with AlignedCell(Pos.CENTER)

  val name = new Column(500)("", _.toVal):
    setCell:
      new StringCell(using _.getName)
      with HyperlinkCell(Desktop.getDesktop.open)

  getColumns.addAll(icon, name)

  setPlaceholder(new Label)