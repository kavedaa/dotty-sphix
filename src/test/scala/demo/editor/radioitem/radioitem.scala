package demo.editor.radioitem

import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.geometry.Orientation

import org.sphix.ui.editor.EditorFactory
import org.sphix.ui.RegionUtils

enum Country:
  case Norway, Sweden, Denmark, Finland, Iceland

object RadioItemDemo extends BorderPane with RegionUtils:

  val mandatoryButton = new Button("Mandatory")
  val optionalButton = new Button("Optional")
  val mandatoryButtonWithInitialValue = new Button("Mandatory with initial value")
  val optionalButtonWithInitialValue = new Button("Optional with initial value")

  mandatoryButton.setOnAction: _ =>    
    new EditorFactory.RadioItem(Country.values)(_.toString).toDialog.showAndWait().ifPresent(println)

  optionalButton.setOnAction: _ =>    
    new EditorFactory.RadioItemOption(Country.values)(_.toString).toDialog.showAndWait().ifPresent(println)

  mandatoryButtonWithInitialValue.setOnAction: _ =>    
    new EditorFactory.RadioItem(Country.values)(_.toString).toDialog.withInitialValue(Country.Denmark).showAndWait().ifPresent(println)

  optionalButtonWithInitialValue.setOnAction: _ =>    
    new EditorFactory.RadioItemOption(Country.values)(_.toString).toDialog.withInitialValue(Some(Country.Finland)).showAndWait().ifPresent(println)

  setCenter:
    grid:
      List(
        List(mandatoryButton, optionalButton),
        List(mandatoryButtonWithInitialValue, optionalButtonWithInitialValue))