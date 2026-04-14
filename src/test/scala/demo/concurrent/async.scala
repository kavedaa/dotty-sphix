package demo.concurrent

import scala.util.*
import scala.concurrent.ExecutionContext.Implicits.global

import javafx.scene.layout.* 
import javafx.scene.control.* 

import org.sphix.*
import org.sphix.concurrent.Async
import org.sphix.control.ValueField
import org.sphix.ui.RegionUtils

class AsyncDemo extends BorderPane with RegionUtils:

  val inputA, inputB = new ValueField[Int]

  val resultLabel = new Label

  val input = (inputA.value, inputB.value).tupled.delayed

  val asyncAdder = Async(input): x =>
    x match  
      case (Value.Valid(a), Value.Valid(b)) => Success(slowAdd(a, b))
      case _ => Failure(IllegalArgumentException("Invalid input"))

  def slowAdd(a: Int, b: Int) =
    Thread.sleep(2000)
    a + b

  resultLabel.textProperty <== asyncAdder.status.map:
    case Async.Status.Succeeded(Success(y)) => y.toString
    case Async.Status.Succeeded(Failure(ex)) => ex.getMessage
    case Async.Status.Failed(ex) => ex.getMessage
    case _ => null

  resultLabel.graphicProperty <== asyncAdder.status.map:
    case Async.Status.Running => new ProgressIndicator { setPrefSize(16, 16) }
    case _ => null

  val content = hbox(inputA, Label("+"), inputB, Label("="), resultLabel)

  setCenter(content)
