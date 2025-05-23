package org.sphix.ui.dialog

import javafx.scene.control.*
import javafx.scene.layout.*

import javafx.scene.image.Image

class InfoDialog(title: Option[String], header: Option[String], message: String)
  extends Dialog[Nothing] {

  def this(title: String, header: String, message: String) = this(Some(title), Some(header), message)
  def this(header: String, message: String) = this(None, Some(header), message)
  def this(title: Option[String], message: String) = this(title, None, message)
  def this(message: String) = this(None, None, message)

  val textArea = new TextArea {
    setEditable(false)
    setWrapText(true)
  }

  title foreach setTitle
  header foreach getDialogPane.setHeaderText
  textArea.setText(message)
  
  getDialogPane.setContent(textArea)

  //  a little trick to get the default graphic used in alerts
  val img = new Label
  img.getStyleClass.addAll("alert", "info", "dialog-pane")
  setGraphic(img)

  getDialogPane.getButtonTypes add ButtonType.CLOSE

  setResizable(true)
}