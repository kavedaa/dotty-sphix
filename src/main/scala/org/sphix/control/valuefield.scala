package org.sphix.control

import scala.util.*

import no.vedaadata.text.Format

import org.sphix.*

object InvalidCharFormat extends InvalidReason("Invalid char format")
object InvalidIntFormat extends InvalidReason("Invalid integer format")
object InvalidDoubleFormat extends InvalidReason("Invalid double format")
object InvalidFloatFormat extends InvalidReason("Invalid float format")
object InvalidBigDecimalFormat extends InvalidReason("Invalid bigdecimal format")

class ValueField[A](using converter: ValueConverter[A]) 
  extends javafx.scene.control.TextField:
  def getValue: Value[A] = converter.deconvert(getText)
  def setValue(x: A): Unit = setText(converter.convert(x))
  def setValue(x: Option[A]): Unit = setText(x.map(converter.convert).orNull)
  val value: Val[Value[A]] = textProperty.map(converter.deconvert)

object ValueField:

  given [A](using ValueConverter[A]): ValueField[A] = new ValueField

  def apply[A](using ValueConverter[A]): ValueField[A] = new ValueField
  def apply[A](x: A)(using ValueConverter[A]): ValueField[A] = new ValueField { setValue(x) }
  def apply[A](x: Option[A])(using ValueConverter[A]): ValueField[A] = new ValueField { x.foreach(setValue) }


trait ValueConverter[A]:
  def convert(x: A): String
  def deconvert(x: String): Value[A]

object ValueConverter:

  given [A](using inner: ValueConverter[A]): ValueConverter[Option[A]] with
    def convert(x: Option[A]) = x.map(inner.convert).getOrElse("")
    def deconvert(x: String) = inner.deconvert(x).liftOption
    
  given ValueConverter[String] with
    def convert(x: String) = x
    def deconvert(x: String) = 
      if x.isEmpty then Value.Empty 
      else Value.Valid(x)

  given ValueConverter[Char] with
    def convert(x: Char) = x.toString
    def deconvert(x: String) = 
      if x.isEmpty then Value.Empty 
      else if x.length == 1 then Value.Valid(x.charAt(0)) 
      else Value.Invalid(Seq(InvalidCharFormat))

  given ValueConverter[Int] with
    def convert(x: Int) = x.toString
    def deconvert(x: String) = 
      if x.isEmpty then Value.Empty 
      else Try(x.toInt) match
        case Success(i) => Value.Valid(i)
        case Failure(_) => Value.Invalid(Seq(InvalidIntFormat))

  given ValueConverter[Integer] with
    def convert(x: Integer) = x.toString
    def deconvert(x: String) = 
      if x.isEmpty then Value.Empty 
      else Try(x.toInt) match
        case Success(i) => Value.Valid(i)
        case Failure(_) => Value.Invalid(Seq(InvalidIntFormat))

  given (using format: Format.DoubleFormat): ValueConverter[Double] with
    def convert(x: Double) = format.formatDouble(x)
    def deconvert(x: String) = 
      if x.isEmpty then Value.Empty 
      else Try(format.parseDouble(x)) match
        case Success(d) => Value.Valid(d)
        case Failure(_) => Value.Invalid(Seq(InvalidDoubleFormat))

  given (using format: Format.BigDecimalFormat): ValueConverter[BigDecimal] with
    def convert(x: BigDecimal) = format.formatBigDecimal(x)
    def deconvert(x: String) = 
      if x.isEmpty then Value.Empty 
      else Try(format.parseBigDecimal(x)) match
        case Success(d) => Value.Valid(d)
        case Failure(_) => Value.Invalid(Seq(InvalidBigDecimalFormat))

