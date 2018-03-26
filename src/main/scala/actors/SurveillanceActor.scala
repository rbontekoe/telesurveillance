package actors

import akka.actor.Actor
import model.domain.EmptyRoom
import model.domain.NonEmptyRoom
import model.api.RoomStateApi

object SurveillanceActor {
  
//  def apply(surveillanceActor: ActorRef) = new SurveillanceActor
}

class SurveillanceActor extends Actor {
  
  val roomStateApi = new RoomStateApi()
  
  def receive = {
    case EmptyRoom(sensorImage) => 
      println(EmptyRoom(sensorImage))
      roomStateApi.create(EmptyRoom(sensorImage))  
    case NonEmptyRoom(sensorImage, alarmType, persons) => 
      println("SurveilanceActor: " + NonEmptyRoom(sensorImage, alarmType, persons))
      roomStateApi.create(NonEmptyRoom(sensorImage, alarmType, persons))
    case x =>
      println("Received unknown message")
  }
  
}