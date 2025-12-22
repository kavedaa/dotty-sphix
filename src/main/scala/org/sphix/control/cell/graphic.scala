package org.sphix.control.cell

import javafx.scene.*
import javafx.scene.image.*

trait ImageCell[T, A] extends Cell[T]:
  def toImage: T => A
  def asOption: A => Option[Image]
  val imageView = new ImageView
  override def onUpdate(x: T) =
    super.onUpdate(x)
    imageView.setImage(asOption(toImage(x)).orNull)
    setGraphic(imageView)

trait StaticImageCell[T] extends GraphicCell[T]:
  def image: Image
  val imageView = new ImageView
  imageView.setImage(image)
  def graphic(x: T) = Some(imageView)

trait BooleanGraphicCell[T] extends DataCell[T, Boolean] with GraphicCell[T]:
  def booleanGraphic: Node
  def graphic(x: T) = dataValue(x) match
    case Some(true) => Some(booleanGraphic)
    case _ => None

trait BooleanImageCell[T] extends DataCell[T, Boolean]:
  def trueImage: Option[Image]
  def falseImage: Option[Image]
  val imageView = new ImageView
  override def onUpdate(x: T) =
    super.onUpdate(x)
    val value = dataValue(x)
    if value.contains(true) then imageView.setImage(trueImage.orNull)
    else if value.contains(false) then imageView.setImage(falseImage.orNull)
    else imageView.setImage(null)
    setGraphic(imageView)    