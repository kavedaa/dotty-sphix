package org.sphix.concurrent

import scala.util.*
import scala.concurrent.*
import scala.annotation.targetName

import javafx.application.*
import javafx.stage.*

import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.geometry.*

import javafx.scene.Scene
import javafx.scene.shape.Rectangle
import javafx.scene.paint.Color
import javafx.scene.effect.DropShadow

import no.vedaadata.text.Render

class FutureModal[A] private
  (title: Option[String], info0: Option[String])
  (f: Modal => Future[A])
  (using modalFactory: ModalFactory)
  (using window: Window)
  (using ExecutionContext):

  val modal = modalFactory.create(title, info0)
  modal.initOwner(window)
  modal.show()

  val future = f(modal)

  def onComplete[U](f: Try[A] => U): Unit = 
    future.onComplete: x =>
      Platform.runLater: () =>
        modal.hide() 
        f(x)

  def onSuccess[U](f: A => U): Unit = 
    onComplete:
      case Success(x) => f(x)
      case Failure(ex) =>     //  do nothing

object FutureModal:
        
  //  the reason for using "targetName" is for overloading

  @targetName("plain") def apply[A]
    (using modalFactory: ModalFactory)
    (info0: String)
    (f: => A)
    (using window: Window)
    (using ec: ExecutionContext):
    FutureModal[A] =
      new FutureModal(None, Some(info0))(_ => Future(f))

  @targetName("plain") def apply[A]
    (using modalFactory: ModalFactory)
    (f: => A)
    (using window: Window)
    (using ExecutionContext):
    FutureModal[A] =
      new FutureModal(None, None)(_ => Future(f))

  @targetName("future") def apply[A]
    (using modalFactory: ModalFactory)
    (info0: String)
    (f: => Future[A])
    (using window: Window)
    (using ec: ExecutionContext):
    FutureModal[A] =
      new FutureModal(None, Some(info0))(_ => f)

  @targetName("future") def apply[A]
    (using modalFactory: ModalFactory)
    (f: => Future[A])
    (using window: Window)
    (using ExecutionContext):
    FutureModal[A] =
      new FutureModal(None, None)(_ => f)

  @targetName("modal") def apply[A]
    (using modalFactory: ModalFactory)
    (title: String)
    (f: Modal => Future[A])
    (using window: Window)
    (using ec: ExecutionContext): 
    FutureModal[A] =
      new FutureModal(Some(title), None)(f)

  @targetName("modal") def apply[A]
    (using modalFactory: ModalFactory)
    (f: Modal => Future[A])
    (using window: Window)
    (using ExecutionContext):
    FutureModal[A] =
      new FutureModal(None, None)(f)


class SequentialFutureModal[A, B] 
  (using modalFactory: ModalFactory)
  (title: Option[String])
  (xs: Iterable[A])
  (f: A => Future[B])
  (using render: Render[A])
  (using window: Window)
  (using ExecutionContext):

  val modal = modalFactory.create(title, None)
  modal.initOwner(window)
  modal.show()

  val total = xs.size

  val future = 
    xs.zipWithIndex
      .iterator
      .foldLeft(Future.successful(List.empty[Try[B]])): 
        case (fbs, (x, index)) =>
          fbs.flatMap: bs =>
            render(x).foreach(modal.updateInfo)
            modal.updateProgress(index / total.toDouble)
            f(x).transform: b =>
              Success(b :: bs)
      .map(_.reverse)

  def onComplete(f: Try[List[Try[B]]] => Unit): Unit = 
    future.onComplete: x =>
      Platform.runLater: () =>
        modal.hide() 
        f(x)

  // def onFlatComplete[B](f: Try[B] => Unit)(using A <:< Try[B]): Unit = 
  //   future.onComplete: x =>
  //     Platform.runLater: () =>
  //       f(x.flatten)


abstract class Modal extends Stage:
  initModality(Modality.WINDOW_MODAL)
  def updateProgress(x: Double): Unit
  def updateInfo(x: String): Unit

trait ModalFactory:
  def create(title: Option[String], info0: Option[String]): Modal

object ModalFactory:

  given default: ModalFactory = Spinner

  object Spinner extends ModalFactory:
    def create(title: Option[String], info0: Option[String]) = new SpinnerModal(title, info0)

  object Bar extends ModalFactory:
    def create(title: Option[String], info0: Option[String]) = new BarModal(title, info0)


//  implementations

abstract class ModalStyleBase extends Modal:

  initStyle(StageStyle.TRANSPARENT)

  val root = new StackPane:
    setPadding(Insets(10))
    setBackground(Background.fill(Color.TRANSPARENT))
    setEffect(new DropShadow)

  val scene = new Scene(root, Color.TRANSPARENT)
  setScene(scene)

  setResizable(false)  


abstract class SpinnerModalBase extends ModalStyleBase:

  protected val spinner = new ProgressIndicator

  protected val titleLabel = new Label
  protected val infoLabel = new Label

  def updateProgress(x: Double): Unit =
    Platform.runLater: () =>
      spinner.setProgress(x)

  def updateProgress(index: Long, total: Long): Unit =
    updateProgress(index.toDouble / total)

  def updateInfo(x: String): Unit = 
    Platform.runLater: () =>
      infoLabel.setText(x)


class SpinnerModal(title: Option[String], info0: Option[String]) extends SpinnerModalBase:
    
  spinner.setPrefSize(75, 75)

  val background = new Region:
    setPrefSize(150, 150)
    setStyle("-fx-background-color: -fx-control-inner-background; -fx-background-radius: 10;")

  val content = new VBox(10):
    setAlignment(Pos.CENTER)
    getChildren.addAll(titleLabel, spinner, infoLabel)

  root.getChildren.addAll(background, content)
  
  title.foreach(titleLabel.setText)
  info0.foreach(infoLabel.setText)


abstract class BarModalBase extends ModalStyleBase:

  protected val bar = new ProgressBar

  protected val titleLabel = new Label
  protected val infoLabel = new Label

  def updateProgress(x: Double): Unit =
    Platform.runLater: () =>
      bar.setProgress(x)

  def updateProgress(index: Long, total: Long): Unit =
    updateProgress(index.toDouble / total)

  def updateInfo(x: String): Unit = 
    Platform.runLater: () =>
      infoLabel.setText(x)


class BarModal(title: Option[String], info0: Option[String]) extends BarModalBase:
    
  val vb = new VBox(10):
    setAlignment(Pos.CENTER)
    getChildren.addAll(titleLabel, bar, infoLabel)

  root.getChildren.addAll(new Rectangle(150, 150, Color.WHITE), vb)

  setResizable(false)  

  title.foreach(titleLabel.setText)
  info0.foreach(infoLabel.setText)


