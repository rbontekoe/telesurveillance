package model.api

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._

import model.domain.ApartmentId
import model.domain.Crud
import model.domain.Room
import model.domain.RoomId
import model.domain.RoomType
import model.domain.SensorId
import slick.jdbc.PostgresProfile.api._
import model.domain.Apartment

/*
 * Room repository
 */
trait RoomApi {
  def findRoom(sensorId: Int): Option[Room]
}

object RoomApi extends Crud[Room] with RoomApi{

  val db = Database.forConfig("chapter01")
  val rooms = TableQuery[RoomTable]

  def findRoom(sensorId: Int): Option[Room] = {
    val selectedRoom = rooms.filter(_.sensorid === sensorId)
    val roomsAction: DBIO[Seq[RoomTableRow]] = selectedRoom.result
    val roomsFuture: Future[Seq[RoomTableRow]] = db.run(roomsAction)
    val roomsResults = Await.result(roomsFuture, 2.seconds)
    
    if (!roomsResults.isEmpty) {
      val res = roomsResults(0)
      Option(Room(RoomId(res.roomid), RoomType.textToRoomType(res.roomtype), ApartmentId(res.apartmentid), SensorId(sensorId)))
    } else None
    
  } // defined findRoom by sensorId

  // TODO
  def create(room: Room): Boolean = { ??? }
  def read(room: Room): Option[Room] = { ??? }
  def update(room: Room): Boolean = { ??? }
  def delete(room: Room): Boolean = { ??? }
} // defined RoomApi functions/methods

final case class RoomTableRow(
  apartmentid: Int,
  sensorid: Int,
  roomid: Int,
  roomtype: String) // defined RoomTableRow

final class RoomTable(tag: Tag) extends Table[RoomTableRow](tag, "room") {
  def apartmentid = column[Int]("apartmentid")
  def sensorid = column[Int]("sensorid")
  def roomid = column[Int]("roomid")
  def roomtype = column[String]("roomtype")
  def * = (apartmentid, sensorid, roomid, roomtype).mapTo[RoomTableRow]
} // defined RoomTable