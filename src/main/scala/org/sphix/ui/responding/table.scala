package org.sphix.ui.responding

import scala.util.*

import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.scene.image.*
import javafx.geometry.Pos

import no.vedaadata.text.Render

import org.sphix.*
import org.sphix.ui.*
import org.sphix.ui.crud.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq

class ItemTryIterableResponder[A, B](using renderA: Render[A], renderB: Render[B])(using texts: CrudTexts, icons: CrudIcons) 
  extends Responder[Iterable[(A, Try[B])]]
  with RespondingUtils:

  def respond(xs: Iterable[(A, Try[B])])(using message: String => String) = 

    val numSuccesses = xs.count((_, t) => t.isSuccess)
    val numFailures = xs.count((_, t) => t.isFailure)

    val filterButtons = FilterButtons(numSuccesses, numFailures)

    val os = xs.to(ObservableSeq)

    val filter = (filterButtons.successesButton.selectedProperty, filterButtons.failuresButton.selectedProperty).mapN: (s, f) =>
      ((x: A, t: Try[B]) => (!s && !f) || (s && t.isSuccess) || (f && t.isFailure)).tupled

    val filtered = os.filtered(filter)

    val table = new ItemTryTableView(filtered)(using renderA, renderB.andThen(message))

    val vb = VBox(10, filterButtons.buttonBar, table)
    VBox.setVgrow(table, Priority.ALWAYS)

    new RespondingMonolog(vb, None).showAndWait()


class ItemTryTableView[A, B](xs: ObservableSeq[(A, Try[B])])(using renderA: Render[A], renderB: Render[B])(using texts: CrudTexts, icons: CrudIcons)
  extends TableView[(A, Try[B])] with TableUtils[(A, Try[B])]:

    val iconColumn = new Column("", _._2.toVal):
      setCell:
        new ImageCell(using x => if x.isSuccess then icons.IsSuccess else icons.IsFailure)
        with AlignedCell(Pos.BASELINE_CENTER)

    val itemColumn = new Column(texts.Item, _._1.toVal):
      setCell:
        new StringCell(using x => renderA(x)) {}

    val tryColumn = new Column(texts.Result, _._2.toVal):
      setCell:
        new TextCell( {
          case Success(x) => renderB(x)
          case Failure(ex) => Some(ex.getMessage)
        } ) 
        with TooltipCell(using {
          case Success(x) => None
          case Failure(ex) => Some(ex.getStackTrace.mkString(System.lineSeparator))
        } )

    getColumns.addAll(iconColumn, itemColumn, tryColumn)
    setItems(xs)
