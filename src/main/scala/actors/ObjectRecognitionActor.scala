package actors

import actors.ObjectRecognitionActor.AnalyseSensorImage
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

object ObjectRecognitionActor{
  case class AnalyseSensorImage(sensorId: SensorId, imageId: ImageId)
  case class SensorImageAnalysed(persons: List[Person])

  val recognizerAdapter = new ObjectRecognitionAdapter 
}

class ObjectRecognitionActor extends Actor with ActorLogging {
  import ObjectRecognitionActor._

  //TODO actor should to be defined more global
  val surveillanceActor = context.actorOf(Props[SurveillanceActor], "surveillant")

  def receive = {
    case AnalyseSensorImage(sensorId, imageId) =>
      log.info("\nStart analysing image")
      // Analyse image
      val persons = recognizerAdapter.analyseImage(sensorId, imageId)
      // Send analysis to surveillant
      log.info("\nInform surveillant about room status")
      if (persons.isEmpty)
        surveillanceActor ! EmptyRoom(SensorImage(sensorId, imageId))
      else
        surveillanceActor ! NonEmptyRoom(SensorImage(sensorId, imageId), AlarmType.Warning, persons)

    case x =>
      log.warning(s"Received unknown message: $x")
  }
}