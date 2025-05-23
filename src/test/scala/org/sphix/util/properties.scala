package org.sphix.util

import scala.util.*

import org.scalatest.funsuite._
import org.scalatest.matchers.should._

import java.util.Properties

class PropertiesTest extends AnyFunSuite with Matchers:

  test("read and write") {

    val properties = new Properties

    PropertiesWriter.writeProduct((123, "hello"), properties)

    PropertiesReader.readProduct[(Int, String)](properties) shouldEqual (Success(123), Success("hello"))
  }