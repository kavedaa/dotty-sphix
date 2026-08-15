package org.sphix.ui.action

import javafx.scene.image.*

trait ActionTexts:

  def Add: String
  def Edit: String
  def Delete: String
  def Open: String
  def Save: String
  def Refresh: String
  def Clear: String

  def Created: String
  def Updated: String

  def Confirm: String
  def ConfirmDeletion: String
  def AskToDelete(x: String): String
  def NumItems(x: Int): String

  def Error: String

end ActionTexts

trait ActionIcons:

  def Add: Option[Image]
  def Edit: Option[Image]
  def Delete: Option[Image]
  def Open: Option[Image]
  def Save: Option[Image]
  def Refresh: Option[Image]
  def Clear: Option[Image]
  def IsSuccess: Option[Image]
  def IsFailure: Option[Image]
