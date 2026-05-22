package demo.util

import javafx.application.Application
import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.util.*
import org.sphix.control.*

@main def main = Application.launch(classOf[Demo])

class Demo extends SimpleApp:

  val places = TreeList(List(
    Tree("North America", List(
      Tree("USA"),
      Tree("Canada")
    )),
    Tree("Europe", List(
      Tree("UK"),
      Tree("Germany")
    ))
  ))

  val selectableTree = SelectableTree.fromTreeList(places, identity, "World")

  val treeTableView = new SelectableTreeTableView(selectableTree)

  selectableTree.setExpanded(true)

  val printButton = new Button("Print selected items")

//  printButton.setOnAction: _ =>


  val pane = new BorderPane:
    setCenter(treeTableView)

  def root = pane


class SelectableTreeTableView(selectableTree: TreeItem[SelectableTree.SelectableItem[String]]) 
  extends TreeTableView[SelectableTree.SelectableItem[String]](selectableTree) 
  with TreeTableUtils[SelectableTree.SelectableItem[String]]:

  val isSelectedAndLabel = new Column("", _.isSelectedAndLabel):
    setCell:
      new CheckBoxLabelCell {}

  getColumns.addAll(isSelectedAndLabel)


