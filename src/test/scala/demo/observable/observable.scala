package demo.observable

import javafx.application.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.collection.ObservableSeq

@main def main = Application.launch(classOf[Demo])

class Demo extends SimpleApp:

  val listView = new ListView[String]:
    setItems(ObservableSeq("One", "Two", "Three"))

  val root = listView

  val selectedItem = listView.getSelectionModel.selectedItemProperty

  val selectedOptionItem = selectedItem.map(Option.apply)

  // selectedItem.onInvalidation { _ =>
  //   println("on invalidation")
  // }

  // selectedOptionItem.onInvalidation { _ =>
  //   println("mapped on invalidation")
  // }

  // selectedOptionItem.onChange { (o, x, y) =>
  //   println(s"mapped on change from $x to $y")
  // }

  // selectedItem.onValue { x =>
  //   println(s"on value $x")
  // }

  selectedItem.map(Option.apply).onValue {
    case Some(x) =>
      println(s"mapped on value $x")
    case None => 
      println("mapped on value (no value)")
  }

