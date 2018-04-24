package actors

import akka.actor.Actor
import model.domain.EmptyRoom
import model.domain.NonEmptyRoom
import model.api.RoomStateApi
import akka.actor.ActorLogging

object SurveillanceActor {
  
}

class SurveillanceActor extends Actor with ActorLogging {
  
  val roomStateApi = new RoomStateApi()
  
  def receive = {
    case EmptyRoom(sensorImage) => 
      roomStateApi create(EmptyRoom(sensorImage)) 
      log.info("\nNonEmptyRoom sensorImage: {}", sensorImage)
    case NonEmptyRoom(sensorImage, alarmType, persons) => 
      roomStateApi create(NonEmptyRoom(sensorImage, alarmType, persons))
      log.info("\nNonEmptyRoom sensorImage: {}, alarmType: {}, person(s): {}", 
        sensorImage, alarmType, persons)
//      println(NonEmptyRoom(sensorImage, alarmType, persons))
    case x =>
      log.info("Received unknown message {}", x)
  }
  
}