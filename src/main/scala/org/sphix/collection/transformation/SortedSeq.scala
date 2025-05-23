package org.sphix.collection.transformation

import javafx.beans.value.ObservableValue
import javafx.collections.transformation.SortedList

import org.sphix.collection.*

class SortedSeq[A](source: ObservableSeq[A])
  extends ObservableSeqImpl[A] {

  protected val observableList: SortedList[A] = new SortedList(source.toObservableList)

  def setComparator(comparator: ObservableValue[java.util.Comparator[A]]) = {
    observableList.comparatorProperty.unbind()
    observableList.comparatorProperty bind comparator
  }

  def toObservableList = observableList
}