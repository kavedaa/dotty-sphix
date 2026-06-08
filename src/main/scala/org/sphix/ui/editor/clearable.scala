package org.sphix.ui.editor

import javafx.scene.control.*
import javafx.scene.image.ImageView

import org.sphix.*

class ClearableEditorFactory[A](inner: EditorFactory[A])(using texts: EditorTexts, icons: EditorIcons) extends EditorFactory[A]:
  def createEditor = new Editor:
    val innerEditor = inner.createEditor
    export innerEditor.{ get, set, value, status, clear }
    val clearButton = new Button:
      icons.ClearSmall match
        case Some(image) => setGraphic(ImageView(image))
        case None => setText(texts.Clear)
      setOnAction(_ => clear())
      disableProperty <== value.map(_.isEmpty)
    def container(label: Option[String]) = 
      innerEditor.container(label) match
        case container: Container.Primitive => 
          new Container.Primitive(this, label, container.node, lateralNodes = container.lateralNodes :+ clearButton)
        case container: Container.MultiPrimitive => 
          new Container.MultiPrimitive(this, label, container.nodes :+ clearButton)
        case container => container
