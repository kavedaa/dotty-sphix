package org.sphix.ui.dialog

import scala.util.*
import scala.jdk.FunctionConverters.*

import java.time.LocalDate

import javafx.scene.Node
import javafx.scene.control._
import javafx.scene.layout._
import javafx.scene.control.Dialog
import javafx.scene.control.ButtonBar.ButtonData

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.*
import org.sphix.util.*

//  all of these for backwards comp.

class InputDialog[A, N <: Node](val inputControl: N, title: String, inputTitle: String, contentDisplay: ContentDisplay)
  extends Dialog[A] { dialog =>

  val content =
    new GridPane {
      setHgap(10)
      setVgap(10)
      val label = new Label(inputTitle)
      getChildren.addAll(label, inputControl)
      GridPane.setHgrow(inputControl, Priority.ALWAYS)
      contentDisplay match {
        case ContentDisplay.RIGHT =>
          GridPane.setColumnIndex(inputControl, 1);
        case _ =>
          GridPane.setRowIndex(inputControl, 1);
      }
    }

  setTitle(title)
  setResizable(false)
  getDialogPane.setContent(content)

  val okButtonType = new ButtonType("OK", ButtonData.OK_DONE)

  getDialogPane.getButtonTypes.addAll(ButtonType.CANCEL, okButtonType)

  val okButton = (getDialogPane.lookupButton(okButtonType)).asInstanceOf[Button]
}

class TextFieldDialog[T](converter: RightConverter[T, String], title: String, inputTitle: String)
  extends InputDialog[Option[T], TextField](new TextField, title, inputTitle, ContentDisplay.RIGHT) {

  val value = inputControl.textProperty.map(converter.deconvert)

  val valueDefined = value.map(_.isEmpty)
  okButton.disableProperty <== valueDefined.as

  setResultConverter { (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType) value()
    else null
  }

  def input(initValue: Option[T] = None) = {
    initValue foreach (v => inputControl.setText(converter.convert(v)))
    inputControl.requestFocus()
    val res = showAndWait()
    if (res.isPresent) res.get else None
  }
}

class TextAreaDialog(title: String, inputTitle: String)
  extends InputDialog[String, TextArea](new TextArea, title, inputTitle, ContentDisplay.BOTTOM) {

  setResultConverter { (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType) inputControl.getText
    else null
  }

  def input(initValue: Option[String] = None) = {
    initValue foreach (v => inputControl.setText(v))
    inputControl.requestFocus()
    val res = showAndWait()
    if (res.isPresent) Some(res.get) else None
  }
}

class ComboBoxDialog[T](cellFactory: => ListCell[T], title: String, inputTitle: String)
  extends InputDialog[Option[T], ComboBox[T]](new ComboBox[T], title, inputTitle, ContentDisplay.RIGHT) {

  inputControl.setMinWidth(150)
  inputControl.setCellFactory((_: ListView[T]) => cellFactory)
  inputControl.setButtonCell(cellFactory)

  okButton.disableProperty <== inputControl.getSelectionModel.selectedItemProperty.isNull

  def f(i: Int) = if (i == 0) inputControl.getSelectionModel.getSelectedItem else null

  setResultConverter { (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType) Some(inputControl.getSelectionModel.getSelectedItem)
    else null
  }

  def input(items: Iterable[T], initValue: Option[T] = None) = {
    val xs = items.to(ObservableSeq)
    inputControl.setItems(xs) //  TODO why we need this??
    initValue foreach (v => inputControl.getSelectionModel.select(v))
    inputControl.requestFocus()
    val res = showAndWait()
    if (res.isPresent) res.get else None
  }
}

class CheckBoxesDialog[T](title: String, inputTitle: String, values: Seq[T], selected: Seq[T], render: T => String)
  extends InputDialog[Seq[T], VBox](new VBox(10), title, inputTitle, ContentDisplay.BOTTOM) {

  val cbs = values map { v =>
    v -> new CheckBox(render(v)) {
      setSelected(selected contains v)
    }
  }

  val xs = (cbs.map (_._2).to(ObservableSeq))   //  TODO why we need this??
  inputControl.getChildren.setAll(xs)

  setResultConverter { (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType) cbs filter (_._2.isSelected) map (_._1)
    else null
  }

  def input(): Option[Seq[T]] = {
    inputControl.requestFocus()
    val res = showAndWait()
    if (res.isPresent) Some(res.get) else None
  }
}

