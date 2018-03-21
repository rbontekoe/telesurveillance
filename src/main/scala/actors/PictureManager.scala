package actors

import java.io.File
import java.nio.file.Paths

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import com.typesafe.config.ConfigFactory

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorNotFound
import akka.actor.ActorSelection
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import akka.stream.scaladsl.Sink
import akka.util.Timeout
import model.api.RemoteRoomApi
import model.domain.ImageId
import model.domain.SensorId

/*
 * Run on Raspberry Pi in Docker container with Scala and Python
 * 
 */
object PictureManager {
  /*
   * Infrared Camera Module v2 (Pi NoIR)
   * The camera creates an image in the root (photo.jpg).
   * This image is compared to to the previous image in the picture folder (pictures/photo2.jpg)
   * Next the root image is copied to the picture folder and overrides the previous image 
   */

  //TODO implement ApartmentSupervisor actor for life cycle management of PictureManager
  case object Analyse
  case object TakePictureAndCompare

  // Read values from application.conf
  val config = ConfigFactory.load.getConfig("RoomMonitor")
  val sensorId = config.getInt("sensorId")            // 10100
  val imageId = config.getString("imageId")           // photo2.jpg
  val threshold = config.getDouble("threshold")       // 2
  val _chunkSize = config.getInt("_chunkSize")        // 500000

  val fileNew = new File(config.getString("fileNew")) // photo.jpg
  val fileOld = new File(config.getString("fileOld")) // pictures/photo2.jpg

  def apply(sensorRepositoryAdapter: ActorSelection, imageProvider: RemoteRoomApi) =
    new PictureManager(sensorRepositoryAdapter, RemoteRoomApi())

  case class ImageSaved(sensorId: SensorId, imageId: ImageId)

}

class PictureManager(roomRepositoryActor: ActorSelection, imageProvider: RemoteRoomApi) extends Actor with ActorLogging {

  import PictureManager._

  var state = "Init"

  def receive = {
    case TakePictureAndCompare => {
      log.info("\nState: " + state)

      // Cleanup folder with pictures
      if (state == "Init") {
        imageProvider.cleanupFolder
        state = "Running"
      }

      // Take picture
      imageProvider.takePicture
      val result = imageProvider.compareImages(fileNew, fileOld)
      println("Difference: " + result)
      if (result > threshold) sendImage

    }
    case x =>
      log.warning(s"Received unknown message: $x")
  }

  // Send image to SensorRepository as ByteString
  private def sendImage: Unit = {

    implicit val materializer = ActorMaterializer()
    implicit val ec = context.dispatcher
    implicit val resolveTimeout = Timeout(5 seconds)

    try {
      val actorRef = Await.result(roomRepositoryActor.resolveOne(), resolveTimeout.duration)

      val source = FileIO.fromPath(Paths.get("photo.jpg"), chunkSize = _chunkSize)

      log.info("\nStreaming image to SensorRepositoryAdapter (on laptop)")

      val last = source.runWith(Sink.actorRef(actorRef, onCompleteMessage = ImageSaved(SensorId(sensorId), ImageId(imageId))))

    } catch {
      case ActorNotFound(_) => log.info("\nActor not found, remote system maybe down")
      case e: Exception     => log.info(s"\nError: ${e.getMessage}")
    }
  }
}