package nullpointer.akkawebapi

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext

object Application extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher
  val webServer = new WebServer
  webServer.startServer("localhost", 8080)
}
