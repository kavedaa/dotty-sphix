package org.sphix.ui.editor

trait DerivedForm[A : EditorFactory]:
  val editor = summon[EditorFactory[A]].createEditor
  val form = editor.container(None).layout

