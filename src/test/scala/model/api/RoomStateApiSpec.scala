package model.api

import org.scalatest.FunSuite

import model.domain.AlarmType
import model.domain.EmptyRoom
import model.domain.ImageId
import model.domain.NonEmptyRoom
import model.domain.Person
import model.domain.PersonId
import model.domain.PersonName
import model.domain.SensorId
import model.domain.SensorImage

object RoomStateApiSpec {
  val sensorImage = SensorImage(SensorId(10100), ImageId("Photo.jpg"))
  val alarmType = AlarmType.Warning
  val person = Person(PersonId(1), PersonName("Mrs Neeltje"))
  
  val nonEmptyRoom = NonEmptyRoom(sensorImage, alarmType, List(person))
  val emptyRoom = EmptyRoom(sensorImage)
}

class RoomStateApiSpec extends FunSuite {
  import RoomStateApiSpec._
  
  val t = new RoomStateApi

  test("A NonEmptyRoom should be created in roomstate table") {
    assert(t.create(nonEmptyRoom))
  }
  
  test("An EmptyRoom should be created in roomstate table") {
    assert(t.create(emptyRoom))
  }
}