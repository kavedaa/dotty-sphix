package demo.binding

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.ui.*

object NestedBindingDemo extends BorderPane with RegionUtils:

  val nameField1, nameField2, nameField3 = new TextField

  val group = new ToggleGroup

  val Radio1 = new RadioButton("1"):
    setToggleGroup(group)
  val Radio2 = new RadioButton("2"):
    setToggleGroup(group)
  val Radio3 = new RadioButton("3"):
    setToggleGroup(group)

  val info = new Label

  setCenter { 
    vbox(
      grid(
        "1" -> nameField1,
        "2" -> nameField2,
        "3" -> nameField3),
      hbox(Radio1, Radio2, Radio3),
      info)
  }

  //  here we show flatMap

  info.textProperty <== group.selectedToggleProperty flatMap { 
    case Radio1 => nameField1.textProperty
    case Radio2 => nameField2.textProperty
    case Radio3 => nameField3.textProperty
    case _ => Val("nothing selected")
  }