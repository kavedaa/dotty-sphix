package demo.editor

import scala.xml.XML
import scala.util.*

import javafx.scene.control.*
import javafx.scene.layout.*
import javafx.event.*

import no.vedaadata.xml.*
import no.vedaadata.text.Show
import no.vedaadata.text.show


import org.sphix.*
import org.sphix.ui.dialog.*
import org.sphix.ui.editor.EditorFactory
import org.sphix.ui.FileChoosing

class ExtendedEditorDialog[A >: Null](label: Option[String] = None, value0: Option[A] = None)
  (using factory: EditorFactory[A])
  (using XmlEncoder[A], XmlDecoder[A], Show[A])
  extends Dialog[A]
  with DialogUtils[A]:

  val editor = factory.createEditor

  value0.foreach(editor.set)

  val title = label.getOrElse("")

  val content = editor.container(label).layout(false)
  
  val ok = "OK"
  
  val valid = editor.status.map(_.toBoolean)

  def result = editor.value().toOption

  setResizable(true)

  init()

  val loadButtonType = ButtonType("Load", ButtonBar.ButtonData.OTHER)
  val saveButtonType = ButtonType("Save", ButtonBar.ButtonData.OTHER)
  val clearButtonType = ButtonType("Clear", ButtonBar.ButtonData.OTHER)

  getDialogPane.getButtonTypes.addAll(loadButtonType, saveButtonType, clearButtonType)
  
  val loadButton = getDialogPane.lookupButton(loadButtonType).asInstanceOf[Button]
  val saveButton = getDialogPane.lookupButton(saveButtonType).asInstanceOf[Button]
  val clearButton = getDialogPane.lookupButton(clearButtonType).asInstanceOf[Button]

  clearButton.addEventFilter(ActionEvent.ACTION, e =>
    e.consume()
    editor.clear()
  )

  loadButton.addEventFilter(ActionEvent.ACTION, e =>
    e.consume()
    FileChoosing.withFileForOpen("XML", "xml") { file =>
      for 
        loadedXml <- Try { XML.loadFile(file) }
        decoded <- XmlDecoder.decode[A](loadedXml)
      do editor.set(decoded)
    }
  )

  saveButton.addEventFilter(ActionEvent.ACTION, e =>
    e.consume()
    FileChoosing.withFileForSave("XML", "xml") { file =>
      editor.value().toOption foreach { data =>
        val dataXml = XmlEncoder.encode(data, "Data")
        XML.save(file.getAbsolutePath, dataXml)
      }
    }
  )