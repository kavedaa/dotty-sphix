package org.sphix.collection.mutable

import scala.jdk.CollectionConverters.*
import scala.collection.SeqFactory
import scala.collection.mutable.{ Buffer, Builder }

import javafx.util.Callback
import javafx.beans.Observable
import javafx.collections.*


class ObservableBuffer[A](protected val observableList: ObservableList[A])
  extends org.sphix.collection.ObservableSeqImpl[A]
  with Buffer[A]
  with scala.collection.mutable.SeqOps[A, ObservableBuffer, ObservableBuffer[A]]:

  override val iterableFactory = ObservableBuffer
  override protected def fromSpecific(coll: IterableOnce[A]) = iterableFactory.from(coll)
  override protected def newSpecificBuilder = iterableFactory.newBuilder
  override def empty = iterableFactory.empty[A]

  //  Buffer implementations

  def addOne(elem: A) =
    observableList.add(elem)
    this

  def clear() = 
    observableList.clear()

  def insert(i: Int, elem: A) =  
    observableList.add(i, elem)

  def insertAll(n: Int, elems: IterableOnce[A]) =
    observableList.addAll(n, elems.iterator.to(Iterable).asJavaCollection)

  def patchInPlace(from: Int, patch: IterableOnce[A], replaced: Int) = 
    val sub = observableList.subList(from, from + replaced)
    sub.clear()
    sub.addAll(patch.iterator.to(Iterable).asJavaCollection)
    this

  def prepend(elem: A) = 
    observableList.add(0, elem)
    this

  def remove(n: Int) = 
    observableList.remove(n)

  def remove(n: Int, count: Int) =
    observableList.subList(n, n + count).clear()

  def update(n: Int, elem: A) = 
    observableList.set(n, elem)

  def update(elems: IterableOnce[A]) = 
    observableList.setAll(elems.iterator.to(Iterable).asJavaCollection)
  
  //	Overrides to avoid element-for-element changes from default implementation

  override def addAll(xs: IterableOnce[A]) = 
    observableList.addAll(xs.iterator.to(Iterable).asJavaCollection)
    this

  override def prependAll(xs: IterableOnce[A]) = 
    observableList.addAll(0, xs.iterator.to(Iterable).asJavaCollection)
    this

  override def subtractAll(xs: IterableOnce[A]) = 
    observableList.removeAll(xs.iterator.to(Iterable).asJavaCollection)
    this

  //  Additional convenience methods

  /**
    *  Remove based on reference equality.
    */
  def removeRef(x: AnyRef) =
    val index = indexWhere(_.asInstanceOf[AnyRef] eq x)
    if index != -1 then remove(index)

  def toObservableList = observableList

object ObservableBuffer extends SeqFactory[ObservableBuffer]:

  def empty[A]: ObservableBuffer[A] = new ObservableBuffer(javaList[A])
  
  def from[A](source: IterableOnce[A]): ObservableBuffer[A] = 
    val list = javaList[A]
    source.iterator.asJava forEachRemaining { x => list.addAll(x) }
    new ObservableBuffer(list)

  def apply[A](elementChange: A => Observable): ObservableBuffer[A] = 
    //	Not sure why JFX use array of Observable
    val callback = new Callback[A, Array[Observable]]:
      def call(a: A) = Array(elementChange(a))
    new ObservableBuffer[A](FXCollections.observableArrayList[A](callback))

  private def javaList[A] = javafx.collections.FXCollections.observableArrayList[A]

  def newBuilder[A] = new Builder[A, ObservableBuffer[A]]:

    private val list = javaList[A]

    def addOne(elem: A) =
      list.add(elem)
      this

    def clear() = 
      list.clear()

    def result = new ObservableBuffer(list)

  end newBuilder

  //  special factory method

  def observeElements[A](xs: A*)(elementChange: A => javafx.beans.Observable): ObservableBuffer[A] = 
    val list = FXCollections.observableArrayList[A](x => Array(elementChange(x)))
    list.addAll(xs*)
    new ObservableBuffer[A](list)

  //  conversions

  implicit def fromObservableList[A](ol: ObservableList[A]): ObservableBuffer[A] = new ObservableBuffer(ol)
