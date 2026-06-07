package org.sphix.ui

import scala.language.implicitConversions
import scala.util.*

import javafx.scene.Node
import javafx.scene.image.*
import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.geometry.Pos

import org.controlsfx.control.Notifications

import no.vedaadata.text.*

import org.sphix.*
import org.sphix.util.given
import org.sphix.ui.dialog.*
import org.sphix.ui.crud.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq

import org.sphix.ui.responding.*

trait RespondingTexts:
  def Item: String
  def Result: String
  def NumOperationsSucceeded(x: Int): String
  def NumOperationsFailed(x: Int): String
  def NoResults: String

trait RespondingIcons:
  def IsSuccess: Option[Image]
  def IsFailure: Option[Image]

enum Message:
  case None
  case As(s: String)
  case With(f: String => String)

object Message:
  extension (message: Message) def transformRender[A](inner: Render[A]): Render[A] =
    message match
      case None => 
        inner
      case As(s) => 
        new Render:
          def apply(x: A) = Some(s)
      case With(f) =>
        inner.andThen(f)    
  

trait Responder[-A]:
  def respond(x: A)(message: Message): Unit

object Responder:

  import Responding.*

  //  default responders

  given unitResponder: Responder[Unit] = UnitResponder
  given defaultResponder[A](using Render[A]): Responder[A] = new DefaultResponder
  given optionResponder[A](using Responder[A]): Responder[Option[A]] = new OptionResponder
  given throwableResponder: Responder[Throwable] = new ThrowableResponder
  given tryResponder[A](using Responder[A])(using throwableResponder: Responder[Throwable]): Responder[Try[A]] = new TryResponder
  given iterableResponder[A](using Render[A]): Responder[Iterable[A]] = new IterableResponder
  given tryIterableResponder[A](using Render[A])(using RespondingTexts, RespondingIcons): Responder[Iterable[Try[A]]] = new TryIterableResponder
  given itemTryIterableResponder[A, B](using Render[A], Render[B])(using RespondingTexts, RespondingIcons): Responder[Iterable[(A, Try[B])]] = new ItemTryIterableResponder

trait Responding:

  def respond[A](x: A)(using resp: Responder[A]) = resp.respond(x)(Message.None)
  def respondAs[A](s: String)(x: A)(using resp: Responder[A]) = resp.respond(x)(Message.As(s))
  def respondWith[A](f: String => String)(x: A)(using resp: Responder[A]) = resp.respond(x)(Message.With(f))

  def notification(msg: String) = 
    Notifications.create.text(msg).showInformation()

  def error(title: Option[String], header: Option[String], ex: Throwable): Unit = 
    val actual = Option(ex.getCause).getOrElse(ex)
    new ExceptionDialog(title, header, actual).showAndWait()

  def error(ex: Throwable): Unit = 
    error(None, None, ex)

object Responding extends Responding

object UnitResponder extends Responder[Unit]:
  def respond(x: Unit)(message: Message) = {}

class DefaultResponder[A](using render: Render[A]) extends Responder[A]:
  def respond(x: A)(message: Message) = 
    val text = message.transformRender(render)(x)
    Responding.notification(text.getOrElse(""))

class OptionResponder[A](using inner: Responder[A]) extends Responder[Option[A]]:
  def respond(x: Option[A])(message: Message) = 
    x.foreach(value => inner.respond(value)(message))

class ThrowableResponder extends Responder[Throwable]:
  def respond(ex: Throwable)(message: Message) = 
    Responding.error(ex)

class TryResponder[A](using inner: Responder[A])(using throwableResponder: Responder[Throwable]) extends Responder[Try[A]]:
  def respond(x: Try[A])(message: Message) = 
    x match
      case Success(value) => inner.respond(value)(message)
      case Failure(ex) => throwableResponder.respond(ex)(message)

class IterableResponder[A](using render: Render[A]) extends Responder[Iterable[A]]:
  def respond(x: Iterable[A])(message: Message) = 
    val listView = new ListView[A] with ListUtils[A]:
      val os = x.to(ObservableSeq)
      setItems(os)
      setCell:
        new TextCell(x => message.transformRender(render)(x)) {}
    new RespondingMonolog(listView, None).showAndWait()



class RespondingMonolog(node: Node, title: Option[String]) extends Dialog[Nothing]:

  title.foreach(setTitle)
  val content = VBox(node)
  VBox.setVgrow(node, Priority.ALWAYS)
  getDialogPane.setContent(content)
  getDialogPane.getButtonTypes.add(ButtonType.CLOSE)
  setResizable(true)

  //  this is a bit too hard-coded but can not find any way to make it automatically adapt to content width
  getDialogPane.setPrefWidth(500)



