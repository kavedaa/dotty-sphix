package org.sphix.util

import javafx.scene.image.*

trait ImageResolver extends (String => Image)

class ResourceImageResolver(cls: Class[?], f: String => String) extends ImageResolver:  
  def apply(filename: String) = 
    val name = f(filename)
    cls.getResourceAsStream(f(filename)) match
      case null => throw new Exception(s"Resolving $name returned null")
      case is => new Image(is)

given (using resolver: ImageResolver): Conversion[String, Image] = 
  x => resolver(x)

given (using resolver: ImageResolver): Conversion[String, ImageView] = 
  x => new ImageView(resolver(x))

given Conversion[Image, ImageView] = 
  x => new ImageView(x)

