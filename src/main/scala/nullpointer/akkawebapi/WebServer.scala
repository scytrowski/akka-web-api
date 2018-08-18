package nullpointer.akkawebapi

import akka.http.scaladsl.server.{HttpApp, Route}
import nullpointer.akkawebapi.models.User
import nullpointer.akkawebapi.repositories.MapBackedRestRepository
import nullpointer.akkawebapi.routes.RestRoute

object WebServer extends HttpApp {
  import nullpointer.akkawebapi.models.ModelJsonFormats._

  override protected def routes: Route = RestRoute[User](new MapBackedRestRepository[User](), "user")
}
