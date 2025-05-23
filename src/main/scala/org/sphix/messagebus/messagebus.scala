package org.sphix.messagebus

trait Message


trait MessageReceiver(using bus: MessageBus):

  val receive: PartialFunction[Message, Unit]

  bus.register(this)

//  given MessageBus = new MessageDispatcher(bus)


trait MessageSender(using bus: MessageBus):
  def send(message: Message): Unit =
    bus.send(this, message)


trait MessageTranceiver(using MessageBus) extends MessageReceiver with MessageSender


// class MessageDispatcher(bus: MessageBus) extends MessageBus with MessageReceiver:
//   val receive =
//     case message: Message => bus.send(this, message)


class MessageBus:

  private val receivers = collection.mutable.Set[MessageReceiver]()

  def register(receiver: MessageReceiver): Unit =
    receivers += receiver

  def unregister(receiver: MessageReceiver): Unit =
    receivers -= receiver

  def send(source: AnyRef, message: Message): Unit =
    for receiver <- receivers if source ne receiver do
      receiver.receive.applyOrElse(message, _ => ())


object Test:

  case class TestMessage(text: String) extends Message

  class TestReceiver(using MessageBus) extends MessageReceiver:
    val receive = 
      case message: TestMessage => println(s"TestReceiver received: $message")
