package org.sphix.ui.dialog

import javafx.scene.control._
import javafx.scene.layout._

import org.sphix.*
import org.sphix.control.*

class ValueDialog[A >: Null](label: Option[String] = None, value0: Option[A] = None)(using valueField: ValueField[A])
  extends Dialog[A]
  with DialogUtils[A]:

  def this(label: String)(using valueField: ValueField[A]) = this(Some(label))
  def this(label: String, value0: A)(using valueField: ValueField[A]) = this(Some(label), Some(value0))

  value0.foreach(valueField.setValue)

  val title = label.getOrElse("")

  def content = valueField
  
  val ok = "OK"
  
  val valid = valueField.value.map(_.valid)

  def result = valueField.value().toOption

  setResizable(true)

  init()
