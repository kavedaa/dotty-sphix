package demo.editor.resolver

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.ui.dialog.EditorDialog
import org.sphix.ui.editor.*

enum Person:
  case Bob, Tom, Joe

val candidates = Map(
  Person.Bob -> List("Bob", "Rob", "Bobby"),
  Person.Tom -> List("Tommy", "Tomm"),
  Person.Joe -> List("Josh", "Joseph"))

// object ResolverDemo extends BorderPane:

//   val resolverButton = new Button("Resolver")

//   given ResolverTexts with
//     def item = "The person"
//     def candidate = "The persons's preferred name"

//   resolverButton.setOnAction: _ =>
//     ResolverEditorFactory(Person.values.toList, candidates)(_.toString, _.toString)
//       .toDialog
//       .withInitialValue(Map(Person.Tom -> "Tommy"))
//       .showAndWait()
//       .ifPresent(println)

//   val toolbar = ToolBar(resolverButton)

//   setCenter(toolbar)