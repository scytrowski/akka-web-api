package nullpointer.akkawebapi.routes
import akka.http.scaladsl.model.HttpEntity
import akka.http.scaladsl.server.Route

object HelloWorldRoute {
  def apply(): HelloWorldRoute = new HelloWorldRoute
}

class HelloWorldRoute extends CustomRoute {
  override def definition: Route =
    path("hello") {
      complete(HttpEntity("Hello, world!"))
    }
}
