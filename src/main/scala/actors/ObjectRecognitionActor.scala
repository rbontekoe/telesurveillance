package actors

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import model.domain.AlarmType
import model.domain.EmptyRoom
import model.domain.ImageId
import model.domain.NonEmptyRoom
import model.domain.Person
import model.domain.SensorId
import model.domain.SensorImage
import model.infrastructure.ObjectRecognitionAdapter
//import commons.AnalyseSensorImage

object ObjectRecognitionActor{
  sealed trait ObjectRecognitionActorMsg
  case class AnalyseSensorImage(sensorId: SensorId, imageId: ImageId, time: Long, difference: Long, timeLapse: Long) extends ObjectRecognitionActorMsg
  
  val recognizerAdapter = new ObjectRecognitionAdapter 
  
  def props = Props[SurveillanceActor]
}

class ObjectRecognitionActor extends Actor with ActorLogging {
  
  import ObjectRecognitionActor._

  //TODO actor should to be defined more global
  val surveillanceActor = context.actorOf(props, "surveillant")

  def receive = {
    case AnalyseSensorImage(sensorId, imageId, time, difference, timeLapse) =>
      // Analyse image
      log.info("\nStart analysing image")  
      val persons = recognizerAdapter.analyseImage(sensorId, imageId)
      
      // Send analysis to surveillant
      log.info("\nInform surveillant about room status")
      if (persons.isEmpty)
        surveillanceActor ! EmptyRoom(SensorImage(sensorId, imageId))
      else {
        val alarmType =  
          if (difference < 200 && timeLapse > 4 * 60 * 1000) AlarmType.Severe
          else if (difference < 5000 && difference > 200) AlarmType.Info 
          else if (difference >= 5000) AlarmType.Warning
          else AlarmType.Error
        println("Alarmtype: " + alarmType)
        println(" ")
        surveillanceActor ! NonEmptyRoom(SensorImage(sensorId, imageId), alarmType, persons)
      }

    case x =>
      log.warning("Received unknown message: {}", x)
  }
}