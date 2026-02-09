package org.sphix.ui.editor

import javafx.scene.control.*

import org.sphix.*
import org.sphix.collection._
import org.sphix.collection.ObservableSeq._
import org.sphix.collection.mutable.ObservableBuffer

class DynamicEditorFactory[A](init: Int = 0)(using itemEditorFactory: EditorFactory[A])(using Layouter[Container.Dynamic]) extends EditorFactory[List[A]]:
  private class DynamicEditor extends Editor[List[A]]:
    private val editors = ObservableBuffer[Editor[A]](x => x.status)
    private val containers = ObservableBuffer[Container]()
    def get = editors.toList.map(_.get)
    val status = editors(e => Status.sequence(e.map(_.status())))
    val value = editors(e => Value.sequence(e.map(_.value()), get))
    def set(items0: List[A]) = 
      val createdEditors = items0.map: item =>
        val editor = itemEditorFactory.createEditor  
        editor.set(item)
        editor
      editors() = createdEditors
      containers() = createdEditors.map(_.container(None))
    def clear() = 
      editors.clear()
      containers.clear()
    def container(label: Option[String]) = Container.Dynamic(this, label, containers, add, remove)
    def add() = 
      val editor = itemEditorFactory.createEditor
      editors += editor
      containers += editor.container(None)
    def remove(container: Container) =
      val index = containers.indexWhere(_ eq container)
//      val editorIndex = editors.indexWhere(_ eq container.editor)
      if index != -1 then 
        editors.remove(index)
        containers.remove(index)
  def createEditor = 
    val editor = new DynamicEditor
    (0 until init) foreach { _ => editor.add() }
    editor