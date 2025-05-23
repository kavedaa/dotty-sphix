package org.sphix.collection

import scala.jdk.CollectionConverters._
import scala.collection.SeqFactory
import scala.collection.mutable.{ Builder, ArrayBuffer }

import javafx.beans.InvalidationListener
import javafx.beans.binding.ObjectBinding

import javafx.collections.*

import org.sphix.*
import org.sphix.collection.transformation.*

trait ObservableSeq[A]
  extends scala.collection.Seq[A] 
  with javafx.beans.Observable:
  os =>

  //	Backing list

  protected def observableList: ObservableList[A]

  //	Seq implementations

  def apply(n: Int) = observableList.get(n)

  def iterator = observableList.iterator.asScala

  def length = observableList.size

  //	ObservableSeq stuff

  def onChange[U](f: Seq[Change[A]] => U) =
    val listener = new ListChangeListener[A]:
      def onChanged(change: javafx.collections.ListChangeListener.Change[_ <: A]) =
        val seqChanges = new ArrayBuffer[Change[A]]()
        while change.next() do
          if change.wasPermutated then
            seqChanges += Change.Permutated(change.getFrom, change.getTo, change.getPermutation)
          if change.wasRemoved then
            seqChanges += Change.Removed(change.getFrom, change.getRemoved.asScala)
          if change.wasAdded then
            seqChanges += Change.Added(change.getFrom, change.getAddedSubList.asScala)
          if change.wasUpdated then
            seqChanges += Change.Updated(change.getFrom, change.getTo)
        f(seqChanges.toSeq)
    observableList.addListener(listener)
    new ListChangeObserver(Seq(observableList), listener)

  def onAdded[U](f: Iterable[A] => U) = onChange { changes =>
    changes collect { case c: Change.Added[_] => c } foreach (c => f(c.added))
  }

  def onRemoved[U](f: Iterable[A] => U) = onChange { changes =>
    changes collect { case c: Change.Removed[_] => c } foreach (c => f(c.removed))
  }

  //  function to Val

  def apply[B](f: ObservableSeq[A] => B): Val[B] =
    new ObjectBinding[B]:
      bind(os)
      def computeValue() = f(os)

  //	Transformations

  def filtered(f: A => Boolean) = new FilteredSeq(this, f)
  def filtered(f: Val[A => Boolean]) = new FilteredSeq(this, f)

  //	Conversion

  def toObservableList: javafx.collections.ObservableList[A]


trait ObservableSeqImpl[A]
  extends ObservableSeq[A] 
  with scala.collection.SeqOps[A, ObservableSeq, ObservableSeq[A]]:

  override val iterableFactory: SeqFactory[ObservableSeq] = ObservableSeq
  override protected def fromSpecific(coll: IterableOnce[A]) = iterableFactory.from(coll)
  override protected def newSpecificBuilder: Builder[A, ObservableSeq[A]] = iterableFactory.newBuilder
  override def empty = iterableFactory.empty[A]

  //	Observable implementations

  def addListener(listener: InvalidationListener) =
    observableList.addListener(listener)

  def removeListener(listener: InvalidationListener) =
    observableList.removeListener(listener)


object ObservableSeq extends SeqFactory[ObservableSeq] {

  //  SeqFactory implementations

  def empty[A]: ObservableSeq[A] = org.sphix.collection.immutable.ObservableSeq.empty[A]

  def from[A](source: IterableOnce[A]): ObservableSeq[A] = org.sphix.collection.immutable.ObservableSeq.from(source)

  def newBuilder[A] = org.sphix.collection.immutable.ObservableSeq.newBuilder[A]

  //  special factory method

  def observeElements[A](xs: A*)(elementChange: A => javafx.beans.Observable): ObservableSeq[A] = 
    val list = FXCollections.observableArrayList[A](x => Array(elementChange(x)))
    list.addAll(xs*)
    new immutable.ObservableSeq[A](list)

  //  conversions

  def fromVal[A](v: Val[List[A]]) = 
    val list = FXCollections.observableArrayList[A](v.getValue.asJavaCollection)
    v.onValue(xs => list.setAll(xs.asJavaCollection))
    new immutable.ObservableSeq[A](list)

  //  collection conversions

  given [A]: Conversion[ObservableSeq[A], ObservableList[A]] =
    _.toObservableList

  def fromObservableList[A](ol: ObservableList[A]): ObservableSeq[A] = 
    org.sphix.collection.immutable.ObservableSeq.fromObservableList(ol)

  given [A]: Conversion[ObservableList[A], ObservableSeq[A]] =
    xs => fromObservableList(xs)
    
  given [A]: Conversion[Iterable[A], ObservableSeq[A]] = 
    xs => fromObservableList(FXCollections.observableList(xs.toList.asJava))

  //  scala.Seq is now the immutable Seq, therefore we need an implicit conversion
  given [A]: Conversion[ObservableSeq[A], List[A]] = 
    _.toList
}