class RadioDialog[T](title: String, inputTitle: String, values: Seq[T], selected: Option[T])(render: T => String)
  extends InputDialog[Option[T], VBox](new VBox(10), title, inputTitle, ContentDisplay.BOTTOM) {

  val group = new ToggleGroup

  val cbs = values map { v =>
    v -> new RadioButton(render(v)) {
      setToggleGroup(group)
      setSelected(selected contains v)
    }
  }

  val xs = (cbs.map (_._2).to(ObservableSeq))   //  TODO why we need this??
  inputControl.getChildren.setAll(xs)

  setResultConverter { (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType) cbs.filter(_._2.isSelected).headOption map (_._1)
    else null
  }

  def input(): Option[T] = {
    inputControl.requestFocus()
    val res = showAndWait()
    if (res.isPresent) res.get else None
  }
}

class PasswordDialog(title: String, inputTitle: String, authenticator: String => Boolean)
  extends InputDialog[Boolean, PasswordField](new PasswordField, title, inputTitle, ContentDisplay.BOTTOM) {

  val correct = inputControl.textProperty.delayed.map(authenticator.asJavaFunction)

  okButton.disableProperty <== correct.map(x => !x)

  setResultConverter { (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType) correct()
    else false
  }

  def input() = {
    inputControl.requestFocus()
    val res = showAndWait()
    if (res.isPresent) res.get else false
  }
}

class DatePickerDialog(title: String, date0: Option[LocalDate] = None) extends Dialog[LocalDate] {

  val picker = new DatePicker

  date0 foreach picker.setValue

  setTitle(title)
  setResizable(false)
  getDialogPane.setContent(picker)

  val okButtonType = new ButtonType("OK", ButtonData.OK_DONE)

  getDialogPane.getButtonTypes.addAll(ButtonType.CANCEL, okButtonType)
  setResultConverter { (dialogButton: ButtonType) =>
    if (dialogButton == okButtonType) picker.getValue
    else null
  }

}

class ErrorDialog(title: Option[String], header: Option[String], message: String)
  extends Dialog[Nothing] {

  def this(title: String, message: String) = this(Some(title), None, message)
  def this(title: Option[String], message: String) = this(title, None, message)
  def this(message: String) = this(None, None, message)

  val textArea= new TextArea {
    setEditable(false)
    setWrapText(true)
  }

  title foreach setTitle
  header foreach getDialogPane.setHeaderText
  textArea.setText(message)
  
  getDialogPane.setContent(textArea)

  //  a little trick to get the default graphic used in alerts
  val img = new Label
  img.getStyleClass.addAll("alert", "error", "dialog-pane")
  setGraphic(img)

  getDialogPane.getButtonTypes.add(ButtonType.CLOSE)

  setResizable(true)
}

class ErrorsDialog[A](title: String, errors: Seq[(A, Failure[?])])(render: A => String)
  extends Dialog[Nothing] {

  val table = new TableView[(A, Failure[?])] with TableUtils[(A, Failure[?])] {

    val item = new Column("Item", x => Val(render(x._1)))
    val error = new Column("Error", x => Val(x._2.exception.getMessage))

    getColumns.addAll(item, error)

    setItems(errors.toObservableList)
  }

  setTitle(title)
  getDialogPane.setHeaderText(title)
  getDialogPane.setContent(table)

  getDialogPane.getButtonTypes.add(ButtonType.CLOSE)

  getDialogPane.setPrefWidth(800)

  setResizable(true)
}

class TrysDialog[A](title: String, xs: Iterable[Try[A]])(render: A => String)
  extends Dialog[Nothing] {

  val list = new ListView[Try[A]] with ListUtils[Try[A]] {

    setCell {
      TextCell({
        case Success(value) => render(value)
        case Failure(ex) => ex.getMessage
      })
    }

    setItems(xs.toObservableList)
  }

  setTitle(title)
  getDialogPane.setHeaderText(title)
  getDialogPane.setContent(list)

  getDialogPane.getButtonTypes.add(ButtonType.CLOSE)

  getDialogPane.setPrefWidth(800)

  setResizable(true)
}

class ContentDialog(title: String, content: Node)
  extends Dialog[Nothing] {

  setTitle(title)
  getDialogPane.setContent(content)

  getDialogPane.getButtonTypes.add(ButtonType.CLOSE)

  setResizable(true)
}

