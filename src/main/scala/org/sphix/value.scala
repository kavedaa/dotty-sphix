package org.sphix

import scala.util.*

trait InvalidReason(val defaultText: String)

object InvalidReason:
  def apply(text: String): InvalidReason = new InvalidReason(text) {}
  
enum Status:
  case Valid
  case Invalid(reasons: Seq[InvalidReason])
  case Empty


object Status:

  def fromBoolean(x: Boolean): Status =
    if x then Status.Valid else Status.Empty

  def sequence(xs: Iterable[Status]): Status =
    if xs.forall(_ == Status.Valid) then Status.Valid
    else
      val invalids = xs collect { case invalid: Status.Invalid => invalid }
      if invalids.nonEmpty then Status.Invalid(invalids.toList.flatMap(_.reasons))
      else Status.Empty

  extension (status: Status)

    def toBoolean: Boolean =
      status match
        case Status.Valid => true
        case _ => false

enum Value[+A]:
  case Valid(x: A) extends Value[A]
  case Invalid(reasons: Seq[InvalidReason]) extends Value[Nothing]
  case Empty extends Value[Nothing]

object Value:

  def sequence[A](xs: Iterable[Value[?]], y: => A): Value[A] =
    if xs.forall(_.valid) then
      Value.Valid(y)
    else
      val invalids = xs collect { case invalid: Value.Invalid => invalid }
      if invalids.nonEmpty then Value.Invalid(invalids.toList.flatMap(_.reasons))
      else Value.Empty


  extension [A] (value: Value[A]) 
    
    def get: A = 
      value match
        case Value.Valid(x) => x
        case _ => throw new NoSuchElementException

    def status: Status =
      value match
        case _: Value.Valid[?] => Status.Valid
        case Value.Invalid(reasons) => Status.Invalid(reasons)
        case Value.Empty => Status.Empty

    def valid: Boolean = 
      value match
        case _: Value.Valid[?] => true
        case _ => false

    def map[B](f: A => B): Value[B] =
      value match 
        case Value.Valid(x) => Value.Valid(f(x))
        case invalid: Value.Invalid => invalid
        case Value.Empty => Value.Empty

    def filter(p: A => Boolean) =
      value match
        case Value.Valid(x) => if p(x) then value else Value.Empty
        case _ => value

    def toOption: Option[A] = 
      value match
        case Value.Valid(x) => Some(x)
        case _ => None

    def liftOption: Value[Option[A]] = 
      value match
        case Value.Valid(x) => Value.Valid(Some(x))
        case invalid: Value.Invalid => invalid
        case Value.Empty => Value.Valid(None)

    def isEmpty: Boolean = 
      value match
        case Value.Empty => true
        case _ => false

  def fromNullable[A](x: A): Value[A] =
    if x == null then Value.Empty else Value.Valid(x)

  def fromOption[A](x: Option[A]) = 
    x match
      case Some(v) => Value.Valid(v)
      case None => Value.Empty

  def fromTry[A](x: Try[A]) = 
    x match
      case Success(v) => Value.Valid(v)
      case Failure(ex) => Value.Invalid(Seq(new InvalidReason(ex.getMessage) {}))

