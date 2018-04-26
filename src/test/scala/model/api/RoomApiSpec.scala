package model.api

import org.scalatest.FunSuite

import model.domain.ApartmentId
import model.domain.Room
import model.domain.RoomId
import model.domain.RoomType
import model.domain.SensorId

object RoomApiSpec {
  val sensorId = 10100
  val roomToRead = Room(RoomId(0), RoomType.Unkown, ApartmentId(0), SensorId(sensorId))
  val roomToGet = Room(RoomId(15), RoomType.Livingroom, ApartmentId(101), SensorId(sensorId))
}

class RoomApiSpec extends FunSuite {
  import RoomApiSpec._
  
  test("Room retrieved should be " + roomToGet) {
    val test = RoomApi.findRoom(sensorId)
    assert(test.getOrElse(roomToRead) == roomToGet)
  }
  
  // TODO test read, create, update, and delete
}