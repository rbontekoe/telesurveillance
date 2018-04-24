package _start

import scala.concurrent.duration.DurationInt

import com.typesafe.config.ConfigFactory

import actors.ApartmentManager
import actors.ObjectRecognitionActor
import actors.PictureManager
import actors.PictureManager.TakePictureAndCompare
import actors.RoomRepositoryActor
import actors.SurveillanceActor
import akka.actor.ActorSystem
import akka.actor.Props
import model.api.RemoteRoomApi

object CentralSystem extends App {
  // Load application.conf section of CentralSystem
  val config = ConfigFactory.load.getConfig("CentralSystem")

  // Create Actorsystem for CentralSystem
  val system = ActorSystem("CentralSystem", config)

  // Create supervisor
  val apartmentManager = system.actorOf(Props[ApartmentManager], "manager")

  // Create actor receiving the remote SourceRef
  val roomRepositoryActor = system.actorOf(Props[RoomRepositoryActor], "repository")
  
}

object RoomMonitor extends App {
  // Load application.conf section of RoomMonitor
  val config = ConfigFactory.load.getConfig("RoomMonitor")

  // Create ActorSystem for RoomMonitor
  val system = ActorSystem("RoomMonitor", config)

  // CentralSystem actor (RoomRepositoryActor) who will receive the image file
  val targetActor = system.actorSelection(config.getString("centralSystemActor"))

  // Local service
  val pictureManager = system.actorOf(Props(PictureManager(targetActor, RemoteRoomApi())), "pictureManager")

  // PictureManager actor scheduler, will be the future task of the ApartmentSupervisor
  import system.dispatcher
  import scala.concurrent.duration._

  system.scheduler.schedule(1 seconds, 20 seconds, pictureManager, TakePictureAndCompare)
}