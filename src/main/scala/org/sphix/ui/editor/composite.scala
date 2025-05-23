package org.sphix.ui.editor

import scala.deriving.*
import scala.compiletime.*

import org.sphix.*

abstract class CompositeEditor[A] extends Editor[A]:
  protected def editors: List[Editor[?]]
  lazy val status = editors.map(_.status).mapSeq(Status.sequence)
  lazy val value = editors.asInstanceOf[List[Editor[Any]]].map(_.value).mapSeq(xs => Value.sequence(xs, get))
  def clear() = editors.foreach(_.clear())

abstract class ProductEditor[A >: Null <: Product](using m: Mirror.ProductOf[A]) extends Editor[A]:
  def elemEditors: List[Editor[Any]]
  def get = 
    val tuple = elemEditors.map(_.get).foldRight[Tuple](EmptyTuple)(_ *: _)
    m.fromProduct(tuple)
  lazy val status = elemEditors.map(_.status) mapSeq { xs =>
    if xs.forall(_ == Status.Valid) then Status.Valid
    else
      val invalids = xs collect { case invalid: Status.Invalid => invalid }
      if invalids.nonEmpty then Status.Invalid(invalids.toList.flatMap(_.reasons))
      else Status.Empty
  }
  lazy val value = elemEditors.map(_.value) mapSeq { xs =>
    if xs.forall(_.valid) then
      val tuple = xs.map(_.get).foldRight[Tuple](EmptyTuple)(_ *: _)
      Value.Valid(m.fromProduct(tuple))
    else
      val invalids = xs collect { case invalid: Value.Invalid => invalid }
      if invalids.nonEmpty then Value.Invalid(invalids.toList.flatMap(_.reasons))
      else Value.Empty
  }
  def set(a: A) =
    val elems = a.productIterator.toSeq
    elems.zip(elemEditors) foreach { case (elem, editor) => 
      editor.set(elem)
    }
  def clear() = elemEditors.foreach(_.clear())

trait LabelTransformation extends (String => String)

object LabelTransformation:
  val FromCamelCase: LabelTransformation = fromCamelCase
  given default: LabelTransformation = FromCamelCase

inline def productEditorFactory[A >: Null <: Product]
  (using m: Mirror.ProductOf[A])
  (using Layouter[Container.Composite])
  (using labelTransformation: LabelTransformation):
  EditorFactory[A] =
    new EditorFactory:
      def createEditor = new ProductEditor[A]:
        type C = Container.Composite
        val elemEditors = summonAll[Tuple.Map[m.MirroredElemTypes, EditorFactory]].toList.asInstanceOf[List[EditorFactory[Any]]].map(_.createEditor)
        val labels = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]].map(labelTransformation)
        val containers = labels.zip(elemEditors) map { case (label, editor) => editor.container(Some(label)) }
        def container(label: Option[String]) = Container.Composite(this, label, containers)