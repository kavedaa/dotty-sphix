package demo

import org.sphix.util.*
import org.sphix.ui.editor.EditorIcons

trait DemoUtils:

  val getIcon = ResourceImageResolver(getClass, "/icons/" + _)

  val arrow = getIcon("arrow_right.png")

  given EditorIcons with
    val Add = Some(getIcon("add.png"))
    val Remove = Some(getIcon("delete.png"))
    val Clear = Some(getIcon("cancel.png"))
