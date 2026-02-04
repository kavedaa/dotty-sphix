package demo.responding

import scala.util.*

import javafx.application.*
import javafx.scene.control.*
import javafx.scene.image.Image
import org.sphix.util.*

import no.vedaadata.generator.*

import org.sphix.*
import org.sphix.ui.*
import org.sphix.ui.crud.*

case class Person(name: String, age: Int)

object Person:
  val generator = (Generator("Alice", "Bob", "Carl"), IntGenerator(20, 50)).mapN(Person.apply)
  given (Person => String) = _.name

@main def main = Application.launch(classOf[Demo])

class Demo extends SimpleApp:

  val getIcon = ResourceImageResolver(getClass, "/icons/" + _)

  val successIcon = getIcon("accept.png")
  val failureIcon = getIcon("cancel.png")

  given CrudTexts = CrudTexts.Default

  given CrudIcons = new CrudIcons.Default:
    override def IsSuccess = Some(successIcon)
    override def IsFailure = Some(failureIcon)

  val defaultButton = new Button("A")
  val optionButton = new Button("Option[A]")
  val tryButton = new Button("Try[A]")
  val optionTryButton = new Button("Option[Try[A]]")
  val unitTryButton = new Button("Try[Unit]")
  val tryIterableButton = new Button("Iterable[Try[A]]")
  val itemTryIterableButton = new Button("Iterable[(A, Try[A])]")
  val itemUnitTryIterableButton = new Button("Iterable[(A, Try[Unit])]")

  val root = new ToolBar(defaultButton, optionButton, tryButton, optionTryButton, unitTryButton, tryIterableButton, itemTryIterableButton, itemUnitTryIterableButton)

  defaultButton.setOnAction: _ =>
    val res = Generator("Foo").generate(1).head
    println(res)
    Responding.respondWith(x => s"Generated $x")(res)

  optionButton.setOnAction: _ =>
    val res = Generator("Foo").andThen[Option].generate(1).head
    println(res)
    Responding.respondWith(x => s"Generated $x")(res)

  tryButton.setOnAction: _ =>
    val res = Generator("Foo").andThen[Try].generate(1).head
    println(res)
    Responding.respondWith(x => s"Generated $x")(res)

  optionTryButton.setOnAction: _ =>
    val res = Generator("Foo").andThen[Try].andThen[Option].generate(1).head
    println(res)
    Responding.respondWith(x => s"Generated $x")(res)

  unitTryButton.setOnAction: _ =>
    val res = Generator(()).andThen[Try].generate(1).head
    println(res)
    Responding.respondWith(x => s"Generated $x")(res)

  tryIterableButton.setOnAction: _ =>
    val res = Generator(1, 2, 3).andThen[Try].generate(10)
    println(res)
    Responding.respondWith(x => s"Generated $x")(res)

  itemTryIterableButton.setOnAction: _ =>
    val res = (Person.generator, Generator[Try[Int]]).mapN((x, t) => x -> t.map(_ => x)).generate(3)
    println(res)
    Responding.respondWith(x => s"Generated $x")(res)

  itemUnitTryIterableButton.setOnAction: _ =>
    val res = 
      (Person.generator, Generator[Boolean])
        .mapN: (x, t) => 
          x -> { if t then Success(()) else Failure(Exception("Ooops")) }
        .generate(3)
    println(res)
    Responding.respondWith(x => s"Okay $x")(res)

  // val res: Try[Seq[Try[String]]] = ???
  // Responding.respond(res)