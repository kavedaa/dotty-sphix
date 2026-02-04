package org.sphix.ui

import javafx.scene.Node
import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.geometry.*

import org.sphix.layout.LayoutUtils

trait FormUtils extends LayoutUtils:

  def boxGap = 15
  def elemGap = 10
  def padding = 15

  // TODO refined result type using match type?
  def padded(node: Node): Node = 
    node match
      case region: Region =>
        region.setPadding(new Insets(padding))
        node
      case x => 
        new StackPane:
          getChildren.add(node)
          setPadding(new Insets(padding))
    
  def vbox(nodes: Node*) = new VBox(boxGap):
    getChildren.addAll(nodes*)

  // for backwards comp.
  def pvbox(nodes: Node*) = new VBox(boxGap) {
    getChildren.addAll(nodes*)
    setPadding(new Insets(padding))
  }

  def hbox(nodes: Node*) = new HBox(boxGap):
    getChildren.addAll(nodes*)
    setAlignment(Pos.CENTER_LEFT)

  // for backwards comp.
  def phbox(nodes: Node*) = new HBox(boxGap) {
    getChildren.addAll(nodes*)
    setPadding(new Insets(padding))
    setAlignment(Pos.CENTER_LEFT)
  }

  def velem(label: String, node: Node) =
    new VBox(elemGap, new Label(label), node)

  def velems(label: String, nodes: Node*) =
    new VBox(elemGap) {
      getChildren.add(new Label(label))
      getChildren.addAll(nodes*)
    }

  def helem(label: String, node: Node) =
    new HBox(elemGap, new Label(label), node):
      setAlignment(Pos.CENTER_LEFT)

  def helems(label: String, nodes: Node*) =
    new HBox(elemGap) {
      getChildren.add(new Label(label))
      getChildren.addAll(nodes*)
      setAlignment(Pos.CENTER_LEFT)
    }

  def vvelems(elems: (String, Node)*) =
    val nodes = elems map { case (label, node) =>
      velem(label, node)
    } 
    vbox(nodes*)

  def vhelems(elems: (String, Node)*) =
    val nodes = elems map { case (label, node) =>
      helem(label, node)
    } 
    vbox(nodes*)

  def hhelems(elems: (String, Node)*) =
    val nodes = elems map { case (label, node) =>
      helem(label, node)
    } 
    hbox(nodes*)

  def hvelems(elems: (String, Node)*) =
    val nodes = elems map { case (label, node) =>
      velem(label, node)
    } 
    hbox(nodes*)

  def grid(nss: Iterable[Iterable[Node]]) = new GridPane:
    nss.zipWithIndex.foreach: (ns, y) =>
      ns.zipWithIndex.foreach: (node, x) =>
        add(node, y, x)
    setHgap(elemGap)
    setVgap(elemGap)


  def grid(items: (String, Node)*) = new GridPane:

    items.zipWithIndex foreach { case ((label, node), index) =>
      add(new Label(label), 0, index)
      add(node, 1, index)
    }

    setHgap(elemGap)
    setVgap(elemGap)    

  def grid2(items: Seq[(Node, Node)]) = new GridPane:

    items.zipWithIndex foreach { case ((elem1, elem2), index) =>
      add(elem1, 0, index)
      add(elem2, 1, index)
    }

    setHgap(elemGap)
    setVgap(elemGap)
    setPadding(new Insets(boxGap))

  def grid3(items: Seq[(Node, Node, Node)]) = new GridPane:

    items.zipWithIndex foreach { case ((elem1, elem2, elem3), index) =>
      add(elem1, 0, index)
      add(elem2, 1, index)
      add(elem3, 2, index)
    }

    setHgap(elemGap)
    setVgap(elemGap)
    setPadding(new Insets(boxGap))

  def titled(title: String, node: Node, collapsible: Boolean = false) =
    new TitledPane(title, node):
      val stack = new StackPane(node)
      stack.setPadding(new Insets(padding))
      setContent(stack)
      setCollapsible(collapsible)

object FormUtils extends FormUtils


trait RegionUtils extends FormUtils:
  this: Region =>
  setPadding(new Insets(padding))



