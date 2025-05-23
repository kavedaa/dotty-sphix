package org.sphix.control.cell

import scala.jdk.CollectionConverters.*

import javafx.geometry.Pos
import javafx.css.PseudoClass
import javafx.scene.control.ContentDisplay

trait AlignmentCell[T] extends Cell[T]:
  def pos(x: T): Option[Pos]
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    setAlignment(pos(x).orNull)

trait AlignedCell[T] extends Cell[T]:
  def pos: Pos
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    setAlignment(pos)

trait ContentDisplayCell[T] extends Cell[T]:
  def contentDisplay: ContentDisplay
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    setContentDisplay(contentDisplay)

trait StyleCell[T] extends Cell[T]:
  def style: T => Option[String]
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    setStyle(style(x).orNull)

trait StyledCell[T] extends Cell[T]:
  def styled: String
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    setStyle(styled)
   
trait StyleClassCell[T] extends Cell[T]:
  def styleClass: Map[String, T => Boolean]
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    styleClass foreach { case (name, condiction) =>
      if (condiction(x)) getStyleClass.add(name)
      else getStyleClass.remove(name)
    }
    
trait StyleClassedCell[T] extends Cell[T]:
  def styleClassed: Iterable[String]
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    getStyleClass.addAll(styleClassed.asJavaCollection)
    
trait PseudoClassCell[T] extends Cell[T]:
  def pseudoClass: Map[String, T => Boolean]
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    pseudoClass foreach { (name, condition) =>
      pseudoClassStateChanged(PseudoClass.getPseudoClass(name), condition(x))
    }
    
trait EnableDisableCell[T] extends Cell[T]:
  def enabled: T => Boolean
  def setEnable(x: Boolean): Unit
  override def onUpdate(x: T) = 
    super.onUpdate(x)
    setEnable(enabled(x))