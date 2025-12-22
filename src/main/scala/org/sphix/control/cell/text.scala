package org.sphix.control.cell

trait BooleanTextCell[T] extends DataCell[T, Boolean] with TextCell[T]:
  def trueText: Option[String]
  def falseText: Option[String]
  def text(x: T) = dataValue(x) match
    case Some(true) => trueText
    case Some(false) => falseText
    case _ => None

trait StripNewLines[T] extends TextCell[T]:
  abstract override def text(x: T) =
    super.text(x).map(_.split("\r\n").mkString(" "))

