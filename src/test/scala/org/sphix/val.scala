package org.sphix

import org.scalatest.funsuite._
import org.scalatest.matchers.should._

class ValTest extends AnyFunSuite with Matchers:

  test("constant val") {
    val x = Val(1)
    x() shouldBe 1   
  }

  test("to val") {
    val x = 1.toVal
    x() shouldBe 1
  }

  test("for syntax") {
    val a = Var(1)
    val b = Var(10)
    val c = Var(a)
    val z = for 
      x <- c
      y <- x
    yield y + 1
    z() shouldBe 2
    a() = 2
    z() shouldBe 3
    c() = b
    z() shouldBe 11
    b() = 20
    z() shouldBe 21
  }

  test("tupled") {
    val a = Var(1)
    val b = Var("foo") 
    val c = (a, b).tupled
    c() shouldBe (1, "foo")
    a() = 2
    b() = "bar"
    c() shouldBe (2, "bar")
  }

  test("mapN") {
    val a = Var(1)
    val b = Var(10)
    val y = (a, b).mapN(_ + _)
    y() shouldBe 11
    a() = 2
    b() = 20
    y() shouldBe 22
  }

  test("flatMapN") {
    val a = Var(1)
    val b = Var(10)
    val c = Var(a)
    val d = Var(b)
    //  TODO type inference issue
    // val z = (c, d).flatMapN((x, y) => (x, y).map(_ + _))
    // z() shouldBe 11
    // a() = 2
    // b() = 11
    // z() shouldBe 13
  }  