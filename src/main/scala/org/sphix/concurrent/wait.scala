package org.sphix.concurrent

import scala.util.*
import scala.concurrent.*

import javafx.application.*
import javafx.stage.*

@deprecated("use FutureModal instead")
class WaitModal[A]
  (future: Future[A])
  (using waitPopup: WaitPopup)
  (using window: Window)
  (using ExecutionContext):

  def apply[U](f: Try[A] => U): Unit = 

    waitPopup.popup.show(window)

    future.onComplete { x =>
      Platform.runLater { () =>
        waitPopup.popup.hide() 
        f(x)
      }
    }

  def ifSuccess[U](f: A => U): Unit = 
    apply {
      case Success(value) => f(value)
      case Failure(ex) => throw ex
    }

  def ifFailure[U](f: Throwable => U): Unit = 
    apply {
      case Success(_) => 
      case Failure(ex) => f(ex)
    }

@deprecated("use FutureModal instead")
object WaitModal:

  //  factories for backwards comp.

  def apply[A, U](future: Future[A])(f: A => U)(using WaitPopup, ExecutionContext, Window) =
    new WaitModal(future).ifSuccess(f)

  def total[A, U](future: Future[A])(f: Try[A] => U)(using WaitPopup, ExecutionContext, Window) =
    new WaitModal(future)(f)