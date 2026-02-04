package org.sphix

import scala.jdk.CollectionConverters.*
import scala.jdk.FunctionConverters.*
import scala.concurrent.duration.*

import java.util.concurrent.TimeUnit

import javafx.beans.*
import javafx.beans.value.*
import javafx.beans.binding.*
import javafx.collections.*

import org.sphix.impl.DelayedVal
import org.sphix.impl.StrongBinding

type Val[A] = ObservableValue[A]

private class ConstVal[A](value: A) extends ObservableValue[A]:
  def getValue() = value
  def addListener(listener: InvalidationListener) = {}
  def removeListener(listener: InvalidationListener) = {}
  def addListener(listener: ChangeListener[? >: A]) = {}
  def removeListener(listener: ChangeListener[? >: A]) = {}

object Val:

  def apply[A](x: A): Val[A] = 
    new ConstVal(x)

  opaque type DelayTime >: Duration = Duration
  extension (d: DelayTime) def toDuration: Duration = d

  given DelayTime = 250.millis

extension [A] (x: A) def toVal: Val[A] = Val(x)

extension [A] (x: Val[A])

  def apply() = x.getValue

  def onChange[U](f: (Val[? <: A], A, A) => U): Observer =
    val observer = new ChangeObserver[A, U](Seq(x), f)
    x.addListener(observer)
    observer

  def onValue[U](f: A => U): Observer = onChange { (ov, oldValue, newValue) => f(newValue) }

  def on[U](value: A)(f: => U): Observer = onValue { v => if v == value then f }

  def mapOption: Val[Option[A]] = 
    new StrongBinding[Option[A]](List(x)):
      def computeValue() = Option(x.getValue)

  def flatten[B](implicit ev: A <:< Val[B]): Val[B] = x.flatMap(identity)

  // def flatMap[B](f: A => Val[B]): Val[B] = 
  //   new ObjectBinding:
  //     var inner = f(v.getValue)
  //     val innerListener: InvalidationListener = { _ =>
  //       invalidate()
  //     }
  //     val outerListener: InvalidationListener = { _ =>
  //       inner.removeListener(innerListener)
  //       inner = f(v.getValue)
  //       inner.addListener(innerListener)
  //       invalidate()
  //     }
  //     def computeValue() = inner.getValue
  //     inner.addListener(innerListener)      
  //     v.addListener(outerListener)

  def as[B](using f: A => B): Val[B] = x.map(f.asJava)

  /**
    *   Delays propagation of the observable value for the given duration.
    */
  def delayed(using delayTime: Val.DelayTime): Val[A] = 
    new DelayedVal(x, delayTime.toDuration)

extension [Tup <: Tuple](tup: Tup)(using Tuple.Union[Tup] <:< Val[?])

  // def tupled: Val[Tuple.InverseMap[Tup, Val]] =
  //   val ovs = tup.toList.asInstanceOf[List[Val[Any]]]
  //   val compute = () =>
  //     val vs = ovs.map(_.getValue)
  //       vs.foldRight[Tuple](EmptyTuple)(_ *: _).asInstanceOf[Tuple.InverseMap[Tup, Val]]
  //   Bindings.createObjectBinding()
  //     bind(ovs*)
  //     def computeValue() = 

  def tupled: Val[Tuple.InverseMap[Tup, Val]] =
    val ovs = tup.toList.asInstanceOf[List[Val[Any]]]
    new StrongBinding[Tuple.InverseMap[Tup, Val]](ovs):
      def computeValue() = 
        val vs = ovs.map(_.getValue)
        vs.foldRight[Tuple](EmptyTuple)(_ *: _).asInstanceOf[Tuple.InverseMap[Tup, Val]]

  def mapN[B](f: Tuple.InverseMap[Tup, Val] => B): Val[B] =
    tupled.map(f.asJava)

  // def mapN[B](f: Tuple.InverseMap[Tup, Val] => B): Val[B] =
  //   val ovs = tup.toList.asInstanceOf[List[Val[Any]]]
  //   new ObjectBinding:
  //     bind(ovs*)
  //     def computeValue() = 
  //       val vs = ovs.map(_.getValue)
  //       val vtup = vs.foldRight[Tuple](EmptyTuple)(_ *: _).asInstanceOf[Tuple.InverseMap[Tup, Val]]
  //       f(vtup)

  def flatMapN[B](f: Tuple.InverseMap[Tup, Val] => Val[B]): Val[B] =
    tupled.flatMap(f.asJava)
//    mapN(f).flatMap(identity)

  def onValueN[U](f: Tuple.InverseMap[Tup, Val] => U): Unit =
    tupled.onValue(f)

extension [A] (xs: Iterable[Val[A]]) 

  def mapSeq[B](f: Iterable[A] => B): Val[B] =
    new StrongBinding[B](xs):
      def computeValue() = f(xs.map(_.getValue))

extension [A] (ol: ObservableList[A]) 

  def asVal: Val[List[A]] =
    new StrongBinding[List[A]](List(ol)):
      val buffer = ol.asScala
      def computeValue() = buffer.toList