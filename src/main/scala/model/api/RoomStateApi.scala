package model.api

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import model.domain.AlarmType
import model.domain.Crud
import model.domain.EmptyRoom
import model.domain.ImageId
import model.domain.NonEmptyRoom
import model.domain.Person
import model.domain.PersonId
import model.domain.PersonName
import model.domain.RoomState
import model.domain.SensorId
import model.domain.SensorImage
import slick.jdbc.PostgresProfile.api._
import java.util.Date
import slick.dbio.DBIOAction
import model.domain.RoomState
import scala.util.Success
import scala.util.Failure

object RoomStateApi {
  val db = Database.forConfig("chapter01")
  val roomStates = TableQuery[RoomStateTable]

  def apply = new RoomStateApi
}

class RoomStateApi extends Crud[RoomState] {
  import RoomStateApi._

  def create(roomState: RoomState): Boolean = {
    // Create table row
    val rowValues = roomState match {
      case EmptyRoom(sensorImage) =>
        RoomStateTableRow(new Date().getTime, sensorImage.sensorId.value, sensorImage.imageId.value, "", 0)
      case NonEmptyRoom(sensorImage, alarmType, personList) =>
        RoomStateTableRow(new Date().getTime, sensorImage.sensorId.value, sensorImage.imageId.value, alarmType.toString(), personList(0).personId.value)
    }
    // Save table row
    try {
      val insertAction = DBIO.seq(roomStates += rowValues)
      val insertFuture = db.run(insertAction)
      val insertResult = Await.result(insertFuture, 2 seconds)
      true
    } catch {
      case e: Exception =>
        println(e)
        false
    } 
//    finally {
//      db.close
//    }

  }

  def read(roomState: RoomState): Option[RoomState] = { ??? }

  def readList(sensorId: Int): Option[Seq[RoomState]] = {
    val selectedRoomState = roomStates.filter(_.sensorid === sensorId)
    val roomStateAction: DBIO[Seq[RoomStateTableRow]] = selectedRoomState.result
    val roomStateFuture: Future[Seq[RoomStateTableRow]] = db.run(roomStateAction)
    val roomStateResult = Await.result(roomStateFuture, 2 seconds)

    if (!roomStateResult.isEmpty)
      Option(
        roomStateResult.map(f =>
          if (f.personid != 0)
            NonEmptyRoom(
            SensorImage(SensorId(f.sensorid), ImageId(f.imageId)),
            AlarmType.Warning,
            List(Person(PersonId(f.personid), PersonName(""))))
          else
            EmptyRoom(
              SensorImage(SensorId(f.sensorid), ImageId(f.imageId)))))
    else
      None
  }

  def update(roomState: RoomState): Boolean = { ??? }
  def delete(roomState: RoomState): Boolean = { ??? }
}

final case class RoomStateTableRow(
  date: Long,
  sensorid: Int,
  imageId: String,
  alarmtype: String,
  personid: Int) // defined RoomStateTableRow

final class RoomStateTable(tag: Tag) extends Table[RoomStateTableRow](tag, "roomstate") {
  def date = column[Long]("date")
  def sensorid = column[Int]("sensorid")
  def imageid = column[String]("imageid")
  def alarmtype = column[String]("alarmtype")
  def personid = column[Int]("personid")
  def * = (date, sensorid, imageid, alarmtype, personid).mapTo[RoomStateTableRow]
} // defined RoomStateTable