package org.sphix.util

import no.vedaadata.text.Format

trait HalfConverter[A, B]:
  def convert(x: A): Option[B]

object HalfConverter:

  def apply[A, B](using converter: HalfConverter[A, B]): HalfConverter[A, B] = converter

  def apply[A, B](f: A => B): HalfConverter[A, B] = 
    new HalfConverter:
      def convert(x: A) = Some(f(x))

  given [A, B](using f: A => B): HalfConverter[A, B] = apply(f)

  given [A, B](using inner: HalfConverter[A, B]): HalfConverter[Option[A], B] = 
    new HalfConverter:
      def convert(x: Option[A]) = x.flatMap(inner.convert)

  extension [A, B] (converter: HalfConverter[A, B])

    def toStringConverter(using ev: B =:= String)(using Null <:< A): javafx.util.StringConverter[A] = 
      new javafx.util.StringConverter:
        def fromString(x: String) = ???
        def toString(x: A) = if x != null then converter.convert(x).map(ev).getOrElse("") else null

end HalfConverter


trait Converter[A, B] extends HalfConverter[A, B]:
  def deconvert(x: B): Option[A]

object Converter extends Converters:

  def apply[A, B](using converter: Converter[A, B]): Converter[A, B] = converter

  def apply[A, B](f: A => B, g: B => A): Converter[A, B] = 
    new Converter:
      def convert(x: A) = Some(f(x))
      def deconvert(x: B) = Some(g(x))

  given from[A, B](using f: A => B, g: B => A): Converter[A, B] = apply(f, g)

  given option[A, B](using inner: Converter[A, B]): Converter[Option[A], B] = 
    new Converter:
      def convert(x: Option[A]) = x.flatMap(inner.convert)
      def deconvert(x: B) = Some(inner.deconvert(x))

  extension [A, B] (converter: Converter[A, B])

    def inverse: Converter[B, A] = 
      new Converter:
        def convert(x: B) = converter.deconvert(x)
        def deconvert(x: A) = converter.convert(x)

    def andThen[C](that: Converter[B, C]): Converter[A, C] = 
      new Converter:
        def convert(x: A) = converter.convert(x).flatMap(that.convert)
        def deconvert(x: C) = that.deconvert(x).flatMap(converter.deconvert)

    def toStringConverter(using ev: B =:= String)(using Null <:< A): javafx.util.StringConverter[A] = 
      new javafx.util.StringConverter:
        def fromString(x: String) = converter.deconvert(ev.flip(x)).orNull
        def toString(x: A) = if x != null then converter.convert(x).map(ev).getOrElse("") else null

end Converter


// for backwards comp.

trait RightConverter[A, B] {
  def convert(a: A): B
  def deconvert(b: B): Option[A]
}

object RightConverter {

  def apply[A, B](convert0: A => B, deconvert0: B => Option[A]) =
    new RightConverter[A, B] {
      def convert(a: A) = convert0(a)
      def deconvert(b: B) = deconvert0(b)
    }
  
  implicit val intConverter: IntConverter.type = IntConverter
  implicit def defaultConverter[A]: DefaultConverter[A] = DefaultConverter[A]()
}
