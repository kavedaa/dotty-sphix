package demo.messagebus

import javafx.stage.*

import org.sphix.messagebus.MessageBus

class ChatStage(isObnoxious: Boolean)(using MessageBus)(using owner: Window) extends Stage:

  initOwner(owner)

  val chatPane = if isObnoxious then new ObnoxiousChatPane else new FriendlyChatPane

  val scene = new javafx.scene.Scene(chatPane)
  setTitle("Chat")
  setScene(scene)

  setOnCloseRequest: _ =>
    chatPane.unregisterMessageBusReceiver()
    println("ChatStage closed and ChatPane unregistered")
