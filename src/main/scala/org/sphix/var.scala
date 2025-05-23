package org.sphix

import javafx.beans.property.*
import javafx.beans.value.*

import org.sphix.util.*
import org.sphix.binding.*

type Var[A] = Property[A]

object Var:
  def apply[A](x: A): Var[A] = new SimpleObjectProperty(x)

extension [A] (v: Property[A])
  
  def update(x: A) = v.setValue(x)

  def <==(that: ObservableValue[A]) = v.bind(that)

  def <==>[B](that: Property[B])(using bidir: BidirectionalFunction[A, B]) =
    bindBidirectional(v, that, bidir)

  def <=~=>[B](that: Property[B])(using converter: Converter[A, B]) =
    bindBidirectionalWithConverter(v, that)