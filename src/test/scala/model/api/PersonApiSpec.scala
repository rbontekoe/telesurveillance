package model.api

import org.scalatest.FunSuite

import model.domain.Person
import model.domain.PersonId
import model.domain.PersonName


object PersonApiSpec {
  val personToRead = Person(PersonId(1), PersonName(""))
  val personToGet = Person(PersonId(1), PersonName("Mrs Neeltje"))
}

class PersonApiSpec extends FunSuite {
  import PersonApiSpec._

  test("Person retrieved should be " + personToGet) {
    val test = PersonApi.read(personToRead)
    assert(test.getOrElse(personToRead) == personToGet)
  }

  // TODO test create, update, and delete
}