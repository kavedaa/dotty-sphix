package org.sphix.util

import javafx.scene.control.ComboBox

import org.controlsfx.control.SearchableComboBox

trait ComboBoxFactory[A]:
  def stringConverter: javafx.util.StringConverter[A]
  def create(): ComboBox[A]

object ComboBoxFactory:

  given default[A >: Null](using converter: HalfConverter[A, String]): ComboBoxFactory[A] = new Default

  def apply[A >: Null](comboBox: => ComboBox[A])(using converter: HalfConverter[A, String]): ComboBoxFactory[A] = 
    new ComboBoxFactory[A]:
      def stringConverter = converter.toStringConverter
      def create() = comboBox

  class Default[A >: Null](using val converter: HalfConverter[A, String]) extends ComboBoxFactory[A]:
    def stringConverter = converter.toStringConverter
    def create() = new ComboBox[A]

  class Searchable[A >: Null](using val converter: HalfConverter[A, String]) extends ComboBoxFactory[A]:
    def stringConverter = converter.toStringConverter
    def create() = new SearchableComboBox[A]

  class Editable[A >: Null](using val converter: Converter[A, String]) extends ComboBoxFactory[A]:
    def stringConverter = converter.toStringConverter
    def create() = new ComboBox[A]:
      setEditable(true)


