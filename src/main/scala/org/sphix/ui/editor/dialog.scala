package org.sphix.ui.editor

import javafx.scene.control.*
import javafx.scene.control.ButtonBar.ButtonData

import org.sphix.*

// class DerivedDialog[A >: Null : EditorFactory](title: String, executeText: String, cancelText: String)(value0: Option[A] = None) 
//   extends Dialog[Value[A]]:

//   val editor = summon[EditorFactory[A]].createEditor
//   val layouter = Layouter.Default

//   val content = editor.container(None).layout

//   setTitle(title)

//   getDialogPane.setContent(content)

//   val executeButtonType = new ButtonType(executeText, ButtonData.OK_DONE)
//   val cancelButtonType = new ButtonType(cancelText, ButtonData.CANCEL_CLOSE)
  
//   getDialogPane.getButtonTypes.addAll(executeButtonType, cancelButtonType)

//   val executeButton = getDialogPane.lookupButton(executeButtonType).asInstanceOf[Button]
//   executeButton.disableProperty bind editor.status.map(_ != Status.Valid)

//   setResultConverter { (dialogButtonType: ButtonType) =>
//     if (dialogButtonType == executeButtonType) editor.value()
//     else null
//   }

//   value0 foreach editor.set

// end DerivedDialog