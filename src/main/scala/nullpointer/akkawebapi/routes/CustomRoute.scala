package nullpointer.akkawebapi.routes

import akka.http.scaladsl.server.{RequestContext, Route, RouteResult}

import scala.concurrent.Future

trait CustomRoute extends Route with akka.http.scaladsl.server.Directives {
  override def apply(context: RequestContext): Future[RouteResult] = definition(context)

  def definition: Route
}
