package org.sphix.collection

import org.scalatest.funsuite._
import org.scalatest.matchers.should._

import org.sphix.*
import org.sphix.collection.mutable.ObservableBuffer

class ObservableSeqTest extends AnyFunSuite with Matchers:

  test("apply") {
    val buf = ObservableBuffer("a", "b", "c")
    val size = buf(_.size)
    size() shouldEqual 3
    buf += "d"
    size() shouldEqual 4
  }

  test("applyN") {
    val buf1 = ObservableBuffer("a", "b", "c")
    val buf2 = ObservableBuffer("c", "d")
    val out = (buf1, buf2).applyN { _.endsWith(_) }
    out() shouldEqual false
    buf1 += "d"
    out() shouldEqual true
  }
