package nullpointer.akkawebapi.routes

import akka.http.scaladsl.server.{Route, StandardRoute}
import nullpointer.akkawebapi.models.ErrorResponses.{ErrorResponse, ErrorResponseJsonSupport}

trait ErrorHandlingRoute extends Route with akka.http.scaladsl.server.Directives with ErrorResponseJsonSupport {
  def completeWithError(error: ErrorResponse): StandardRoute =
    complete(error.statusCode, error)
}
