package org.sphix.concurrent

import javafx.stage.*
import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.scene.shape.Rectangle
import javafx.scene.paint.Color
import javafx.scene.effect.DropShadow
import javafx.geometry.Pos

//  wrap the popup to avoid implicit conflict with Window
abstract class WaitPopup:
  def popup: Popup

object WaitPopup:
  given spinnerPopup: WaitPopup = new SpinnerPopup(None)

class SpinnerPopup(message: Option[String]) extends WaitPopup:
  val popup = new Popup:
    val spinner = new ProgressIndicator:
      setPrefSize(75, 75)
    val pane = new StackPane:
      val vb = new VBox(10):
        setAlignment(Pos.CENTER)
        getChildren.add(spinner)
        message.foreach: x =>
          val label = new Label(x)
          getChildren.add(label)
      getChildren.addAll(new Rectangle(150, 150, Color.WHITE), vb)
      setEffect(new DropShadow)
    getContent.add(pane)
