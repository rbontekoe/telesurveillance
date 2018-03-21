package actors

import actors.PictureManager.Analyse
import akka.actor.Actor
import akka.actor.Props

object ApartmentSupervisor {
  case object Start
}

class ApartmentSupervisor extends Actor {

  import ApartmentSupervisor._

  val pictureManager = context.actorOf(Props[PictureManager], "pictureManager")
  println(pictureManager)

  //  context.system.scheduler.schedule(2 seconds, 1 seconds, roomStatusProvider, Analyse)

  def receive = {
    case Start =>
      pictureManager ! Analyse
    case _ =>
      println("Received unknown message")
  }
}
