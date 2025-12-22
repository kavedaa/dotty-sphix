package demo.concurrent

import scala.util.Try
import scala.concurrent.*
import scala.concurrent.ExecutionContext.Implicits.global

import javafx.stage.*
import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.concurrent.*
import org.sphix.ui.Responding
import org.sphix.ui.crud.*


class ModalDemo(stage: Stage) extends BorderPane:

  given Window = stage

  //  1) Three different syntaxes for basic usage, all do the same          

  val modal1Button = new Button("FutureModal Example 1")

  //  Passing an existing future

  modal1Button.setOnAction: _ =>
    val future = Future:
      Thread.sleep(1000)
      "Done"
    FutureModal(future).onComplete: res =>
      Responding.respond(res)

  //  Creating the future as a by-name argument

  modal1Button.setOnAction: _ =>
    FutureModal: 
      Future:
        Thread.sleep(1000)
        "Done"
    .onComplete: res =>
      Responding.respond(res)

  //  Having the future be created by the method

  modal1Button.setOnAction: _ =>
    FutureModal: 
      Thread.sleep(1000)
      "Done"
    .onComplete: res =>
      Responding.respond(res)

  //  2) Adding info text

  val modal2Button = new Button("FutureModal Example 2")

  modal2Button.setOnAction: _ =>
    FutureModal("Please wait..."): 
      Thread.sleep(1000)
      "Done"
    .onComplete: res =>
      Responding.respond(res)

  //  3) Addding title text and updating info text by calling method on the modal
  //  (note that this variant, that can refer to the modal, can not be combined with creating the future automatically)

  val modal3Button = new Button("FutureModal Example 3")

  modal3Button.setOnAction: _ =>
    FutureModal("Please wait..."): modal =>
      Future:
        modal.updateInfo("Doing first")
        Thread.sleep(1000)
        modal.updateInfo("Doing second")
        Thread.sleep(1000)
        modal.updateInfo("Doing third")
        Thread.sleep(1000)
        "Done"
    .onComplete: res =>
      Responding.respond(res)

  //  4) Using a specific modal instead of the default, and updating the progress

  val modal4Button = new Button("FutureModal Example 4")

  modal4Button.setOnAction: _ =>
    FutureModal(using ModalFactory.Bar)("Processing..."): m =>
      Future:
        val total = 25
        (1 to total).map: index =>
          m.updateInfo(s"$index of $total")
          m.updateProgress(index / total.toDouble)
          Thread.sleep(100)
          index
    .onComplete(Responding.respond)

  //  5) Executing futures sequentially

  val seqModal1Button = new Button("SequentialFutureModal Example 1")

  def slowMultiply(a: Int, b: Int) = Future:
    Thread.sleep(100)
    a * b

  given CrudTexts = CrudTexts.Default
  given CrudIcons = CrudIcons.Default

  seqModal1Button.setOnAction: _ =>
    new SequentialFutureModal(using ModalFactory.Bar)(Some("Computing..."))(1 to 100)(x => slowMultiply(x, x))
    .onComplete(Responding.respond)

  val toolbar = new ToolBar(modal1Button, modal2Button, modal3Button, modal4Button, seqModal1Button)

  setCenter(toolbar)