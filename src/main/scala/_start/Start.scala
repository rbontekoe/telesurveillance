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
  val appartmentManager = system.actorOf(Props[ApartmentManager], "manager")
  
  // Create actors for CentralSystem
  val roomRepositoryActor = system.actorOf(Props[RoomRepositoryActor], "repository")
  
  val surveillant = system.actorOf(Props[SurveillanceActor], "surveillant")
  
  val recognizer = system.actorOf(Props[ObjectRecognitionActor], "recognizer")
}

object RoomMonitor extends App {
  // Load application.conf section of RoomMonitor
  val config = ConfigFactory.load.getConfig("RoomMonitor")

  // Create Actorsystem for RoomMonitor
  val system = ActorSystem("RoomMonitor", config)

  // CentralSystem actor who will receive the image file
  val roomRepositoryActor = system.actorSelection(config.getString("centralSystemActor"))

  //TODO ApartmentSupervisor managing the lifecycle of PictureManager actors

  // Local service
  val pictureManager = system.actorOf(Props(PictureManager(roomRepositoryActor, RemoteRoomApi())), "pictureManager")

  // PictureManager actor scheduler, will be the future task of the ApartmentSupervisor
  import system.dispatcher
  import scala.concurrent.duration._

  system.scheduler.schedule(1 seconds, 20 seconds, pictureManager, TakePictureAndCompare)
}