package functional

import model.infrastructure.RoomRepositoryAdapter
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.dispatch.OnSuccess

object RoomApartmentPerson extends App {

  val sensorId = 10100
  val rra = new RoomRepositoryAdapter
  
  /*
   * Result should be:
   * 
   * Room(RoomId(15),Livingroom,ApartmentId(101),SensorId(10100))
   * Apartment(ApartmentId(101),PersonId(1),ApartmentName(Vesuvius))
   * Person(PersonId(1),PersonName(Mrs Neeltje))
   */
  
  val result = for {
    room <- rra.findRoomNumber(sensorId)
    apartment <- room.getApartment(room.apartmentId.value)
    person <- apartment.getPerson(apartment.personId.value)
  } yield (room, apartment, person)
  
  result match {
    case Some(result) => {
      println(result._1)
      println(result._2)
      println(result._3)
    }
    case None => println("Nothing")
  }
  
  Thread.sleep(5000)
}