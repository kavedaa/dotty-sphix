package org.sphix.util

import scala.util.*

import scala.deriving.*
import scala.compiletime.*

import java.util.Properties
import java.time.*
import java.time.format.*

trait PropertiesReader[+A]:
  def read(key: String, properties: Properties): Try[A]

trait PropertiesWriter[-A]:
  def write(x: A, key: String, properties: Properties): Unit  

object PropertiesReader:

  inline def readProduct[P <: Product](properties: Properties)(using m: Mirror.ProductOf[P]): Tuple.Map[m.MirroredElemTypes, Try] = 
    val readers = summonAll[Tuple.Map[m.MirroredElemTypes, PropertiesReader]].toList.asInstanceOf[List[PropertiesReader[Any]]]
    val keys = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
    val list = keys.zip(readers) map { (key, reader) => reader.read(key, properties) }
    val tuple = list.foldRight[Tuple](EmptyTuple)(_ *: _)
    tuple.asInstanceOf[Tuple.Map[m.MirroredElemTypes, Try]]

  given PropertiesReader[String] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties)

  given PropertiesReader[Boolean] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap {
        case "true" => Success(true)
        case "false" => Success(false)
        case x => Failure(PropertiesException.InvalidFormat(key, "boolean", x))
      }

  given PropertiesReader[Byte] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(x.toByte).recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "byte", x))
        }  
      }

  given PropertiesReader[Short] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(x.toShort).recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "short", x))
        }  
      }

  given PropertiesReader[Int] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(x.toInt).recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "integer", x))
        }  
      }

  given PropertiesReader[Long] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(x.toLong).recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "long", x))
        }  
      }

  given PropertiesReader[Float] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(x.toFloat).recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "float", x))
        }  
      }

  given PropertiesReader[Double] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(x.toDouble).recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "double", x))
        }  
      }

  given PropertiesReader[BigInt] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(BigInt(x)).recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "big integer", x))
        }  
      }

  given PropertiesReader[BigDecimal] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(BigDecimal(x)).recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "big decimal", x))
        }  
      }

  given PropertiesReader[LocalDate] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(LocalDate.parse(x, dateFormatter)) recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "date", x))
        }
      }

  given PropertiesReader[LocalTime] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(LocalTime.parse(x, dateFormatter)) recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "time", x))
        }
      }

  given PropertiesReader[LocalDateTime] with
    def read(key: String, properties: Properties) =
      getProperty(key, properties) flatMap { x =>
        Try(LocalDateTime.parse(x, dateFormatter)) recoverWith {
          case ex => Failure(PropertiesException.InvalidFormat(key, "datetime", x))
        }
      }

  private def getProperty(key: String, properties: Properties): Try[String] =
    Option(properties.getProperty(key)) match 
      case Some(x) => Success(x)
      case None => Failure(PropertiesException.PropertyNotFound(key))

end PropertiesReader

object PropertiesWriter:

  inline def writeProduct[P <: Product](p: P, properties: Properties)(using m: Mirror.ProductOf[P]): Unit = 
    val writers = summonAll[Tuple.Map[m.MirroredElemTypes, PropertiesWriter]].toList.asInstanceOf[List[PropertiesWriter[Any]]]
    val keys = constValueTuple[m.MirroredElemLabels].toList.asInstanceOf[List[String]]
    val xs = p.productIterator.toList
    xs.zip(keys).zip(writers) map { case ((x, key), writer) => writer.write(x, key, properties) }

  given PropertiesWriter[String] with
    def write(x: String, key: String, properties: Properties) =
      properties.setProperty(key, x)

  given PropertiesWriter[Boolean] with
    def write(x: Boolean, key: String, properties: Properties) =
      properties.setProperty(key, x.toString)

  given PropertiesWriter[Byte] with
    def write(x: Byte, key: String, properties: Properties) =
      properties.setProperty(key, x.toString)

  given PropertiesWriter[Short] with
    def write(x: Short, key: String, properties: Properties) =
      properties.setProperty(key, x.toString)

  given PropertiesWriter[Int] with
    def write(x: Int, key: String, properties: Properties) =
      properties.setProperty(key, x.toString)

  given PropertiesWriter[Long] with
    def write(x: Long, key: String, properties: Properties) =
      properties.setProperty(key, x.toString)

  given PropertiesWriter[Float] with
    def write(x: Float, key: String, properties: Properties) =
      properties.setProperty(key, x.toString)

  given PropertiesWriter[Double] with
    def write(x: Double, key: String, properties: Properties) =
      properties.setProperty(key, x.toString)

  given PropertiesWriter[BigInt] with
    def write(x: BigInt, key: String, properties: Properties) =
      properties.setProperty(key, x.toString)

  given PropertiesWriter[BigDecimal] with
    def write(x: BigDecimal, key: String, properties: Properties) =
      properties.setProperty(key, x.toString)

  given PropertiesWriter[LocalDate] with
    def write(x: LocalDate, key: String, properties: Properties) =
      properties.setProperty(key, dateFormatter.format(x))

  given PropertiesWriter[LocalTime] with
    def write(x: LocalTime, key: String, properties: Properties) =
      properties.setProperty(key, timeFormatter.format(x))

  given PropertiesWriter[LocalDateTime] with
    def write(x: LocalDateTime, key: String, properties: Properties) =
      properties.setProperty(key, dateTimeFormatter.format(x))

end PropertiesWriter

enum PropertiesException(message: String) extends Exception(message):
  case PropertyNotFound(key: String) extends PropertiesException(s"Property $key not found")
  case InvalidFormat(key: String, dataType: String, value: String) extends PropertiesException(s"Invalid $dataType format $value for property $key")

private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
