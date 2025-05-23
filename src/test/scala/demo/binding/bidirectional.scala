package demo.binding

import scala.util.*

import javafx.scene.layout.*
import javafx.scene.control.*

import org.sphix.*
import org.sphix.util.*
import org.sphix.binding.*
import org.sphix.control.*
import org.sphix.ui.*

object BidirectionalBindingDemo extends BorderPane with RegionUtils:

  val field1, field2 = new TextField
  val field3, field4 = new TextField
  val field5, field6 = new TextField

  setCenter { 
    grid(
      "Upper/lower case" -> hbox(field1, field2),
      "Reverse" -> hbox(field3, field4),
      "Celcius / Fahrenheit" -> hbox(field5, field6))
  }

  val caseConverter = new Converter[String, String]:
    def convert(x: String) = Some(x.toUpperCase)
    def deconvert(x: String) = Some(x.toLowerCase)

  bindBidirectionalWithConverter(field1.textProperty, field2.textProperty)(using caseConverter)

  val reverseConverter = new Converter[String, String]:
    def convert(x: String) = Some(x.reverse)
    def deconvert(x: String) = Some(x.reverse)

  bindBidirectionalWithConverter(field3.textProperty, field4.textProperty)(using reverseConverter)

  val temperatureConverter = new Converter[Double, Double]:
    def convert(x: Double) = Some(x * 9 / 5 + 32)
    def deconvert(x: Double) = Some((x - 32) * 5 / 9)

  val doubleConverter = new Converter[String, Double]:
    def convert(x: String) = Try(x.toDouble).toOption
    def deconvert(x: Double) = Some(x.toString)

  val stringTemperatureConverter = doubleConverter.andThen(temperatureConverter).andThen(doubleConverter.inverse)

   bindBidirectionalWithConverter(field5.textProperty, field6.textProperty)(using stringTemperatureConverter)
