package org.sphix.ui.dialog

import scala.language.implicitConversions

import javafx.scene.Node
import javafx.scene.control._
import javafx.scene.layout._
import javafx.scene.control.Dialog
import javafx.scene.control.ButtonBar.ButtonData

import org.sphix.collection.*
import org.sphix.collection.transformation.SortedSeq

abstract class TableDialogBase[A, B](xs: Iterable[A])(using table: TableView[A])
  extends Dialog[B]:

  def title: Option[String]
  def graphic: Option[Node]
  def headerText: Option[String]

  val sortedItems = SortedSeq(xs)

  table.setItems(sortedItems)
  sortedItems.setComparator(table.comparatorProperty)

  val vb = VBox(table)
  VBox.setVgrow(table, Priority.ALWAYS)

  title.foreach(setTitle)
  graphic.foreach(getDialogPane.setGraphic)
  headerText.foreach(setHeaderText)
  
  getDialogPane.setContent(vb)
  setResizable(true)


class TableDialog[A](xs: Iterable[A])
  (val title: Option[String], val headerText: Option[String], val graphic: Option[Node], executeText: Option[String])
  (using table: TableView[A])
  extends TableDialogBase[A, Any](xs)
  with AnyDialog(executeText):

  def this(xs: Iterable[A])(using TableView[A]) = this(xs)(None, None, None, None)
  def this(xs: Iterable[A])(title: String)(using TableView[A]) = this(xs)(Some(title), None, None, None)
  def this(xs: Iterable[A])(title: String, executeText: String)(using TableView[A]) = this(xs)(Some(title), None, None, Some(executeText))
  def this(xs: Iterable[A])(title: String, headerText: String, executeText: String)(using TableView[A]) = this(xs)(Some(title), Some(headerText), None, Some(executeText))
  def this(xs: Iterable[A])(title: String, headerText: String, graphic: Node, executeText: String)(using TableView[A]) = this(xs)(Some(title), Some(headerText), Some(graphic), Some(executeText))


class TableMonolog[A](xs: Iterable[A])
  (val title: Option[String], val headerText: Option[String], val graphic: Option[Node])
  (using table: TableView[A])
  extends TableDialogBase[A, Nothing](xs)
  with Monolog:

  def this(xs: Iterable[A])(using TableView[A]) = this(xs)(None, None, None)
  def this(xs: Iterable[A])(title: String)(using TableView[A]) = this(xs)(Some(title), None, None)
  def this(xs: Iterable[A])(title: String, headerText: String)(using TableView[A]) = this(xs)(Some(title), Some(headerText), None)
  def this(xs: Iterable[A])(title: String, headerText: String, graphic: Node)(using TableView[A]) = this(xs)(Some(title), Some(headerText), Some(graphic))


//  TODO move somewhere else

trait Monolog:
  this: Dialog[Nothing] =>

  getDialogPane.getButtonTypes.addAll(ButtonType.CLOSE)

trait AnyDialog(executeText: Option[String]):
  this: Dialog[Any] =>

  val executeButtonType = new ButtonType(executeText.getOrElse("OK"), ButtonData.OK_DONE)

  getDialogPane.getButtonTypes.addAll(executeButtonType, ButtonType.CANCEL)

  setResultConverter: (dialogButtonType: ButtonType) =>
    if dialogButtonType == executeButtonType then new AnyRef
    else null
