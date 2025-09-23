package org.sphix.ui.editor

import javafx.scene.Node

import org.sphix.*
import org.sphix.collection.ObservableSeq

trait Container:
  def layout(isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None): Node
//  def withEditor(editor: Editor[?]): Container

object Container:

  case class Primitive(
    editor: Editor[?], 
    label: Option[String],     
    node: Node,
    lateralNodes: Seq[Node] = Nil)
    (using layouter: Layouter[Primitive])
    extends Container:
      def layout(isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None) = layouter.layout(this, isTopLevel, onLayoutChange)
      def withEditor(that: Editor[?]) = copy(editor = that)

  case class MultiPrimitive(
    editor: Editor[?], 
    label: Option[String], 
    nodes: Seq[Node])
    (using layouter: Layouter[MultiPrimitive])
    extends Container:
      def layout(isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None) = layouter.layout(this, isTopLevel, onLayoutChange)
      def withEditor(that: Editor[?]) = copy(editor = that)

  case class Composite(
    editor: Editor[?], 
    label: Option[String], 
    containers: Seq[Container])
    (using layouter: Layouter[Composite])
    extends Container:
      def layout(isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None) = layouter.layout(this, isTopLevel, onLayoutChange)
      def withEditor(that: Editor[?]) = copy(editor = that)

  case class Dynamic(
    editor: Editor[?], 
    label: Option[String], 
    containers: ObservableSeq[Container], 
    add: () => Unit, 
    remove: Container => Unit)
    (using layouter: Layouter[Dynamic])
    extends Container:
      def layout(isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None) = layouter.layout(this, isTopLevel, onLayoutChange)
      def withEditor(that: Editor[?]) = copy(editor = that)

