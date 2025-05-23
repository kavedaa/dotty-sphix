package org.sphix.ui

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

trait Responder[-A]:
  def respond(x: A)(message: String => String): Unit
//  def respondVerbose(x: A)(using message: String => String) = respond

object Responder:

  import Responding.*

  given unitResponder: Responder[Unit] = UnitResponder
  given defaultResponder[A](using Render[A]): Responder[A] = new DefaultResponder
  given optionResponder[A](using Responder[A]): Responder[Option[A]] = new OptionResponder
  given tryResponder[A](using Responder[A]): Responder[Try[A]] = new TryResponder
  given iterableResponder[A](using Render[A]): Responder[Iterable[A]] = new IterableResponder
  given tryIterableResponder[A](using Render[A])(using CrudTexts, CrudIcons): Responder[Iterable[Try[A]]] = new TryIterableResponder
  @deprecated given tryItemIterableResponder[A, B](using Render[A], Render[B]): Responder[Iterable[(Try[A], B)]] = new TryItemIterableResponder
  given itemTryIterableResponder[A, B](using Render[A], Render[B])(using CrudTexts, CrudIcons): Responder[Iterable[(A, Try[B])]] = new ItemTryIterableResponder

trait Responding:

  def respond[A](x: A)(using resp: Responder[A]) = resp.respond(x)(identity)
  def respondWith[A](message: String => String)(x: A)(using resp: Responder[A]) = resp.respond(x)(message)

  def notification(msg: String) = 
    Notifications.create.text(msg).showInformation()

  def error(title: Option[String], header: Option[String], ex: Throwable): Unit = 
    val actual = Option(ex.getCause).getOrElse(ex)
    new ExceptionDialog(title, header, actual).showAndWait()

  def error(ex: Throwable): Unit = 
    error(None, None, ex)

object Responding extends Responding

object UnitResponder extends Responder[Unit]:
  def respond(x: Unit)(message: String => String) = {}

class DefaultResponder[A](using render: Render[A]) extends Responder[A]:
  def respond(x: A)(message: String => String) = 
    Responding.notification(render(x).map(message).getOrElse(""))

class OptionResponder[A](using inner: Responder[A]) extends Responder[Option[A]]:
  def respond(x: Option[A])(usinmessage: String => String) = 
    x.foreach(inner.respond)

class TryResponder[A](using inner: Responder[A]) extends Responder[Try[A]]:
  def respond(x: Try[A])(message: String => String) = x match
    case Success(value) => inner.respond(value)(message)
    case Failure(ex) => Responding.error(ex)

class IterableResponder[A](using render: Render[A]) extends Responder[Iterable[A]]:
  def respond(x: Iterable[A])(message: String => String) = 
    val listView = new ListView[A] with ListUtils[A]:
      val os = x.to(ObservableSeq)
      setItems(os)
      setCell:
        new TextCell(x => render(x).map(message)) {}
    new RespondingMonolog(listView, None).showAndWait()


@deprecated
class TryItemIterableResponder[A, B](using renderA: Render[A], renderB: Render[B]) extends Responder[Iterable[(Try[A], B)]]:
  def respond(x: Iterable[(Try[A], B)])(message: String => String) = 
    val tableView = new TableView[(Try[A], B)] with TableUtils[(Try[A], B)]:
      val os = x.to(ObservableSeq)
      setItems(os)
      val tryColumn = new Column("", _._1.toVal):
        setCell:
          new TextCell( {
            case Success(x) => renderA(x).map(message)
            case Failure(ex) => Some(ex.getMessage)
          } ) {}
      val itemColumn = new Column("", _._2.toVal):
        setCell:
          new StringCell(using x => renderB(x)) {}
      getColumns.addAll(tryColumn, itemColumn)
    new RespondingMonolog(tableView, None).showAndWait()



class RespondingMonolog(node: Node, title: Option[String]) extends Dialog[Nothing]:

  title.foreach(setTitle)
  val content = VBox(node)
  VBox.setVgrow(node, Priority.ALWAYS)
  getDialogPane.setContent(content)
  getDialogPane.getButtonTypes.add(ButtonType.CLOSE)
  setResizable(true)

  //  this is a bit too hard-coded but can not find any way to make it automatically adapt to content width
  getDialogPane.setPrefWidth(500)



