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
          case Success(x) => Async.Status.Succeeded(x)
          case Failure(ex) => Async.Status.Failed(ex)

  val status: Val[Async.Status[B]] = status0

  val value: Val[Option[B]] = status.map:
    case Async.Status.Succeeded(result) => Some(result)
    case _ => None

object Async:

  enum Status[+A]:
    case Init extends Status[Nothing]
    case Running extends Status[Nothing]
    case Succeeded(result: A) extends Status[A]
    case Failed(exception: Throwable) extends Status[Nothing]