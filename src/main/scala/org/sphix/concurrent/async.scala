package org.sphix.concurrent

import scala.util.*
import scala.concurrent.*

import javafx.application.Platform

import org.sphix.*

class Async[A, B](source: Val[A])(f: A => B)(using ExecutionContext):

  private val status0 = Var[Async.Status[B]](Async.Status.Init)

  source.onValue: x =>
    status0() = Async.Status.Running
    Future(f(x)).onComplete: result =>
      Platform.runLater: () =>
        status0() = result match
          case Success(x) => Async.Status.Success(x)
          case Failure(ex) => Async.Status.Failure(ex)

  val status: Val[Async.Status[B]] = status0

object Async:

  enum Status[+A]:
    case Init extends Status[Nothing]
    case Running extends Status[Nothing]
    case Success(result: A) extends Status[A]
    case Failure(exception: Throwable) extends Status[Nothing]