package actors

import java.io.File
import java.nio.file.Paths
import java.util.Date

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt

import com.typesafe.config.ConfigFactory

import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorNotFound
import akka.actor.ActorSelection
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.FileIO
import akka.stream.scaladsl.StreamRefs
import akka.util.Timeout
import commons.StorePicture
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
  case class ImageSaved(sensorId: SensorId, imageId: ImageId)

  // Read values from application.conf
  val config = ConfigFactory.load.getConfig("RoomMonitor")
  val sensorId = config.getInt("sensorId") // 10101
  val imageId = config.getString("imageId") // photo2.jpg
  val threshold = config.getDouble("threshold") // 2
  val picturePath = config.getString("picturePath") // pictures/

  val fileOld = new File(picturePath + sensorId.toString() + imageId)
  val fileNew = new File(config.getString("fileNew")) // photo.jpg (created by IR camera)

  def apply(sensorRepositoryAdapter: ActorSelection, imageProvider: RemoteRoomApi) =
    new PictureManager(sensorRepositoryAdapter, RemoteRoomApi())
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
      log.info("Difference: {}", result)

      // Send image to targetActor
      if (result > threshold) {
        implicit val materializer = ActorMaterializer()
        implicit val ec = context.dispatcher
        implicit val resolveTimeout = Timeout(5 seconds)

        try {
          // obtain the picture
          val source = FileIO.fromPath(Paths.get(picturePath + sensorId + imageId))

          // materialize the SourceRef
          val sourceRef = source.runWith(StreamRefs.sourceRef())

          // send sourceRef to target actor (RoomRepostitoryActor)
          val actorRef = Await.result(roomRepositoryActor.resolveOne(), resolveTimeout.duration)
          val time = new Date().getTime
          actorRef ! StorePicture(sourceRef, sensorId, time)

        } catch { 
          case ActorNotFound(_) => log.info("\nActor not found, remote system maybe down")
          case e: Exception => log.info("\nError: {}", e.getMessage)
        }
      }
    }
    case x =>
      log.warning("Received unknown message: {}", x)
  }
}