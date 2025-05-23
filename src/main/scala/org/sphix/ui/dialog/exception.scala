package org.sphix.ui.dialog

import javafx.scene.control.*
import javafx.scene.layout.*

import org.sphix.ui.FormUtils

class ExceptionDialog(title: Option[String], header: Option[String], exception: Throwable)
  extends Dialog[Nothing]:

  def this(title: String, header: String, exception: Throwable) = this(Some(title), Some(header), exception)
  def this(header: String, exception: Throwable) = this(None, Some(header), exception)
  def this(title: Option[String], exception: Throwable) = this(title, None, exception)
  def this(exception: Throwable) = this(None, None, exception)

  val textArea, stackTraceTextArea = new TextArea:
    setEditable(false)
    setWrapText(true)

  title.foreach(setTitle)
  getDialogPane.setHeaderText(header.getOrElse(exception.getClass.getName))
  textArea.setText(exception.getMessage)
  stackTraceTextArea.setText(exception.getStackTrace.mkString(System.lineSeparator))

  val content = VBox(textArea)

  getDialogPane.setContent(content)
  getDialogPane.setExpandableContent(VBox(stackTraceTextArea))

  //  a little trick to get the default graphic used in alerts
  val img = new Label
  img.getStyleClass.addAll("alert", "error", "dialog-pane")
  setGraphic(img)

  getDialogPane.getButtonTypes.add(ButtonType.CLOSE)

  setResizable(true)