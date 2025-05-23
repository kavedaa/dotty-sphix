package org.sphix.util

import scala.util.Try

import java.util.*
import java.text.MessageFormat

abstract class I18n(basename: String, locale: Locale):

  val bundle = ResourceBundle.getBundle(basename, locale)

  def get(key: String, args: Any*): String = 
    val text = Try { bundle.getString(key) } getOrElse key
    MessageFormat.format(text, args.map(_.asInstanceOf[AnyRef])*)