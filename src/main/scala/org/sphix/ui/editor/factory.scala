package org.sphix.ui.editor

import scala.deriving.*

import java.time.LocalDate

import javafx.scene.control.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq
import org.sphix.ui.dialog.EditorDialog

trait EditorFactory[A]:
  def createEditor: Editor[A]

object EditorFactory:

  def apply[A](using editorFactory: EditorFactory[A]): EditorFactory[A] = editorFactory

  def apply[A](factory: => Editor[A]): EditorFactory[A] = new EditorFactory:
    def createEditor = factory

  // given option[A](using inner: EditorFactory[A]): EditorFactory[Option[A]] =
  //   new EditorFactory:
  //     def createEditor = new Editor:
  //       val innerEditor = inner.createEditor
  //       type C = innerEditor.C
  //       def get = Option(innerEditor.get) // TODO not sure if this is right
  //       def value = innerEditor.value.map(_.liftOption)
  //       def set(x: Option[A]) = x match
  //         case Some(value) => innerEditor.set(value)
  //         case None => clear()
  //       val status = Val(Status.Valid)
  //       def clear() = innerEditor.clear()
  //       def container(label: Option[String]) = innerEditor.container(label).withEditor(this)

  extension [A] (self: EditorFactory[A]) 
    def transform[B](using f: A => B)(using g: B => A): EditorFactory[B] = 
      new EditorFactory:
        def createEditor = self.createEditor.transform(f)(g)

  extension [A >: Null] (self: EditorFactory[A]) 
    def toDialog(label: Option[String], value0: Option[A]): EditorDialog[A] = new EditorDialog(label, value0)(using self)
    def toDialog: EditorDialog[A] = toDialog(None, None)
    def toDialog(label: String): EditorDialog[A] = toDialog(Some(label), None)

  //  default primitive editor factories

  given boolean(using Layouter[Container.Primitive]): EditorFactory[Boolean] = new CheckBoxEditorFactory
  given javaBoolean(using Layouter[Container.Primitive]): EditorFactory[java.lang.Boolean] = new CheckBoxEditorFactory().transform

  given (using Layouter[Container.Primitive]): EditorFactory[String] = new TextFieldEditorFactory
  given (using Layouter[Container.Primitive]): EditorFactory[LocalDate] = new DatePickerEditorFactory
//  given (using Layouter[Container.Primitive[Option[LocalDate]], Option[LocalDate]]): EditorFactory[Option[LocalDate]] = new DatePickerOptionEditorFactory

  given [A] (using converter: ValueConverter[A])(using Layouter[Container.Primitive]): EditorFactory[A] = new ValueEditorFactory

  //  the secret sauce

  inline given derived[A >: Null <: Product] (using m: Mirror.ProductOf[A])(using Layouter[Container.Composite]): EditorFactory[A] = productEditorFactory

  //  built-in factories, exported here for discoverability

  export org.sphix.ui.editor.ClearableEditorFactory as Clearable

  export org.sphix.ui.editor.TextFieldEditorFactory as TextField
  export org.sphix.ui.editor.TextAreaEditorFactory as TextArea
  export org.sphix.ui.editor.CheckBoxEditorFactory as CheckBox
  export org.sphix.ui.editor.ListViewListEditorFactory as ListViewList
  export org.sphix.ui.editor.CheckBoxListEditorFactory as CheckBoxList
  export org.sphix.ui.editor.ListViewItemEditorFactory as ListViewItem
  export org.sphix.ui.editor.RadioItemEditorFactory as RadioItem
  export org.sphix.ui.editor.ComboBoxEditorFactory as ComboBox
  export org.sphix.ui.editor.FilesEditorFactory as Files
  
end EditorFactory

