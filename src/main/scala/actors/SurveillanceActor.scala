package actors

import akka.actor.Actor
import model.domain.EmptyRoom
import model.domain.NonEmptyRoom

object SurveillanceActor {
  
//  def apply(surveillanceActor: ActorRef) = new SurveillanceActor
}

class SurveillanceActor extends Actor {
  
  def receive = {
    case EmptyRoom(sensorImage) => 
      println(EmptyRoom(sensorImage))
    case NonEmptyRoom(sensorImage, alarmType, persons) => 
      println("SurveilanceActor: " + NonEmptyRoom(sensorImage, alarmType, persons))
    case x =>
      println("Received unknown message")
  }
  
}