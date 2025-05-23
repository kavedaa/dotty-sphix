package demo.binding

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.ui.*
import javafx.beans.binding.ObjectBinding
import javafx.beans.binding.Bindings
import javafx.beans.binding.StringBinding
import javafx.beans.Observable
import javafx.beans.InvalidationListener

object MultiBindingDemo extends BorderPane with RegionUtils:

  val nameField, cityField = new TextField
  val ageField = ValueField[Int]

  val info = new Label
  val button = new Button("Print")

  setCenter { 
    vbox(
      grid(
        "Name" -> nameField, 
        "Age" -> ageField, 
        "City" -> cityField), 
      info,
      button)
  }

  // abstract class StrongBinding[A](xs: Seq[Observable]) extends ObjectBinding[A]: 
  //   val listener: InvalidationListener = o => invalidate()
  //   xs.foreach(_.addListener(listener))

  // def binding1 = new StrongBinding[String](Seq(nameField.textProperty, cityField.textProperty)):    
  //   def computeValue() = nameField.textProperty.getValue + cityField.textProperty.getValue    

  // binding1.onValue(x => println(s"binding1 $x"))

  // def binding2 = Bindings.createObjectBinding((new java.util.concurrent.Callable[String]:
  //   def call = nameField.textProperty.getValue + cityField.textProperty.getValue),
  //   nameField.textProperty, cityField.textProperty)

  // binding2.onValue(x => println(s"binding2 $x"))

  // Bindings.createObjectBinding((new java.util.concurrent.Callable[String]:
  //   def call = nameField.textProperty.getValue + cityField.textProperty.getValue),
  //   nameField.textProperty, cityField.textProperty).onValue(x => println(s"binding3 $x"))

  // Bindings.concat(nameField.textProperty, cityField.textProperty).onValue(x => println(s"binding4 $x"))

  // nameField.textProperty.map(_.toUpperCase).onValue(x => println(s"map $x"))

  // def binding5 = new com.sun.javafx.binding.MappedBinding[String, String](nameField.textProperty, _.toUpperCase)

  // binding5.onValue(x => println(s"binding5 $x"))

//  val tupled = (nameField.textProperty, ageField.value, cityField.textProperty).tupled

  //  TODO this works
//  tupled onValue println

  //  but not this
//  (nameField.textProperty, ageField.value, cityField.textProperty).onValueN(println)

  //  nor this
  // (nameField.textProperty, ageField.value, cityField.textProperty).tupled.onValue(println)

  // //  nor this
  // Var[Unit](()) <== (nameField.textProperty, ageField.value, cityField.textProperty).tupled.map(_ => println("map"))


  // Var("") <== (nameField.textProperty, ageField.value, cityField.textProperty).mapN { (x, _, _) => println("mapN"); x }

  // (nameField.textProperty, ageField.value, cityField.textProperty).mapN { (name, age, city) =>
  //   println("mapN3")  
  // }


  val text = (nameField.textProperty, ageField.value, cityField.textProperty).mapN { (name, age, city) =>
//    println("mapN2")
    val ageText = age match {
      case Value.Valid(x) => s"your age is $x"
      case _ => "you won't disclose your age"
    }
    s"Hello, your name is $name and $ageText and you live in $city"  
  }

  info.textProperty <== text

  button.setOnAction: _ =>
    println(text())
