package demo.messagebus

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.ui.dialog.InfoDialog
import org.sphix.messagebus.*

case class ChatMessage(text: String) extends Message

abstract class ChatPane(using MessageBus) extends BorderPane with MessageBus.Sender with MessageBus.Receiver:

  val textField = new TextField
  val button = new Button("Send")
  val messageLabel = new Label("No messages yet")

  val content = VBox(10, HBox(10, textField, button), messageLabel)

  setCenter(content)

  button.setOnAction: _ =>
    val message = ChatMessage(textField.getText)
    MessageBus.send(message)


class FriendlyChatPane(using MessageBus) extends ChatPane:

  val receive =
    case ChatMessage(text) =>
      println(s"ChatPane received: $text")
      messageLabel.setText(s"Got message: $text")


class ObnoxiousChatPane(using MessageBus) extends ChatPane:

  val receive =
    case ChatMessage(text) =>
      println(s"ObnoxiousChatPane received: $text")
      messageLabel.setText(s"HEY! I GOT YOUR MESSAGE: $text!!!")
      InfoDialog("ObnoxiousChatPane", s"HEY! I GOT YOUR MESSAGE: $text!!!").showAndWait()
