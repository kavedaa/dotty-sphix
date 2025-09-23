package org.sphix.messagebus

trait Message



//  given MessageBus = new MessageDispatcher(bus)


// trait MessageTranceiver(using MessageBus) extends MessageReceiver with MessageSender


// class MessageDispatcher(bus: MessageBus) extends MessageBus with MessageReceiver:
//   val receive =
//     case message: Message => bus.send(this, message)


class MessageBus:

  private val receivers = collection.mutable.Set[MessageBus.Receiver]()

  def register(receiver: MessageBus.Receiver): Unit =
    onRegister(receiver)
    receivers += receiver

  def unregister(receiver: MessageBus.Receiver): Unit =
    onUnregister(receiver)
    receivers -= receiver

  def send(source: MessageBus.Source, message: Message): Unit =
    onSend(message)
    for receiver <- receivers if source.obj ne receiver do
      onReceive(message)
      receiver.receive.applyOrElse(message, _ => ())

  def getReceivers(): List[MessageBus.Receiver] = receivers.toList

  //  overrideable hooks

  def onRegister(receiver: MessageBus.Receiver): Unit = {}
  def onUnregister(receiver: MessageBus.Receiver): Unit = {}
  def onSend(message: Message): Unit = {}
  def onReceive(message: Message): Unit = {}


object MessageBus:

  case class Source(obj: AnyRef)

  trait Sender:
    given Source = Source(this)

  trait Receiver(using bus: MessageBus):
    val receive: PartialFunction[Message, Unit]
    def unregisterMessageBusReceiver() = bus.unregister(this)
    bus.register(this)

  def send(message: Message)(using bus: MessageBus, source: Source): Unit =
    bus.send(source, message)

