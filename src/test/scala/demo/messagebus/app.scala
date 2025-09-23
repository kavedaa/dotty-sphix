package demo.messagebus

import javafx.application.Application
import javafx.scene.Scene
import javafx.stage.*

import javafx.scene.control.*
import javafx.scene.layout.*

import org.sphix.messagebus.*
import org.sphix.messagebus.MessageBus.Receiver

object MessageBusDemo:
  def main(args: Array[String]) =
    Application.launch(classOf[MessageBusDemoApp])


class MessageBusDemoApp extends Application:


  given messageBus: MessageBus = new MessageBus:

    override def onRegister(receiver: Receiver) =
      println(s"Registered receiver: $receiver")

    override def onUnregister(receiver: Receiver) =
      println(s"Unregistered receiver: $receiver")

    override def onSend(message: Message) =
      println(s"Sending message: $message")

    override def onReceive(message: Message) =
      println(s"Receiving message: $message")


  def start(stage: Stage) =

    given Window = stage

    val chatPane1 = new FriendlyChatPane

    val newButton = new Button("New Chat Window")
    val newObnoxious = new Button("New Obnoxious Chat Window")

    val printReceiversButton = new Button("Print Receivers")

    newButton.setOnAction: _ =>
        val chatStage = new ChatStage(isObnoxious = false)
        chatStage.show()

    newObnoxious.setOnAction: _ =>
        val chatStage = new ChatStage(isObnoxious = true)
        chatStage.show()

    printReceiversButton.setOnAction: _ =>
      messageBus.getReceivers().foreach(println)

    val root = VBox(10, chatPane1, newButton, newObnoxious, printReceiversButton)

    val scene = new Scene(root, 400, 200)
    stage.setTitle("MessageBus Demo")
    stage.setScene(scene)
    stage.show()

