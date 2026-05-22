package org.sphix.control.cell

import javafx.scene.control.{ TableCell, CheckBox, TableColumn }
import javafx.beans.{ InvalidationListener, Observable }
import javafx.beans.property.Property
import javafx.util.Callback
import javafx.geometry.Pos

import org.sphix.*
import org.sphix.util.*
import org.sphix.binding.*

trait CheckBoxCell extends Cell[Boolean] with DataCell[Boolean, Boolean]:
  cell =>

  lazy val checkbox = new CheckBox

  def setEnable(x: Boolean) = checkbox.setDisable(!x)

  val listener = new InvalidationListener:
    def invalidated(o: Observable) =
//      println("INVALIDATED")
      val item = checkbox.isSelected
//      println(s"checkbox.isSelected: $item")
      //  TODO figure on why onUpdate is called multiple times during editing
//      println("startEdit")
      cell.startEdit()
//      println("commitEdit")
      cell.commitEdit(item)

  override def onUpdate(item: Boolean) =
//    println(s"onUpdate --------------------- $item")
    super.onUpdate(item)
//    println("removeListener")
    checkbox.selectedProperty.removeListener(listener)
//    println("setSelected")
    checkbox.setSelected(item)
//    println("addListener")
    checkbox.selectedProperty.addListener(listener)
//    println("setGraphic")
    setGraphic(checkbox)

/**
  * This is a bit of a hack to allow for showing a label in an editable checkbox cell.
  * The JavaFX cell model requires that the cell's item type is the same as the type of the value being edited.
  * Hence we have to make the label part of the item, and then write it back when committing the edit. 
  * (Even if the label itself is not editable.)
  * The data value is the label when the checkbox is selected, and None when it is not.
  */
trait CheckBoxLabelCell extends Cell[(Boolean, String)] with DataCell[(Boolean, String), String]:
  cell =>

  def dataValue(x: (Boolean, String)) = Option.when(x._1)(x._2)

  lazy val checkBox = new CheckBox

  def setEnable(x: Boolean) = checkBox.setDisable(!x)

  val listener = new InvalidationListener:
    def invalidated(o: Observable) =
      val item = (checkBox.isSelected, checkBox.getText)
      cell.startEdit()
      cell.commitEdit(item)

  override def onUpdate(item: (Boolean, String)) =
    super.onUpdate(item)
    checkBox.selectedProperty.removeListener(listener)
    checkBox.setSelected(item._1)
    checkBox.setText(item._2)
    checkBox.selectedProperty.addListener(listener)
    setGraphic(checkBox)


class TriStateModel(checked0: Boolean, indeterminate0: Boolean):
  val checked = Var(checked0)
  val indeterminate = Var(indeterminate0)
  val asVal = (checked, indeterminate).tupled
  val asOption = (checked, indeterminate).mapN((c, i) => Option.when(!i)(c))

object TriStateModel:
  def fromOption(x: Option[Boolean]) = TriStateModel(x.contains(true), x.isEmpty)

trait TriStateCheckBoxCell[S, T] extends TableCell[S, T] with DataCell[T, Option[Boolean]]:

  def checked(s: S): Property[Boolean]
  def indeterminate(s: S): Property[Boolean]
  
  lazy val checkbox = new CheckBox:
    setAllowIndeterminate(true)
  
  setAlignment(Pos.CENTER)

  var checkedBinding: BidirectionalConverterBinding[java.lang.Boolean, Boolean] = null
  var indeterminateBinding: BidirectionalConverterBinding[java.lang.Boolean, Boolean] = null

  override def updateItem(item: T, empty: Boolean) =
    super.updateItem(item, empty)
    if !empty then
      setGraphic(checkbox)
      if checkedBinding != null then checkedBinding.unbind()
      if indeterminateBinding != null then indeterminateBinding.unbind()
      val rowItem = getTableView.getItems.get(getIndex)
      checkedBinding = bindBidirectionalWithConverter(checkbox.selectedProperty, checked(rowItem))
      indeterminateBinding = bindBidirectionalWithConverter(checkbox.indeterminateProperty, indeterminate(rowItem))
    else    
      setGraphic(null)


// legacy

trait CheckBoxTableCell[S] extends TableCell[S, Boolean] with Cell[Boolean]:

  def f(s: S): Property[Boolean]
  
  lazy val checkbox = new CheckBox
  
  var binding: BidirectionalConverterBinding[java.lang.Boolean, Boolean] = null

  override def onUpdate(item: Boolean) =
    super.onUpdate(item)
    setGraphic(checkbox)
    if binding != null then binding.unbind()
    val rowItem = getTableView.getItems.get(getIndex)
    binding = bindBidirectionalWithConverter(checkbox.selectedProperty, f(rowItem))

object CheckBoxTableCell {
  def apply[S](f0: S => Property[Boolean]) = new Callback[TableColumn[S, Boolean], TableCell[S, Boolean]] {
    def call(c: TableColumn[S, Boolean]) = new CheckBoxTableCell[S] {
      def f(s: S) = f0(s)
    }
  }
}

trait TriStateCheckBoxTableCell[S, T] extends TableCell[S, T]:

  def checked(s: S): Property[Boolean]
  def indeterminate(s: S): Property[Boolean]
  
  lazy val checkbox = new CheckBox:
    setAllowIndeterminate(true)
  
  setAlignment(Pos.CENTER)

  var checkedBinding: BidirectionalConverterBinding[java.lang.Boolean, Boolean] = null
  var indeterminateBinding: BidirectionalConverterBinding[java.lang.Boolean, Boolean] = null

  override def updateItem(item: T, empty: Boolean) =
    super.updateItem(item, empty)
    if !empty then
      setGraphic(checkbox)
      if checkedBinding != null then checkedBinding.unbind()
      if indeterminateBinding != null then indeterminateBinding.unbind()
      val rowItem = getTableView.getItems.get(getIndex)
      checkedBinding = bindBidirectionalWithConverter(checkbox.selectedProperty, checked(rowItem))
      indeterminateBinding = bindBidirectionalWithConverter(checkbox.indeterminateProperty, indeterminate(rowItem))
    else    
      setGraphic(null)

// object TriStateCheckBoxTableCell {
//   def apply[S, T](checked0: S => Property[Boolean], indeterminate0: S => Property[Boolean]) = new Callback[TableColumn[S, T], TableCell[S, T]] {
//     def call(c: TableColumn[S, T]) = new TriStateCheckBoxTableCell[S, T] {
//       def checked(s: S) = checked0(s)
//       def indeterminate(s: S) = indeterminate0(s)
//     }
//   }
// }
