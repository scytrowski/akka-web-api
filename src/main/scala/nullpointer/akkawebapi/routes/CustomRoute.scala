package nullpointer.akkawebapi.routes

import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}

import scala.concurrent.Future

trait CustomRoute extends ErrorHandlingRoute {
  override def apply(context: RequestContext): Future[RouteResult] = definition(context)

  def definition: Route
}
