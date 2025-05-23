package org.sphix.binding

import javafx.beans.*
import javafx.beans.property.Property

import org.sphix.util.*
      
def bindBidirectional[A, B](property1: Property[A], property2: Property[B], bidir: BidirectionalFunction[A, B]) = 
   bindBidirectionalWithConverter(property1, property2)(using bidir.toConverter)

def bindBidirectionalWithConverter[A, B](property1: Property[A], property2: Property[B])(using converter: Converter[A, B]) = 
  new BidirectionalConverterBinding(property1, property2, converter)

class BidirectionalConverterBinding[A, B] private[binding] (property1: Property[A], property2: Property[B], converter: Converter[A, B]) 
  extends InvalidationListener:

  var isUpdating = false

  def invalidated(source: Observable) =
    if !isUpdating then
      isUpdating = true
      if source eq property1 then
        val newValue = property1.getValue()
        converter.convert(newValue) match
          case Some(convertedValue) =>
            property2.setValue(convertedValue)
          case None =>
            //  ignore
      else if source eq property2 then
        val newValue = property2.getValue()
        converter.deconvert(newValue) match
          case Some(convertedValue) =>
            property1.setValue(convertedValue)
          case None =>
            //  ignore
      isUpdating = false

  converter.deconvert(property2.getValue()).foreach(property1.setValue)
  property1.addListener(this)
  property2.addListener(this)

  def unbind() =
    property1.removeListener(this)
    property2.removeListener(this)
          
