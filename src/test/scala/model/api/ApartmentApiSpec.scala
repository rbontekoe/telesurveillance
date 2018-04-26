package model.api

import org.scalatest.FunSuite

import model.domain.Apartment
import model.domain.ApartmentId
import model.domain.ApartmentName
import model.domain.PersonId

object ApartmentApiSpec {
  val apartmentToRead = Apartment(ApartmentId(101), PersonId(0), ApartmentName(""))
  val apartmentToGet = Apartment(ApartmentId(101), PersonId(1), ApartmentName("Vesuvius"))
}

class ApartmentApiSpec extends FunSuite {
  import ApartmentApiSpec._

  test("Apartment retrieved should be " + apartmentToGet) {
    val test = ApartmentApi.read(apartmentToRead)
    assert(test.getOrElse(apartmentToRead) == apartmentToGet)
  }

  // TODO test create, update, and delete
}