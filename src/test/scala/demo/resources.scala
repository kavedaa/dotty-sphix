package demo

import org.sphix.util.*
import org.sphix.ui.editor.*
import org.sphix.ui.crud.*

trait Resources:

  val getIcon = ResourceImageResolver(getClass, "/icons/" + _)

  val arrow = getIcon("arrow_right.png")

  given SphixTexts with
    val Add = "Add"
    val Edit = "Edit"
    val Remove = "Remove"
    val Delete = "Delete"
    val Open = "Open"
    val Save = "Save"
    val Refresh = "Refresh"
    val Clear = "Clear"
    val Created = "Created"
    val Updated = "Updated"
    val Confirm = "Confirm"
    val ConfirmDeletion = "Confirm deletion"
    def AskToDelete(x: String) = s"Delete $x items?"
    def NumItems(x: Int) = s"$x items"
    val Item = "Item"
    val Result = "Result"
    def NumOperationsSucceeded(x: Int) = s"$x operations succeeded"
    def NumOperationsFailed(x: Int) = s"$x operations failed"
    val NoResults = "No results"
    val Error = "Error"


  given SphixIcons with
    val Add = Some(getIcon("add.png"))
    val Remove = Some(getIcon("delete.png"))
    val Clear = Some(getIcon("cancel.png"))
    val Edit = None
    val Delete = None
    val Open = None
    val Save = None
    val Refresh = None
    val IsSuccess = Some(getIcon("accept.png"))
    val IsFailure = Some(getIcon("cancel.png"))
