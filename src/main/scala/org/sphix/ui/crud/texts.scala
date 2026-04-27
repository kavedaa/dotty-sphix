package org.sphix.ui.crud

trait CrudTexts:

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