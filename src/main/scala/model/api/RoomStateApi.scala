package model.api

import model.domain.Crud
import model.domain.RoomState

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

object RoomStateApi {
  val db = Database.forConfig("chapter01")
  val rooms = TableQuery[RoomStateTable]
}

class RoomStateApi extends Crud[RoomState] {
  import RoomStateApi._
  
  def create(obj: RoomState): Boolean = {???}
  def read(obj: RoomState): Option[RoomState] = {???}
  def update(obj: RoomState): Boolean = {???}
  def delete(obj: RoomState): Boolean = {???}
}

final case class RoomStateTableRow(
  apartmentid: Int,
  sensorid: Int,
  roomid: Int,
  roomtype: String) // defined RoomTableRow

final class RoomStateTable(tag: Tag) extends Table[RoomStateTableRow](tag, "roomstate") {
  def apartmentid = column[Int]("apartmentid")
  def sensorid = column[Int]("sensorid")
  def roomid = column[Int]("roomid")
  def roomtype = column[String]("roomtype")
  def * = (apartmentid, sensorid, roomid, roomtype).mapTo[RoomStateTableRow]
} // defined RoomTable