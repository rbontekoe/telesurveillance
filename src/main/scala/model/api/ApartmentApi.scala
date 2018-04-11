package model.api

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._

import model.domain.ApartmentId
import model.domain.Crud
import slick.jdbc.PostgresProfile.api._
import model.domain.Apartment
import model.domain.ApartmentName
import model.domain.PersonId
import model.domain.Person
import model.domain.PersonName

/*
 * Apartment repository
 */
object ApartmentApi extends Crud[Apartment] {

  val db = Database.forConfig("chapter01")
  val apartments = TableQuery[ApartmentTable]

  // TODO
  def create(apartment: Apartment): Boolean = { ??? }
  def read(apartment: Apartment): Option[Apartment] = {
    val selectedApartment = apartments.filter(_.apartmentid === apartment.apartmentId.value)
    val apartmentsAction: DBIO[Seq[ApartmentTableRow]] = selectedApartment.result
    val apartmentsFuture: Future[Seq[ApartmentTableRow]] = db.run(apartmentsAction)
    val apartmentsResult = Await.result(apartmentsFuture, 2.seconds)

    if (!apartmentsResult.isEmpty) {
      val res = apartmentsResult(0)
      Option(Apartment(ApartmentId(res.apartmentid), PersonId(res.personid), ApartmentName(res.name)))
    } else None

  } // define read

  def update(apartment: Apartment): Boolean = { ??? }
  def delete(apartment: Apartment): Boolean = { ??? }

  def getPerson(personId: Int): Option[Person] = {
    val person = Person(PersonId(personId), PersonName(""))
    val res = Option(PersonApi.read(person))
    res match {
      case Some(person) => person
      case None         => None
    }
  }
}

final case class ApartmentTableRow(
  apartmentid: Int,
  personid: Int,
  name: String) // defined RoomTableRow

final class ApartmentTable(tag: Tag) extends Table[ApartmentTableRow](tag, "apartment") {
  def apartmentid = column[Int]("apartmentid")
  def personid = column[Int]("personid")
  def name = column[String]("name")
  def * = (apartmentid, personid, name).mapTo[ApartmentTableRow]
} // defined ApartmentTable