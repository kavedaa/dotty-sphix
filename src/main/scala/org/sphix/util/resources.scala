package org.sphix.util

import org.sphix.ui.crud.*
import org.sphix.ui.editor.*
import org.sphix.ui.*

abstract class SphixTexts
  extends CrudTexts
  with EditorTexts
  with RespondingTexts

abstract class SphixIcons
  extends CrudIcons
  with EditorIcons
  with RespondingIcons