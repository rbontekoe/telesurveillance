package functional

import model.infrastructure.RoomRepositoryAdapter

object RoomApartmentPerson extends App {
  // A test to discover room, apartment, and person data
  val sensorId = 10100

  /*
   * Result should be:
   * 
   * Room(RoomId(15),Livingroom,ApartmentId(101),SensorId(10100))
   * Apartment(ApartmentId(101),PersonId(1),ApartmentName(Vesuvius))
   * Person(PersonId(1),PersonName(Mrs Neeltje))
   */

  
  val rra = new RoomRepositoryAdapter
  // Find the room with sensorid 10100
  val res = rra.findRoomNumber(sensorId)
  res match {
    case Some(room) => {
      println(room)
      // Get apartment
      val apartment = room.getApartment(room.apartmentId.value)
      apartment match {
        case Some(apartment) => {
          println(apartment)
          // Get person (owner/inhabitant)
          val person = apartment.getPerson(apartment.personId.value)
          person match {
            case Some(p) => {
              println(p)
            }
            case None => println("No person found")
          }
        }
        case None => println("No apartment found")
      }
    }
    case None => println("No room found")
  } // End test
}