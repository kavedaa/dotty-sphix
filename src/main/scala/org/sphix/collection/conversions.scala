package org.sphix.collection

import scala.jdk.CollectionConverters._

import javafx.collections.*

extension [A] (xs: Iterable[A])

  /**
    *   Scala `Iterable` to JFX `ObservableList`
    */
  def toObservableList: ObservableList[A] = FXCollections.observableList(xs.toList.asJava)

//  don't do this, it would override the conversion from ObservableSeq to ObservableList
// given [A]: Conversion[Iterable[A], ObservableList[A]] =
//   _.toObservableList


extension [A] (xs: ObservableList[A])

  /**
    *   JFX `ObservableList` to Scala `List`
    */
  def toList: List[A] = xs.asScala.toList

  //  convenience for backwards comp. consider if to keep
  def toSeq: Seq[A] = toList


//  not sure if we should allow this explicit conversion, could be confusing
given [A]: Conversion[ObservableList[A], List[A]] =
  _.toList
  

