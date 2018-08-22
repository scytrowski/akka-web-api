package nullpointer.akkawebapi

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import nullpointer.akkawebapi.Configurations.ServerConfiguration
import nullpointer.akkawebapi.Entities.RestEntity
import nullpointer.akkawebapi.Models.User
import nullpointer.akkawebapi.db.{DatabaseConfiguration, Tables}

import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

object Application extends App with DatabaseConfiguration {
  import config.profile.api._

  def await[T](future: Future[T]): T = Await.result(future, 5 seconds)

  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher

//  val config = ConfigFactory.load()
//  val serverConfiguration = ServerConfiguration.ofConfig(config)
//  val webServer = new WebServer(serverConfiguration)
//  webServer.start()

  val db = Database.forConfig("db")
  try {
    await(db.run(Tables.users.insertOrUpdate(RestEntity[User](1, User("abc", "def")))))
    db.run(Tables.users.result).foreach(println)
    println("After print!")
  } finally db.close
}
