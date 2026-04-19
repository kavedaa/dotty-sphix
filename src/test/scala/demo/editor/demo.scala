package demo.editor

import java.time.LocalDate

import javafx.application.*
import javafx.scene.control.*
import javafx.scene.layout.*

import no.vedaadata.xml.*

import org.sphix.*
import org.sphix.ui.*
import org.sphix.ui.editor.*
import org.sphix.ui.dialog.*
import org.sphix.collection.ObservableSeq
import org.sphix.util.ComboBoxFactory

import demo.editor.primitive.PrimitiveDemo
import demo.editor.composite.CompositeDemo
import demo.editor.option.OptionDemo
import demo.editor.combobox.ComboBoxDemo
import demo.editor.table.TableDemo
import demo.editor.dynamic.DynamicDemo
import demo.editor.clearable.ClearableDemo
import demo.editor.files.FilesDemo
import demo.editor.radioitem.RadioItemDemo
import demo.editor.dialog.DialogDemo

// enum PetKind:
//   case Dog, Cat, Rabbit

// object PetKind:
//   given (PetKind => String) = _.toString

// case class Pet(kind: PetKind, isMammal: Boolean, birthDate: LocalDate)
// // case class Pet(name: String, age: Int)

// enum Hobby(val name: String, val description: String):
//   case Golf extends Hobby("Golf", "Hitting balls with clubs")
//   case Tennis extends Hobby("Tennis", "Hitting balls with rackets")
//   case Chess extends Hobby("Chess", "Moving pieces on a board")  

// object Hobby:
//   given (Hobby => String) = _.name

// enum City:
//   case Oslo, Bergen, London, Berlin

// case class Country(code: String, name: String)

// object Country:
//   val Norway = Country("NO", "Norway")
//   val Sweden = Country("SE", "Sweden")
//   val Denmark = Country("DK", "Denmark")
//   val items = List(Norway, Sweden, Denmark)

// case class Company(name: String, founded: Option[LocalDate])

// case class Person(
//   name: String,
// //  age: Option[Int])
//   pets: List[Pet],
//   hobbies: List[Hobby])

// case class Human(
//   name: String,
//   age: Int,
//   pet: Pet)

@main def main = Application.launch(classOf[Demo])

