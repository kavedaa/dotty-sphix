package org.sphix.ui.crud

import javafx.scene.control.{ Alert, ButtonType }

import Alert.AlertType

import org.sphix.ui.action.ActionTexts

object Confirmation:

  def confirmDelete[A](items: Iterable[A])(using f: A => String)(using ActionTexts): Boolean =
    if items.size == 1 then confirmDelete(f(items.head)) else confirmDelete(items.size)

  def confirmDelete(item: String)(using texts: ActionTexts) = confirm(texts.ConfirmDeletion, texts.AskToDelete(item))
  def confirmDelete(num: Int)(using texts: ActionTexts) = confirm(texts.ConfirmDeletion, texts.AskToDelete(texts.NumItems(num)))

  def confirm(header: String, text: String)(using texts: ActionTexts): Boolean = 
    showAlert(AlertType.CONFIRMATION, Some(texts.Confirm), Some(header), Some(text))
      .filter(_ == ButtonType.OK)
      .isPresent

  private def showAlert(alertType: AlertType, title: Option[String], header: Option[String], details: Option[String]) = 
    val alert = new Alert(alertType):
      title foreach setTitle
      header foreach setHeaderText
      details foreach setContentText
    alert.showAndWait()

