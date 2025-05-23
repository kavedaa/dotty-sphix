package org.sphix.control.cell

import javafx.scene.control.*
import javafx.event.EventHandler
import javafx.scene.input._

import org.sphix.*
import org.sphix.control.ComboBoxUtils
import org.sphix.collection.ObservableSeq

// //  a combobox cell where the available items is a function of T
// //  TODO naming

// case class SelectItemModel[A](selected0: Option[A], items: ObservableSeq[A]):
//   val selected = Var(selected0)

// trait ComboBox2TableCell[S, A] extends TableCell[S, SelectItemModel[A]] { cell =>

//   def render: A => String

//   this.getStyleClass().add("combo-box-table-cell");

//   private lazy val comboBox = new ComboBox[A] with ComboBoxUtils[A] {

//     setCell(TextCell(render))
//     setButtonCell(TextCell(render))

//     setMaxWidth(java.lang.Double.MAX_VALUE);

//     getSelectionModel.selectedItemProperty onValue { v =>
//       Option(v) foreach { item =>
//         cell.getItem.selected() = Some(item)
//         commitEdit(getItem)
//       }
//     }

//     setOnKeyPressed(new EventHandler[KeyEvent] {
//       def handle(t: KeyEvent) = {
//         if (new KeyCodeCombination(KeyCode.ESCAPE) `match` t) {
//           cell.cancelEdit()
//         }
//       }
//     })

//   }

//   override def startEdit() = {
//     if (isEditable && getTableView.isEditable && getTableColumn.isEditable) {
    
//       comboBox setItems cell.getItem.items
//       getItem.selected().foreach(comboBox.getSelectionModel.select) //	important that this comes before super.startEdit()

//       super.startEdit()

//       setText(null)
//       setGraphic(comboBox)

//       //      comboBox show ()	//	this is wanted for good UX but triggers weird bug 
//       comboBox.requestFocus()
//     }
//   }

//   override def cancelEdit() = {
//     super.cancelEdit()
//     setText(getItem.selected().map(render).getOrElse(""))
//     setGraphic(null)
//   }

//   override def updateItem(item: SelectItemModel[A], empty: Boolean) = {
//     super.updateItem(item, empty)
//     if (!empty) {
//       if (isEditing) {
//         getItem.selected().foreach(comboBox.getSelectionModel.select)
//         setText(null)
//         setGraphic(comboBox)
//       }
//       else {
//         setText(getItem.selected().map(render).getOrElse(""))
//         setGraphic(null)
//       }
//     }
//     else {
//       setText(null)
//       setGraphic(null)
//     }
//   }
// }
