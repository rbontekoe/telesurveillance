package model.domain

import org.scalatest.FunSuite

object Aggregates {
  val room = Room(RoomId(101), RoomType.Livingroom, ApartmentId(101), SensorId(10100))

  val person = Person(PersonId(1), PersonName("Mrs Neeltje"))

  val apartment = Apartment(ApartmentId(room.apartmentId.value), person.personId, ApartmentName("Vesuvius"))

  val emptyRoom = EmptyRoom(SensorImage(room.sensorId, ImageId("Photo.jpg")))

  val nonEmptyRoom = NonEmptyRoom(SensorImage(room.sensorId, ImageId("Photo.jpg")), AlarmType.Info, List(person))
}

class RoomSpec extends FunSuite {
  import Aggregates._

  test("the id of the room should be 101") {
    assert(room.roomId.value == 101)
  }

  test("the type of the room should be Livingroom") {
    assert(room.roomType == RoomType.Livingroom)
  }

  test("the apartment id of the room should be 101") {
    assert(room.apartmentId.value == 101)
  }

  test("the sensor id of the room should be 10100") {
    assert(room.sensorId.value == 10100)
  }

}

class PersonSpec extends FunSuite {
  import Aggregates._

  test("the id of the person should be 1") {
    assert(person.personId.value == 1)
  }

  test("the name of the person should be Mrs Neeltje") {
    assert(person.name.value == "Mrs Neeltje")
  }
}

class ApartmentSpec extends FunSuite {
  import Aggregates._

  test("the id of the apartment should be 101") {
    assert(room.apartmentId.value == 101)
  }

  test("the id of the habitant should be 1") {
    assert(apartment.personId.value == 1)
  }

  test("the name of the apartment should be: Vesuvius") {
    assert(apartment.name.value == "Vesuvius")
  }

}

class EmptyRoomSpec extends FunSuite {
    import Aggregates._

    test("the sensorId should be 10100") {
      assert(emptyRoom.sensorImage.sensorId.value == 10100)
    }

  }

class NonEmptyRoomSpec extends FunSuite {
    import Aggregates._
    
    test("the sensorId should be 10100") {
      assert(nonEmptyRoom.sensorImage.sensorId.value == 10100)
    }
    
    test("the alarmType should be Info") {
      assert(nonEmptyRoom.alarmType == AlarmType.Info)
    }
    
    test("the personId should be 1") {
      assert(nonEmptyRoom.persons(0).personId.value == 1)
    }
    
    test("the personName should be Mrs Neeltje") {
      assert(nonEmptyRoom.persons(0).name.value == "Mrs Neeltje")
    }
  }