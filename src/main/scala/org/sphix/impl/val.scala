package org.sphix.impl

import scala.collection.mutable.ListBuffer

import javafx.beans.InvalidationListener
import javafx.beans.value.ChangeListener

import org.sphix.*

//  custom ObservableValue implementation, for when we need to control the firing

trait ObservableImpl:

  protected lazy val invalidationListeners = ListBuffer[InvalidationListener]()

  def addListener(listener: InvalidationListener): Unit =
    invalidationListeners += listener

  def removeListener(listener: InvalidationListener): Unit =
    invalidationListeners -= listener


trait ValImpl[A] extends ObservableImpl:

  protected lazy val changeListeners = ListBuffer[ChangeListener[? >: A]]()

  def addListener(listener: ChangeListener[? >: A]): Unit =
    changeListeners += listener

  def removeListener(listener: ChangeListener[? >: A]): Unit =
    changeListeners -= listener


trait FirableVal[A] extends Val[A] with ValImpl[A]:

  protected def currentValue: A

  def fire() =
    invalidationListeners.toSeq.foreach(_ invalidated this)
    changeListeners.toSeq.foreach(_ changed (this, currentValue, getValue))


trait LazyVal[A] extends FirableVal[A]:

  protected def compute: A      

  protected var value: A = _

  protected var valid = false

  protected def currentValue = value
  
  protected def invalidate(o: javafx.beans.Observable) =
    valid = false
    fire()

  protected lazy val lazyListener = new InvalidationListener:
    def invalidated(o: javafx.beans.Observable) = 
      invalidate(o)

  def getValue() = if (valid) then value else
    value = compute
    valid = true
    value


