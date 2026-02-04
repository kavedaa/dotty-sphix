package org.sphix.ui.dialog

import javafx.beans.value.ObservableValue
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.control.ButtonBar.ButtonData

import org.sphix.*
import org.sphix.ui.*

trait DialogUtils[A >: Null] extends Dialog[A] with FormUtils:

  def title: String
  def content: Node
  def ok: String
  def valid: ObservableValue[Boolean]

  def result: Option[A]

  def init() =

    setTitle(title)
    getDialogPane.setContent(content)

    val executeButtonType = new ButtonType(ok, ButtonData.OK_DONE)
  
    getDialogPane.getButtonTypes.addAll(ButtonType.CANCEL, executeButtonType)

    val executeButton = getDialogPane.lookupButton(executeButtonType).asInstanceOf[Button]
    executeButton.disableProperty.bind(valid.map(x => !x))

    setResultConverter: (dialogButtonType: ButtonType) =>
      if (dialogButtonType == executeButtonType) && valid() then result.orNull
      else null
