package org.sphix

import scala.util.*
import scala.concurrent.*

import org.sphix.impl.*
import org.sphix.concurrent.fxt

case class FutureVal[A](future: Future[A])(implicit ec: ExecutionContext)
  extends FirableVal[Option[Try[A]]]:

  //  we don't really need this here, there will only ever be one invalidation
  def currentValue = None

  def getValue = future.value

  future onComplete {
    case _ => fxt { fire() }
  }
