package demo.table

import scala.util.*
import java.time.*

import no.vedaadata.generator.*

case class Data(
  string: String,
  boolean: Boolean,
  byte: Byte,
  short: Short,
  int: Int,
  long: Long,
  float: Float,
  double: Double,
  bigInt: BigInt,
  bigDecimal: BigDecimal,
  localDate: LocalDate,
  localTime: LocalTime,
  localDateTime: LocalDateTime)

object Data:

  val generator = 
    (Generator("Foo", "Bar", "And", "Little", "More"),
    Generator[Boolean],
    Generator[Byte],
    Generator[Short],
    Generator[Int],
    Generator[Long],
    Generator[Float],
    Generator[Double],
    Generator[BigInt],
    Generator[BigDecimal],
    Generator[LocalDate],
    Generator[LocalTime],
    Generator[LocalDateTime]).mapN(Data.apply)

enum Country(val code: String, val name: String, val description: String):
  case Nor extends Country("NO", "Norway", "Land of snow and ice")
  case Swe extends Country("SE", "Sweden", "Here be dragons")
  case Den extends Country("DK", "Denmark", "Rødgrøt med fløde")

case class Person(
  name: String, 
  age: Int,
  birthDate: LocalDate,
  isAdult: Option[Boolean],
  country: Option[Country],
  height: Option[Int],
  weight: Option[Long],
  rating: Option[Double],
  fortune: Try[BigDecimal],
  lastLogin: LocalDateTime,
  favoriteWebSite: Option[(String, Option[String])])

object Person:

  opaque type Height = Double
  def Height(x: Double): Height = x
  extension (x: Height) def value: Double = x

  val firstNames = List("Tom", "Bob", "Lisa", "David", "John", "Ann", "Charles", "Eric")
  val lastNames = List("Smith", "Mason", "Fogg", "Anderson", "McDuck")

  val generator = 
    ((Generator(firstNames), Generator(lastNames)) mapN { (x, y) => s"$x $y" },
      IntGenerator(20, 80),
      LocalDateGenerator(LocalDate.of(1900, 1, 1), LocalDate.of(2020, 1, 1)),
      Generator[Option[Boolean]],
      Generator.from(Country.values).andThen[Option],
      IntGenerator(100, 200).andThen[Option],
      LongGenerator(50, 200).andThen[Option],
      DoubleGenerator(0.0, 1.0).andThen[Option],
      Generator[Try[BigDecimal]],
      Generator[LocalDateTime],
      Generator[Option[(String, Option[String])]](Some("Facebook"-> Some("facebook.com")))).mapN(Person.apply)