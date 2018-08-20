package nullpointer.akkawebapi

import akka.http.scaladsl.server.{HttpApp, Route}
import nullpointer.akkawebapi.Models.{Category, Thread, Post, User}
import nullpointer.akkawebapi.repositories.MapBackedRestRepository
import nullpointer.akkawebapi.routes.RestRoute

import scala.concurrent.ExecutionContext

class WebServer(implicit ec: ExecutionContext) extends HttpApp {
  import nullpointer.akkawebapi.json.ModelJsonFormats._

  override protected def routes: Route =
    RestRoute[User](new MapBackedRestRepository[User](), "user") ~
    RestRoute[Category](new MapBackedRestRepository[Category](), "category") ~
    RestRoute[Thread](new MapBackedRestRepository[Thread](), "thread") ~
    RestRoute[Post](new MapBackedRestRepository[Post](), "post")
}
