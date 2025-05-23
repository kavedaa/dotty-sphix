package org.sphix.ui.responding

import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.scene.image.*

import org.sphix.ui.crud.*

trait RespondingUtils(using texts: CrudTexts, icons: CrudIcons):

  class FilterButtons(numSuccesses: Int, numFailures: Int):

    val buttonGroup = new ToggleGroup

    val successesButton = new ToggleButton(texts.NumOperationsSucceeded(numSuccesses)):
      icons.IsSuccess.foreach(x => setGraphic(ImageView(x)))
      setDisable(numSuccesses == 0)
      setToggleGroup(buttonGroup)

    val failuresButton = new ToggleButton(texts.NumOperationsFailed(numFailures)):
      icons.IsFailure.foreach(x => setGraphic(ImageView(x)))
      setDisable(numFailures == 0)
      setToggleGroup(buttonGroup)

    val buttonBar = HBox(5, successesButton, failuresButton)
