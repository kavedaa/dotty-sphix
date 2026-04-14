package demo.table.derived

import no.vedaadata.generator.*

enum Animal:
  case Dog, Cat, Bird

case class Pet(
  name: String,
  animal: Animal)

case class Person(
  name: String, 
  age: Int,
  salary: BigDecimal,
  pet: Pet)

object Person:

  val petNames = List("Cracker", "Firefly", "Dustin", "Grump", "Yoyo")
  val names = List("Tom", "Bob", "Lisa", "David", "John", "Ann", "Charles", "Eric")

  given Generator[Pet] = (Generator.from(petNames), Generator.from(Animal.values.toList)).mapN(Pet.apply)

  given generator: Generator[Person] = (Generator.from(names), Generator.between(20, 80), Generator.between(10000L, 1500000L).map(BigDecimal.apply), Generator[Pet]).mapN(Person.apply)