package nullpointer.akkawebapi

import akka.http.scaladsl.server.{HttpApp, Route}
import com.typesafe.config.Config
import nullpointer.akkawebapi.Configurations.ServerConfiguration
import nullpointer.akkawebapi.Models.{Category, Post, Thread, User}
import nullpointer.akkawebapi.WebServer.WebApp
import nullpointer.akkawebapi.repositories.MapBackedRestRepository
import nullpointer.akkawebapi.routes.RestRoute

import scala.concurrent.ExecutionContext

class WebServer(configuration: ServerConfiguration)(implicit ec: ExecutionContext) {
  private val app = WebApp(configuration)

  def start(): Unit = {
    app.startServer(configuration.host, configuration.port)
  }
}

object WebServer {
  private case class WebApp private(configuration: ServerConfiguration)(implicit ec: ExecutionContext) extends HttpApp {
    import nullpointer.akkawebapi.json.ModelJsonFormats._

    override protected def routes: Route =
      path(configuration.basePath) {
        RestRoute[User](new MapBackedRestRepository[User](), "user") ~
          RestRoute[Category](new MapBackedRestRepository[Category](), "category") ~
          RestRoute[Thread](new MapBackedRestRepository[Thread](), "thread") ~
          RestRoute[Post](new MapBackedRestRepository[Post](), "post")
      }
  }
}
