package org.sphix

import javafx.beans.Observable
import javafx.beans.binding.ObjectBinding

abstract class Filter[A](val observables: Observable*)
  extends ObjectBinding[A => Boolean]: 
  self =>

  bind(observables*)

  def predicate(x: A): Boolean

  def computeValue() = (x: A) => predicate(x)

  def andThen(that: Filter[A]) =
    new Filter[A]((self.observables ++ that.observables)*):
      def predicate(x: A): Boolean = self.predicate(x) && that.predicate(x)