package org.sphix.ui.editor

import scala.jdk.CollectionConverters.*

import javafx.application.Platform
import javafx.scene.control.*
import javafx.scene.control.ButtonBar.ButtonData
import javafx.scene.layout.*

import org.sphix.*
import org.sphix.ui.editor.EditorFactory
import org.sphix.util.NodeUtil
import org.sphix.ui.dialog.DialogUtils

/**
  * A simple way to have an [[Editor]] shown in a dialog and return the result when the dialog is submitted. 
  * The dialog can be customized with title, "OK" button text and label for the editor. 
  * The dialog will only allow submission when the data in the editor is valid. 
  * The dialog is resizable and will size to the content.
  * This dialog can also be constructed from an [[EditorFactory]] with the `toDialog` method.
  */
class EditorDialog[A >: Null](title: Option[String] = None, ok: Option[String] = None, label: Option[String] = None)(using factory: EditorFactory[A])
  extends Dialog[A]:
  
  def this()(using EditorFactory[A]) = this(None, None, None)
  def this(title: String)(using EditorFactory[A]) = this(Some(title), None, None)
  def this(title: String, ok: String)(using EditorFactory[A]) = this(Some(title), Some(ok), None)
  def this(title: String, ok: String, label: String)(using EditorFactory[A]) = this(Some(title), Some(ok), Some(label))

  val editor = factory.createEditor

  def withInitialValue(value: A): EditorDialog[A] = 
    editor.set(value)
    this

  def withInitialValueOption(value: Option[A]): EditorDialog[A] = 
    value.map(x => withInitialValue(x)).getOrElse(this)

  title.foreach(setTitle)

  val content = editor.container(label).layout(isTopLevel = true, Some(onLayoutChange))

  val valid = editor.status.map(_.toBoolean)

  getDialogPane.setContent(content)

  val executeButtonType = new ButtonType(ok.getOrElse("OK"), ButtonData.OK_DONE)

  getDialogPane.getButtonTypes.addAll(executeButtonType, ButtonType.CANCEL)

  val executeButton = getDialogPane.lookupButton(executeButtonType).asInstanceOf[Button]
  executeButton.disableProperty.bind(valid.map(x => !x))

  setResultConverter: (dialogButtonType: ButtonType) =>
    if (dialogButtonType == executeButtonType) && valid() then editor.value().toOption.orNull
    else null
 
  setResizable(true)

  def onLayoutChange() = getDialogPane.getScene.getWindow.sizeToScene()

  Platform.runLater: () =>
    NodeUtil.firstFocusTraversable(content).foreach: node =>
      node.requestFocus()
