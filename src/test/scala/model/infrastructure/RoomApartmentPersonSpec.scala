package model.infrastructure

import org.scalatest.FunSuite

import model.api.ApartmentApi
import model.api.RoomApi
import model.domain.Apartment
import model.domain.ApartmentId
import model.domain.ApartmentName
import model.domain.Person
import model.domain.PersonId
import model.domain.PersonName
import model.domain.Room
import model.domain.RoomId
import model.domain.RoomType
import model.domain.SensorId

object RoomApartmentPerson {

  val sensorId = 10100

  val room = Room(RoomId(15), RoomType.Livingroom, ApartmentId(101), SensorId(sensorId))
  val apartment = Apartment(ApartmentId(101), PersonId(1), ApartmentName("Vesuvius"))
  val person = Person(PersonId(1), PersonName("Mrs Neeltje"))

  val rra = new RoomRepositoryAdapter

  val result = for {
    room <- rra.findRoomNumber(sensorId)
    apartment <- RoomApi.getApartment(room.apartmentId.value)
    person <- ApartmentApi.getPerson(apartment.personId.value)
  } yield (room, apartment, person)
}

class RoomApartmentPersonRead extends FunSuite {
  import RoomApartmentPerson._

  test("Room retrieved should be " + room) {
    assert(!result.filter(p => p._1 == room).isEmpty)
  }

  test("Apartment retrieved should be " + apartment) {
    assert(!result.filter(p => p._2 == apartment).isEmpty)
  }

  test("Person retrieved should be " + person) {
    assert(!result.filter(p => p._3 == person).isEmpty)
  }
}