class Demo extends SimpleApp:

  // val hobbies = Hobby.values.to(ObservableSeq)

  // val petKinds = PetKind.values.to(ObservableSeq)

  // val cities = City.values.to(ObservableSeq)

  // given petKindEditor: EditorFactory[PetKind] = new ComboBoxEditorFactory(petKinds)
  // given hobbyEditor: EditorFactory[List[Hobby]] = new ListViewListEditorFactory(hobbies)
  // given EditorFactory[Hobby] = new RadioItemEditorFactory(hobbies)(_.name)
  // given EditorFactory[City] = ListViewItemEditorFactory(cities)

  // given EditorFactory[List[Pet]] = new DynamicEditorFactory(2)

  // val pane = new BorderPane with RegionUtils:
  
  //   val map = Map(
  //     PetKind.Dog -> Seq(Hobby.Golf, Hobby.Tennis),
  //     PetKind.Cat -> Hobby.values.toSeq
  //   )

  //   val editor = new KeyValuesEditorFactory(map)(_.toString)(_.toString).createEditor

  //   val button = new Button("OK") {
  //     setDefaultButton(true)
  //   }

  //   button.disableProperty <== editor.status.map(!_.toBoolean)

  //   editor.value.onValue(println)

  //   button setOnAction { _ =>
  //     println(editor.value())
  //   }

  //   val content = vbox(editor.container(None).layout, button)
  //   setCenter(content)    

  val primitiveDemo = new Tab("Primitive"):
    setClosable(false)
    setContent(PrimitiveDemo)

  val comboBoxDemo = new Tab("ComboBox"):
    setClosable(false)
    setContent(ComboBoxDemo)

  val radioItemDemo = new Tab("RadioItem"):
    setClosable(false)
    setContent(RadioItemDemo)

  val compositeDemo = new Tab("Composite"):
    setClosable(false)
    setContent(CompositeDemo)

  val optionDemo = new Tab("Option"):
    setClosable(false)
    setContent(OptionDemo)

  val tableDemo = new Tab("Table"):
    setClosable(false)
    setContent(TableDemo)

  val dynamicDemo = new Tab("Dynamic"):
    setClosable(false)
    setContent(DynamicDemo)

  val clearableDemo = new Tab("Clearable"):
    setClosable(false)
    setContent(ClearableDemo)

  val filesDemo = new Tab("Files"):
    setClosable(false)
    setContent(FilesDemo)

  val dialogDemo = new Tab("Dialog"):
    setClosable(false)
    setContent(DialogDemo)
    
  // val listButton = new Button("List"):
  //   setOnAction { _ =>
  //     given EditorFactory[Country] = EditorFactory.ListViewItem(Country.items)(_.name)
  //     new EditorDialog[Country]("Please select a country").showAndWait().ifPresent(println)
  //   }

  // val comboButton = new Button("Combo"):
  //   setOnAction { _ =>
  //     given EditorFactory[Hobby] = EditorFactory.ComboBox(hobbies)(using new ComboBoxFactory.Searchable)
  //     new EditorDialog[Hobby]("Please select a hobby").showAndWait().ifPresent(println)
  //   }
  // val framesButton = new Button("Frames"):
  //   setOnAction { _ =>
  //     // given EditorFactory[PetKind] = EditorFactory.ComboBox[PetKind](PetKind.values)
  //     // val dialog = new EditorDialog[Human]
  //     // dialog.showAndWait().ifPresent(println)
  //   }

  // val layout1Button = new Button("Layout1"):
  //   case class Person(name: String, age: Option[Int])
  //   case class People(boss: Person, janitor: Person)
  //   case class Company(name: String, people: People)
  //   setOnAction { _ =>
  //     given Layouter.Strategy = Layouter.Strategy.Horizontal
  //     given Layouter.Frame = Layouter.Frame.Border
  //     EditorDialog[Company]().showAndWait().ifPresent(println)
  //   }
  // val toolbar = ToolBar(
  //   primitiveButton, 
  //   compositeButton,
  //   listButton, 
  //   comboButton, 
  //   dynamicButton, 
  //   dynamicCompositeButton, 
  //   framesButton, 
  //   dynamic2Button, 
  //   dynamic3Button, 
  //   layout1Button)

  override def stylesheet = Some(getClass.getResource("editor.css").toExternalForm)

  val root = new TabPane(
    primitiveDemo, 
    comboBoxDemo, 
    radioItemDemo,
    compositeDemo, 
    optionDemo, 
    tableDemo, 
    dynamicDemo, 
    clearableDemo,
    filesDemo,
    dialogDemo)

//  override def stylesheet = Some("style.css")

end Demo

// class PersonEditor extends ProductEditor[Person]:

//   type C = PersonContainer

// //  private def editor[A : EditorFactory] = summon[EditorFactory[A]].createEditor

//   val petKinds = PetKind.values.to(ObservableSeq)
//   val hobbies = Hobby.values.to(ObservableSeq)

//   given petKindEditor: EditorFactory[PetKind] = new ComboBoxEditorFactory[PetKind](petKinds)
//   given hobbyEditor: EditorFactory[Hobby] = new ComboBoxEditorFactory[Hobby](hobbies)

//   given EditorFactory[List[Pet]] = new DynamicEditorFactory(2)

//   val nameEditor = Editor[String]
//   val petsEditor = Editor[List[Pet]]
//   val hobbiesEditor = Editor[Hobby]

//   def elemEditors = List(nameEditor, petsEditor, hobbiesEditor).asInstanceOf[List[Editor[Any]]]

//   def container(label: Option[String]) = PersonContainer(this)

// class PersonContainer(val editor: PersonEditor) extends Container with FormUtils:
//   def layout(isTopLevel: Boolean, onLayoutChange: Option[() => Unit] = None) = 
//     vbox(
//       editor.nameEditor.container(Some("Name")).layout(false),
//       hbox(
//         editor.petsEditor.container(Some("Pets")).layout(false),
//         editor.hobbiesEditor.container(Some("Hobbies")).layout(false)))