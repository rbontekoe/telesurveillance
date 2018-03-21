package actors

import akka.actor.Actor

object ApartmentManager {
  
}

class ApartmentManager extends Actor {

  def receive = {
    case _ => println("Apartmentmanager")
  }
}
