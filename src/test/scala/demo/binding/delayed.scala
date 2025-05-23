package demo.binding

import scala.concurrent.duration.*

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.ui.*

object DelayedBindingDemo extends BorderPane with RegionUtils:

  val nameField = new TextField

  val nameLabel1, nameLabel2, nameLabel3, nameLabel4, nameLabel5 = new Label

  setCenter { 
    grid(
      "Enter name" -> nameField,
      "Your name is:" -> nameLabel1,
      "Your name is:" -> nameLabel2,
      "Your name is:" -> nameLabel3,
      "Your name is:" -> nameLabel4,
      "Your name is:" -> nameLabel5)
  }

  //  no delay
  nameLabel1.textProperty <== nameField.textProperty

  //  using default delay time
  nameLabel2.textProperty <== nameField.textProperty.delayed  

  {
    //  using scoped context delay time
    given Val.DelayTime = 1.second
    nameLabel3.textProperty <== nameField.textProperty.delayed
    nameLabel4.textProperty <== nameField.textProperty.delayed
  }
  
  //  using directly passed delay time
  nameLabel5.textProperty <== nameField.textProperty.delayed(using 3.seconds)

