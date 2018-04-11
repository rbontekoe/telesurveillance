package model.domain

trait Crud[T] {
  def create(obj: T): Boolean
  def read(obj: T): Option[T]
  def update(obj: T): Boolean
  def delete(obj: T): Boolean
} // defined trait for CRUD operations

trait ObjectRecognitionApi {
  def analyseImage(sensorId: SensorId, imageId: ImageId): List[Person]
} // defined api for face recognition

trait SensorRepositoryApi {
  def findRoomNbr(sensorId: Int): Option[Room]
} // defined api for repository

// Fields
case class SensorId(value: Int) extends AnyVal
case class ImageId(value: String) extends AnyVal
case class RoomId(value: Int) extends AnyVal
case class PersonId(value: Int) extends AnyVal
case class ApartmentId(value: Int) extends AnyVal
case class PersonName(value: String) extends AnyVal
case class ApartmentName(value: String) extends AnyVal

case class Person(personId: PersonId, name: PersonName)
case class SensorImage(sensorId: SensorId, imageId: ImageId)

trait RoomType
object RoomType {
  case object Kitchen extends RoomType
  case object Bathroom extends RoomType
  case object Bedroom extends RoomType
  case object Livingroom extends RoomType
  case object Balcony extends RoomType
  case object Unkown extends RoomType

  // values for textToRoomType conversion function
  val bathroom = RoomType.Bathroom.toString()
  val bedroom = RoomType.Bedroom.toString()
  val livingroom = RoomType.Livingroom.toString()
  val kitchen = RoomType.Kitchen.toString()
  val balcony = RoomType.Balcony.toString()

  def textToRoomType(r: String): RoomType = r.toLowerCase().capitalize match {
    case `bathroom`   => RoomType.Bathroom
    case `bedroom`    => RoomType.Bedroom
    case `livingroom` => RoomType.Livingroom
    case `kitchen`    => RoomType.Kitchen
    case `balcony`    => RoomType.Balcony
    case _            => RoomType.Unkown
  } // defined textToRoomType
} // defined RoomType 

case class Room(roomId: RoomId, roomType: RoomType, apartmentId: ApartmentId, sensorId: SensorId) 

case class Apartment(apartmentId: ApartmentId, personId: PersonId, name: ApartmentName)

trait AlarmType
object AlarmType {
  case object Info extends AlarmType
  case object Warning extends AlarmType
  case object Severe extends AlarmType
  case object Error extends AlarmType
  case object Unkown extends AlarmType

  // values for textToAlarmType conversion function
  val info = AlarmType.Info.toString()
  val warning = AlarmType.Warning.toString()
  val severe = AlarmType.Severe.toString()
  val error = AlarmType.Error.toString()

  def textToAlarmType(r: String): AlarmType = r.toLowerCase().capitalize match {
    case `info`    => AlarmType.Info
    case `warning` => AlarmType.Warning
    case `severe`  => AlarmType.Severe
    case `error`   => AlarmType.Error
    case _         => AlarmType.Unkown
  } // defined textToAlarmType

} // defined enumeration AlarmType

trait RoomState
case class EmptyRoom(sensorImage: SensorImage) extends RoomState
case class NonEmptyRoom(sensorImage: SensorImage, alarmType: AlarmType, persons: List[Person]) extends RoomState
