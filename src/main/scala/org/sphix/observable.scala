package org.sphix

import javafx.beans._
import javafx.beans.binding._

extension (observable: Observable)

  def onInvalidation[U](f: Observable => U): Unit =
    observable addListener new InvalidationListener:
      def invalidated(x: Observable) = f(x)

  def observe[U](f: => U): Unit = observable.onInvalidation(_ => f)

class Observables(observables: Iterable[Observable]) extends Observable:

  def addListener(listener: InvalidationListener): Unit = 
    observables.foreach(_.addListener(listener))

  def removeListener(listener: InvalidationListener): Unit = 
    observables.foreach(_.removeListener(listener))

extension (observables: Iterable[Observable])

  def toObservables = Observables(observables)

  def onInvalidation[U](f: Observable => U): Unit =
    toObservables.onInvalidation(f)

  def observe[U](f: => U): Unit = onInvalidation(_ => f)

extension [Tup <: Tuple](tup: Tup)(using Tuple.Union[Tup] <:< Observable)  

  def toObservables = Observables(tup.toList.asInstanceOf[List[Observable]])

  def onInvalidationN[U](f: Observable => U): Unit =
    toObservables.onInvalidation(f)
   
  def observeN[U](f: => U): Unit = onInvalidationN(_ => f)

  def applyN[B](f: Tup => B): Val[B] =
    val observables = tup.toList.asInstanceOf[List[Observable]]
    new ObjectBinding:
      bind(observables*)
      def computeValue() = 
        f(tup)


