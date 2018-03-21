package actors

import java.nio.file.Paths

import actors.ObjectRecognitionActor.AnalyseSensorImage
import actors.PictureManager.ImageSaved
import akka.NotUsed
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.Props
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import akka.stream.scaladsl.Keep
import akka.stream.scaladsl.Source
import akka.util.ByteString
import model.infrastructure.RoomRepositoryAdapter

object RoomRepositoryActor {
  case class RetrieveRequest(sensorId: Int)
  
  val sra = new RoomRepositoryAdapter

  def apply() = new RoomRepositoryActor
}

class RoomRepositoryActor extends Actor with ActorLogging {
  import RoomRepositoryActor._

  val objectRecogntionActor = context.actorOf(Props[ObjectRecognitionActor], "recognizer")

  def receive = {

    // Stream picture
    case i: ByteString =>
      log.info("\nStart receiving image")
      implicit val materializer = ActorMaterializer()

      val source: Source[ByteString, NotUsed] = Source.single(i)

      val sink = FileIO.toPath(Paths.get("photo4.jpg"))

      val stream = source.toMat(sink)(Keep.right)
      val result = stream.run()

    // Notify ObjectRecogntionActor
    case ImageSaved(sensorId, imageId) =>
      // inform recognizer
      log.info("\nInforming ObjectRecognizerAdapter")
      objectRecogntionActor ! AnalyseSensorImage(sensorId, imageId)

    case x =>
      log.warning(s"\nReceived unknown message: $x")
  }

}