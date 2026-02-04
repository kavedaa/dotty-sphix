package org.sphix.ui.viewer

import javafx.scene.control.*

import org.sphix.ui.dialog.DialogUtils

class ViewerDialog[A](value: A, title0: Option[String])(using viewer: Viewer[A])
  extends Dialog[Nothing]:

  def this(value: A)(using Viewer[A]) = this(value, None)
  def this(value: A, title0: String)(using Viewer[A]) = this(value, Some(title0))

  title0.foreach(setTitle)

  val content = viewer(value)
  
  getDialogPane.setContent(content)

  getDialogPane.getButtonTypes.add(ButtonType.CLOSE)

  setResizable(true)

end ViewerDialog