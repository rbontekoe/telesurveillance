package actors

import org.scalatest.BeforeAndAfterAll
import org.scalatest.FlatSpecLike
import org.scalatest.MustMatchers

import com.typesafe.config.ConfigFactory

import actors.PictureManager.TakePictureAndCompare
import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.TestKit
import akka.testkit.TestProbe


//import context.dispatcher
import scala.concurrent.duration._
import model.api.RemoteRoomApi

class PictureManagerActorTest extends TestKit(ActorSystem("test-actor"))
    with FlatSpecLike
    with BeforeAndAfterAll
    with MustMatchers {

  override def afterAll() = {
    TestKit.shutdownActorSystem(system)
  }

  "PictureManager actor" should "handle the command TakePictureAndCompare" in {
    val sender = TestProbe()

    val config = ConfigFactory.load.getConfig("RoomMonitor")

    val targetActor = system.actorSelection(config.getString("centralSystemActor"))

    val pm = system.actorOf(Props(PictureManager(targetActor, RemoteRoomApi())))

    sender.send(pm, TakePictureAndCompare)
    
    expectNoMessage(1 second)

  }

  it should "ignore any other messages" in {

    val config = ConfigFactory.load.getConfig("RoomMonitor")

    val targetActor = system.actorSelection(config.getString("centralSystemActor"))

    val pm = system.actorOf(Props(PictureManager(targetActor, RemoteRoomApi())))

    pm ! "Hello world"

    expectNoMessage(1 second)

  }
}