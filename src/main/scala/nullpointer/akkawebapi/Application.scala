package nullpointer.akkawebapi

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import nullpointer.akkawebapi.routes.HelloWorldRoute

import scala.concurrent.ExecutionContext
import scala.io.StdIn

object Application extends App {
  implicit val system: ActorSystem = ActorSystem()
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val route = new HelloWorldRoute()
  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)
  println("Server started")
  StdIn.readLine()
  bindingFuture
    .flatMap(_.unbind())
    .onComplete(_ => system.terminate())
}
