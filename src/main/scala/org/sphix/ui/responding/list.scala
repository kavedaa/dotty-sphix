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

class TryIterableResponder[A](using render: Render[A])(using texts: CrudTexts, icons: CrudIcons) 
  extends Responder[Iterable[Try[A]]]
  with RespondingUtils:

  def respond(xs: Iterable[Try[A]])(using message: String => String) = 

    val numSuccesses = xs.count(_.isSuccess)
    val numFailures = xs.count(_.isFailure)

    val filterButtons = FilterButtons(numSuccesses, numFailures)

    val os = xs.to(ObservableSeq)

    val filter = (filterButtons.successesButton.selectedProperty, filterButtons.failuresButton.selectedProperty).mapN: (s, f) =>
      (x: Try[A]) => (!s && !f) || (s && x.isSuccess) || (f && x.isFailure)

    val filtered = os.filtered(filter)

    val list = new TryListView(filtered)(using render.andThen(message))

    val vb = VBox(10, filterButtons.buttonBar, list)
    VBox.setVgrow(list, Priority.ALWAYS)

    new RespondingMonolog(vb, None).showAndWait()

class TryListView[A](xs: ObservableSeq[Try[A]])(using render: Render[A])(using texts: CrudTexts, icons: CrudIcons)
  extends ListView[Try[A]] with ListUtils[Try[A]]:

    setCell:
      new TextCell( {
        case Success(x) => render(x)
        case Failure(ex) => Some(ex.getMessage)
      } )
      with ImageCell(using x => if x.isSuccess then icons.IsSuccess else icons.IsFailure)   

    setItems(xs)

