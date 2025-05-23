package org.sphix.util

trait BidirectionalFunction[A, B]:
  def right(x: A): B
  def left(x: B): A

object BidirectionalFunction:

  def apply[A, B](f: A => B, g: B => A): BidirectionalFunction[A, B] = 
    new BidirectionalFunction:
      def right(x: A) = f(x)
      def left(x: B) = g(x)

  given [A, B](using f: A => B, g: B => A): BidirectionalFunction[A, B] = BidirectionalFunction(f, g)

  def apply[A, B](using bidir: BidirectionalFunction[A, B]): BidirectionalFunction[A, B] = bidir

  extension [A, B] (bidir: BidirectionalFunction[A, B])

    def toConverter: Converter[A, B] = new Converter:
      def convert(x: A) = Some(bidir.right(x))
      def deconvert(x: B) = Some(bidir.left(x))

    def inverse: BidirectionalFunction[B, A] = new BidirectionalFunction:
      def right(x: B) = bidir.left(x)
      def left(x: A) = bidir.right(x)

    def andThen[C](that: BidirectionalFunction[B, C]): BidirectionalFunction[A, C] = new BidirectionalFunction:
      def right(x: A) = that.right(bidir.right(x))
      def left(x: C) = bidir.left(that.left(x))
