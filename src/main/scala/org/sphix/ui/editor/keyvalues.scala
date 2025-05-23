package org.sphix.ui.editor

import javafx.geometry.*
import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.given
import org.sphix.collection.ObservableSeq.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq

//  this didn't work very well

// class KeyValuesEditorFactory[A, B](source: Map[A, Seq[B]])(renderKey: A => String)(renderValue: B => String)(using Layouter[Container.Primitive]) 
//   extends EditorFactory[(A, Seq[B])]:
//     def createEditor = new Editor[(A, Seq[B])]:
//       type C = Container.Primitive
//       val comboBox = new ComboBox[(A, Seq[B])] with StringComboBoxUtils[(A, Seq[B])]((a, bs) => renderKey(a)):
//         setItems(source.toSeq)
//       val itemCheckboxes = comboBox.getSelectionModel.selectedItemProperty map Option.apply flatMap { 
//         case Some((key, values)) =>
//           val checks = values.map { value =>
//             new CheckBox(renderValue(value)) -> value
//           } .toList
//           ObservableSeq.observeElements(checks: _*)((cb, _) => cb.selectedProperty).asVal
//         case None =>
//           ObservableSeq().asVal
//         }
//       val selectedKey = comboBox.getSelectionModel.selectedItemProperty map Option.apply
//       val value = (selectedKey, itemCheckboxes) mapN { 
//         case (Some((key, values)), cbs) =>
//           Value.Valid(key -> cbs.filter(_._1.isSelected).map(_._2))
//         case _ => 
//           Value.Empty
//       }
//       val status = selectedKey map {
//         case Some(_) => Status.Valid
//         case _ => Status.Empty
//       }
//       def set(keyValue: (A, Seq[B])) = comboBox.getSelectionModel.select(keyValue)    
//       def clear() = comboBox.getSelectionModel.clearSelection()
//       def selectAll() = itemCheckboxes() foreach { (cb, _) => cb.setSelected(true) }
//       def selectNone() = itemCheckboxes() foreach { (cb, _) => cb.setSelected(false) }
//       def container(label: Option[String]) =
//         val checksPane = new BorderPane:
//           centerProperty <== itemCheckboxes map { cbs =>
//             new FlowPane:
//               getChildren.addAll(cbs.map(_._1))
//               setHgap(5)
//               setVgap(5)
//           }
//         val selectAllButton = new Button("*"):
//           setOnAction(_ => selectAll())
//         val selectNoneButton = new Button("x"):
//           setOnAction(_ => selectNone())
//         val pane = new VBox(5, comboBox, checksPane, new HBox(5, selectAllButton, selectNoneButton))
//         Container.Primitive(this, label, pane, status)
          

