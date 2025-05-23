package org.sphix.control

import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.collections.*

class Spring extends Region:
  HBox.setHgrow(this, Priority.ALWAYS)


class NoTableViewSelectionModel[S](val tableView: TableView[S]) extends TableView.TableViewSelectionModel(tableView):
  def getSelectedCells() = FXCollections.emptyObservableList()
  def selectLeftCell() = {}
  def selectRightCell() = {}
  def selectAboveCell() = {}
  def selectBelowCell() = {}
  def clearSelection(i: Int, tableColumn: TableColumn[S, ?]) = {}
  def clearAndSelect(i: Int, tableColumn: TableColumn[S, ?]) = {}
  def select(i: Int, tableColumn: TableColumn[S, ?]) = {}
  def isSelected(i: Int, tableColumn: TableColumn[S, ?]) = false
  override def getSelectedIndices() = FXCollections.emptyObservableList()
  override def getSelectedItems() = FXCollections.emptyObservableList()
  override def selectIndices(i: Int, is: Int*) = {}
  override def selectAll() = {}
  override def clearAndSelect(i: Int) = {}
  override def select(i: Int) = {}
  override def select(x: S) = {}
  override def clearSelection(i: Int) = {}
  override def clearSelection() = {}
  override def isSelected(i: Int) = false
  override def isEmpty() = true
  override def selectPrevious() = {}
  override def selectNext() = {}
  override def selectFirst() = {}
  override def selectLast() = {}


class NoTableViewFocusModel[S](val tableView: TableView[S]) extends TableView.TableViewFocusModel[S](tableView):
  def focusFirstCell() = {}
  def focusLastCell() = {}
  override def focus(index: Int, tableColumn: TableColumn[S, ?]) = {}
  override def isFocused(index: Int, tableColumn: TableColumn[S, ?]) = false
  override def focus(index: Int) = {}
  override def isFocused(index: Int) = false
  override def focusPrevious() = {}
  override def focusNext() = {}
  override def focusAboveCell() = {}
  override def focusBelowCell() = {}
  override def focusLeftCell() = {}
  override def focusRightCell() = {}
