package demo.editor.radioitem

import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.geometry.Orientation

import org.sphix.ui.editor.*
import org.sphix.ui.RegionUtils

enum Country:
  case Norway, Sweden, Denmark, Finland, Iceland

object RadioItemDemo extends BorderPane with RegionUtils:

  val mandatoryButton = new Button("Mandatory")
  val optionalButton = new Button("Optional")
  val mandatoryWithInitialValueButton = new Button("Mandatory with initial value")
  val optionalWithInitialValueButton = new Button("Optional with initial value")
  val mandatoryClearableButton = new Button("Mandatory - clearable")
  val optionalClearableButton = new Button("Optional - clearable")

  mandatoryButton.setOnAction: _ =>    
    new EditorFactory.RadioItem(Country.values)(_.toString).toDialog.showAndWait().ifPresent(println)

  optionalButton.setOnAction: _ =>    
    new EditorFactory.RadioItemOption(Country.values)(_.toString).toDialog.showAndWait().ifPresent(println)

  mandatoryWithInitialValueButton.setOnAction: _ =>    
    new EditorFactory.RadioItem(Country.values)(_.toString).toDialog.withInitialValue(Country.Denmark).showAndWait().ifPresent(println)

  optionalWithInitialValueButton.setOnAction: _ =>    
    new EditorFactory.RadioItemOption(Country.values)(_.toString).toDialog.withInitialValue(Some(Country.Finland)).showAndWait().ifPresent(println)

  mandatoryClearableButton.setOnAction: _ =>
    new ClearableEditorFactory(new EditorFactory.RadioItem(Country.values)(_.toString)).toDialog.showAndWait().ifPresent(println)

  optionalClearableButton.setOnAction: _ =>
    new ClearableEditorFactory(new EditorFactory.RadioItemOption(Country.values)(_.toString)).toDialog.showAndWait().ifPresent(println)

  setCenter:
    grid:
      List(
        List(mandatoryButton, optionalButton),
        List(mandatoryWithInitialValueButton, optionalWithInitialValueButton),
        List(mandatoryClearableButton, optionalClearableButton))