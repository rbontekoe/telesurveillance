package actors

import java.nio.file.Paths

import scala.util.Failure
import scala.util.Success

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import commons.AnalyseSensorImage
import commons.StorePicture
import model.domain.ImageId
import model.domain.SensorId
import model.infrastructure.RoomRepositoryAdapter

object RoomRepositoryActor {
  
  case class RetrieveRequest(sensorId: Int)

  val sra = new RoomRepositoryAdapter

  def apply() = new RoomRepositoryActor
}

class RoomRepositoryActor extends Actor with ActorLogging {
  import RoomRepositoryActor._
  import scala.concurrent.ExecutionContext.Implicits.global

  val objectRecogntionActor = context.actorOf(Props[ObjectRecognitionActor], "recognizer")

  var time: Long = 0

  def receive = {
    
    // Save picture
    case StorePicture(sourceRef, id, time) =>
      sourceRef.onComplete({
        case Success(value) =>
          implicit val materializer = ActorMaterializer()
          value.source.runWith(FileIO.toPath(Paths.get("roomstate/" + id + "-" + time + "-photo4.jpg")))
          
          // Notify ObjectRecognitionActor
          objectRecogntionActor ! AnalyseSensorImage(SensorId(id), ImageId("photo4.jpg"))
          
        case Failure(e) => println(e.getMessage)
      })

    // Notify ObjectRecognitionActor
//    case ImageSaved(sensorId, imageId) =>
//      // inform recognizer
//      log.info("\nInforming ObjectRecognizerAdapter")
//      objectRecogntionActor ! AnalyseSensorImage(sensorId, imageId)

    case x =>
      log.warning("\nReceived unknown message: {}", x)

  }

}