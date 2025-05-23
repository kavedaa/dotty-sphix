package demo.fields

import javafx.application.*
import javafx.scene.control.*
import javafx.scene.layout.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.ui.*

@main def main = Application.launch(classOf[Demo])

class Demo extends SimpleApp:

  val stringField = new ValueField[String]
  val intField = new ValueField[Int]
  val doubleField = new ValueField[Double]
  val charField = new ValueField[Char]

  val stringOptionField = new ValueField[Option[String]]
  val intOptionField = new ValueField[Option[Int]]


  val stringLabel, intLabel, doubleLabel, charLabel, stringOptionLabel, intOptionLabel = new Label

  stringLabel.textProperty <== stringField.value.map(_.toString)
  intLabel.textProperty <== intField.value.map(_.toString)
  doubleLabel.textProperty <== doubleField.value.map(_.toString)
  charLabel.textProperty <== charField.value.map(_.toString)

  stringOptionLabel.textProperty <== stringOptionField.value.map(_.toString)
  intOptionLabel.textProperty <== intOptionField.value.map(_.toString)


  val pane = new BorderPane with FormUtils:
    val content = grid(
      "String" -> hbox(stringField, stringLabel),
      "Int" -> hbox(intField, intLabel),
      "Double" -> hbox(doubleField, doubleLabel),
      "Char" -> hbox(charField, charLabel),
      "Option[String]" -> hbox(stringOptionField, stringOptionLabel),
      "Option[Int]" -> hbox(intOptionField, intOptionLabel)
    )
    setCenter(content)
    setPrefWidth(500)

  def root = pane