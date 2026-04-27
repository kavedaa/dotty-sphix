package org.sphix.ui.editor

import javafx.scene.image.Image

trait EditorTexts:
  def Add: String
  def Remove: String
  def Clear: String

trait EditorIcons:
  def Add: Option[Image]
  def Remove: Option[Image]
  def Clear: Option[Image]

