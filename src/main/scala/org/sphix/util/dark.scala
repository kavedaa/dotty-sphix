package org.sphix.util

import com.sun.javafx.css.StyleManager

object DarkMode:

  private val styleSheet = "org/sphix/css/dark.css"

  /**
    * Sets dark mode by adding a stylesheet.
    */
  def setDarkMode(): Unit =
    StyleManager.getInstance.addUserAgentStylesheet(styleSheet)

  /**
    * Unsets dark mode by removing a stylesheet.
    */
  def unsetDarkMode(): Unit =
    StyleManager.getInstance.removeUserAgentStylesheet(styleSheet)