package org.sphix.collection.transformation

import scala.jdk.FunctionConverters.*

import java.util.function.Predicate

import javafx.beans.value.ObservableValue
import javafx.collections.transformation.FilteredList

import org.sphix._
import org.sphix.collection.*

class FilteredSeq[A](source: ObservableSeq[A], predicate: Val[A => Boolean])
  extends ObservableSeqImpl[A] {

  def this(source: ObservableSeq[A], predicate: A => Boolean) =
    this(source, Val(predicate))

  protected val observableList: FilteredList[A] = new FilteredList(source.toObservableList)
  
  val javaPredicate: Val[Predicate[A]] = predicate.map(_.asJava)

  observableList.predicateProperty.bind(javaPredicate)
  
  def toObservableList = observableList
}
