package nullpointer.akkawebapi

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import nullpointer.akkawebapi.Configurations.ServerConfiguration

import scala.concurrent.ExecutionContext

object Application extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher
  val config = ConfigFactory.load()
  val serverConfiguration = ServerConfiguration.ofConfig(config)
  val webServer = new WebServer(serverConfiguration)
  webServer.start()
}
