package demo.datepicker

import javafx.application.Application
import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.ui.FormUtils

@main def main = Application.launch(classOf[Demo])

class Demo extends SimpleApp with FormUtils:

  val datePicker = new DatePicker    

  val datePicker1 = new DatePicker    
  datePicker1.setEditable(false)

  val button = new Button("Test")

  datePicker.valueProperty.onValue(s => println("Value: " + s))
  datePicker.getEditor.textProperty.onValue(s => println("Text: " + s))

  button.setOnAction: _ =>
    println("Got the value: " + datePicker.getValue)
    println("Got the text: " + datePicker.getEditor.getText)

  val root = vbox(datePicker, datePicker1, button)


