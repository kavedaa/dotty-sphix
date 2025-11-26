package org.sphix.ui.editor

import scala.jdk.CollectionConverters.*

import javafx.scene.*
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.paint.*
import javafx.geometry.*
import javafx.css.PseudoClass

import org.controlsfx.tools.Borders

import org.sphix.*
import org.sphix.collection.Change

trait Layouter[C <: Container]:

  def layout(container: C, isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None): Node

  protected def updateStatus(node: Node, status: Status) =
    node.pseudoClassStateChanged(PseudoClass.getPseudoClass("status-valid"), status == Status.Valid)
    node.pseudoClassStateChanged(PseudoClass.getPseudoClass("status-invalid"), status.isInstanceOf[Status.Invalid])
    node.pseudoClassStateChanged(PseudoClass.getPseudoClass("status-empty"), status == Status.Empty)

object Layouter:

  enum Strategy:
    case Vertical
    case Horizontal
    case Flow
    case Grid
    
  object Strategy:
    given default: Strategy = Vertical

  enum Frame:
    case None
    case Border
    case Titled

  object Frame:
    given default: Frame = Titled

  given Layouter[Container.Primitive] = Default.Primitive
  given Layouter[Container.MultiPrimitive] = Default.MultiPrimitive
  given (using Layouter.Strategy, Layouter.Frame): Layouter[Container.Composite] = new Default.Composite
  given (using Layouter.Strategy): Layouter[Container.Dynamic] = new Default.Dynamic

  object Default:

    val StyleClassName = "sphix-editor"

    object Primitive extends Layouter[Container.Primitive]:
      def layout(primitive: Container.Primitive, isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None): Node =
        val node = primitive.node
        node.getStyleClass.add(StyleClassName)
        primitive.editor.status.onValue: status => 
          updateStatus(node, status)
        updateStatus(node, primitive.editor.status())
        node match
          case c: Control =>
            c.tooltipProperty <== primitive.editor.status.map: s =>
              s match 
                case Status.Invalid(reasons) => new Tooltip(reasons.map(_.defaultText) mkString ", ")
                case _ => null 
          case _ =>
        node match 
          case labeled: Labeled => 
            primitive.label.foreach(label => labeled.setText(label))
            new VBox(5):
              val orthogonal = new HBox(5):
                getChildren.add(node)
                primitive.lateralNodes.foreach(x => getChildren.add(x))
              getChildren.add(orthogonal)
          case other =>
            new VBox(5):
              val orthogonal = new HBox(5):
                getChildren.add(other)
                primitive.lateralNodes.foreach(x => getChildren.add(x))
              primitive.label.foreach(label => getChildren.add(new Label(label)))
              getChildren.add(orthogonal)

    object MultiPrimitive extends Layouter[Container.MultiPrimitive]:
      def layout(multiPrimitive: Container.MultiPrimitive, isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None): Node =
        val node = new VBox(5):
          multiPrimitive.label.foreach(label => getChildren.add(new Label(label)))
          getChildren.addAll(multiPrimitive.nodes*)
        node.getStyleClass.add(StyleClassName)
        multiPrimitive.editor.status onValue { status => updateStatus(node, status) }
        updateStatus(node, multiPrimitive.editor.status())
        node

    class Composite(using strategy: Layouter.Strategy, frame: Layouter.Frame) extends Layouter[Container.Composite]:
      def layout(composite: Container.Composite, isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None): Node =      
        val nodes = composite.containers.map(_.layout(false, onLayoutChange))
        val pane = strategy match
          case Layouter.Strategy.Vertical =>
            new VBox(10):
              getChildren.addAll(nodes.asJava)
          case Layouter.Strategy.Horizontal =>
            new HBox(10):
              getChildren.addAll(nodes.asJava)
              nodes.foreach(x => HBox.setHgrow(x, Priority.ALWAYS))
          case Layouter.Strategy.Flow =>      
            new FlowPane:
              setHgap(10)
              setVgap(10)
              setPadding(new Insets(10))
          case Layouter.Strategy.Grid =>
            ???
        pane.setPadding(new Insets(10))
        if isTopLevel then pane
        else frame match
          case Layouter.Frame.None => 
            pane
          case Layouter.Frame.Border =>
            Borders.wrap(pane).lineBorder.color(Color.LIGHTGREY).innerPadding(0).outerPadding(0).buildAll
          case Layouter.Frame.Titled =>            
            new TitledPane:
              composite.label foreach { label => setText(label) }
              setContent(pane)
              setCollapsible(false)

    class Dynamic(using strategy: Layouter.Strategy) extends Layouter[Container.Dynamic]:
      def layout(dynamic: Container.Dynamic, isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None): Node =
        val pane = strategy match
          case Layouter.Strategy.Vertical =>
            new VBox(10)
          case Layouter.Strategy.Horizontal =>
            new HBox(10)
          case Layouter.Strategy.Flow =>      
            new FlowPane:
              setHgap(10)
              setVgap(10)
              setPadding(new Insets(10))
          case Layouter.Strategy.Grid =>
            ???
        def layoutSubContainer(c: Container) =
          val node = c.layout(false, onLayoutChange)
          val removeButton = new Button("-"):
            setOnAction(_ => dynamic.remove(c))
          new HBox(5, node, removeButton):
            setAlignment(Pos.CENTER_LEFT)
            HBox.setHgrow(node, Priority.ALWAYS)
        pane.getChildren.addAll(dynamic.containers.map(layoutSubContainer).asJavaCollection)
        dynamic.containers.onChange: xs =>
          xs.map:
            case Change.Removed(index, xs) =>
              pane.getChildren.remove(index, index + xs.size)
            case Change.Added(index, xs) =>
              pane.getChildren.addAll(index, xs.map(layoutSubContainer).asJavaCollection)
            case _ => //  don't expect any other change here
          onLayoutChange.foreach(x => x())
        val addButton = new Button("+"):
          setOnAction(_ => dynamic.add())
        new TitledPane:
          dynamic.label.foreach(label => setText(label))
          setContent(VBox(10, pane, addButton))
          setCollapsible(false)

end Layouter
