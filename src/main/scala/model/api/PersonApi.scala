package model.api

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.duration._

import model.domain.PersonId
import model.domain.Crud
import model.domain.Person
import slick.jdbc.PostgresProfile.api._
import model.domain.PersonName

/*
 * Person repository
 */
object PersonApi extends Crud[Person] {

  val db = Database.forConfig("chapter01")
  val persons = TableQuery[PersonTable]

  // TODO
  def create(person: Person): Boolean = { ??? }
  def read(person: Person): Option[Person] = {
    val selectedPerson = persons.filter(_.personid === person.personId.value)
    val personsAction: DBIO[Seq[PersonTableRow]] = selectedPerson.result
    val personsFuture: Future[Seq[PersonTableRow]] = db.run(personsAction)
    val personsResult = Await.result(personsFuture, 2.seconds)
    
    if (!personsResult.isEmpty) {
      val res = personsResult(0)
      Option(Person(PersonId(res.personid), PersonName(res.name)))
    } else None
    
  } // define read  
  def update(person: Person): Boolean = { ??? }
  def delete(person: Person): Boolean = { ??? }
}

final case class PersonTableRow(
  personid: Int,
  name: String) // defined PersonTableRow

final class PersonTable(tag: Tag) extends Table[PersonTableRow](tag, "person") {
  def personid = column[Int]("personid")
  def name = column[String]("name")
  def * = (personid, name).mapTo[PersonTableRow]
} // defined PersonTable