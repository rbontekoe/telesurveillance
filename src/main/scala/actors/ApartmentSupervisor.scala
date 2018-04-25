package actors

import com.typesafe.config.ConfigFactory

import actors.PictureManager.TakePictureAndCompare
import akka.actor.Actor
import akka.actor.Props
import model.api.RemoteRoomApi

object ApartmentSupervisor {
  case object Start
  
}

class ApartmentSupervisor extends Actor {

  import ApartmentSupervisor._
  import context.dispatcher
  import scala.concurrent.duration._
  
  
  // Load application.conf section of RoomMonitor
  val config = ConfigFactory.load.getConfig("RoomMonitor")
  
  // CentralSystem actor (RoomRepositoryActor) who will receive the image file
  val targetActor = context.actorSelection(config.getString("centralSystemActor"))
  
  // Local service
  val pictureManager = context.actorOf(Props(PictureManager(targetActor, RemoteRoomApi())), "pictureManager")

  // Activate Picturemanager
  context.system.scheduler.schedule(2 seconds, 20 seconds, self, Start)

  def receive = {
    case Start =>
      pictureManager ! TakePictureAndCompare
    case _ =>
      println("Received unknown message")
  }
}
