package commons

import scala.concurrent.Future
import akka.stream.SourceRef
import akka.util.ByteString
import model.domain.SensorId
import model.domain.ImageId
import model.domain.Person

//case object ApartmentSupervisorRegistration

// PictureManager to RoomRepositoryActor
case class StorePicture(sourceRef: Future[SourceRef[ByteString]], sensorId: Int, time: Long)

case class AnalyseSensorImage(sensorId: SensorId, imageId: ImageId)
case class SensorImageAnalysed(persons: List[Person])
