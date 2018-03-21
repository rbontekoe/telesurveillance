package model.infrastructure

import model.api.PersonApi
import model.domain.Room
import model.domain.SensorRepositoryApi
import model.api.RoomApi

//RoomRepositoryAdapter

trait RoomLookup {
  def findRoomNbr(sensorId: Int): Option[Room]
  def findRoomNumber(sensorId: Int): Option[Room]
}

object RoomRepositoryAdapter {
  def apply = new RoomRepositoryAdapter()
}

class RoomRepositoryAdapter() extends SensorRepositoryApi with RoomLookup {
  import RoomRepositoryAdapter._

  def findRoomNbr(sensorId: Int): Option[Room] = findRoomNumber(sensorId: Int)

  def findRoomNumber(sensorId: Int): Option[Room] = RoomApi.findRoom(sensorId)

}