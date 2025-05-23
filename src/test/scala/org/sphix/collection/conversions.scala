package org.sphix.collection

import javafx.collections.*

import org.sphix.*

import org.scalatest.funsuite._
import org.scalatest.matchers.should._

class ConversionsTest extends AnyFunSuite with Matchers:

  //  don't test behaviour, only that the conversions compile

  test("ObservableSeq to JFX ObservableList - implicit conversion") {
    val source = ObservableSeq(1, 2, 3)
    val dest: ObservableList[Int] = source
    val seqfunc = SeqFunc(Val(1), Val(2)) { (x, y) => Seq(x() + y()) }
    val foo: ObservableList[Int] = seqfunc
  }

  test("JFX ObservableList to ObservableSeq - implicit conversion") {
    val source = FXCollections.observableArrayList(1, 2, 3)
    val dest: ObservableSeq[Int] = source
  }

  test("Iterable to ObservableSeq - implicit conversion") {
    val source = Seq(1, 2, 3)
    val dest: ObservableSeq[Int] = source
  }

  test("ObservableSeq to List - extension method") {
    val source = ObservableSeq(1, 2, 3)
    val dest: List[Int] = source.toList
  }

  test("ObservableSeq to List - implicit conversion") {
    val source = ObservableSeq(1, 2, 3)
    val dest: List[Int] = source
  }

  test("Iterable to JFX ObservableList - extension method") {
    val source = Seq(1, 2, 3)
    val dest: ObservableList[Int] = source.toObservableList
  }

  // test("Iterable to JFX ObservableList - implicit conversion") {
  //   val source = Seq(1, 2, 3)
  //   val dest: ObservableList[Int] = source
  // }

  test("JFX ObservableList to List - extension method") {
    val source = FXCollections.observableArrayList(1, 2, 3)
    val dest: List[Int] = source.toList
  }

  test("JFX ObservableList to List - implicit conversion") {
    val source = FXCollections.observableArrayList(1, 2, 3)
    val dest: List[Int] = source
  }

