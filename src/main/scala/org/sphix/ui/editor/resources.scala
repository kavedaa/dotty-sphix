package org.sphix.ui.editor

import javafx.scene.image.Image

trait EditorTexts:
  def Add: String
  def Remove: String
  def Clear: String

object EditorTexts:
  given Default: EditorTexts with
    def Add = "+"
    def Remove = "-"
    def Clear = "x"

trait EditorIcons:
  def Add: Option[Image]
  def Remove: Option[Image]
  def Clear: Option[Image]

object EditorIcons:
  given Default: EditorIcons with
    def Add = None
    def Remove = None
    def Clear = None