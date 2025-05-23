package org.sphix.control.cell

import scala.util.*

trait To[-A, +B] extends (A => B):
  def apply(x: A): B

object To:

  given identity[A]: To[A, A] with
    def apply(x: A) = x

  given conversion[A, B](using f: A => B): To[A, B] with
    def apply(x: A) = f(x)


trait AsOption[-A, +B] extends (A => Option[B]):
  def apply(x: A): Option[B]

object AsOption:
  given identity[A]: AsOption[A, A] = Some(_)
  given option[A, B](using outer: AsOption[A, B]): AsOption[Option[A], B] = _.flatMap(outer)
  given `try`[A, B](using outer: AsOption[A, B]): AsOption[Try[A], B] = _.toOption.flatMap(outer)  

  