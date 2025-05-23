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

  def Item: String
  def Result: String
  def NumOperationsSucceeded(x: Int): String
  def NumOperationsFailed(x: Int): String
  def NoResults: String

  def Error: String

object CrudTexts:

  class Default extends CrudTexts:
    def Add = "Add"
    def Edit = "Edit"
    def Delete = "Delete"
    def Open = "Open"
    def Save = "Save"
    def Refresh = "Refresh"
    def Clear = "Clear"
    def Created = "Created"
    def Updated = "Updated"
    def Confirm = "Confirm"
    def ConfirmDeletion = "Confirm deletion"
    def AskToDelete(x: String) = s"Delete $x items?"
    def NumItems(x: Int) = s"$x items"
    def Item = "Item"
    def Result = "Result"
    def NumOperationsSucceeded(x: Int) = s"$x operations succeeded"
    def NumOperationsFailed(x: Int) = s"$x operations failed"
    def NoResults = "No results"
    def Error = "Error"

  object Default extends Default