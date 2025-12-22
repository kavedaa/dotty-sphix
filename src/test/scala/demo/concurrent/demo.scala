package demo.concurrent

import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*

@main def main = Application.launch(classOf[ConcurrentDemo])

class ConcurrentDemo extends SimpleApp2("Concurrent demo"):

  def root(stage: Stage) = 
    
    val modalTab = new Tab("FutureModal"):
      setContent(new ModalDemo(stage))
      setClosable(false)

    val asyncTab = new Tab("Async"):
      setContent(new AsyncDemo)
      setClosable(false)

    new TabPane(modalTab, asyncTab)


