package nullpointer.akkawebapi

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import nullpointer.akkawebapi.repositories.UserRepositories.{MapBackedUserRepository, UserRepository}
import nullpointer.akkawebapi.routes.{HelloWorldRoute, UserRoute}

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Application extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val userRepositoryEc = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(4))
  val userRepository = new MapBackedUserRepository()(userRepositoryEc)
  val route: Route = HelloWorldRoute() ~ UserRoute(userRepository)
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  println("Server started")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
