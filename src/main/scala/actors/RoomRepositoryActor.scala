package actors

import java.nio.file.Paths

import scala.util.Failure
import scala.util.Success

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
//import commons.AnalyseSensorImage
import commons.StorePicture
import model.domain.ImageId
import model.domain.SensorId
import model.infrastructure.RoomRepositoryAdapter
import actors.ObjectRecognitionActor.AnalyseSensorImage
import java.util.Date

object RoomRepositoryActor {
  
  // Repository Adapter
  val sra = new RoomRepositoryAdapter
  
  def props = Props[ObjectRecognitionActor]
}

class RoomRepositoryActor extends Actor with ActorLogging {
  import RoomRepositoryActor._
  import scala.concurrent.ExecutionContext.Implicits.global

  val objectRecogntionActor = context.actorOf(props, "recognizer")

//  var time: Long = 0

  def receive = {
    
    // Save picture to disk
    case StorePicture(sourceRef, id, time, diff, timeLapse) =>
      sourceRef.onComplete ({
        case Success(value) =>
          println("RECEIVED\n========\nId: " + id + "\nTime: " + (new Date(time)) + "\nTimeLapse: " + (timeLapse/(1000*60)) + "\nDifference: " + diff)
          
          implicit val materializer = ActorMaterializer()
          value.source.runWith(FileIO.toPath(Paths.get("roomstate/" + id + "-" + time + "-photo4.jpg"))) 
          
          // Notify ObjectRecognitionActor
          objectRecogntionActor ! AnalyseSensorImage(SensorId(id), ImageId("photo4.jpg"), time, diff, timeLapse)
          
        case Failure(e) => println(e.getMessage)
      })

    case x =>
      log.warning("\nReceived unknown message: {}", x)

  }

}