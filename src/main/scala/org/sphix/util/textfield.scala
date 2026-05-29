package org.sphix.util

import javafx.scene.control.*

import org.controlsfx.control.textfield.TextFields

trait TextFieldFactory:
  def create(): TextField

object TextFieldFactory:

  given default: TextFieldFactory = Default

  def apply(textField: => TextField): TextFieldFactory = 
    new TextFieldFactory:
      def create() = textField

  val Default = TextFieldFactory(new TextField)

  val Password = TextFieldFactory(new PasswordField)

  val Clearable = TextFieldFactory(TextFields.createClearableTextField())

end TextFieldFactory