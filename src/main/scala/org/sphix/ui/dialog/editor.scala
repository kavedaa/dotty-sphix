package org.sphix.ui.dialog

import scala.jdk.CollectionConverters.*

import javafx.application.Platform
import javafx.scene.Parent
import javafx.scene.control._
import javafx.scene.layout._

import org.sphix.*
import org.sphix.ui.editor.EditorFactory
import org.sphix.util.NodeUtil

class EditorDialog[A >: Null](label0: Option[String], value0: Option[A])(using factory: EditorFactory[A])
  extends Dialog[A]
  with DialogUtils[A]:

  def this()(using EditorFactory[A]) = this(None, None)
  def this(label: String)(using EditorFactory[A]) = this(Some(label), None)

  def withInitialValue(value0: A) = new EditorDialog[A](label0, Some(value0))

  val editor = factory.createEditor

  value0.foreach(editor.set)

  val title = label0.getOrElse("")

  val content = editor.container(label0).layout(isTopLevel = true, Some(onLayoutChange))
  
  val ok = "OK"
  
  val valid = editor.status.map(_.toBoolean)

  def result = editor.value().toOption

  setResizable(true)

  init()

  def onLayoutChange() = getDialogPane.getScene.getWindow.sizeToScene()

  Platform.runLater: () =>
    NodeUtil.firstFocusTraversable(content).foreach: node =>
      node.requestFocus()
