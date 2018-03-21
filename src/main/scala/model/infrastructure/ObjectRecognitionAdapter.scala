package model.infrastructure

import model.domain.ImageId
import model.domain.ObjectRecognitionApi
import model.domain.Person
import model.domain.PersonId
import model.domain.PersonName
import model.domain.SensorId

/*
 * ObjectRecognitionAdapter
 */
trait RestApi {
  def analyse(sensorId: SensorId, imageId: ImageId): List[Person]
}

//object ObjectRecognitionAdapter {
////  case class AnalyseSensorImage(sensorId: SensorId, imageId: ImageId)
////  case class SensorImageAnalysed(persons: List[Person])
//}

class ObjectRecognitionAdapter extends ObjectRecognitionApi with RestApi {
//  import ObjectRecognitionAdapter._

  def analyseImage(sensorId: SensorId, imageId: ImageId): List[Person] =
    analyse(sensorId: SensorId, imageId: ImageId)

  def analyse(sensorId: SensorId, imageId: ImageId): List[Person] =
    //TODO Retrieve image and analyse
    List(new Person(new PersonId(210), PersonName("Mrs Neeltje")))

  /*
     * For now: test purpose
     * For future projects: Face recognition
     * Also video: Hand gesture recognition, Speech recognition, etc. 
     */
}