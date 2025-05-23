package org.sphix

import javafx.beans.*
import javafx.beans.value.*

trait Observer:
  def dispose(): Unit

abstract class InvalidationObserverBase(observables: Seq[Observable])
  extends InvalidationListener with Observer:
    def dispose() = observables foreach { _ removeListener this }

class InvalidationObserver[U](observables: Seq[Observable], f: Observable => U)
  extends InvalidationObserverBase(observables):
    def invalidated(o: Observable) = f(o)

abstract class ChangeObserverLike[A](observableValues: Seq[ObservableValue[A]])
  extends ChangeListener[A] with Observer:
    def dispose() = observableValues foreach { _ removeListener this }

class ChangeObserver[A, U](observableValues: Seq[ObservableValue[A]], f: (ObservableValue[_ <: A], A, A) => U)
  extends ChangeObserverLike(observableValues):
    def changed(ov: ObservableValue[_ <: A], oldValue: A, newValue: A) = f(ov, oldValue, newValue)