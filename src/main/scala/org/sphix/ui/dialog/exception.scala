package org.sphix.ui.dialog

import javafx.scene.control.*
import javafx.scene.layout.*

/**
  * A dialog that shows the message and stack trace of an exception, including all nested causes. 
  * The message is shown in a non-editable text area, for convenient copying of the text.
  * The stack trace is shown in the expandable content of the dialog.
  * The `title` and `header` parameters are optional. If the `header` is not provided, the class name of the exception is used as the header.
  * Secondary constructors are provided for convenience, to allow omitting the `title` and/or `header`.
  */
class ExceptionDialog(title: Option[String], header: Option[String], exception: Throwable)
  extends Dialog[Nothing]:

  def this(title: String, header: String, exception: Throwable) = this(Some(title), Some(header), exception)
  def this(header: String, exception: Throwable) = this(None, Some(header), exception)
  def this(title: Option[String], exception: Throwable) = this(title, None, exception)
  def this(exception: Throwable) = this(None, None, exception)

  val textArea, stackTraceTextArea = new TextArea:
    setEditable(false)
    setWrapText(true)

  def createCauseList(exception: Throwable): List[Throwable] =
    exception :: Option(exception.getCause).toList.flatMap(createCauseList)

  val causeList = createCauseList(exception)

  title.foreach(setTitle)

  val headerText = header.getOrElse(exception.getClass.getName)

  getDialogPane.setHeaderText(headerText)

  val textList = causeList.headOption.map(_.getMessage).toList ::: causeList.tail.map(ex => s"Caused by: ${ex.getClass.getName}: ${ex.getMessage}")  

  textArea.setText(textList.mkString(System.lineSeparator * 2))

  val stackTraceText = 
    causeList.headOption.map(_.getStackTrace.mkString(System.lineSeparator)).toList :::
      causeList.tail.map(ex => s"Caused by: ${ex.getClass.getName}: ${ex.getMessage}${System.lineSeparator * 2}${ex.getStackTrace.mkString(System.lineSeparator)}")

  stackTraceTextArea.setText(stackTraceText.mkString(System.lineSeparator * 2))

  val content = VBox(textArea)
  VBox.setVgrow(textArea, Priority.ALWAYS)

  val expandableContent = VBox(stackTraceTextArea)
  VBox.setVgrow(stackTraceTextArea, Priority.ALWAYS)

  getDialogPane.setContent(content)
  getDialogPane.setExpandableContent(expandableContent)

  //  a little trick to get the default graphic used in alerts
  val img = new Label
  img.getStyleClass.addAll("alert", "error", "dialog-pane")
  setGraphic(img)

  getDialogPane.getButtonTypes.add(ButtonType.CLOSE)

  setResizable(true)