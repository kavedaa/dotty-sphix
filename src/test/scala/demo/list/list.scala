package demo.list

import scala.language.implicitConversions

import java.time.LocalDate

import javafx.application.Application
import javafx.scene.control.*

import org.sphix.*
import org.sphix.control.*
import org.sphix.collection.ObservableSeq
import org.sphix.collection.ObservableSeq.*

case class Hobby(name: String, description: String)

@main def main = Application.launch(classOf[ListDemo])

class ListDemo extends SimpleApp:

  val hobbies = ObservableSeq(
    Hobby("Golf", "Hitting balls with clubs"),
    Hobby("Tennis", "Hitting balls with rackets"),
    Hobby("Chess", "Moving pieces on a board")
  )

  val list = new ListView[Hobby] with ListUtils[Hobby]:
    setCell(new StringCell(using _.name) {})

  list.setItems(hobbies)

  def root = list

