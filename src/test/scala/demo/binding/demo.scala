package demo.binding

import javafx.application.Application
import javafx.scene.control.*

import org.sphix.*

@main def main() = Application.launch(classOf[BindingDemo])

class BindingDemo extends SimpleApp("Binding demos"):

  val multiBindingDemo = new Tab("Multiple dependencies (mapN)"):
    setContent(MultiBindingDemo)
    setClosable(false)

  val nestedBindingDemo = new Tab("Nested dependencies (flatMap)"):
    setContent(NestedBindingDemo)
    setClosable(false)

  val delayedBindingDemo = new Tab("Delayed"):
    setContent(DelayedBindingDemo)
    setClosable(false)

  val bidirectionalBindingDemo = new Tab("Bidirectional"):
    setContent(BidirectionalBindingDemo)
    setClosable(false)

  val tabPane = new TabPane(multiBindingDemo, nestedBindingDemo, delayedBindingDemo, bidirectionalBindingDemo)

  def root = tabPane