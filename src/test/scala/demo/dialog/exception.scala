package demo.dialog.exception

import javafx.application.Application
import javafx.scene.control.*

import org.sphix.*
import org.sphix.util.*
import org.sphix.ui.dialog.*

@main def main = Application.launch(classOf[Demo])

class CustomException(message: String) extends Exception(message)

class Demo extends SimpleApp:

  val basicButton = new Button("Basic")
  val decoratedButton = new Button("Decorated")
  val customButton = new Button("Custom")
  val nestedButton = new Button("Nested")
  val deeplyNestedButton = new Button("Deeply nested")

  basicButton.setOnAction: _ =>
    val exception = new Exception("This is an exception")
    ExceptionDialog(exception).showAndWait()

  decoratedButton.setOnAction: _ =>
    val exception = new Exception("This is an exception")
    ExceptionDialog("This is the title", "This is the header", exception).showAndWait()

  customButton.setOnAction: _ =>    
    val customException = new CustomException("This is a custom exception")
    ExceptionDialog(customException).showAndWait()

  nestedButton.setOnAction: _ =>
    val exception = new Exception("This is an exception with a cause", new Exception("This is the cause"))
    ExceptionDialog(exception).showAndWait()

  deeplyNestedButton.setOnAction: _ =>
    val exception = new Exception("This is an exception with a cause", new Exception("This is the cause", new CustomException("This is the root cause")))
    ExceptionDialog(exception).showAndWait()

  def root = ToolBar(basicButton, decoratedButton, customButton, nestedButton, deeplyNestedButton)

