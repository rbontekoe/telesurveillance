package commons

import scala.concurrent.Future

import akka.stream.SourceRef
import akka.util.ByteString

// StorePicture message is transfered to the RoomRepositoryActor
case class StorePicture(sourceRef: Future[SourceRef[ByteString]], sensorId: Int, time: Long, difference: Long, timeLapse: Long)

