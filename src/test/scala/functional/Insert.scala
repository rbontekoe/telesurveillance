package functional

import model.api.RoomStateApi
import model.domain.AlarmType
import model.domain.PersonId
import model.domain.ImageId
import model.domain.PersonName
import model.domain.NonEmptyRoom
import model.domain.Person
import model.domain.SensorId
import model.domain.SensorImage

object Insert extends App {
  val roomStateApi = new RoomStateApi()
  
  val room = NonEmptyRoom(
      SensorImage(SensorId(10101), ImageId("photo.jpg")), 
      AlarmType.Warning, 
      List(Person(PersonId(1), PersonName("Rob")))
  )
      
  val result = roomStateApi.create(room)
  
  println(result)
}