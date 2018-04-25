package _start

import com.typesafe.config.ConfigFactory

import actors.ApartmentManager
import actors.ApartmentSupervisor
import actors.RoomRepositoryActor
import akka.actor.ActorSystem
import akka.actor.Props

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

  // Create AppartmentSupervisor
  val supervisor = system.actorOf(Props[ApartmentSupervisor], "supervisor")

}